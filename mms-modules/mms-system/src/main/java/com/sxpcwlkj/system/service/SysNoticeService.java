package com.sxpcwlkj.system.service;

import com.sxpcwlkj.framework.service.BaseService;
import com.sxpcwlkj.system.entity.SysNotice;
import com.sxpcwlkj.system.entity.bo.SysNoticeBo;
import com.sxpcwlkj.system.entity.export.SysNoticeExport;
import com.sxpcwlkj.system.entity.vo.SysNoticeVo;

import java.util.Set;

/**
 * 系统公告-接口
 *
 * @author mmsAdmin
 * @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
 * @describe  支持自定义扩展,已继承接口：{insert、deleteById、updateByIdBase、selectById、getByEntityListPage}（更多查看BaseService接口）
 */
public interface SysNoticeService extends BaseService<SysNotice, SysNoticeVo, SysNoticeBo> {
    /**
    * 导出系统公告
    * @param list 系统公告列表
    * @return true：成功 false ：失败
    */
    Boolean imports(Set<SysNoticeExport> list);

    Long selectTool();
}
