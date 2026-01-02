package com.sxpcwlkj.docAdmin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sxpcwlkj.common.utils.DataUtil;
import com.sxpcwlkj.common.utils.MapstructUtil;
import com.sxpcwlkj.datasource.entity.page.PageQuery;
import com.sxpcwlkj.datasource.entity.page.TableDataInfo;
import com.sxpcwlkj.datasource.mapper.BaseMapperPlus;
import com.sxpcwlkj.docAdmin.entity.DocProduct;
import com.sxpcwlkj.docAdmin.entity.bo.DocProductBo;
import com.sxpcwlkj.docAdmin.entity.export.DocProductExport;
import com.sxpcwlkj.docAdmin.entity.vo.DocProductVo;
import com.sxpcwlkj.docAdmin.mapper.DocProductMapper;
import com.sxpcwlkj.docAdmin.service.DocProductService;
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
 * 文档商品-接口实现
 *
* @author 西决
* @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
 */
@Slf4j
@Transactional
@Service("doc_product")
@RequiredArgsConstructor
public class DocProductServiceImpl extends BaseServiceImpl<DocProduct, DocProductVo,DocProductBo> implements DocProductService {

   private final DocProductMapper baseMapper;

    @Override
    public BaseMapperPlus<DocProduct, DocProductVo> getBaseMapper() {
        return baseMapper;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean insert(DocProductBo bo) {
        try {
            int row;
            bo.setProdId(null);
            DocProduct obj = MapstructUtil.convert(bo, DocProduct.class);
            assert obj != null;
            row = this.getBaseMapper().insert(obj);
            bo.setProdId(obj.getProdId());
            return row > 0;
        } catch (Exception e) {
            log.error("文档商品,insert 操作失败", e);
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
            log.error("文档商品,deleteById 操作失败", e);
            throw e;
        }
    }

    @Override
    public Boolean updateById(DocProductBo bo) {
        try {
            int row;
            DocProduct obj = MapstructUtil.convert(bo, DocProduct.class);
            row = this.getBaseMapper().updateById(obj);
            return row > 0;
        } catch (Exception e) {
            log.error("文档商品,updateById 操作失败", e);
            throw e;
        }
    }

    @Override
    public DocProductVo selectVoById(Serializable id) {
        return this.getBaseMapper().selectVoById(id);

    }
    @Override
    public TableDataInfo<DocProductVo> selectListVoPage(DocProductBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<DocProduct> lqw = buildQueryWrapper(bo);
        Page<DocProductVo> page = baseMapper.selectVoPage(pageQuery.build(),lqw);
        return TableDataInfo.build(page);
    }

    private LambdaQueryWrapper<DocProduct> buildQueryWrapper(DocProductBo query){
        if(query==null){
            query=new DocProductBo();
        }
        LambdaQueryWrapper<DocProduct> wrapper = Wrappers.lambdaQuery();
        return wrapper;
    }

    @Override
    public Boolean imports(Set<DocProductExport> list) {
        return true;
    }
}
