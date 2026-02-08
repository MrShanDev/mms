package com.sxpcwlkj.system.service;

import com.sxpcwlkj.framework.service.BaseService;
import com.sxpcwlkj.system.entity.SysLog;
import com.sxpcwlkj.system.entity.bo.SysLogBo;
import com.sxpcwlkj.system.entity.vo.SysLogVo;
import com.sxpcwlkj.system.entity.export.SysLogExport;

import java.util.Set;

/**
 * 操作日志记录表-接口
 *
 * @author mmsAdmin
 * @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
 * @describe  支持自定义扩展,已继承接口：{insert、deleteById、updateByIdBase、selectById、getByEntityListPage}（更多查看BaseService接口）
 */
public interface SysLogService extends BaseService<SysLog, SysLogVo, SysLogBo> {
    /**
    * 导出操作日志记录表
    * @param list 操作日志记录表列表
    * @return true：成功 false ：失败
    */
    Boolean imports(Set<SysLogExport> list);
}
