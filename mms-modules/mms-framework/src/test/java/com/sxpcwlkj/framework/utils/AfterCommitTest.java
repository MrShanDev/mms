package com.sxpcwlkj.framework.utils;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import java.util.concurrent.atomic.AtomicInteger;
import static org.junit.jupiter.api.Assertions.*;
class AfterCommitTest {
    @Test void sideEffectWaitsForCommitAndIsNotInvokedOnRollback() {
        var count=new AtomicInteger();
        TransactionSynchronizationManager.setActualTransactionActive(true);
        TransactionSynchronizationManager.initSynchronization();
        try {
            AfterCommit.run(count::incrementAndGet); assertEquals(0,count.get());
            var callbacks=TransactionSynchronizationManager.getSynchronizations();
            callbacks.forEach(c->c.afterCompletion(1)); assertEquals(0,count.get());
            callbacks.forEach(c->c.afterCommit()); assertEquals(1,count.get());
        } finally { TransactionSynchronizationManager.clearSynchronization(); TransactionSynchronizationManager.setActualTransactionActive(false); }
    }
}
