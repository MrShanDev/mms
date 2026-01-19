package com.sxpcwlkj.store.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sxpcwlkj.common.utils.DataUtil;
import com.sxpcwlkj.common.utils.MapstructUtil;
import com.sxpcwlkj.datasource.entity.page.PageQuery;
import com.sxpcwlkj.datasource.entity.page.TableDataInfo;
import com.sxpcwlkj.datasource.mapper.BaseMapperPlus;
import com.sxpcwlkj.framework.service.impl.BaseServiceImpl;
import com.sxpcwlkj.store.entity.StoreAttrKeySpu;
import com.sxpcwlkj.store.entity.bo.StoreAttrKeySpuBo;
import com.sxpcwlkj.store.entity.export.StoreAttrKeySpuExport;
import com.sxpcwlkj.store.entity.vo.StoreAttrKeySpuVo;
import com.sxpcwlkj.store.mapper.StoreAttrKeySpuMapper;
import com.sxpcwlkj.store.service.StoreAttrKeySpuService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
/**
 * 商品规格项关系表-接口实现
 *
* @author mmsAdmin
* @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
 */
@Slf4j
@Transactional
@Service("store_attr_key_spu")
@RequiredArgsConstructor
public class StoreAttrKeySpuServiceImpl extends BaseServiceImpl<StoreAttrKeySpu, StoreAttrKeySpuVo, StoreAttrKeySpuBo> implements StoreAttrKeySpuService {

   private final StoreAttrKeySpuMapper baseMapper;

    @Override
    public BaseMapperPlus<StoreAttrKeySpu, StoreAttrKeySpuVo> getBaseMapper() {
        return baseMapper;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean insert(StoreAttrKeySpuBo bo) {
        try {
            int row;
            bo.setId(null);
            StoreAttrKeySpu obj = MapstructUtil.convert(bo, StoreAttrKeySpu.class);
            assert obj != null;
            row = this.getBaseMapper().insert(obj);
            bo.setId(obj.getId());
            return row > 0;
        } catch (Exception e) {
            log.error("商品规格项关系表,insert 操作失败", e);
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
            log.error("商品规格项关系表,deleteById 操作失败", e);
            throw e;
        }
    }

    @Override
    public Boolean updateById(StoreAttrKeySpuBo bo) {
        try {
            int row;
            StoreAttrKeySpu obj = MapstructUtil.convert(bo, StoreAttrKeySpu.class);
            row = this.getBaseMapper().updateById(obj);
            return row > 0;
        } catch (Exception e) {
            log.error("商品规格项关系表,updateById 操作失败", e);
            throw e;
        }
    }

    @Override
    public StoreAttrKeySpuVo selectVoById(Serializable id) {
        return this.getBaseMapper().selectVoById(id);

    }
    @Override
    public TableDataInfo<StoreAttrKeySpuVo> selectListVoPage(StoreAttrKeySpuBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<StoreAttrKeySpu> lqw = buildQueryWrapper(bo);
        Page<StoreAttrKeySpuVo> page = baseMapper.selectVoPage(pageQuery.build(),lqw);
        return TableDataInfo.build(page);
    }

    private LambdaQueryWrapper<StoreAttrKeySpu> buildQueryWrapper(StoreAttrKeySpuBo query){
        if(query==null){
            query=new StoreAttrKeySpuBo();
        }
        LambdaQueryWrapper<StoreAttrKeySpu> wrapper = Wrappers.lambdaQuery();
        return wrapper;
    }

    @Override
    public Boolean imports(Set<StoreAttrKeySpuExport> list) {
        return true;
    }
}
