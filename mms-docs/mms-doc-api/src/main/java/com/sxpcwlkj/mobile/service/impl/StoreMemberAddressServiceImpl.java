package com.sxpcwlkj.mobile.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sxpcwlkj.common.utils.MapstructUtil;
import com.sxpcwlkj.common.utils.StringUtil;
import com.sxpcwlkj.datasource.entity.page.PageQuery;
import com.sxpcwlkj.datasource.entity.page.TableDataInfo;
import com.sxpcwlkj.datasource.mapper.BaseMapperPlus;
import com.sxpcwlkj.framework.sercice.impl.BaseServiceImpl;
import com.sxpcwlkj.mobile.entity.StoreMemberAddress;
import com.sxpcwlkj.mobile.entity.bo.StoreMemberAddressBo;
import com.sxpcwlkj.mobile.entity.vo.StoreMemberAddressVo;
import com.sxpcwlkj.mobile.mapper.StoreMemberAddressMapper;
import com.sxpcwlkj.mobile.service.StoreMemberAddressService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.io.Serializable;


/**
 * 会员收件地址;
 *
 * @author 西决 942879858@qq.com
 * @since 1.0.0 2024-05-11
 */
@Slf4j
@Service("store_member_address")
@RequiredArgsConstructor
public class StoreMemberAddressServiceImpl extends BaseServiceImpl<StoreMemberAddress, StoreMemberAddressVo,StoreMemberAddressBo> implements StoreMemberAddressService {

   private final StoreMemberAddressMapper baseMapper;


    @Override
    public BaseMapperPlus<StoreMemberAddress, StoreMemberAddressVo> getBaseMapper() {
        return baseMapper;
    }


    @Override
    public Boolean insert(StoreMemberAddressBo bo) {
        int row = 0;
        StoreMemberAddress obj = MapstructUtil.convert(bo, StoreMemberAddress.class);
        row = this.getBaseMapper().insert(obj);
        return row > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteById(Serializable id) {
        return this.getBaseMapper().deleteById(id) > 0;
    }

    @Override
    public Boolean updateById(StoreMemberAddressBo bo) {
        int row = 0;
        StoreMemberAddress obj = MapstructUtil.convert(bo, StoreMemberAddress.class);
        row = this.getBaseMapper().updateById(obj);
        return row > 0;
    }

    @Override
    public StoreMemberAddressVo selectVoById(Serializable id) {
        return this.getBaseMapper().selectVoById(id);
    }

    @Override
    public TableDataInfo<StoreMemberAddressVo> selectListVoPage(StoreMemberAddressBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<StoreMemberAddress> lqw = buildQueryWrapper(bo);
        Page<StoreMemberAddressVo> page = baseMapper.selectVoPage(pageQuery.build(),lqw);
        return TableDataInfo.build(page);
    }


    private LambdaQueryWrapper<StoreMemberAddress> buildQueryWrapper(StoreMemberAddressBo query){
        if(query==null){
            query=new StoreMemberAddressBo();
        }
        //Map<String, Object> params = query.getParams();

        LambdaQueryWrapper<StoreMemberAddress> wrapper = Wrappers.lambdaQuery();

        wrapper.eq(StringUtil.isNotBlank(query.getMemberId()),StoreMemberAddress::getMemberId,query.getMemberId());
        wrapper.orderByDesc(StoreMemberAddress::getTolerant);
        return wrapper;
    }

    @Override
    public StoreMemberAddressVo selectVoByIdAndMid(String id, String mid) {
        return baseMapper.selectVoOne(new LambdaQueryWrapper<StoreMemberAddress>().eq(StoreMemberAddress::getMemberId, mid).eq(StoreMemberAddress::getId, id));
    }

    @Override
    public Boolean deleteByIdAndMid(String id, String mid) {
        return baseMapper.delete(new LambdaQueryWrapper<StoreMemberAddress>().eq(StoreMemberAddress::getMemberId,mid).eq(StoreMemberAddress::getId,id))>0;
    }
}
