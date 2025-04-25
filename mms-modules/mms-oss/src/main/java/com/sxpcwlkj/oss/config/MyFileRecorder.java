package com.sxpcwlkj.oss.config;


import lombok.RequiredArgsConstructor;
import org.dromara.x.file.storage.core.FileInfo;
import org.dromara.x.file.storage.core.recorder.FileRecorder;
import org.dromara.x.file.storage.core.upload.FilePartInfo;
import org.springframework.stereotype.Component;

/**
 * @Description X Spring FileUtil Storage
 * @Author sxpcwlkj
 * @Version v1.0.0
 */
@Component
@RequiredArgsConstructor
public class MyFileRecorder implements FileRecorder {


    @Override
    public boolean save(FileInfo fileInfo) {
        return Boolean.TRUE;
    }

    @Override
    public void update(FileInfo fileInfo) {

    }

    @Override
    public FileInfo getByUrl(String url) {
        return null;
    }

    @Override
    public boolean delete(String url) {
        return Boolean.TRUE;
    }

    @Override
    public void saveFilePart(FilePartInfo filePartInfo) {

    }

    @Override
    public void deleteFilePartByUploadId(String uploadId) {

    }

}
