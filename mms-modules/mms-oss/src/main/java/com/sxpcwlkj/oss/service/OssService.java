package com.sxpcwlkj.oss.service;

import com.sxpcwlkj.common.utils.R;
import jakarta.servlet.http.HttpServletRequest;
import org.dromara.x.file.storage.core.FileInfo;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Map;

/**
 * OssService
 *
 * @author mmsAdmin
 */
public interface OssService {

    FileInfo upload(MultipartFile file);

    /**
     * File文件上传
     *
     * @param file
     * @return
     */
    FileInfo upload(File file);

    /**
     * HttpServletRequest 方式上传
     *
     * @param request
     * @return
     */
    FileInfo upload(HttpServletRequest request);

    /**
     * 自定义文件上传
     *
     * @param file
     * @param w          上传后的宽度
     * @param h          上传后的高度
     * @param thumbnailW 缩略图宽度
     * @param thumbnailH 缩略图高度
     * @return
     */
    FileInfo upload(MultipartFile file, Integer w, Integer h, Integer thumbnailW, Integer thumbnailH);

    /**
     * el-upload 上传文件
     */
    R<Map<String,Object>> elUpload(MultipartFile file);


}
