package com.sxpcwlkj.bbs.service;

import com.sxpcwlkj.framework.service.BaseService;
import com.sxpcwlkj.bbs.entity.BbsCate;
import com.sxpcwlkj.bbs.entity.bo.BbsCateBo;
import com.sxpcwlkj.bbs.entity.vo.BbsCateVo;
import com.sxpcwlkj.bbs.entity.export.BbsCateExport;
import java.util.List;
import java.util.Set;

/**
 * 话题分类-接口
 *
 * @author mmsAdmin
 * @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
 * @describe  支持自定义扩展,已继承接口：{insert、deleteById、updateById、selectById、getByEntityListPage}（更多查看BaseService接口）
 */
public interface BbsCateService extends BaseService<BbsCate, BbsCateVo, BbsCateBo> {
    /**
    * 话题分类列表
    * @param isAll true：全部数据 false：有效数据(status=0)
    * @param showLevel 显示级别，0：全部 1：一级 2：二级 3：三级
    * @return 话题分类数结构列表
    */
    List<BbsCateVo> queryTree(boolean isAll,int showLevel);

    /**
    * 按照话题分类ID，查询下级所有话题分类
    *
    * @param id      话题分类ID
    * @param endList 最终的数据
    */
    void queryListSon(String id, List<BbsCateVo> endList);
    /**
    * 导出话题分类
    * @param list 话题分类列表
    * @return true：成功 false ：失败
    */
    Boolean imports(Set<BbsCateExport> list);
}
