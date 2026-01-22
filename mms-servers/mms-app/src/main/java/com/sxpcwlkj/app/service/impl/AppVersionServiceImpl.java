package com.sxpcwlkj.app.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sxpcwlkj.app.entity.AppVersion;
import com.sxpcwlkj.app.entity.vo.AppVersionVo;
import com.sxpcwlkj.app.mapper.AppVersionMapper;
import com.sxpcwlkj.app.service.IAppVersionService;
import org.springframework.stereotype.Service;

/**
 * App版本发布Service业务层处理
 *
 * @author Qoder
 * @date 2026-01-22
 */
@Service
public class AppVersionServiceImpl extends ServiceImpl<AppVersionMapper, AppVersion> implements IAppVersionService {

    @Override
    public AppVersionVo getLatestVersion(String appCode, String platform) {
        LambdaQueryWrapper<AppVersion> lqw = new LambdaQueryWrapper<>();
        lqw.eq(AppVersion::getAppCode, appCode);
        lqw.eq(AppVersion::getPlatform, platform);
        lqw.eq(AppVersion::getPublishStatus, "published");
        lqw.eq(AppVersion::getStatus, 1);
        lqw.orderByDesc(AppVersion::getVersionCode);
        lqw.last("LIMIT 1");
        return baseMapper.selectVoOne(lqw);
    }
}
