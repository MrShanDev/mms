package com.sxpcwlkj.framework.exception;

import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.exception.NotPermissionException;
import cn.dev33.satoken.exception.NotRoleException;
import com.sxpcwlkj.common.enums.ErrorCodeEnum;
import com.sxpcwlkj.common.exception.*;
import com.sxpcwlkj.common.utils.R;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.exceptions.PersistenceException;
import org.mybatis.spring.MyBatisSystemException;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.jdbc.UncategorizedSQLException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.nio.file.AccessDeniedException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 全局异常类
 *
 * @author shanpengnian
 */
@Slf4j
@RestControllerAdvice
public class GlobalException {
    // 获取异常的根本原因
    private Throwable getRootCause(Throwable throwable) {
        Throwable rootCause = throwable;
        while (rootCause.getCause() != null && rootCause.getCause() != rootCause) {
            rootCause = rootCause.getCause();
        }
        return rootCause;
    }
    /**
     * 根据错误码匹配
     */
    private Map<String, Object> getResult(ErrorCodeEnum e) {
        Map<String, Object> map = new HashMap<>();
        map.put("success", false);
        map.put("code", e.getKey());
        map.put("msg", e.getValue());
        return map;
    }



    // 专门处理演示模式异常
    @ExceptionHandler(DemoModeException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public R<Void> handleDemoModeException(DemoModeException e) {
        log.error("演示模式拦截操作: {}", e.getMessage());
        return R.fail(e.getErrorCode(), e.getMessage());
    }

    @ExceptionHandler(AccessDeniedException.class)
    public R<Void> handleAccessDeniedException(AccessDeniedException e,
                                               HttpServletRequest request) {
        String requestUrl = request.getRequestURI();
        log.error("请求地址{},权限校验失败{}", requestUrl, e.getMessage());
        return R.fail(ErrorCodeEnum.FORBIDDEN.getKey(), ErrorCodeEnum.FORBIDDEN.getValue());
    }

    /**
     * 参数校验不通过
     *
     * @param e       异常信息
     * @param request 请求信息
     */
    @ExceptionHandler(value = ParamsException.class)
    @ResponseStatus(HttpStatus.OK)
    public R<Void> handleObjectExistException(ParamsException e,
                                                          HttpServletRequest request) {
        String requestUrl = request.getRequestURI();
        String message = e.getMessage();
        log.error("请求地址'{}',参数异常'{}'", requestUrl, message);

        // 检查是否是验签相关的异常
//        if (message != null && (
//            message.contains("签名") ||
//            message.contains("sign") ||
//            message.contains("验签") ||
//            message.contains("时间戳") ||
//            message.contains("timestamp") ||
//            message.contains("请求参数body不能为空")
//        )) {
//            log.warn("请求地址'{}',检测到验签失败: {}", requestUrl, message);
//            return R.fail("验签失败，请检查请求签名参数");
//        }

        return R.fail(message);
    }

    @ExceptionHandler(value = HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public R<Void> handleHttpMessageNotReadableException(HttpMessageNotReadableException e,
                                                         HttpServletRequest request) {
        String requestUrl = request.getRequestURI();
        log.error("请求地址'{}',参数异常'{}'", requestUrl, e.getMessage());
        return R.fail(HttpStatus.BAD_REQUEST.value(), "请求参数错误，请检查请求体格式");
    }

    /**
     * 处理参数类型不匹配异常，通常由验签或解密失败引起
     */
    @ExceptionHandler(IllegalStateException.class)
    public R<Void> handleIllegalStateException(IllegalStateException ex, HttpServletRequest request) {
        String message = ex.getMessage();
        String requestUrl = request.getRequestURI();
        log.error("请求地址'{}',参数类型不匹配异常: ", requestUrl, ex);

        // 检查是否是验签或解密相关的异常
        if (message != null && (
            message.contains("argument type mismatch") ||
            message.contains("ApiSecurityParam") ||
            message.contains("签名") ||
            message.contains("sign") ||
            message.contains("验签") ||
            message.contains("加密") ||
            message.contains("timestamp") ||
            message.contains("时间戳") ||
            message.contains("appId")
        )) {
            log.warn("请求地址'{}',检测到验签或解密失败: {}", requestUrl, message);
            return R.fail("验签失败，请检查请求签名参数");
        }

        return R.fail("请求参数错误: " + message);
    }


    /**
     * 用户校验不通过
     *
     * @param e       异常信息
     * @param request 请求信息
     */
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(value = NoHandlerFoundException.class)
    public R<Void> handleNotFoundException(NoHandlerFoundException e, HttpServletRequest request) {
        String requestUrl = request.getRequestURI();
        log.error("请求地址'{}',参数异常'{}'", requestUrl, e.getMessage());
        return R.fail(HttpStatus.NOT_FOUND.value(), "接口不存在: " + e.getRequestURL());
    }
    
    /**
     * 静态资源找不到异常处理
     */
    @ExceptionHandler(value = NoResourceFoundException.class)
    public R<Void> handleNoResourceFoundException(NoResourceFoundException e, HttpServletRequest request) {
        String requestUrl = request.getRequestURI();
        log.debug("请求静态资源'{}',资源不存在: {}", requestUrl, e.getResourcePath());
        // 返回404状态，而不是错误日志
        return R.fail(HttpStatus.NOT_FOUND.value(), "Resource not found");
    }
    /**
     * 用户校验不通过
     *
     * @param e       异常信息
     * @param request 请求信息
     */
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(value = TokenExpireException.class)
    public Map<String, Object> handle(TokenExpireException e, HttpServletRequest request) {
        return getResult(ErrorCodeEnum.USER_NOT_LOGIN);
    }

    /**
     * 权限码异常
     */
    @ExceptionHandler(NotPermissionException.class)
    public R<Void> handleNotPermissionException(NotPermissionException e, HttpServletRequest request) {
        String requestUrl = request.getRequestURI();
        log.error("请求地址'{}',权限码校验失败'{}'", requestUrl, e.getMessage());
        return R.fail(ErrorCodeEnum.FORBIDDEN.getKey(), ErrorCodeEnum.FORBIDDEN.getValue());
    }

    /**
     * 角色权限异常
     */
    @ExceptionHandler(NotRoleException.class)
    public R<Void> handleNotRoleException(NotRoleException e, HttpServletRequest request) {
        String requestUrl = request.getRequestURI();
        log.error("请求地址'{}',角色权限校验失败'{}'", requestUrl, e.getMessage());
        return R.fail(ErrorCodeEnum.FORBIDDEN.getKey(), ErrorCodeEnum.FORBIDDEN.getValue());
    }

    /**
     * 认证失败
     */
    @ExceptionHandler(NotLoginException.class)
    public R<Void> handleNotLoginException(NotLoginException e, HttpServletRequest request) {
        String requestUrl = request.getRequestURI();
        log.error("请求地址'{}',认证失败'{}',无法访问系统资源", requestUrl, e.getMessage());
        return R.fail(ErrorCodeEnum.USER_NOT_LOGIN.getKey(), "认证失败，无法访问系统资源");
    }

    /**
     * 请求方式不支持
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public R<Void> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException e,
                                                       HttpServletRequest request) {
        String requestUrl = request.getRequestURI();
        log.error("请求地址'{}',不支持'{}'请求", requestUrl, e.getMethod());
        return R.fail(e.getMessage());
    }

    /**
     * 主键或UNIQUE索引，数据重复异常
     */
    @ExceptionHandler(DuplicateKeyException.class)
    public R<Void> handleDuplicateKeyException(DuplicateKeyException e, HttpServletRequest request) {
        String requestUrl = request.getRequestURI();
        log.error("请求地址'{}',数据库中已存在记录'{}'", requestUrl, e.getMessage());
        return R.fail("数据库中已存在该记录，请联系管理员确认");
    }

    /**
     * Mybatis系统异常 通用处理
     */
    @ExceptionHandler(MyBatisSystemException.class)
    public R<Void> handleCannotFindDataSourceException(MyBatisSystemException e, HttpServletRequest request) {
        Throwable rootCause = getRootCause(e);
        // 2.1 如果是演示模式异常，按演示模式异常处理
        if (rootCause instanceof DemoModeException) {
            return handleDemoModeException((DemoModeException) rootCause);
        }
        String requestUrl = request.getRequestURI();
        String message = e.getMessage();
        if (message!=null && message.contains("CannotFindDataSourceException")) {
            log.error("请求地址'{}', 未找到数据源", requestUrl);
            return R.fail("未找到数据源，请联系管理员确认");
        }
        log.error("请求地址'{}', Mybatis系统异常", requestUrl, e);
        return R.fail(ErrorCodeEnum.INTERNAL_ERROR.getKey(), message != null ? message : ErrorCodeEnum.INTERNAL_ERROR.getValue());
    }

    // 3. 处理数据访问异常
    @ExceptionHandler(DataAccessException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public R<Void> handleDataAccessException(DataAccessException e) {
        Throwable rootCause = getRootCause(e);

        // 3.1 如果是演示模式异常，按演示模式异常处理
        if (rootCause instanceof DemoModeException) {
            return handleDemoModeException((DemoModeException) rootCause);
        }

        // 3.2 其他数据访问异常
        log.error("数据访问异常", e);
        return R.fail(ErrorCodeEnum.INTERNAL_ERROR.getKey(), "数据库访问错误");
    }

    // 4. 处理PersistenceException
    @ExceptionHandler(PersistenceException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public R<?> handlePersistenceException(PersistenceException e) {
        Throwable rootCause = getRootCause(e);

        // 4.1 如果是演示模式异常，按演示模式异常处理
        if (rootCause instanceof DemoModeException) {
            return handleDemoModeException((DemoModeException) rootCause);
        }

        // 4.2 其他持久化异常
        log.error("MyBatis持久化异常", e);
        return R.fail(ErrorCodeEnum.INTERNAL_ERROR.getKey(), "持久化操作失败");
    }


    /**
     * 拦截未知的运行时异常
     */
    @ExceptionHandler(RuntimeException.class)
    public R<Void> handleRuntimeException(RuntimeException e, HttpServletRequest request) {
        String requestUrl = request.getRequestURI();
        log.error("请求地址'{}',发生未知异常.", requestUrl, e);
        String msg = e.getMessage() != null ? e.getMessage() : ErrorCodeEnum.INTERNAL_ERROR.getValue();
        return R.fail(ErrorCodeEnum.INTERNAL_ERROR.getKey(), msg);
    }


    /**
     * 自定义验证异常
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public R<Void> handleMethodArgumentNotValidException(MethodArgumentNotValidException e, HttpServletRequest request) {

        String requestUrl = request.getRequestURI();
        log.error("请求地址'{}',发生验证异常:'{}'", requestUrl, e.getMessage());
        String message = Objects.requireNonNull(e.getBindingResult().getFieldError()).getDefaultMessage();
        return R.fail(message);
    }

    /**
     * 自定义验证异常
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public R<Void> handleMethodArgumentNotValidException(ConstraintViolationException e, HttpServletRequest request) {

        String requestUrl = request.getRequestURI();
        log.error("请求地址'{}',发生验证异常:'{}'", requestUrl, e.getMessage());
        String message = Objects.requireNonNull(e.getMessage());
        return R.fail(message);
    }

    /**
     * 租户异常
     */
    @ExceptionHandler(LoginException.class)
    public R<Void> loginException(LoginException e, HttpServletRequest request) {

        String requestUrl = request.getRequestURI();
        log.error("请求地址'{}',登录异常:'{}'", requestUrl, e.getMessage());
        String message = e.getMessage();
        return R.fail(message);
    }

    /**
     * 租户异常
     */
    @ExceptionHandler(TenantException.class)
    public R<Void> tenantException(TenantException e, HttpServletRequest request) {

        String requestUrl = request.getRequestURI();
        log.error("请求地址'{}',发生验证异常:'{}'", requestUrl, e.getMessage());
        String message = e.getMessage();
        return R.fail(message);
    }




    /**
     * 错误SQL语句异常
     */
    @ExceptionHandler(BadSqlGrammarException.class)
    public R<Void> handleBadSqlGrammarException(BadSqlGrammarException e,
                                                HttpServletRequest request) {
        String requestUrl = request.getRequestURI();
        log.error("请求地址'{}',发生数据库异常.", requestUrl, e);
        return R.fail(ErrorCodeEnum.INTERNAL_ERROR.getKey(), "数据库异常！");
    }

    /**
     * 拦截表示违反数据库的完整性约束导致的异常
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public R<Void> handleDataIntegrityViolationException(DataIntegrityViolationException e,
                                                         HttpServletRequest request) {
        String requestUrl = request.getRequestURI();
        log.error("请求地址'{}',发生数据库异常.", requestUrl, e);
        return R.fail(ErrorCodeEnum.INTERNAL_ERROR.getKey(), "数据库异常！");
    }

    /**
     * 拦截违反数据库的非完整性约束导致的异常，可能也会拦截一些也包括 SQL 语句错误、连接问题、权限问题等各种数据库异常
     */
    @ExceptionHandler(UncategorizedSQLException.class)
    public R<Void> handleUncategorizedSqlException(UncategorizedSQLException e,
                                                   HttpServletRequest request) {
        String requestUrl = request.getRequestURI();
        log.error("请求地址'{}',发生数据库异常.", requestUrl, e);
        return R.fail(ErrorCodeEnum.INTERNAL_ERROR.getKey(), "数据库异常！");
    }

    /**
     * 系统异常
     */
    @ExceptionHandler(MmsException.class)
    public R<Void> handleServiceException(MmsException e,
                                          HttpServletRequest request) {
        String requestUrl = request.getRequestURI();
        String message = e.getMessage();
        log.error("请求地址'{}','{}'.", requestUrl, message);
        return R.fail(e.getCode(), e.getMessage());
    }


    /**
     * 系统异常,全局异常
     */
    @ExceptionHandler(Exception.class)
    public R<Void> handleException(Exception e, HttpServletRequest request) {
        String requestUrl = request.getRequestURI();
        log.error("请求地址'{}',发生系统异常.", requestUrl, e);
        String msg = e.getMessage() != null ? e.getMessage() : ErrorCodeEnum.INTERNAL_ERROR.getValue();
        return R.fail(ErrorCodeEnum.INTERNAL_ERROR.getKey(), msg);
    }


}

