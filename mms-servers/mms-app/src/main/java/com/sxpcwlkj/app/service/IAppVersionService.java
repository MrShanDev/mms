package com.sxpcwlkj.app.service;

import com.sxpcwlkj.app.entity.AppVersion;
import com.sxpcwlkj.app.entity.vo.AppVersionVo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * App版本发布Service接口
 *
 * @author Qoder
 * @date 2026-01-22
 */
public interface IAppVersionService extends IService<AppVersion> {

    /**
     * 查询最新版本
     *
     * @param appCode  应用编码
     * @param platform 平台类型
     * @return 最新版本信息
     */
    AppVersionVo getLatestVersion(String appCode, String platform);
}
