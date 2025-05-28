package com.sxpcwlkj.mobile.controller;

import cn.dev33.satoken.annotation.SaIgnore;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.sxpcwlkj.common.code.controller.BaseController;
import com.sxpcwlkj.common.utils.R;
import com.sxpcwlkj.mobile.entity.StoreAdvertisingLocation;
import com.sxpcwlkj.mobile.entity.bo.AdvertisingBo;
import com.sxpcwlkj.mobile.entity.bo.StoreAdvertisingBo;
import com.sxpcwlkj.mobile.entity.vo.StoreAdvertisingLocationVo;
import com.sxpcwlkj.mobile.service.StoreAdvertisingLocationService;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


import java.util.List;


/**
* 广告列表
*
* @author 西决 942879858@qq.com
* @since 1.0.0 2024-05-13
*/
@Slf4j
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/location")
public class ApiStoreAdvertisingController extends BaseController{

    private final StoreAdvertisingLocationService storeAdvertisingLocationService;


    /**
     * 所有广告位列表
     * @return
     */
    @SaIgnore
    @PostMapping("/locationList")
    public R<List<StoreAdvertisingLocationVo>> locationList(){
         StoreAdvertisingBo bo = new StoreAdvertisingBo();
         bo.setStatus(1);
         List<StoreAdvertisingLocationVo> list= storeAdvertisingLocationService.selectVoList(new LambdaQueryWrapper<StoreAdvertisingLocation>()
                .eq(StoreAdvertisingLocation::getStatus,1).orderByDesc(StoreAdvertisingLocation::getSort));
         return R.success(list);
    }


    /**
     * 某个广告位下广告
     * @param bo 广告位编码【code】
     * @return
     */
    @SaIgnore
    @PostMapping("/locationListById")
    public R<StoreAdvertisingLocationVo> locationListById(@Validated @RequestBody AdvertisingBo bo){

        return R.success(storeAdvertisingLocationService.selectVoListByCode(bo.getCode()));
    }

}
