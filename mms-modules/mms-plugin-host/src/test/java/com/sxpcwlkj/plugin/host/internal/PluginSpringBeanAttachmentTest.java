package com.sxpcwlkj.plugin.host.internal;

import com.sxpcwlkj.plugin.PluginException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PluginSpringBeanAttachmentTest {

    @Test
    void registerReleaseAndDisposable() throws Exception {
        try (AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext()) {
            ctx.refresh();
            PluginSpringBeanAttachment att = new PluginSpringBeanAttachment(ctx);
            String sid = "sess-1";
            AtomicBoolean destroyed = new AtomicBoolean();
            att.registerSingleton(
                    sid,
                    "svc",
                    new DisposableBean() {
                        @Override
                        public void destroy() {
                            destroyed.set(true);
                        }
                    });
            String full = "mms.plugin.bean." + sid + ".svc";
            assertTrue(ctx.getBeanFactory().containsSingleton(full));
            att.releaseSession(sid);
            assertFalse(((DefaultListableBeanFactory) ctx.getBeanFactory()).containsSingleton(full));
            assertTrue(destroyed.get());
        }
    }

    @Test
    void duplicateLogicalNameInSessionFails() {
        try (AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext()) {
            ctx.refresh();
            PluginSpringBeanAttachment att = new PluginSpringBeanAttachment(ctx);
            String sid = "sess-2";
            att.registerSingleton(sid, "a", new Object());
            assertThrows(PluginException.class, () -> att.registerSingleton(sid, "a", new Object()));
        }
    }
}
