package com.sxpcwlkj.log.utils;

import com.sxpcwlkj.log.enums.OperationType;
import com.sxpcwlkj.log.service.OperLogService;
import org.springframework.stereotype.Component;

/**
 * MMS 日志工具类 - 静态方法，无需注入
 *
 * @author mmsAdmin
 */
@Component
public class MmsLog {

    private static OperLogService operLogService;

    public MmsLog(OperLogService operLogService) {
        MmsLog.operLogService = operLogService;
    }

    /**
     * 记录日志
     */
    public static void log(String module, OperationType operType, String description) {
        if (operLogService != null) {
            operLogService.log(module, operType, description);
        }
    }

    /**
     * 记录日志 - 带结果
     */
    public static void log(String module, OperationType operType, String description, Object result) {
        if (operLogService != null) {
            operLogService.log(module, operType, description, result);
        }
    }

    /**
     * 记录错误日志
     */
    public static void logError(String module, OperationType operType, String description, Exception e) {
        if (operLogService != null) {
            operLogService.logError(module, operType, description, e);
        }
    }

    /**
     * 快速记录 - 新增（自动识别模块）
     */
    public static void insert(String description) {
        log(autoModule(), OperationType.INSERT, description);
    }

    /**
     * 快速记录 - 新增（指定模块）
     */
    public static void insert(String module, String description) {
        log(module, OperationType.INSERT, description);
    }

    /**
     * 快速记录 - 修改（自动识别模块）
     */
    public static void update(String description) {
        log(autoModule(), OperationType.UPDATE, description);
    }

    /**
     * 快速记录 - 修改（指定模块）
     */
    public static void update(String module, String description) {
        log(module, OperationType.UPDATE, description);
    }

    /**
     * 快速记录 - 删除（自动识别模块）
     */
    public static void delete(String description) {
        log(autoModule(), OperationType.DELETE, description);
    }

    /**
     * 快速记录 - 删除（指定模块）
     */
    public static void delete(String module, String description) {
        log(module, OperationType.DELETE, description);
    }

    /**
     * 快速记录 - 查询（自动识别模块）
     */
    public static void select(String description) {
        log(autoModule(), OperationType.SELECT, description);
    }

    /**
     * 快速记录 - 查询（指定模块）
     */
    public static void select(String module, String description) {
        log(module, OperationType.SELECT, description);
    }

    /**
     * 快速记录 - 导出（自动识别模块）
     */
    public static void export(String description) {
        log(autoModule(), OperationType.EXPORT, description);
    }

    /**
     * 快速记录 - 导出（指定模块）
     */
    public static void export(String module, String description) {
        log(module, OperationType.EXPORT, description);
    }

    /**
     * 快速记录 - 导入（自动识别模块）
     */
    public static void imports(String description) {
        log(autoModule(), OperationType.IMPORT, description);
    }

    /**
     * 快速记录 - 导入（指定模块）
     */
    public static void imports(String module, String description) {
        log(module, OperationType.IMPORT, description);
    }

    /**
     * 快速记录 - 清空（自动识别模块）
     */
    public static void clean(String description) {
        log(autoModule(), OperationType.CLEAN, description);
    }

    /**
     * 快速记录 - 清空（指定模块）
     */
    public static void clean(String module, String description) {
        log(module, OperationType.CLEAN, description);
    }

    /**
     * 自动识别模块名称（从调用栈获取类名）
     */
    private static String autoModule() {
        try {
            StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
            // stackTrace[0] = getStackTrace
            // stackTrace[1] = autoModule
            // stackTrace[2] = insert/update/delete等方法
            // stackTrace[3] = 实际调用的类
            if (stackTrace.length > 3) {
                String className = stackTrace[3].getClassName();
                String simpleName = className.substring(className.lastIndexOf('.') + 1);
                
                // 根据类名推断模块
                if (simpleName.contains("User")) return "用户管理";
                if (simpleName.contains("Role")) return "角色管理";
                if (simpleName.contains("Dept")) return "部门管理";
                if (simpleName.contains("Menu")) return "菜单管理";
                if (simpleName.contains("Dict")) return "字典管理";
                if (simpleName.contains("Config")) return "参数管理";
                if (simpleName.contains("Notice")) return "通知管理";
                if (simpleName.contains("Log")) return "日志管理";
                if (simpleName.contains("Post")) return "岗位管理";
                if (simpleName.contains("Order")) return "订单管理";
                if (simpleName.contains("Product")) return "商品管理";
                
                return "系统管理";
            }
        } catch (Exception e) {
            // 异常时返回默认值
        }
        return "系统管理";
    }
}
