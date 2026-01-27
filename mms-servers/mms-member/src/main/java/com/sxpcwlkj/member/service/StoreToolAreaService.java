package com.sxpcwlkj.member.service;

import com.sxpcwlkj.framework.service.BaseService;
import com.sxpcwlkj.member.entity.StoreToolArea;
import com.sxpcwlkj.member.entity.bo.StoreToolAreaBo;
import com.sxpcwlkj.member.entity.export.StoreToolAreaExport;
import com.sxpcwlkj.member.entity.vo.StoreToolAreaVo;

import java.util.List;
import java.util.Set;

/**
 * 行政区域-接口
 *
 * @author mmsAdmin
 * @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
 * @describe  支持自定义扩展,已继承接口：{insert、deleteById、updateById、selectById、getByEntityListPage}（更多查看BaseService接口）
 */
public interface StoreToolAreaService extends BaseService<StoreToolArea, StoreToolAreaVo, StoreToolAreaBo> {
    /**
     * 导出行政区域
     * @param list 行政区域列表
     * @return true：成功 false ：失败
     */
    Boolean imports(Set<StoreToolAreaExport> list);
}
