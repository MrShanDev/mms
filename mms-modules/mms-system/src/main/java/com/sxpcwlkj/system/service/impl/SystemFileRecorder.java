package com.sxpcwlkj.system.service.impl;

import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sxpcwlkj.common.exception.MmsException;
import com.sxpcwlkj.system.entity.SysOss;
import com.sxpcwlkj.system.mapper.SysOssMapper;
import lombok.RequiredArgsConstructor;
import org.dromara.x.file.storage.core.FileInfo;
import org.dromara.x.file.storage.core.recorder.FileRecorder;
import org.dromara.x.file.storage.core.upload.FilePartInfo;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

/** Persist uploaded object metadata in the tenant-scoped file management table. */
@Component
@Primary
@RequiredArgsConstructor
public class SystemFileRecorder implements FileRecorder {
    private final SysOssMapper files;
    private final ObjectMapper json;
    @Override public boolean save(FileInfo info) {
        SysOss record = new SysOss();
        record.setOssId(IdUtil.getSnowflakeNextIdStr()); info.setId(record.getOssId());
        record.setFileName(info.getFilename()); record.setOriginalName(info.getOriginalFilename());
        record.setFileSuffix(info.getExt() == null ? "" : info.getExt());
        record.setUrl(info.getUrl()); record.setPlatform(info.getPlatform());
        record.setBasePath(info.getBasePath()); record.setContentType(info.getContentType());
        record.setStatus(1); record.setSort(0); record.setFileInfoJson(encode(info));
        return files.insert(record) == 1;
    }
    @Override public void update(FileInfo info) {
        SysOss record = find(info.getUrl());
        if (record == null) throw new MmsException("文件记录不存在");
        record.setFileInfoJson(encode(info));
        if (files.updateById(record) != 1) throw new MmsException("文件记录更新失败");
    }
    @Override public FileInfo getByUrl(String url) {
        SysOss record = find(url);
        if (record == null || record.getFileInfoJson() == null) return null;
        try { return json.readValue(record.getFileInfoJson(), FileInfo.class); }
        catch (Exception e) { throw new MmsException("文件元数据读取失败"); }
    }
    @Override public boolean delete(String url) {
        SysOss record = find(url);
        return record != null && files.deleteById(record.getOssId()) == 1;
    }
    private SysOss find(String url) { return files.selectOne(new LambdaQueryWrapper<SysOss>().eq(SysOss::getUrl,url).last("LIMIT 1")); }
    private String encode(FileInfo info) {
        try { return json.writeValueAsString(info); }
        catch (Exception e) { throw new MmsException("文件元数据保存失败"); }
    }
    @Override public void saveFilePart(FilePartInfo part) { throw new MmsException("当前基础存储暂不支持分片上传"); }
    @Override public void deleteFilePartByUploadId(String uploadId) { throw new MmsException("当前基础存储暂不支持分片上传"); }
}
