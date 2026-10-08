package com.sxpcwlkj.framework.exception;
import com.sxpcwlkj.common.exception.MmsException;
import org.mybatis.spring.MyBatisSystemException;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import static org.junit.jupiter.api.Assertions.*;
class GlobalExceptionRegressionTest {
    @Test void databaseWrapperPreservesSessionExpiryCode() {
        var result=new GlobalException().handleCannotFindDataSourceException(new MyBatisSystemException(new MmsException("请重新登录",401)),new MockHttpServletRequest());
        assertEquals(401,result.getCode()); assertEquals("请重新登录",result.getMsg());
    }
    @Test void unknownErrorDoesNotReturnInternalDetailsToClient() {
        var result=new GlobalException().handleRuntimeException(new RuntimeException("internal credential must not reach client"),new MockHttpServletRequest());
        assertEquals(500,result.getCode()); assertFalse(result.getMsg().contains("credential"));
    }
}
