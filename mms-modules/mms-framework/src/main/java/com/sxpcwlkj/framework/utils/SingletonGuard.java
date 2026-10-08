package com.sxpcwlkj.framework.utils;
import com.sxpcwlkj.redis.RedisUtil;
import com.sxpcwlkj.common.exception.MmsException;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/** Serialize first-write singleton creation until its database transaction completes. */
public final class SingletonGuard {
    private SingletonGuard() { }
    public static <T> T execute(String key, Supplier<T> operation) {
        return execute(RedisUtil.getClient(), key, operation);
    }
    static <T> T execute(org.redisson.api.RedissonClient client, String key, Supplier<T> operation) {
        var lock=client.getLock("mms:singleton:"+key);
        boolean deferred=false;
        try {
            if (!lock.tryLock(5,TimeUnit.SECONDS)) throw new MmsException("配置正在保存，请稍后重试");
            if (TransactionSynchronizationManager.isActualTransactionActive() && TransactionSynchronizationManager.isSynchronizationActive()) {
                TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                    @Override public void afterCompletion(int status) { if(lock.isHeldByCurrentThread()) lock.unlock(); }
                });
                deferred=true;
            }
            return operation.get();
        } catch (InterruptedException e) { Thread.currentThread().interrupt(); throw new MmsException("配置保存已中断"); }
        finally { if(!deferred && lock.isHeldByCurrentThread()) lock.unlock(); }
    }
}
