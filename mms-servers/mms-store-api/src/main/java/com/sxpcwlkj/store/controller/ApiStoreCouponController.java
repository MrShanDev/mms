package com.sxpcwlkj.store.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.sxpcwlkj.common.utils.R;
import com.sxpcwlkj.store.entity.StoreCoupon;
import com.sxpcwlkj.store.entity.vo.StoreCouponVo;
import com.sxpcwlkj.store.mapper.StoreCouponMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "🌳商城模块-优惠卷",description = "")
@Slf4j
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("mall-api/v1/common/coupon")
public class ApiStoreCouponController {

    private final StoreCouponMapper storeCouponMapper;


    @Operation(summary = "优惠卷查询", description = "")
    @GetMapping("selectByCode")
    public R<StoreCouponVo> selectByCode(@NonNull String code){
        StoreCouponVo vo=  storeCouponMapper.selectVoOne(new LambdaQueryWrapper<StoreCoupon>()
            .eq(StoreCoupon::getCouponCode,code));
        return R.success(vo);
    }


}
