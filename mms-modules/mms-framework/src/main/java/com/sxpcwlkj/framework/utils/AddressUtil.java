package com.sxpcwlkj.framework.utils;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.resource.ClassPathResource;
import cn.hutool.core.util.ObjectUtil;
import com.sxpcwlkj.common.exception.MmsException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.lionsoul.ip2region.xdb.Searcher;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;

/**
 * @author xijue
 */

@Slf4j
@Component
@RequiredArgsConstructor
public class AddressUtil {
    private static final Searcher searcher;

    static {
        String fileName = "/ip2region.xdb";
        File existFile = FileUtil.file(FileUtil.getTmpDir() + FileUtil.FILE_SEPARATOR + fileName);
        if (!FileUtil.exist(existFile)) {
            ClassPathResource fileStream = new ClassPathResource(fileName);
            if (ObjectUtil.isEmpty(fileStream.getStream())) {
                throw new MmsException("RegionUtils初始化失败，原因：IP地址库数据不存在！");
            }
            FileUtil.writeFromStream(fileStream.getStream(), existFile);
        }
        String dbPath = existFile.getPath();
        // 1、从 dbPath 加载整个 xdb 到内存。
        byte[] cBuff;
        try {
            cBuff = Searcher.loadContentFromFile(dbPath);
        } catch (Exception e) {
            throw new MmsException("RegionUtils初始化失败，原因：从ip2region.xdb文件加载内容失败！" + e.getMessage());
        }
        // 2、使用上述的 cBuff 创建一个完全基于内存的查询对象。
        try {
            log.info("IP2region初始化成功");
            searcher = Searcher.newWithBuffer(cBuff);
        } catch (Exception e) {
            throw new MmsException("RegionUtils初始化失败，原因：" + e.getMessage());
        }
    }

    /**
     * 根据IP地址离线获取城市
     */
    public static String getCityInfo(String ip) {
        try {
            ip = ip.trim();
            // 3、执行查询
            String region = searcher.search(ip);
            return region.replace("0|", "").replace("|0", "");
        } catch (Exception e) {
            log.error("IP地址离线获取城市异常 {}", ip);
            return "未知";
        }
    }

    public static void main(String[] args) throws IOException {
        //1、完全基于文件查询,
        String info1 = getCityInfo("203.15.235.101");
        System.out.println(info1);

    }
}
