package com.sxpcwlkj.system.entity.export;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.sxpcwlkj.common.annotation.Dict;
import com.sxpcwlkj.common.annotation.PrintColumn;
import com.sxpcwlkj.common.annotation.SensitivityEncrypt;
import com.sxpcwlkj.common.enums.PrintTypeEnum;
import com.sxpcwlkj.common.enums.SensitivityTypeEnum;
import com.sxpcwlkj.framework.entity.BaseEntityVo;
import com.sxpcwlkj.framework.interceptor.DictExcelConverter;
import com.sxpcwlkj.system.entity.vo.SysUserVo;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 用户导出VO
 * @author xijue
 * @Doc mmsadmin.cn
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AutoMapper(target = SysUserVo.class)
public class SysUserExportVo extends BaseEntityVo {

    /**
     * 主键ID
     */
    @ExcelProperty("用户ID")
    @PrintColumn(title = "用户ID", type = PrintTypeEnum.TEXT)
    private String userId;
    /**
     * 部门ID
     * 忽略这个字段
     */
    @ExcelIgnore
    private String deptId;
    /**
     * 部门名称
     */
    @ExcelProperty("部门名称")
    private String department;
    /**
     * 用户账号
     */
    @ExcelProperty("用户账号")
    @PrintColumn(title = "用户账号", type = PrintTypeEnum.TEXT)
    private String userName;
    /**
     * 用户昵称
     */
    @ExcelProperty(value = "用户昵称")
    @PrintColumn(title = "用户昵称", type = PrintTypeEnum.TEXT)
    private String nickName;
    /**
     * 用户类型
     */
    @ExcelProperty("用户类型")
    private String userType;
    /**
     * 邮箱
     */
    @ExcelProperty("邮箱")
    @PrintColumn(title = "邮箱", type = PrintTypeEnum.TEXT)
    private String email;
    /**
     * 手机号
     */
    @ExcelProperty("手机号")
    @SensitivityEncrypt(type = SensitivityTypeEnum.PHONE)
    @PrintColumn(title = "手机号", type = PrintTypeEnum.TEXT)
    private String phoneNumber;
    /**
     * 性别;0：保密 1：男2：女
     */
    @ExcelProperty(value = "性别", converter = DictExcelConverter.class)
    @Dict(value="SYS_SEX")
    @PrintColumn(title = "性别", type = PrintTypeEnum.TEXT)
    private String sex;
    /**
     * 头像
     */
    @ExcelProperty("头像")
    @PrintColumn(title = "头像", type = PrintTypeEnum.IMAGE, width = "50", height = "50")
    private String avatar;
    /**
     * 最后登录ip
     */
    @ExcelProperty("最后登录ip")
    private String loginIp;
    /**
     * 最后登录时间
     */
    @ExcelProperty("最后登录时间")
    @PrintColumn(title = "最后登录时间", type = PrintTypeEnum.TEXT)
    private Date loginDate;
    /**
     * 状态
     */
    @ExcelProperty(value = "状态", converter = DictExcelConverter.class)
    @Dict("SYS_STATE")
    private Integer status;
}
