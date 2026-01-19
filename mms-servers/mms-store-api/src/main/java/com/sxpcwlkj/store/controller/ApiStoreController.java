package com.sxpcwlkj.store.controller;

import cn.dev33.satoken.annotation.SaIgnore;
import com.sxpcwlkj.common.code.controller.BaseController;
import com.sxpcwlkj.common.utils.R;
import com.sxpcwlkj.datasource.entity.page.PageQuery;
import com.sxpcwlkj.datasource.entity.page.TableDataInfo;
import com.sxpcwlkj.store.entity.bo.StoreProductSpuBo;
import com.sxpcwlkj.store.entity.vo.StoreProductSpuVo;
import com.sxpcwlkj.store.entity.vo.StoreVo;
import com.sxpcwlkj.store.service.StoreProductSpuService;
import com.sxpcwlkj.store.service.StoreService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@Tag(name = "🌳商城模块-店铺列表",description = "店铺列表等一些基础功能")
@Slf4j
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("mall-api/v1/common/store")
public class ApiStoreController extends BaseController {

    private final StoreService storeService;
    private final StoreProductSpuService storeProductSpuService;
    @SaIgnore
    @Operation(summary = "店铺详情", description = "店铺详情")
    @GetMapping("/storeInfo")
    public R<Map<String, Object>> getStoreInfo(String storeId) {
        StoreVo storeVo= storeService.selectVoById(storeId);
        Map<String, Object> map = new HashMap<>();
        map.put("title",storeVo.getStoreName());
        map.put("bgImg",storeVo.getStoreBgImg());
        map.put("logo",storeVo.getStoreLogo());
        map.put("phone",storeVo.getStorePhone());
        map.put("storeScore",storeVo.getStoreScore());
        map.put("storeRank",storeVo.getStoreRank());
        map.put("storeImgOne",storeVo.getStoreImgOne());
        return R.success(map);

    }
    @SaIgnore
    @Operation(summary = "店铺商品列表", description = "店铺商品列表")
    @GetMapping("/getStoreSpuList")
    public TableDataInfo<StoreProductSpuVo> getStoreSpuList(
        @RequestParam(required = true,defaultValue = "1") Integer pageNum,
        @RequestParam(required = true,defaultValue = "10") Integer pageSize,
        @Parameter(description = "店铺ID") String storeId) {
        PageQuery pageQuery=new PageQuery(pageNum,pageSize);
        StoreProductSpuBo bo=new StoreProductSpuBo();
        bo.setCateId(null);
        bo.setBrandId(null);
        bo.setTitle(null);
        bo.setStoreId(storeId);
        bo.setStatus(1);
        return storeProductSpuService.selectListVoPage(bo, pageQuery);
    }



}
