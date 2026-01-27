package com.sxpcwlkj.app.service;

import com.sxpcwlkj.app.entity.AppVersion;
import com.sxpcwlkj.app.entity.bo.AppVersionBo;
import com.sxpcwlkj.app.entity.export.AppVersionExport;
import com.sxpcwlkj.app.entity.vo.AppVersionVo;
import com.sxpcwlkj.framework.service.BaseService;

import java.util.List;
import java.util.Set;

/**
 * App版本发布表-接口
 *
 * @author mmsAdmin
 * @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
 * @describe  支持自定义扩展,已继承接口：{insert、deleteById、updateById、selectById、getByEntityListPage}（更多查看BaseService接口）
 */
public interface AppVersionService extends BaseService<AppVersion, AppVersionVo, AppVersionBo> {
    /**
     * 导出App版本发布表
     * @param list App版本发布表列表
     * @return true：成功 false ：失败
     */
    Boolean imports(Set<AppVersionExport> list);

    /**
     * 查询最新版本
     *
     * @param appCode  应用编码
     * @param platform 平台类型
     * @return 最新版本信息
     */
    AppVersionVo getLatestVersion(String appCode, String platform);
}
