package com.sxpcwlkj.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sxpcwlkj.common.enums.OssTypeEnum;
import com.sxpcwlkj.common.utils.DataUtil;
import com.sxpcwlkj.common.utils.MapstructUtil;
import com.sxpcwlkj.common.utils.StringUtil;
import com.sxpcwlkj.datasource.entity.page.PageQuery;
import com.sxpcwlkj.datasource.entity.page.TableDataInfo;
import com.sxpcwlkj.system.entity.SysOss;
import com.sxpcwlkj.system.entity.bo.SysOssBo;
import com.sxpcwlkj.system.entity.vo.SysOssConfigVo;
import com.sxpcwlkj.system.entity.vo.SysOssVo;
import com.sxpcwlkj.system.mapper.SysOssMapper;
import com.sxpcwlkj.system.service.SysOssConfigService;
import com.sxpcwlkj.system.service.SysOssService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description 对象存储
 * @Author sxpcwlkj
 * @Version v1.0.0
 */
@RequiredArgsConstructor
@Service("sysOss")
@Slf4j
public class SysOssServiceImpl implements SysOssService {

    private final SysOssMapper baseMapper;
    private final SysOssConfigService sysOssConfigService;

    @Override
    public TableDataInfo<SysOssVo> selectPageList(SysOssBo bo, PageQuery pageQuery) {
        Page<SysOssVo> page = baseMapper.selectByPage(pageQuery.build(), this.buildQueryWrapper(bo));
        if(StringUtil.isNotEmpty(bo.getLookImg())&&bo.getLookImg()){
            page.getRecords().forEach(this::toBase64);
        }
        return TableDataInfo.build(page);
    }

    private Wrapper<SysOss> buildQueryWrapper(SysOssBo bo) {

        QueryWrapper<SysOss> wrapper = Wrappers.query();
        wrapper.orderByAsc("sort");
        return wrapper;
    }

    @Override
    public SysOssVo selectVoById(Long id) {
        SysOssVo vo = baseMapper.selectVoById(id);
        return toBase64(vo);
    }

    private SysOssVo toBase64(SysOssVo vo){
        if (vo != null) {
            try {
                String base64String = "";
                if ((OssTypeEnum.local.getType()+"-1").equals(vo.getPlatform())) {
                    SysOssConfigVo configVo = sysOssConfigService.selectVoByKey("local-plus");
                    if (configVo == null) return vo;
                    String filePath = configVo.getExt1() + configVo.getPrefix() + "/" + vo.getFileName();
                    File file = new File(filePath);
                    if (!file.exists()) {
                        return vo;
                    }
                    byte[] fileContent;
                    try (FileInputStream fis = new FileInputStream(file)) {
                        fileContent = fis.readAllBytes();
                    }
                    // 读取图片文件 /Users/shanpengnian/mms/localFile
                    BufferedImage bufferedImage = ImageIO.read(file);
                    if(bufferedImage!=null){
                        // 在这里可以对图片进行处理
                        vo.setWidth(bufferedImage.getWidth() + "");
                        vo.setHeight(bufferedImage.getHeight() + "");
                        vo.setSize(fileContent.length + "");
                        // 将图片转换为Base64编码的字符串
                        Base64 base64Encoder = new Base64();
                        base64String = base64Encoder.encodeToString(fileContent);
                    }
                } else {
                    URL url = URI.create(vo.getUrl()).toURL();
                    java.net.URLConnection connection = url.openConnection();
                    connection.setConnectTimeout(5000);
                    connection.setReadTimeout(5000);
                    BufferedImage bufferedImage;
                    try (InputStream inputStream = connection.getInputStream()) {
                        bufferedImage = ImageIO.read(inputStream);
                    }
                    if(bufferedImage != null){
                        // 在这里可以对图片进行处理
                        vo.setWidth(bufferedImage.getWidth() + "");
                        vo.setHeight(bufferedImage.getHeight() + "");
                        // 创建一个ByteArrayOutputStream来捕获图片数据
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        // 使用ImageIO将BufferedImage写入到ByteArrayOutputStream中
                        ImageIO.write(bufferedImage, "png", baos);

                        // 从ByteArrayOutputStream中获取字节数据
                        byte[] imageBytes = baos.toByteArray();
                        vo.setSize(imageBytes.length + "");
                        byte[] bytes = imageBytes;
                        Base64 base64Encoder = new Base64();
                        base64String = base64Encoder.encodeToString(bytes);
                    }
                }
                if(!base64String.isEmpty()){
                    vo.setBase64("data:image/png;base64,"+base64String);
                }
            } catch (Exception e) {
                e.printStackTrace();
                vo.setBase64("");
            }

        }
        return vo;
    }

    @Override
    public Boolean updateByIdBase(SysOssBo bo) {
        int row;
        SysOss convert = MapstructUtil.convert(bo, SysOss.class);
        return baseMapper.updateById(convert) > 0;
    }

    @Override
    public Boolean insert(SysOssBo bo) {
        SysOss convert = MapstructUtil.convert(bo, SysOss.class);
        return baseMapper.insert(convert) > 0;
    }

    @Override
    public Boolean deleteById(Serializable ids) {
        String[] array = DataUtil.getCatStr(ids.toString(), ",");
        return baseMapper.deleteByIds(new ArrayList<>(List.of(array)))>0;
    }
}
