package com.sxpcwlkj.framework.utils;

import com.sxpcwlkj.redis.RedisUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;
import org.mockito.Mockito;
import org.redisson.Redisson;
import org.redisson.config.Config;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import static org.junit.jupiter.api.Assertions.*;

@EnabledIfSystemProperty(named="mms.lab.redis",matches="true")
class SingletonGuardRedisIntegrationTest {
    @Test void concurrentFirstWritesHoldRealRedisLockUntilTransactionCompletes() throws Exception {
        Config config = new Config();
        config.useSingleServer().setAddress("redis://127.0.0.1:16379").setPassword(System.getenv("LAB_REDIS_PASSWORD"));
        var client = Redisson.create(config);
        var workers = Executors.newFixedThreadPool(8);
        String key = "regression:" + UUID.randomUUID();
        AtomicInteger records = new AtomicInteger();
        AtomicInteger active = new AtomicInteger();
        try {
            List<Future<?>> tasks = new ArrayList<>();
            for (int i=0;i<24;i++) tasks.add(workers.submit(() -> {
                {
                    TransactionSynchronizationManager.setActualTransactionActive(true);
                    TransactionSynchronizationManager.initSynchronization();
                    try {
                        SingletonGuard.execute(client, key, () -> {
                            assertEquals(1, active.incrementAndGet());
                            if (records.get()==0) records.incrementAndGet();
                            return true;
                        });
                        assertTrue(client.getLock("mms:singleton:"+key).isHeldByCurrentThread());
                        active.decrementAndGet();
                    } finally {
                        TransactionSynchronizationManager.getSynchronizations().forEach(c -> c.afterCompletion(TransactionSynchronization.STATUS_COMMITTED));
                        TransactionSynchronizationManager.clearSynchronization();
                        TransactionSynchronizationManager.setActualTransactionActive(false);
                    }
                }
            }));
            for (Future<?> task : tasks) task.get(30,TimeUnit.SECONDS);
            assertEquals(1, records.get()); assertEquals(0, active.get());
            assertFalse(client.getLock("mms:singleton:"+key).isLocked());
        } finally { workers.shutdownNow(); client.shutdown(); }
    }
}
