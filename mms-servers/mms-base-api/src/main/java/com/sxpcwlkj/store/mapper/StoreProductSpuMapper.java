package com.sxpcwlkj.store.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sxpcwlkj.datasource.mapper.BaseMapperPlus;
import com.sxpcwlkj.store.entity.StoreProductSpu;
import com.sxpcwlkj.store.entity.vo.StoreProductSpuVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
* 店铺商品-Mapper
*
* @author mmsAdmin
* @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
*/
@Mapper
@Repository
public interface StoreProductSpuMapper extends BaseMapperPlus<StoreProductSpu, StoreProductSpuVo> {

    /**
     * 向上关联查询（父级分类包含子级分类，但子级分类不包含父级分类）
     * @param spuCateId  分类ID
     * @return  List<StoreProductSpuVo>
     */
    List<StoreProductSpuVo> selectVoByCateId(@Param("spuCateId") String spuCateId, @Param("limit") int limit);

    /**
     * 首页商品,商品和商品分类状态都为1
     * @param spuCateId 分类ID
     * @param limit 商品数量
     * @return 商品数据
     */
    List<StoreProductSpuVo> homeSpuList(@Param("spuCateId") String spuCateId, @Param("limit") int limit);
    /**
     * 分页查询 已上架的商品
     * @param cateId 分类ID ，支持下级分类商品
     * @return 分页对象
     */
    Page<StoreProductSpuVo> selectVoPageXmlAdmin(@Param("page") Page<Object> build,
                                                 @Param("storeId") String storeId ,
                                                 @Param("cateId")String cateId,
                                                 @Param("brandId")String brandId,
                                                 @Param("title")String title,
                                                 @Param("status")Integer status);

    /**
     * 分页查询 已上架的商品
     * @param cateId 分类ID
     * @return 分页对象
     */
    Page<StoreProductSpuVo> selectVoPageXml(@Param("page") Page<Object> build, @Param("cateId") String cateId);
}
