package com.sxpcwlkj.log.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 操作日志实体
 *
 * @author mmsAdmin
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("sys_log")
public class SysOperLog implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 日志主键
     */
    @TableId(type = IdType.AUTO)
    private Long operId;

    /**
     * 租户ID
     */
    private Long tenantId;

    /**
     * 模块名称
     */
    private String module;

    /**
     * 操作类型(0其它 1新增 2修改 3删除 4查询 5导出 6导入 7登录 8退出 9授权 10清空)
     */
    private Integer operType;

    /**
     * 操作描述
     */
    private String description;

    /**
     * 请求方法
     */
    private String requestMethod;

    /**
     * 操作方法(类名.方法名)
     */
    private String method;

    /**
     * 请求URL
     */
    private String operUrl;

    /**
     * 操作人员ID
     */
    private Long userId;

    /**
     * 操作人员账号
     */
    private String userName;

    /**
     * 操作人员角色
     */
    private String userRoles;

    /**
     * 主机地址
     */
    private String operIp;

    /**
     * 操作地点
     */
    private String operLocation;

    /**
     * 请求参数
     */
    private String operParam;

    /**
     * 操作前数据
     */
    private String beforeData;

    /**
     * 返回结果
     */
    private String jsonResult;

    /**
     * 操作状态(0成功 1失败)
     */
    private Integer status;

    /**
     * 错误消息
     */
    private String errorMsg;

    /**
     * 操作时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime operTime;

    /**
     * 消耗时间(毫秒)
     */
    private Long costTime;

    /**
     * 用户代理
     */
    private String userAgent;

    /**
     * 浏览器类型
     */
    private String browser;

    /**
     * 操作系统
     */
    private String os;
}
