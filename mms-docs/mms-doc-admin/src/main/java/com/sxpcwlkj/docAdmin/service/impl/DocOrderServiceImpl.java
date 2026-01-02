package com.sxpcwlkj.docAdmin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sxpcwlkj.common.utils.DataUtil;
import com.sxpcwlkj.common.utils.MapstructUtil;
import com.sxpcwlkj.datasource.entity.page.PageQuery;
import com.sxpcwlkj.datasource.entity.page.TableDataInfo;
import com.sxpcwlkj.datasource.mapper.BaseMapperPlus;
import com.sxpcwlkj.docAdmin.entity.DocOrder;
import com.sxpcwlkj.docAdmin.entity.bo.DocOrderBo;
import com.sxpcwlkj.docAdmin.entity.export.DocOrderExport;
import com.sxpcwlkj.docAdmin.entity.vo.DocOrderVo;
import com.sxpcwlkj.docAdmin.mapper.DocOrderMapper;
import com.sxpcwlkj.docAdmin.service.DocOrderService;
import com.sxpcwlkj.framework.sercice.impl.BaseServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
/**
 * 文档订单-接口实现
 *
* @author 西决
* @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
 */
@Slf4j
@Transactional
@Service("doc_order")
@RequiredArgsConstructor
public class DocOrderServiceImpl extends BaseServiceImpl<DocOrder, DocOrderVo,DocOrderBo> implements DocOrderService {

   private final DocOrderMapper baseMapper;

    @Override
    public BaseMapperPlus<DocOrder, DocOrderVo> getBaseMapper() {
        return baseMapper;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean insert(DocOrderBo bo) {
        try {
            int row;
            bo.setOrderId(null);
            DocOrder obj = MapstructUtil.convert(bo, DocOrder.class);
            assert obj != null;
            row = this.getBaseMapper().insert(obj);
            bo.setOrderId(obj.getOrderId());
            return row > 0;
        } catch (Exception e) {
            log.error("文档订单,insert 操作失败", e);
            throw e;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteById(Serializable ids) {
        try {
            String[] array = DataUtil.getCatStr(ids.toString(), ",");
            return this.getBaseMapper().deleteByIds(new ArrayList<>(List.of(array)))>0;
        } catch (Exception e) {
            log.error("文档订单,deleteById 操作失败", e);
            throw e;
        }
    }

    @Override
    public Boolean updateById(DocOrderBo bo) {
        try {
            int row;
            DocOrder obj = MapstructUtil.convert(bo, DocOrder.class);
            row = this.getBaseMapper().updateById(obj);
            return row > 0;
        } catch (Exception e) {
            log.error("文档订单,updateById 操作失败", e);
            throw e;
        }
    }

    @Override
    public DocOrderVo selectVoById(Serializable id) {
        return this.getBaseMapper().selectVoById(id);

    }
    @Override
    public TableDataInfo<DocOrderVo> selectListVoPage(DocOrderBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<DocOrder> lqw = buildQueryWrapper(bo);
        Page<DocOrderVo> page = baseMapper.selectVoPage(pageQuery.build(),lqw);
        return TableDataInfo.build(page);
    }

    private LambdaQueryWrapper<DocOrder> buildQueryWrapper(DocOrderBo query){
        if(query==null){
            query=new DocOrderBo();
        }
        LambdaQueryWrapper<DocOrder> wrapper = Wrappers.lambdaQuery();
        return wrapper;
    }

    @Override
    public Boolean imports(Set<DocOrderExport> list) {
        return true;
    }
}
