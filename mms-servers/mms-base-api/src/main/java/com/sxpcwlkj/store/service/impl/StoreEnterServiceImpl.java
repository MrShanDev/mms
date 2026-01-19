package com.sxpcwlkj.store.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sxpcwlkj.common.enums.SystemCommonEnum;
import com.sxpcwlkj.common.utils.DataUtil;
import com.sxpcwlkj.common.utils.MapstructUtil;
import com.sxpcwlkj.datasource.entity.page.PageQuery;
import com.sxpcwlkj.datasource.entity.page.TableDataInfo;
import com.sxpcwlkj.datasource.mapper.BaseMapperPlus;
import com.sxpcwlkj.framework.service.impl.BaseServiceImpl;
import com.sxpcwlkj.store.entity.StoreEnter;
import com.sxpcwlkj.store.entity.bo.StoreBo;
import com.sxpcwlkj.store.entity.bo.StoreEnterBo;
import com.sxpcwlkj.store.entity.export.StoreEnterExport;
import com.sxpcwlkj.store.entity.vo.StoreEnterVo;
import com.sxpcwlkj.store.enums.StoreDisableStatusEnum;
import com.sxpcwlkj.store.mapper.StoreEnterMapper;
import com.sxpcwlkj.store.service.StoreEnterService;
import com.sxpcwlkj.store.service.StoreService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 店铺入驻-接口实现
 *
* @author mmsAdmin
* @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
 */
@Slf4j
@Transactional
@Service("store_enter")
@RequiredArgsConstructor
public class StoreEnterServiceImpl extends BaseServiceImpl<StoreEnter, StoreEnterVo, StoreEnterBo> implements StoreEnterService {

   private final StoreEnterMapper baseMapper;
   private final StoreService storeService;

    @Override
    public BaseMapperPlus<StoreEnter, StoreEnterVo> getBaseMapper() {
        return baseMapper;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean insert(StoreEnterBo bo) {
        try {
            int row;
            bo.setId(null);
            StoreEnter obj = MapstructUtil.convert(bo, StoreEnter.class);
            assert obj != null;
            row = this.getBaseMapper().insert(obj);
            bo.setId(obj.getId());
            return row > 0;
        } catch (Exception e) {
            log.error("店铺入驻,insert 操作失败", e);
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
            log.error("店铺入驻,deleteById 操作失败", e);
            throw e;
        }
    }

    @Override
    public Boolean updateById(StoreEnterBo bo) {
        try {
            int row;
            StoreEnter obj = MapstructUtil.convert(bo, StoreEnter.class);
            row = this.getBaseMapper().updateById(obj);
            if(row>0){

                //TODO 业务代码
                if(bo.getStatus().equals(StoreDisableStatusEnum.OPEN.getValue())){

                    StoreBo store = new StoreBo();
                    store.setId(String.valueOf(System.currentTimeMillis()));
                    store.setMemberId(bo.getMemberId());
                    store.setStoreTypeId("1");
                    store.setStoreName(bo.getStoreName());
                    store.setStoreType(1);
                    store.setBusinessState(1);
                    store.setStoreAddress(bo.getStoreAddressDetail());
                    store.setStoreLatitude("");
                    store.setStoreLongitude("");
                    store.setStoreLogo(bo.getStoreLogo());
                    store.setStoreBgImg("");
                    store.setStoreIntroduction(bo.getStoreDesc());
                    store.setStorePhone(bo.getLinkPhone());
                    store.setMonthlySales(new BigDecimal(0));
                    store.setStoreRank("1");
                    store.setStartingPrice(new BigDecimal(0));
                    store.setStoreAuditState(StoreDisableStatusEnum.OPEN.getValue());
                    store.setStoreAuditWhy(bo.getStoreAuditWhy());
                    store.setStatus(StoreDisableStatusEnum.OPEN.getValue());
                    store.setTenantId(bo.getId());
                    storeService.insert(store);
                }
            }
            return row > 0;
        } catch (Exception e) {
            log.error("店铺入驻,updateById 操作失败", e);
            throw e;
        }
    }

    @Override
    public StoreEnterVo selectVoById(Serializable id) {
        return this.getBaseMapper().selectVoById(id);

    }
    @Override
    public TableDataInfo<StoreEnterVo> selectListVoPage(StoreEnterBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<StoreEnter> lqw = buildQueryWrapper(bo);
        Page<StoreEnterVo> page = baseMapper.selectVoPage(pageQuery.build(),lqw);
        return TableDataInfo.build(page);
    }

    private LambdaQueryWrapper<StoreEnter> buildQueryWrapper(StoreEnterBo query){
        if(query==null){
            query=new StoreEnterBo();
        }
        LambdaQueryWrapper<StoreEnter> wrapper = Wrappers.lambdaQuery();
        wrapper.ne(StoreEnter::getStatus,StoreDisableStatusEnum.OPEN.getValue());
        return wrapper;
    }

    @Override
    public Boolean imports(Set<StoreEnterExport> list) {
        return true;
    }

    @Override
    public StoreEnterVo selectVoByMemberId(String mId) {
        return baseMapper.selectVoOne(new  LambdaQueryWrapper<StoreEnter>().eq(StoreEnter::getMemberId,mId).last(SystemCommonEnum.LIMIT_ONE.getCode()));
    }
}
