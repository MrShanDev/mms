package com.sxpcwlkj.oss.service.impl;

import com.sxpcwlkj.common.utils.R;
import com.sxpcwlkj.oss.service.OssService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.dromara.x.file.storage.core.FileInfo;
import org.dromara.x.file.storage.core.FileStorageService;
import org.dromara.x.file.storage.core.constant.Constant;
import org.dromara.x.file.storage.core.platform.FileStorage;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author mmsAdmin
 */
@Service
@RequiredArgsConstructor
public class OssServiceImpl implements OssService {

    private final FileStorageService fileStorageService;

    @Override
    public FileInfo upload(MultipartFile file) {

        FileInfo fileInfo = fileStorageService.of(file)
                //保存到相对路径下，为了方便管理，不需要可以不写
                .setPath("upload/")
                //关联对象id，为了方便管理，不需要可以不写
                .setObjectId("0")
                //关联对象类型，为了方便管理，不需要可以不写
                .setObjectType("0")
                //保存一些属性，可以在切面、保存上传记录、自定义存储平台等地方获取使用，不需要可以不写
                .putAttr("role", "admin")
                //将文件上传到对应地方
                .upload();
        return uploadCommon(fileInfo);
    }

    @Override
    public FileInfo upload(File file) {
        //将文件上传到对应地方
        FileInfo fileInfo = fileStorageService.of(file).setPath("upload/").upload();
        return uploadCommon(fileInfo);
    }

    @Override
    public FileInfo upload(HttpServletRequest request) {
        FileInfo fileInfo = fileStorageService.of(request).upload();
        return uploadCommon(fileInfo);
    }

    @Override
    public FileInfo upload(MultipartFile file, Integer w, Integer h, Integer thumbnailW, Integer thumbnailH) {
        FileInfo fileInfo = fileStorageService.of(file)
                //将图片大小调整到 1000*1000
                .image(img -> img.size(w, h))
                //再生成一张 200*200 的缩略图
                .thumbnail(th -> th.size(thumbnailW, thumbnailH)).upload();

        return uploadCommon(fileInfo);
    }

    @Override
    public R<Map<String,Object>> elUpload(MultipartFile file) {
        Map<String,Object> end=new HashMap<>();
        end.put("errno", 0);
        Map<String,Object> data=new HashMap<>();
        data.put("url", fileStorageService.of(file).upload().getUrl());
        end.put("data", data);
        return R.success(end);
    }

    private FileInfo uploadCommon(FileInfo fileInfo) {
        if (Objects.isNull(fileInfo)) {
            throw new RuntimeException("上传失败！");
        }
        //判断对应的存储平台是否支持 ACL
        FileStorage storage = fileStorageService.getFileStorage();
        boolean supportAcl = fileStorageService.isSupportAcl(storage);
        if (supportAcl) {
            //文件上传成功后修改 ACL 为公共读
            fileStorageService.setFileAcl(fileInfo, Constant.ACL.PUBLIC_READ);
            fileStorageService.setThFileAcl(fileInfo, Constant.ACL.PUBLIC_READ);
        }
        return fileInfo;
    }

}
