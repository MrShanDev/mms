package com.sxpcwlkj.demo.oss;

import com.sxpcwlkj.common.utils.R;
import jakarta.servlet.http.HttpServletRequest;
import org.dromara.x.file.storage.core.FileInfo;
import org.springframework.web.multipart.MultipartFile;

public interface DemoOssService {

    /**
     * 上传文件
     */
    R<FileInfo> elUpload(MultipartFile file);

    /**
     * 上传文件
     */
    FileInfo upload(MultipartFile file);

    /**
     * 上传文件，成功返回文件 url
     */

    String upload2(MultipartFile file);

    /**
     * 上传图片，成功返回文件信息
     * 图片处理使用的是 https://github.com/coobird/thumbnailator
     */

    FileInfo uploadImage(MultipartFile file);

    /**
     * 上传文件到指定存储平台，成功返回文件信息
     */

    FileInfo uploadPlatform(MultipartFile file);

    /**
     * 直接读取 HttpServletRequest 中的文件进行上传，成功返回文件信息
     * 使用这种方式有些注意事项，请查看文档 基础功能-上传 章节
     */

    FileInfo uploadPlatform(HttpServletRequest request);

}
