package com.sxpcwlkj.store.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sxpcwlkj.common.utils.MapstructUtil;
import com.sxpcwlkj.common.utils.DataUtil;
import com.sxpcwlkj.datasource.entity.page.PageQuery;
import com.sxpcwlkj.datasource.entity.page.TableDataInfo;
import com.sxpcwlkj.datasource.mapper.BaseMapperPlus;
import com.sxpcwlkj.framework.service.impl.BaseServiceImpl;
import com.sxpcwlkj.store.entity.StoreAttrValueSpu;
import com.sxpcwlkj.store.entity.bo.StoreAttrValueSpuBo;
import com.sxpcwlkj.store.entity.vo.StoreAttrValueSpuVo;
import com.sxpcwlkj.store.entity.export.StoreAttrValueSpuExport;
import com.sxpcwlkj.store.mapper.StoreAttrValueSpuMapper;
import com.sxpcwlkj.store.service.StoreAttrValueSpuService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.io.Serializable;
import java.util.Set;
import java.util.ArrayList;
import java.util.List;

/**
 * 商品规格值关系表-接口实现
 *
* @author mmsAdmin
* @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
 */
@Slf4j
@Transactional
@Service("store_attr_value_spu")
@RequiredArgsConstructor
public class StoreAttrValueSpuServiceImpl extends BaseServiceImpl<StoreAttrValueSpu, StoreAttrValueSpuVo, StoreAttrValueSpuBo> implements StoreAttrValueSpuService {

   private final StoreAttrValueSpuMapper baseMapper;

    @Override
    public BaseMapperPlus<StoreAttrValueSpu, StoreAttrValueSpuVo> getBaseMapper() {
        return baseMapper;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean insert(StoreAttrValueSpuBo bo) {
        try {
            int row;
            bo.setId(null);
            StoreAttrValueSpu obj = MapstructUtil.convert(bo, StoreAttrValueSpu.class);
            assert obj != null;
            row = this.getBaseMapper().insert(obj);
            bo.setId(obj.getId());
            return row > 0;
        } catch (Exception e) {
            log.error("商品规格值关系表,insert 操作失败", e);
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
            log.error("商品规格值关系表,deleteById 操作失败", e);
            throw e;
        }
    }

    @Override
    public Boolean updateById(StoreAttrValueSpuBo bo) {
        try {
            int row;
            StoreAttrValueSpu obj = MapstructUtil.convert(bo, StoreAttrValueSpu.class);
            row = this.getBaseMapper().updateById(obj);
            return row > 0;
        } catch (Exception e) {
            log.error("商品规格值关系表,updateById 操作失败", e);
            throw e;
        }
    }

    @Override
    public StoreAttrValueSpuVo selectVoById(Serializable id) {
        return this.getBaseMapper().selectVoById(id);

    }
    @Override
    public TableDataInfo<StoreAttrValueSpuVo> selectListVoPage(StoreAttrValueSpuBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<StoreAttrValueSpu> lqw = buildQueryWrapper(bo);
        Page<StoreAttrValueSpuVo> page = baseMapper.selectVoPage(pageQuery.build(),lqw);
        return TableDataInfo.build(page);
    }

    private LambdaQueryWrapper<StoreAttrValueSpu> buildQueryWrapper(StoreAttrValueSpuBo query){
        if(query==null){
            query=new StoreAttrValueSpuBo();
        }
        LambdaQueryWrapper<StoreAttrValueSpu> wrapper = Wrappers.lambdaQuery();
        return wrapper;
    }

    @Override
    public Boolean imports(Set<StoreAttrValueSpuExport> list) {
        return true;
    }
}
