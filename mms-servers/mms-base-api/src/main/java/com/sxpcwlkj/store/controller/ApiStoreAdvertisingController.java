package com.sxpcwlkj.store.controller;

import cn.dev33.satoken.annotation.SaIgnore;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.sxpcwlkj.common.code.controller.BaseController;
import com.sxpcwlkj.common.enums.SystemCommonEnum;
import com.sxpcwlkj.common.utils.R;
import com.sxpcwlkj.store.entity.StoreAdvertisingLocation;
import com.sxpcwlkj.store.entity.bo.StoreAdvertisingBo;
import com.sxpcwlkj.store.entity.vo.StoreAdvertisingLocationVo;
import com.sxpcwlkj.store.entity.vo.StoreAdvertisingVo;
import com.sxpcwlkj.store.service.StoreAdvertisingLocationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
* 广告列表
 * @author mmsAdmin3
*/
@Tag(name = "🌳商城模块-广告列表",description = "广告列表等一些基础功能")
@Slf4j
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("mall-api/v1/common/location")
public class ApiStoreAdvertisingController extends BaseController{

    private final StoreAdvertisingLocationService storeAdvertisingLocationService;


    /**
     * 所有广告位列表
     */
    @SaIgnore
    @Operation(summary = "所有广告位列表", description = "所有广告位列表")
    @GetMapping("/locationList")
    public R<List<StoreAdvertisingLocationVo>> locationList(){
         StoreAdvertisingBo bo = new StoreAdvertisingBo();
         bo.setStatus(SystemCommonEnum.SYS_COMMON_STATE_OPEN.getValue());
         List<StoreAdvertisingLocationVo> list= storeAdvertisingLocationService.selectVoList(new LambdaQueryWrapper<StoreAdvertisingLocation>()
                .eq(StoreAdvertisingLocation::getStatus, SystemCommonEnum.SYS_COMMON_STATE_OPEN.getValue()).orderByDesc(StoreAdvertisingLocation::getSort));
         return R.success(list);
    }

    /**
     * 根据广告位编码查询广告
     * @param advertisingCode 广告位编码
     * @param size 查询条数
     * @return 数据
     */
    @SaIgnore
    @GetMapping("/selectByAdvertisingCode")
    public R<List<StoreAdvertisingVo>> selectByAdvertisingCode( String advertisingCode,  @RequestParam(defaultValue = "5") int size){
        return R.success(storeAdvertisingLocationService.selectVoListByCode(advertisingCode,size));
    }




}
