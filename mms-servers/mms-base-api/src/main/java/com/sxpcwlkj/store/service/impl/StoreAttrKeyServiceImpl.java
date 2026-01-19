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
import com.sxpcwlkj.store.entity.StoreAttrKey;
import com.sxpcwlkj.store.entity.bo.StoreAttrKeyBo;
import com.sxpcwlkj.store.entity.export.StoreAttrKeyExport;
import com.sxpcwlkj.store.entity.vo.StoreAttrKeyVo;
import com.sxpcwlkj.store.mapper.StoreAttrKeyMapper;
import com.sxpcwlkj.store.service.StoreAttrKeyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
/**
 * 属性键表-接口实现
 *
* @author mmsAdmin
* @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
 */
@Slf4j
@Transactional
@Service("store_attr_key")
@RequiredArgsConstructor
public class StoreAttrKeyServiceImpl extends BaseServiceImpl<StoreAttrKey, StoreAttrKeyVo, StoreAttrKeyBo> implements StoreAttrKeyService {

   private final StoreAttrKeyMapper baseMapper;

    @Override
    public BaseMapperPlus<StoreAttrKey, StoreAttrKeyVo> getBaseMapper() {
        return baseMapper;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean insert(StoreAttrKeyBo bo) {
        try {
            int row;
            bo.setId(null);
            StoreAttrKey obj = MapstructUtil.convert(bo, StoreAttrKey.class);
            assert obj != null;
            row = this.getBaseMapper().insert(obj);
            bo.setId(obj.getId());
            return row > 0;
        } catch (Exception e) {
            log.error("属性键表,insert 操作失败", e);
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
            log.error("属性键表,deleteById 操作失败", e);
            throw e;
        }
    }

    @Override
    public Boolean updateById(StoreAttrKeyBo bo) {
        try {
            int row;
            StoreAttrKey obj = MapstructUtil.convert(bo, StoreAttrKey.class);
            row = this.getBaseMapper().updateById(obj);
            return row > 0;
        } catch (Exception e) {
            log.error("属性键表,updateById 操作失败", e);
            throw e;
        }
    }

    @Override
    public StoreAttrKeyVo selectVoById(Serializable id) {
        return this.getBaseMapper().selectVoById(id);

    }
    @Override
    public TableDataInfo<StoreAttrKeyVo> selectListVoPage(StoreAttrKeyBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<StoreAttrKey> lqw = buildQueryWrapper(bo);
        Page<StoreAttrKeyVo> page = baseMapper.selectVoPage(pageQuery.build(),lqw);
        return TableDataInfo.build(page);
    }

    private LambdaQueryWrapper<StoreAttrKey> buildQueryWrapper(StoreAttrKeyBo query){
        if(query==null){
            query=new StoreAttrKeyBo();
        }
        LambdaQueryWrapper<StoreAttrKey> wrapper = Wrappers.lambdaQuery();
        return wrapper;
    }

    @Override
    public Boolean imports(Set<StoreAttrKeyExport> list) {
        return true;
    }
}
