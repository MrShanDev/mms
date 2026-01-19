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
import com.sxpcwlkj.store.entity.StoreShipmentSender;
import com.sxpcwlkj.store.entity.bo.StoreShipmentSenderBo;
import com.sxpcwlkj.store.entity.export.StoreShipmentSenderExport;
import com.sxpcwlkj.store.entity.vo.StoreShipmentSenderVo;
import com.sxpcwlkj.store.mapper.StoreShipmentSenderMapper;
import com.sxpcwlkj.store.service.StoreShipmentSenderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 发货人信息-接口实现
 *
* @author mmsAdmin
* @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
 */
@Slf4j
@Transactional
@Service("store_shipment_sender")
@RequiredArgsConstructor
public class StoreShipmentSenderServiceImpl extends BaseServiceImpl<StoreShipmentSender, StoreShipmentSenderVo, StoreShipmentSenderBo> implements StoreShipmentSenderService {

   private final StoreShipmentSenderMapper baseMapper;

    @Override
    public BaseMapperPlus<StoreShipmentSender, StoreShipmentSenderVo> getBaseMapper() {
        return baseMapper;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean insert(StoreShipmentSenderBo bo) {
        try {
            int row;
            bo.setId(null);
            StoreShipmentSender obj = MapstructUtil.convert(bo, StoreShipmentSender.class);
            assert obj != null;
            row = this.getBaseMapper().insert(obj);
            bo.setId(obj.getId());
            return row > 0;
        } catch (Exception e) {
            log.error("发货人信息,insert 操作失败", e);
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
            log.error("发货人信息,deleteById 操作失败", e);
            throw e;
        }
    }

    @Override
    public Boolean updateById(StoreShipmentSenderBo bo) {
        try {
            int row;
            StoreShipmentSender obj = MapstructUtil.convert(bo, StoreShipmentSender.class);
            row = this.getBaseMapper().updateById(obj);
            return row > 0;
        } catch (Exception e) {
            log.error("发货人信息,updateById 操作失败", e);
            throw e;
        }
    }

    @Override
    public StoreShipmentSenderVo selectVoById(Serializable id) {
        return this.getBaseMapper().selectVoById(id);

    }
    @Override
    public TableDataInfo<StoreShipmentSenderVo> selectListVoPage(StoreShipmentSenderBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<StoreShipmentSender> lqw = buildQueryWrapper(bo);
        Page<StoreShipmentSenderVo> page = baseMapper.selectVoPage(pageQuery.build(),lqw);
        return TableDataInfo.build(page);
    }

    private LambdaQueryWrapper<StoreShipmentSender> buildQueryWrapper(StoreShipmentSenderBo query){
        if(query==null){
            query=new StoreShipmentSenderBo();
        }
        LambdaQueryWrapper<StoreShipmentSender> wrapper = Wrappers.lambdaQuery();
        return wrapper;
    }

    @Override
    public Boolean imports(Set<StoreShipmentSenderExport> list) {
        return true;
    }
}
