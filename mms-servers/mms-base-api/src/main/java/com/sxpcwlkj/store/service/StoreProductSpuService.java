package com.sxpcwlkj.store.service;

import com.sxpcwlkj.datasource.entity.page.PageQuery;
import com.sxpcwlkj.datasource.entity.page.TableDataInfo;
import com.sxpcwlkj.framework.service.BaseService;
import com.sxpcwlkj.store.entity.StoreProductSpu;
import com.sxpcwlkj.store.entity.bo.StoreProductSpuBo;
import com.sxpcwlkj.store.entity.export.StoreProductSpuExport;
import com.sxpcwlkj.store.entity.vo.StoreProductSpuVo;

import java.util.List;
import java.util.Set;

/**
 * 店铺商品-接口
 *
 * @author mmsAdmin
 * @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
 * @describe  支持自定义扩展,已继承接口：{insert、deleteById、updateById、selectById、getByEntityListPage}（更多查看BaseService接口）
 */
public interface StoreProductSpuService extends BaseService<StoreProductSpu, StoreProductSpuVo, StoreProductSpuBo> {
    /**
    * 导出店铺商品
    * @param list 店铺商品列表
    * @return true：成功 false ：失败
    */
    Boolean imports(Set<StoreProductSpuExport> list);

    /**
     *  根据分类ID查询商品
     * @param spuCateId  分类ID
     * @return  List<StoreProductSpuVo>
     */
    List<StoreProductSpuVo> selectVoByCateId(String spuCateId, Integer limit);

    /**
     * 商品下单验证
     */
    void checkOrder(String spuId, String skuId, Integer num);

    /**
     * 首页商品 商品和商品分类状态都为1
     * @param spuCateId 分类ID
     * @param productNum 商品数量
     * @return 商品数据
     */
    List<StoreProductSpuVo> homeSpuList(String spuCateId, Integer productNum);

    /**
     * 商品列表
     * @param bo 查询参数
     * @param pageQuery 分页参数
     * @return 商品数据
     */
    TableDataInfo<StoreProductSpuVo> selectListVoPageXml(StoreProductSpuBo bo, PageQuery pageQuery);
}
