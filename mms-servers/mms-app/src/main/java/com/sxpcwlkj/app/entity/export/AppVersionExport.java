package com.sxpcwlkj.app.entity.export;


import com.sxpcwlkj.app.entity.vo.AppVersionVo;
import com.sxpcwlkj.common.annotation.Dict;
import com.alibaba.excel.annotation.ExcelIgnore;
import com.sxpcwlkj.common.annotation.PrintColumn;
import com.sxpcwlkj.common.enums.PrintTypeEnum;
import java.io.Serial;
import lombok.Data;
import lombok.EqualsAndHashCode;
import io.github.linpeilie.annotations.AutoMapper;
import com.sxpcwlkj.framework.entity.BaseEntityVo;
import com.sxpcwlkj.framework.interceptor.DictExcelConverter;
import com.alibaba.excel.annotation.ExcelProperty;
import java.util.Date;

/**
 * App版本发布表Export
 *
 * @author mmsAdmin
 * @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
 */
@Data
@AutoMapper(target = AppVersionVo.class)
@EqualsAndHashCode(callSuper=false)
public class AppVersionExport  extends BaseEntityVo{
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @ExcelIgnore
    @ExcelProperty("主键ID")
    @PrintColumn(title = "主键ID", type = PrintTypeEnum.TEXT)
    private  Long id;
    /**
     * 应用编码（唯一标识）
     */
    @ExcelProperty("应用编码（唯一标识）")
    @PrintColumn(title = "应用编码（唯一标识）", type = PrintTypeEnum.TEXT)
    private  String appCode;
    /**
     * 应用名称
     */
    @ExcelProperty("应用名称")
    @PrintColumn(title = "应用名称", type = PrintTypeEnum.TEXT)
    private  String appName;
    /**
     * 平台类型
     */
    @Dict("appType")
    @ExcelProperty(value ="平台类型",converter = DictExcelConverter.class)
    @PrintColumn(title = "平台类型", type = PrintTypeEnum.TEXT)
    private  String platform;
    /**
     * 内部版本号（用于比较，如：1001）
     */
    @ExcelProperty("内部版本号（用于比较，如：1001）")
    @PrintColumn(title = "内部版本号（用于比较，如：1001）", type = PrintTypeEnum.TEXT)
    private  String versionCode;
    /**
     * 用户版本号（如：1.2.3）
     */
    @ExcelProperty("用户版本号（如：1.2.3）")
    @PrintColumn(title = "用户版本号（如：1.2.3）", type = PrintTypeEnum.TEXT)
    private  String versionName;
    /**
     * 构建号
     */
    @ExcelProperty("构建号")
    @PrintColumn(title = "构建号", type = PrintTypeEnum.TEXT)
    private  String buildNumber;
    /**
     * 下载地址
     */
    @ExcelProperty("下载地址")
    @PrintColumn(title = "下载地址", type = PrintTypeEnum.TEXT)
    private  String downloadUrl;
    /**
     * 文件大小（字节）
     */
    @ExcelProperty("文件大小（字节）")
    @PrintColumn(title = "文件大小（字节）", type = PrintTypeEnum.TEXT)
    private  Long fileSize;
    /**
     * 文件MD5校验值
     */
    @ExcelProperty("文件MD5校验值")
    @PrintColumn(title = "文件MD5校验值", type = PrintTypeEnum.TEXT)
    private  String fileMd5;
    /**
     * 更新说明
     */
    @ExcelProperty("更新说明")
    @PrintColumn(title = "更新说明", type = PrintTypeEnum.TEXT)
    private  String releaseNotes;
    /**
     * 是否强制更新（0-否，1-是）
     */
    @Dict("SYS_IS")
    @ExcelProperty(value ="是否强制更新（0-否，1-是）",converter = DictExcelConverter.class)
    @PrintColumn(title = "是否强制更新（0-否，1-是）", type = PrintTypeEnum.TEXT)
    private  Integer isForceUpdate;
    /**
     * 最小支持版本
     */
    @ExcelProperty("最小支持版本")
    @PrintColumn(title = "最小支持版本", type = PrintTypeEnum.TEXT)
    private  String minRequiredVersion;
    /**
     * 发布方式
     */
    @ExcelProperty("发布方式")
    @PrintColumn(title = "发布方式", type = PrintTypeEnum.TEXT)
    private  String publishType;
    /**
     * 发布状态
     */
    @ExcelProperty("发布状态")
    @PrintColumn(title = "发布状态", type = PrintTypeEnum.TEXT)
    private  String publishStatus;
    /**
     * 发布时间
     */
    @ExcelProperty("发布时间")
    @PrintColumn(title = "发布时间", type = PrintTypeEnum.TEXT)
    private  Date publishTime;
    /**
     * 发布人
     */
    @ExcelProperty("发布人")
    @PrintColumn(title = "发布人", type = PrintTypeEnum.TEXT)
    private  String publishUser;
}
