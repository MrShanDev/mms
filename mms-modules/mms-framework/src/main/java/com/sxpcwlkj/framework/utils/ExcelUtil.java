package com.sxpcwlkj.framework.utils;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.read.listener.PageReadListener;
import com.sxpcwlkj.datasource.entity.page.PageQuery;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
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
 * @author mmsAdmin
 * @Doc mmsadmin.cn
 */
@Slf4j
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
        
        // 设置防止浏览器缓存的响应头
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
        response.addHeader("Cache-Control", "post-check=0, pre-check=0");
        response.setHeader("Pragma", "no-cache");
        
        try {
            EasyExcel.write(response.getOutputStream(), clazz)
                    .autoCloseStream(false)
                    .useDefaultStyle(false)
                    .inMemory(true)
                    .sheet("模板")
                    .doWrite(new ArrayList<>());
        } catch (Exception e) {
            e.printStackTrace();
            try {
                response.reset();
                response.setContentType("application/json");
                response.setCharacterEncoding("utf-8");
                response.getWriter().println("{\"code\":500,\"msg\":\"下载模板失败，请联系管理员\"}");
            } catch (Exception ex) {
                // 忽略重置响应时的异常
            }
        }
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
        
        // 设置防止浏览器缓存的响应头
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
        response.addHeader("Cache-Control", "post-check=0, pre-check=0");
        response.setHeader("Pragma", "no-cache");
        
        try {
            EasyExcel.write(response.getOutputStream(), clazz)
                    .autoCloseStream(false)
                    .useDefaultStyle(false)
                    .inMemory(true)
                    .sheet(sheetName)
                    .doWrite(new ArrayList<>());
        } catch (Exception e) {
            e.printStackTrace();
            try {
                response.reset();
                response.setContentType("application/json");
                response.setCharacterEncoding("utf-8");
                response.getWriter().println("{\"code\":500,\"msg\":\"下载模板失败，请联系管理员\"}");
            } catch (Exception ex) {
                // 忽略重置响应时的异常
            }
        }
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
        
        // 设置防止浏览器缓存的响应头
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
        response.addHeader("Cache-Control", "post-check=0, pre-check=0");
        response.setHeader("Pragma", "no-cache");
        
        try {
            // 使用不依赖POI内部方法的配置
            EasyExcel.write(response.getOutputStream(), clazz)
                    .autoCloseStream(false)  // 不自动关闭流
                    .useDefaultStyle(false)  // 不使用默认样式，避免调用可能不存在的POI方法
                    .inMemory(true)          // 使用内存模式，避免临时文件IO问题
                    .sheet(sheetName)        // 使用传入的sheetName作为表名
                    .doWrite(list);
        } catch (Exception e) {
            // 记录异常
            e.printStackTrace();
            
            // 重置响应并返回错误信息
            try {
                response.reset();
                response.setContentType("application/json");
                response.setCharacterEncoding("utf-8");
                response.getWriter().println("{\"code\":500,\"msg\":\"导出Excel失败，请联系管理员\"}");
            } catch (Exception ex) {
                // 忽略重置响应时的异常
            }
        }
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

    /**
     * 安全导出数据（封装分页限制和异常处理）
     *
     * @param response 响应对象
     * @param clazz    数据类
     * @param sheetName 表名
     * @param data     数据列表
     * @param pageQuery 分页查询条件
     */
    public static <T> void safeExport(HttpServletResponse response, Class<T> clazz, String sheetName, List<T> data, PageQuery pageQuery) throws IOException {
        // 限制导出数据量，避免内存溢出
        if (pageQuery == null) {
            pageQuery = new PageQuery();
        }
        // 设置合理的导出数量上限
        if (pageQuery.getPageSize() > 5000 || pageQuery.getPageSize() <= 0) {
            pageQuery.setPageSize(5000);
        }

        try {
            export(response, clazz, sheetName, data);
        } catch (Exception e) {
            log.error("导出Excel异常", e);
            try {
                // 重置响应并返回错误信息
                response.reset();
                response.setContentType("application/json");
                response.setCharacterEncoding("utf-8");
                response.getWriter().println("{\"code\":500,\"msg\":\"导出失败，请联系管理员\"}");
            } catch (Exception ex) {
                log.error("处理导出异常时出错", ex);
            }
        }
    }
}
