package com.sxpcwlkj.member.service;

import com.sxpcwlkj.framework.service.BaseService;
import com.sxpcwlkj.member.entity.StoreMemberAddress;
import com.sxpcwlkj.member.entity.bo.StoreMemberAddressBo;
import com.sxpcwlkj.member.entity.export.StoreMemberAddressExport;
import com.sxpcwlkj.member.entity.vo.StoreMemberAddressVo;

import java.util.Set;

/**
 * 会员收件地址;
 * 支持自定义扩展,已继承接口：insert、deleteById、updateById、selectById、getByEntityListPage（更多查看BaseService接口）
 * @author 西决 942879858@qq.com
 * @since 1.0.0 2024-05-11
 */
public interface StoreMemberAddressService extends BaseService<StoreMemberAddress, StoreMemberAddressVo, StoreMemberAddressBo> {
    StoreMemberAddressVo selectVoByIdAndMid(String id, String id1);

    Boolean deleteByIdAndMid(String id, String id1);

    /**
     * 导出会员收货地址
     * @param list 会员收货地址列表
     * @return true：成功 false ：失败
     */
    Boolean imports(Set<StoreMemberAddressExport> list);

    /**
     *  根据会员ID查询会员收货地址
     * @param id  地址ID
     * @param mid  会员ID
     * @return
     */
    StoreMemberAddressVo selectVoByIdMid(String id, String mid);
}
