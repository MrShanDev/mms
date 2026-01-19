package com.sxpcwlkj.store.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sxpcwlkj.datasource.mapper.BaseMapperPlus;
import com.sxpcwlkj.store.entity.StoreRefundApply;
import com.sxpcwlkj.store.entity.vo.StoreRefundApplyVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
* 退款申请表-Mapper
*
* @author mmsAdmin
* @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
*/
@Mapper
@Repository
public interface StoreRefundApplyMapper extends BaseMapperPlus<StoreRefundApply, StoreRefundApplyVo> {

    Page<StoreRefundApplyVo> selectVoPageAfterSever(@Param("page") Page<?> page, @Param("userId") String userId, @Param("orderStatus") Integer orderStatus, @Param("orderNo") String orderNo);
}
