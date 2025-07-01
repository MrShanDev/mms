package com.sxpcwlkj.oss.service.impl;

import com.sxpcwlkj.common.code.entity.FileStorageConfig;
import com.sxpcwlkj.common.enums.ConfigKeyNum;
import com.sxpcwlkj.common.enums.OssTypeEnum;
import com.sxpcwlkj.oss.service.MyFileStorageService;
import com.sxpcwlkj.redis.RedisUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.x.file.storage.core.FileStorageProperties;
import org.dromara.x.file.storage.core.FileStorageProperties.*;
import org.dromara.x.file.storage.core.FileStorageService;
import org.dromara.x.file.storage.core.FileStorageServiceBuilder;
import org.dromara.x.file.storage.core.platform.FileStorage;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author mmsAdmin
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class MyFileStorageServiceImpl implements MyFileStorageService {
    private final FileStorageService fileStorageService;

    @EventListener
    public void init(ContextRefreshedEvent event) {
        initFileService();
    }

    @Override
    public Boolean initFileService() {
        List<FileStorageConfig> configs=RedisUtil.getCacheList(ConfigKeyNum.config_oss.getKey());
        if(configs==null|| configs.isEmpty()){
            return Boolean.FALSE;
        }
        CopyOnWriteArrayList<FileStorage> list = fileStorageService.getFileStorageList();

        configs.forEach(config -> {
            //先删除
            FileStorage myStorage = fileStorageService.getFileStorage(config.getPlatform()+"-1");
            if(myStorage!=null){
                list.remove(myStorage);
                myStorage.close();//释放资源
            }
            // 添加
            if(OssTypeEnum.aliyun.getType().equals(config.getPlatform())){
                //阿里云oss配置
                AliyunOssConfig aliyunOssConfig=new AliyunOssConfig();
                aliyunOssConfig.setPlatform(config.getPlatform()+"-1");
                aliyunOssConfig.setAccessKey(config.getAccessKey());
                aliyunOssConfig.setSecretKey(config.getSecretKey());
                aliyunOssConfig.setBucketName(config.getBucketName());
                aliyunOssConfig.setEndPoint(config.getEndPoint());
                aliyunOssConfig.setDomain(config.getDomain()+"/");
                aliyunOssConfig.setBasePath(config.getBasePath()+"/");
                list.addAll(FileStorageServiceBuilder.buildAliyunOssFileStorage(Collections.singletonList(aliyunOssConfig),null));
            }
            if(OssTypeEnum.tencent.getType().equals(config.getPlatform())){
                //腾讯云oss配置
                TencentCosConfig tencentOssConfig=new TencentCosConfig();
                tencentOssConfig.setPlatform(config.getPlatform()+"-1");
                tencentOssConfig.setSecretId(config.getAccessKey());
                tencentOssConfig.setSecretKey(config.getSecretKey());
                tencentOssConfig.setBucketName(config.getBucketName());
                tencentOssConfig.setRegion(config.getEndPoint());
                tencentOssConfig.setDomain(config.getDomain());
                tencentOssConfig.setBasePath("/"+config.getBasePath()+"/");
                tencentOssConfig.setRegion(config.getRegion());
                list.addAll(FileStorageServiceBuilder.buildTencentCosFileStorage(Collections.singletonList(tencentOssConfig),null));
            }
            if(OssTypeEnum.huawei.getType().equals(config.getPlatform())){
                //华为云oss配置
                HuaweiObsConfig huaweiOssConfig=new HuaweiObsConfig();
                huaweiOssConfig.setPlatform(config.getPlatform()+"-1");
                huaweiOssConfig.setAccessKey(config.getAccessKey());
                huaweiOssConfig.setSecretKey(config.getSecretKey());
                huaweiOssConfig.setBucketName(config.getBucketName());
                huaweiOssConfig.setEndPoint(config.getEndPoint());
                huaweiOssConfig.setDomain(config.getDomain()+"/");
                huaweiOssConfig.setBasePath(config.getBasePath()+"/");
                list.addAll(FileStorageServiceBuilder.buildHuaweiObsFileStorage(Collections.singletonList(huaweiOssConfig),null));
            }
            if(OssTypeEnum.baidu.getType().equals(config.getPlatform())){
                //百度云oss配置
                BaiduBosConfig baiduOssConfig=new BaiduBosConfig();
                baiduOssConfig.setPlatform(config.getPlatform()+"-1");
                baiduOssConfig.setAccessKey(config.getAccessKey());
                baiduOssConfig.setSecretKey(config.getSecretKey());
                baiduOssConfig.setBucketName(config.getBucketName());
                baiduOssConfig.setEndPoint(config.getEndPoint());
                baiduOssConfig.setDomain(config.getDomain()+"/");
                baiduOssConfig.setBasePath(config.getBasePath()+"/");
                list.addAll(FileStorageServiceBuilder.buildBaiduBosFileStorage(Collections.singletonList(baiduOssConfig),null));
            }
            if (OssTypeEnum.qiniu.getType().equals(config.getPlatform())){
                //七牛云oss配置
                QiniuKodoConfig qiniuOssConfig=new QiniuKodoConfig();
                qiniuOssConfig.setPlatform(config.getPlatform()+"-1");
                qiniuOssConfig.setAccessKey(config.getAccessKey());
                qiniuOssConfig.setSecretKey(config.getSecretKey());
                qiniuOssConfig.setBucketName(config.getBucketName());
                qiniuOssConfig.setDomain(config.getDomain()+"/");
                qiniuOssConfig.setBasePath(config.getBasePath()+"/");
                list.addAll(FileStorageServiceBuilder.buildQiniuKodoFileStorage(Collections.singletonList(qiniuOssConfig),null));
            }
            if(OssTypeEnum.amazon.getType().equals(config.getPlatform())){
                //亚马逊oss配置
                AmazonS3Config amazonOssConfig=new AmazonS3Config();
                amazonOssConfig.setPlatform(config.getPlatform()+"-1");
                amazonOssConfig.setAccessKey(config.getAccessKey());
                amazonOssConfig.setSecretKey(config.getSecretKey());
                amazonOssConfig.setBucketName(config.getBucketName());
                amazonOssConfig.setEndPoint(config.getEndPoint());
                amazonOssConfig.setDomain(config.getDomain()+"/"+config.getBucketName()+"/");
                amazonOssConfig.setBasePath(config.getBasePath()+"/");
                //amazonOssConfig.setRegion(config.getRegion());
                list.addAll(FileStorageServiceBuilder.buildAmazonS3FileStorage(Collections.singletonList(amazonOssConfig),null));
            }
            if(OssTypeEnum.minio.getType().equals(config.getPlatform())){
                //minio配置
                MinioConfig minioConfig=new MinioConfig();
                minioConfig.setPlatform(config.getPlatform()+"-1");
                minioConfig.setAccessKey(config.getAccessKey());
                minioConfig.setSecretKey(config.getSecretKey());
                minioConfig.setBucketName(config.getBucketName());
                minioConfig.setEndPoint(config.getEndPoint());
                minioConfig.setDomain(config.getDomain()+"/");
                minioConfig.setBasePath(config.getBasePath());
                list.addAll(FileStorageServiceBuilder.buildMinioFileStorage(Collections.singletonList(minioConfig),null));
            }
            if(OssTypeEnum.local.getType().equals(config.getPlatform())){
                //本地配置
                LocalPlusConfig localPlusConfig=new LocalPlusConfig();
                localPlusConfig.setPlatform(config.getPlatform()+"-1");
                localPlusConfig.setStoragePath(config.getStoragePath());
                localPlusConfig.setDomain(config.getDomain()+"/");
                localPlusConfig.setBasePath(config.getBasePath()+"/");
                list.addAll(FileStorageServiceBuilder.buildLocalPlusFileStorage(Collections.singletonList(localPlusConfig)));
            }
        });
        FileStorageProperties properties = fileStorageService.getProperties();
        properties.setDefaultPlatform(configs.get(0).getPlatform()+"-1");
        fileStorageService.setProperties(properties);

        log.info("初始化 OSS 配置");
        return Boolean.TRUE;
    }
}
