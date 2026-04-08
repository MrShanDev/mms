package com.sxpcwlkj.datasource.handler;

import cn.dev33.satoken.exception.NotWebContextException;
import cn.dev33.satoken.exception.SaTokenContextException;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.http.HttpStatus;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.sxpcwlkj.authority.LoginObject;
import com.sxpcwlkj.common.enums.SystemCommonEnum;
import com.sxpcwlkj.common.exception.MmsException;
import com.sxpcwlkj.datasource.entity.BaseEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @Description
 * @Author sxpcwlkj
 * @Version v1.0.0
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class MybatisPlusMetaObjectHandler implements MetaObjectHandler {

    /**
     * Servlet 异步线程（如 {@code StreamingResponseBody}）、线程池任务等场景无 Sa-Token Web 上下文，
     * {@link StpUtil#isLogin()} 会抛 {@link SaTokenContextException}；与 {@link com.sxpcwlkj.authority.LoginObject#isLogin()} 对齐，视为未登录并跳过按登录态填充。
     */
    private static boolean isLoginWithWebContext() {
        try {
            return StpUtil.isLogin();
        } catch (NotWebContextException | SaTokenContextException e) {
            return false;
        }
    }

    @Override
    public void insertFill(MetaObject metaObject) {
        try {
            if (ObjectUtil.isNotNull(metaObject) && metaObject.getOriginalObject() instanceof BaseEntity baseEntity) {
                Date current = ObjectUtil.isNotNull(baseEntity.getCreatedTime())
                        ? baseEntity.getCreatedTime() : new Date();
                baseEntity.setCreatedTime(current);
                baseEntity.setUpdatedTime(current);
                if(baseEntity.getStatus()==null){
                    baseEntity.setStatus(SystemCommonEnum.SYS_COMMON_STATE_OPEN.getValue());
                }
                if(baseEntity.getSort()==null){
                    baseEntity.setSort(0);
                }
                baseEntity.setRevision(1L);
                if (isLoginWithWebContext()) {
                    Long userId = ObjectUtil.isNotNull(baseEntity.getCreatedBy()) ? baseEntity.getCreatedBy() : StpUtil.getLoginIdAsLong();
                    // 当前已登录 且 创建人为空 则填充
                    baseEntity.setCreatedBy(userId);
                    // 当前已登录 且 更新人为空 则填充
                    baseEntity.setUpdatedBy(userId);
                    // 当前已登录 且 租户为空 则填充
                    baseEntity.setTenantId(getValidTenantId());
                }
            }
        } catch (Exception e) {
            throw new MmsException("自动注入异常 => " + e.getMessage(), HttpStatus.HTTP_UNAUTHORIZED);
        }
    }
    /**
     * 获取租户号
     */
    private String getValidTenantId() {
        String tenantId = LoginObject.getLoginTenant();
        return tenantId != null ? tenantId : "default_tenant";
    }
    @Override
    public void updateFill(MetaObject metaObject) {
        try {
            if (ObjectUtil.isNotNull(metaObject) && metaObject.getOriginalObject() instanceof BaseEntity baseEntity) {
                Date current = new Date();
                // 更新时间填充(不管为不为空)
                baseEntity.setUpdatedTime(current);
                // 当前已登录 更新人填充(不管为不为空)
                if (isLoginWithWebContext()) {
                    baseEntity.setUpdatedBy(StpUtil.getLoginIdAsLong());
                }
            }
        } catch (Exception e) {
            throw new MmsException("自动注入异常 => " + e.getMessage(), HttpStatus.HTTP_UNAUTHORIZED);
        }
    }



}
