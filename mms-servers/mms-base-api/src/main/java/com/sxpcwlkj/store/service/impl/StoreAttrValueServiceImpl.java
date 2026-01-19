package com.sxpcwlkj.store.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sxpcwlkj.common.utils.DataUtil;
import com.sxpcwlkj.common.utils.MapstructUtil;
import com.sxpcwlkj.common.utils.StringUtil;
import com.sxpcwlkj.datasource.entity.page.PageQuery;
import com.sxpcwlkj.datasource.entity.page.TableDataInfo;
import com.sxpcwlkj.datasource.mapper.BaseMapperPlus;
import com.sxpcwlkj.framework.service.impl.BaseServiceImpl;
import com.sxpcwlkj.store.entity.StoreAttrValue;
import com.sxpcwlkj.store.entity.bo.StoreAttrValueBo;
import com.sxpcwlkj.store.entity.export.StoreAttrValueExport;
import com.sxpcwlkj.store.entity.vo.StoreAttrValueVo;
import com.sxpcwlkj.store.mapper.StoreAttrValueMapper;
import com.sxpcwlkj.store.service.StoreAttrValueService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
/**
 * 属性值表-接口实现
 *
* @author mmsAdmin
* @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
 */
@Slf4j
@Transactional
@Service("store_attr_value")
@RequiredArgsConstructor
public class StoreAttrValueServiceImpl extends BaseServiceImpl<StoreAttrValue, StoreAttrValueVo, StoreAttrValueBo> implements StoreAttrValueService {

   private final StoreAttrValueMapper baseMapper;

    @Override
    public BaseMapperPlus<StoreAttrValue, StoreAttrValueVo> getBaseMapper() {
        return baseMapper;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean insert(StoreAttrValueBo bo) {
        try {
            int row;
            bo.setId(null);
            StoreAttrValue obj = MapstructUtil.convert(bo, StoreAttrValue.class);
            assert obj != null;
            row = this.getBaseMapper().insert(obj);
            bo.setId(obj.getId());
            return row > 0;
        } catch (Exception e) {
            log.error("属性值表,insert 操作失败", e);
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
            log.error("属性值表,deleteById 操作失败", e);
            throw e;
        }
    }

    @Override
    public Boolean updateById(StoreAttrValueBo bo) {
        try {
            int row;
            StoreAttrValue obj = MapstructUtil.convert(bo, StoreAttrValue.class);
            row = this.getBaseMapper().updateById(obj);
            return row > 0;
        } catch (Exception e) {
            log.error("属性值表,updateById 操作失败", e);
            throw e;
        }
    }

    @Override
    public StoreAttrValueVo selectVoById(Serializable id) {
        return this.getBaseMapper().selectVoById(id);

    }
    @Override
    public TableDataInfo<StoreAttrValueVo> selectListVoPage(StoreAttrValueBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<StoreAttrValue> lqw = buildQueryWrapper(bo);
        Page<StoreAttrValueVo> page = baseMapper.selectVoPage(pageQuery.build(),lqw);
        return TableDataInfo.build(page);
    }

    private LambdaQueryWrapper<StoreAttrValue> buildQueryWrapper(StoreAttrValueBo query){
        if(query==null){
            query=new StoreAttrValueBo();
        }
        LambdaQueryWrapper<StoreAttrValue> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(StringUtil.isNotEmpty(query.getId()), StoreAttrValue::getId, query.getId());
        wrapper.eq(StringUtil.isNotEmpty(query.getAttrKeyId()), StoreAttrValue::getAttrKeyId, query.getAttrKeyId());
        wrapper.orderByAsc(StoreAttrValue::getSort);
        return wrapper;
    }

    @Override
    public Boolean imports(Set<StoreAttrValueExport> list) {
        return true;
    }
}
