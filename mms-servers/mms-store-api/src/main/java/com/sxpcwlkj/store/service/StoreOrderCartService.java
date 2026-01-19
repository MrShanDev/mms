package com.sxpcwlkj.store.service;

import com.sxpcwlkj.framework.service.BaseService;
import com.sxpcwlkj.store.entity.StoreOrderCart;
import com.sxpcwlkj.store.entity.bo.AddCartBo;
import com.sxpcwlkj.store.entity.bo.StoreOrderCartBo;
import com.sxpcwlkj.store.entity.vo.StoreOrderCartVo;
import com.sxpcwlkj.store.entity.export.StoreOrderCartExport;
import java.util.List;
import java.util.Set;

/**
 * 购物车表-接口
 *
 * @author mmsAdmin
 * @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
 * @describe  支持自定义扩展,已继承接口：{insert、deleteById、updateById、selectById、getByEntityListPage}（更多查看BaseService接口）
 */
public interface StoreOrderCartService extends BaseService<StoreOrderCart, StoreOrderCartVo, StoreOrderCartBo> {
    /**
    * 导出购物车表
    * @param list 购物车表列表
    * @return true：成功 false ：失败
    */
    Boolean imports(Set<StoreOrderCartExport> list);

    /**
     *  添加购物车
     * @param bo bo
     * @return  true：成功 false ：失败
     */
    Boolean addCart(AddCartBo bo);

    /**
     *  获取购物车列表
     * @param loginId  登录id
     * @return  List<StoreOrderCartVo>
     */
    List<StoreOrderCartVo> getCartList(String loginId);

    /**
     *   删除购物车
     * @param bo  bo
     * @return    true：成功 false ：失败
     */
    Boolean delCart(AddCartBo bo);
}
