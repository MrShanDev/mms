package com.sxpcwlkj.demo.oss.impl;

import com.sxpcwlkj.common.utils.R;
import com.sxpcwlkj.demo.oss.DemoOssService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.dromara.x.file.storage.core.FileInfo;
import org.dromara.x.file.storage.core.FileStorageService;
import org.dromara.x.file.storage.core.constant.Constant;
import org.dromara.x.file.storage.core.platform.FileStorage;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class DemoOssServiceImpl implements DemoOssService {

    private final FileStorageService fileStorageService;

    @Override
    public R<FileInfo> elUpload(MultipartFile file) {

        FileInfo fileInfo = null;
        fileInfo = fileStorageService.of(file).upload();  //将文件上传到对应地方
        if (fileInfo == null) {
            return R.fail("上传失败！");
        }
        //判断对应的存储平台是否支持 ACL
        FileStorage storage = fileStorageService.getFileStorage();
        boolean supportACL = fileStorageService.isSupportAcl(storage);
        if(supportACL){
            //文件上传成功后修改 ACL 为公共读
            fileStorageService.setFileAcl(fileInfo, Constant.ACL.PUBLIC_READ);
            fileStorageService.setThFileAcl(fileInfo, Constant.ACL.PUBLIC_READ);
        }
        return R.success(fileInfo);
    }

    @Override
    public FileInfo upload(MultipartFile file) {
        FileInfo fileInfo = null;
        fileInfo = fileStorageService.of(file).upload();  //将文件上传到对应地方
        if (fileInfo == null) {
            throw  new RuntimeException("上传失败！");
        }
        //判断对应的存储平台是否支持 ACL
        FileStorage storage = fileStorageService.getFileStorage();
        boolean supportACL = fileStorageService.isSupportAcl(storage);
        if(supportACL){
            //文件上传成功后修改 ACL 为公共读
            fileStorageService.setFileAcl(fileInfo, Constant.ACL.PUBLIC_READ);
            fileStorageService.setThFileAcl(fileInfo, Constant.ACL.PUBLIC_READ);
        }
        return fileInfo;
    }

    @Override
    public String upload2(MultipartFile file) {
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
        if (fileInfo == null) {
            throw  new RuntimeException("上传失败！");
        }
        //判断对应的存储平台是否支持 ACL
        FileStorage storage = fileStorageService.getFileStorage();
        boolean supportACL = fileStorageService.isSupportAcl(storage);
        if(supportACL){
            //文件上传成功后修改 ACL 为公共读
            fileStorageService.setFileAcl(fileInfo, Constant.ACL.PUBLIC_READ);
            fileStorageService.setThFileAcl(fileInfo, Constant.ACL.PUBLIC_READ);
        }
        return  fileInfo.getUrl();
    }

    @Override
    public FileInfo uploadImage(MultipartFile file) {
        FileInfo fileInfo =fileStorageService.of(file)
                //将图片大小调整到 1000*1000
                .image(img -> img.size(1000, 1000))
                //再生成一张 200*200 的缩略图
                .thumbnail(th -> th.size(200, 200)).upload();

        if (fileInfo == null) {
            throw  new RuntimeException("上传失败！");
        }
        //判断对应的存储平台是否支持 ACL
        FileStorage storage = fileStorageService.getFileStorage();
        boolean supportACL = fileStorageService.isSupportAcl(storage);
        if(supportACL){
            //文件上传成功后修改 ACL 为公共读
            fileStorageService.setFileAcl(fileInfo, Constant.ACL.PUBLIC_READ);
            fileStorageService.setThFileAcl(fileInfo, Constant.ACL.PUBLIC_READ);
        }
        return fileInfo;
    }

    @Override
    public FileInfo uploadPlatform(MultipartFile file) {
        FileInfo fileInfo = fileStorageService.of(file)
                //使用指定的存储平台
                .setPlatform("aliyun-oss-1").upload();
        //判断对应的存储平台是否支持 ACL
        FileStorage storage = fileStorageService.getFileStorage();
        boolean supportACL = fileStorageService.isSupportAcl(storage);
        if(supportACL){
            //文件上传成功后修改 ACL 为公共读
            fileStorageService.setFileAcl(fileInfo, Constant.ACL.PUBLIC_READ);
            fileStorageService.setThFileAcl(fileInfo, Constant.ACL.PUBLIC_READ);
        }
        return fileInfo;
    }

    @Override
    public FileInfo uploadPlatform(HttpServletRequest request) {
        FileInfo fileInfo = fileStorageService.of(request).upload();
        //判断对应的存储平台是否支持 ACL
        FileStorage storage = fileStorageService.getFileStorage();
        boolean supportACL = fileStorageService.isSupportAcl(storage);
        if(supportACL){
            //文件上传成功后修改 ACL 为公共读
            fileStorageService.setFileAcl(fileInfo, Constant.ACL.PUBLIC_READ);
            fileStorageService.setThFileAcl(fileInfo, Constant.ACL.PUBLIC_READ);
        }
        return fileInfo;
    }
}
