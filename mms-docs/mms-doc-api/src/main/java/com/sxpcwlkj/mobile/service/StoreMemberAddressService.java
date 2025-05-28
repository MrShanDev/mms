package com.sxpcwlkj.mobile.service;

import com.sxpcwlkj.framework.sercice.BaseService;
import com.sxpcwlkj.mobile.entity.StoreMemberAddress;
import com.sxpcwlkj.mobile.entity.bo.StoreMemberAddressBo;
import com.sxpcwlkj.mobile.entity.vo.StoreMemberAddressVo;

import java.util.List;

/**
 * 会员收件地址;
 * 支持自定义扩展,已继承接口：insert、deleteById、updateById、selectById、getByEntityListPage（更多查看BaseService接口）
 * @author 西决 942879858@qq.com
 * @since 1.0.0 2024-05-11
 */
public interface StoreMemberAddressService extends BaseService<StoreMemberAddress, StoreMemberAddressVo, StoreMemberAddressBo> {
    StoreMemberAddressVo selectVoByIdAndMid(String id, String id1);

    Boolean deleteByIdAndMid(String id, String id1);


}
