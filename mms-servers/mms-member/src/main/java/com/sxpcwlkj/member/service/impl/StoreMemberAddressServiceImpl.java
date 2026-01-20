package com.sxpcwlkj.member.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sxpcwlkj.common.enums.SystemCommonEnum;
import com.sxpcwlkj.common.utils.DataUtil;
import com.sxpcwlkj.common.utils.MapstructUtil;
import com.sxpcwlkj.common.utils.StringUtil;
import com.sxpcwlkj.datasource.entity.page.PageQuery;
import com.sxpcwlkj.datasource.entity.page.TableDataInfo;
import com.sxpcwlkj.datasource.mapper.BaseMapperPlus;
import com.sxpcwlkj.framework.service.impl.BaseServiceImpl;
import com.sxpcwlkj.member.entity.StoreMemberAddress;
import com.sxpcwlkj.member.entity.bo.StoreMemberAddressBo;
import com.sxpcwlkj.member.entity.export.StoreMemberAddressExport;
import com.sxpcwlkj.member.entity.vo.StoreMemberAddressVo;
import com.sxpcwlkj.member.mapper.StoreMemberAddressMapper;
import com.sxpcwlkj.member.service.StoreMemberAddressService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;


/**
 * 会员收件地址;
 *
 * @author 西决 942879858@qq.com
 * @since 1.0.0 2024-05-11
 */
@Slf4j
@Service("store_member_address")
@RequiredArgsConstructor
public class StoreMemberAddressServiceImpl extends BaseServiceImpl<StoreMemberAddress, StoreMemberAddressVo, StoreMemberAddressBo> implements StoreMemberAddressService {
    private final StoreMemberAddressMapper baseMapper;

    @Override
    public BaseMapperPlus<StoreMemberAddress, StoreMemberAddressVo> getBaseMapper() {
        return baseMapper;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean insert(StoreMemberAddressBo bo) {
        try {
            int row;
            bo.setId(null);
            StoreMemberAddress obj = MapstructUtil.convert(bo, StoreMemberAddress.class);
            assert obj != null;
            row = this.getBaseMapper().insert(obj);
            bo.setId(obj.getId());
            return row > 0;
        } catch (Exception e) {
            log.error("会员收货地址,insert 操作失败", e);
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
            log.error("会员收货地址,deleteById 操作失败", e);
            throw e;
        }
    }

    @Override
    public Boolean updateById(StoreMemberAddressBo bo) {
        try {
            int row;
            StoreMemberAddress obj = MapstructUtil.convert(bo, StoreMemberAddress.class);
            row = this.getBaseMapper().updateById(obj);
            return row > 0;
        } catch (Exception e) {
            log.error("会员收货地址,updateById 操作失败", e);
            throw e;
        }
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
        LambdaQueryWrapper<StoreMemberAddress> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(StringUtil.isNotEmpty(query.getMemberId()), StoreMemberAddress::getMemberId, query.getMemberId());
        wrapper.eq(StringUtil.isNotEmpty(query.getPhone()), StoreMemberAddress::getPhone, query.getPhone());
        return wrapper;
    }

    @Override
    public Boolean imports(Set<StoreMemberAddressExport> list) {
        return true;
    }

    @Override
    public StoreMemberAddressVo selectVoByIdMid(String id, String mid) {
        return baseMapper.selectVoOne(new LambdaQueryWrapper<StoreMemberAddress>()
            .eq(StoreMemberAddress::getMemberId, mid)
            .eq(StoreMemberAddress::getId, id).last(SystemCommonEnum.LIMIT_ONE.getCode()));
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
