package com.sxpcwlkj.framework.utils;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.read.listener.PageReadListener;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * excel工具类
 *
 * @author xijue
 * @Doc mmsadmin.cn
 */
public class ExcelUtil {


    /**
     * 模版下载
     *
     * @param response response
     * @param clazz    class
     */
    public static <T> void download(HttpServletResponse response, Class<T> clazz) throws IOException {
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        String fileName = URLEncoder.encode("模版文件", "UTF-8");
        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
        EasyExcel.write(response.getOutputStream(), clazz).sheet("模板").doWrite(new ArrayList<>());
    }

    /**
     * 模版下载
     *
     * @param response  response
     * @param clazz     class
     * @param sheetName 表名称
     */
    public static <T> void download(HttpServletResponse response, Class<T> clazz, String sheetName) throws IOException {
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        String fileName = URLEncoder.encode(sheetName, "UTF-8");
        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
        EasyExcel.write(response.getOutputStream(), clazz).sheet("模板").doWrite(new ArrayList<>());
    }

    /**
     * 数据导出
     *
     * @param response  response
     * @param clazz     class
     * @param sheetName 表名称
     */
    public static <T> void export(HttpServletResponse response, Class<T> clazz, String sheetName, List<T> list) throws IOException {
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        String fileName = URLEncoder.encode(sheetName, "UTF-8");
        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
        EasyExcel.write(response.getOutputStream(), clazz).sheet("模板").doWrite(list);
    }

    /**
     * 数据导入(去重)
     *
     * @param file  文件
     * @param clazz 类
     */
    public static <T> Set<T> imports(MultipartFile file, Class<T> clazz) throws IOException {
        // 读取Excel文件
        Set<T> data = new LinkedHashSet<>();
        // 使用EasyExcel读取Excel文件
        // 添加到Set中自动去重
        EasyExcel.read(file.getInputStream(), clazz, new PageReadListener<T>(data::addAll)).sheet().doRead();
        return data;
    }

    /**
     * 数据导入(去重)
     *
     * @param file  文件
     * @param clazz 类
     */
    public static <T> List<T> importsData(MultipartFile file, Class<T> clazz) throws IOException {
        // 读取Excel文件
        List<T> data = new ArrayList<>();
        // 使用EasyExcel读取Excel文件
        // 添加到Set中自动去重
        EasyExcel.read(file.getInputStream(), clazz, new PageReadListener<T>(data::addAll)).sheet().doRead();
        return data;
    }
}
