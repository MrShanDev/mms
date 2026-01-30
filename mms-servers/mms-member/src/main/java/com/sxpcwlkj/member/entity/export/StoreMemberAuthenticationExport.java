package com.sxpcwlkj.member.entity.export;

import com.sxpcwlkj.common.annotation.Dict;
import com.alibaba.excel.annotation.ExcelIgnore;
import com.sxpcwlkj.common.annotation.PrintColumn;
import com.sxpcwlkj.common.enums.PrintTypeEnum;
import java.io.Serial;

import com.sxpcwlkj.member.entity.vo.StoreMemberAuthenticationVo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import io.github.linpeilie.annotations.AutoMapper;

import com.sxpcwlkj.framework.entity.BaseEntityVo;
import com.sxpcwlkj.framework.interceptor.DictExcelConverter;
import com.alibaba.excel.annotation.ExcelProperty;
import java.util.Date;

/**
 * 会员认证Export
 *
 * @author mmsAdmin
 * @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
 */
@Data
@AutoMapper(target = StoreMemberAuthenticationVo.class)
@EqualsAndHashCode(callSuper=false)
public class StoreMemberAuthenticationExport  extends BaseEntityVo{
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @ExcelProperty("ID")
    @PrintColumn(title = "ID", type = PrintTypeEnum.TEXT)
    private  String id;
    /**
     * 会员ID
     */
    @ExcelProperty("会员ID")
    @PrintColumn(title = "会员ID", type = PrintTypeEnum.TEXT)
    private  String memberId;
    /**
     * 姓名
     */
    @ExcelProperty("姓名")
    @PrintColumn(title = "姓名", type = PrintTypeEnum.TEXT)
    private  String name;
    /**
     * 身份证号
     */
    @ExcelProperty("身份证号")
    @PrintColumn(title = "身份证号", type = PrintTypeEnum.TEXT)
    private  String number;
    /**
     * 手机号
     */
    @ExcelProperty("手机号")
    @PrintColumn(title = "手机号", type = PrintTypeEnum.TEXT)
    private  String phone;
    /**
     * 身份证正面
     */
    @ExcelProperty("身份证正面")
    @PrintColumn(title = "身份证正面", type = PrintTypeEnum.TEXT)
    private  String imageFront;
    /**
     * 身份证背面
     */
    @ExcelProperty("身份证背面")
    @PrintColumn(title = "身份证背面", type = PrintTypeEnum.TEXT)
    private  String imageBack;
    /**
     * 营业执照
     */
    @ExcelProperty("营业执照")
    @PrintColumn(title = "营业执照", type = PrintTypeEnum.TEXT)
    private  String businessLicense;
    /**
     * 性别
     */
    @ExcelProperty("性别")
    @PrintColumn(title = "性别", type = PrintTypeEnum.TEXT)
    private  String sex;
    /**
     * 地址
     */
    @ExcelProperty("地址")
    @PrintColumn(title = "地址", type = PrintTypeEnum.TEXT)
    private  String address;
    /**
     * 生日
     */
    @ExcelProperty("生日")
    @PrintColumn(title = "生日", type = PrintTypeEnum.TEXT)
    private  String nationality;
}

