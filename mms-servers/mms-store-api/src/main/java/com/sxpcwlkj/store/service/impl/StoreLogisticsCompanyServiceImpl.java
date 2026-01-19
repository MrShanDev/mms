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
import com.sxpcwlkj.store.entity.StoreLogisticsCompany;
import com.sxpcwlkj.store.entity.bo.StoreLogisticsCompanyBo;
import com.sxpcwlkj.store.entity.export.StoreLogisticsCompanyExport;
import com.sxpcwlkj.store.entity.vo.StoreLogisticsCompanyVo;
import com.sxpcwlkj.store.mapper.StoreLogisticsCompanyMapper;
import com.sxpcwlkj.store.service.StoreLogisticsCompanyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 快递公司名称-接口实现
 *
* @author mmsAdmin
* @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
 */
@Slf4j
@Transactional
@Service("store_logistics_company")
@RequiredArgsConstructor
public class StoreLogisticsCompanyServiceImpl extends BaseServiceImpl<StoreLogisticsCompany, StoreLogisticsCompanyVo, StoreLogisticsCompanyBo> implements StoreLogisticsCompanyService {

   private final StoreLogisticsCompanyMapper baseMapper;

    @Override
    public BaseMapperPlus<StoreLogisticsCompany, StoreLogisticsCompanyVo> getBaseMapper() {
        return baseMapper;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean insert(StoreLogisticsCompanyBo bo) {
        try {
            int row;
            bo.setId(null);
            StoreLogisticsCompany obj = MapstructUtil.convert(bo, StoreLogisticsCompany.class);
            assert obj != null;
            row = this.getBaseMapper().insert(obj);
            bo.setId(obj.getId());
            return row > 0;
        } catch (Exception e) {
            log.error("快递公司名称,insert 操作失败", e);
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
            log.error("快递公司名称,deleteById 操作失败", e);
            throw e;
        }
    }

    @Override
    public Boolean updateById(StoreLogisticsCompanyBo bo) {
        try {
            int row;
            StoreLogisticsCompany obj = MapstructUtil.convert(bo, StoreLogisticsCompany.class);
            row = this.getBaseMapper().updateById(obj);
            return row > 0;
        } catch (Exception e) {
            log.error("快递公司名称,updateById 操作失败", e);
            throw e;
        }
    }

    @Override
    public StoreLogisticsCompanyVo selectVoById(Serializable id) {
        return this.getBaseMapper().selectVoById(id);

    }
    @Override
    public TableDataInfo<StoreLogisticsCompanyVo> selectListVoPage(StoreLogisticsCompanyBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<StoreLogisticsCompany> lqw = buildQueryWrapper(bo);
        Page<StoreLogisticsCompanyVo> page = baseMapper.selectVoPage(pageQuery.build(),lqw);
        return TableDataInfo.build(page);
    }

    private LambdaQueryWrapper<StoreLogisticsCompany> buildQueryWrapper(StoreLogisticsCompanyBo query){
        if(query==null){
            query=new StoreLogisticsCompanyBo();
        }
        LambdaQueryWrapper<StoreLogisticsCompany> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(StringUtil.isNotEmpty(query.getCode()), StoreLogisticsCompany::getCode, query.getCode());
        wrapper.eq(StringUtil.isNotEmpty(query.getName()), StoreLogisticsCompany::getName, query.getName());
        wrapper.eq(StringUtil.isNotEmpty(query.getNumber()), StoreLogisticsCompany::getNumber, query.getNumber());
        return wrapper;
    }

    @Override
    public Boolean imports(Set<StoreLogisticsCompanyExport> list) {
        return true;
    }
}
