package com.sxpcwlkj;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = {
    "dromara.x-file-storage.default-platform=",
    "dromara.x-file-storage.aliyun-oss[0].enable-storage=false"
})
@Tag("local")
@Tag("dev")
class MmsAdminApplicationTests {

    @Test
    void contextLoads() {
        // 验证 Spring 上下文可正常启动
    }
}
