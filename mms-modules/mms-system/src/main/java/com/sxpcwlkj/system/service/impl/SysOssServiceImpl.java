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
    private final org.dromara.x.file.storage.core.FileStorageService fileStorageService;
    private final SystemFileRecorder recorder;


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

    private SysOssVo toBase64(SysOssVo vo) {
        // Preview uses the object URL in the browser; never fetch client-editable URLs on the server.
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
        List<SysOss> records = new ArrayList<>();
        for (String id : array) {
            SysOss record = baseMapper.selectById(id);
            if (record == null) throw new com.sxpcwlkj.common.exception.MmsException("文件不存在或不属于当前租户");
            records.add(record);
        }
        for (SysOss record : records) {
            var info = recorder.getByUrl(record.getUrl());
            if (info == null) throw new com.sxpcwlkj.common.exception.MmsException("历史文件缺少元数据，请先核对并迁移记录");
            if (!fileStorageService.delete(info)) throw new com.sxpcwlkj.common.exception.MmsException("文件删除失败");
        }
        return true;
    }
}
