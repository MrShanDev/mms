package com.sxpcwlkj.store.controller;

import cn.dev33.satoken.annotation.SaIgnore;
import com.alibaba.excel.util.StringUtils;
import com.sxpcwlkj.authority.LoginObject;
import com.sxpcwlkj.common.exception.MmsException;
import com.sxpcwlkj.store.service.StoreSearchResService;
import com.sxpcwlkj.common.utils.R;
import com.sxpcwlkj.datasource.entity.page.PageQuery;
import com.sxpcwlkj.datasource.entity.page.TableDataInfo;
import com.sxpcwlkj.store.entity.bo.StoreProductSpuBo;
import com.sxpcwlkj.store.entity.vo.StoreProductSpuVo;
import com.sxpcwlkj.store.service.StoreProductSpuService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(name = "🌳商城模块-商品接口",description = "")
@Slf4j
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("mall-api/v1/common/sup")
public class ApiStoreSupController {
    private final StoreProductSpuService baseService;
    private final StoreSearchResService  storeSearchResService;

    @SaIgnore
    @Operation(summary = "商品搜索列表", description = "商品搜索分页，只需要传入关键字，其他参数为空")
    @GetMapping("/getSearchSpuList")
    public TableDataInfo<StoreProductSpuVo> getSearchSpuList(
        @RequestParam(required = true,defaultValue = "1") Integer pageNum,
        @RequestParam(required = true,defaultValue = "10") Integer pageSize,
        @Parameter(description = "搜索关键字") String keywords) {
        PageQuery pageQuery=new PageQuery(pageNum,pageSize);
        StoreProductSpuBo bo=new StoreProductSpuBo();
        bo.setCateId(null);
        bo.setBrandId(null);
        bo.setTitle(keywords);
        bo.setStoreId(null);
        bo.setStatus(1);
        if(StringUtils.isBlank(keywords)){
            throw new MmsException("关键字不能为空");
        }
        storeSearchResService.addSearchRes(keywords,LoginObject.isLogin()?LoginObject.getLoginId():null);
        return baseService.selectListVoPage(bo, pageQuery);
    }

    /**
     * 商品列表
     * @param brandId  品牌ID （选填）
     * @param cateId   分类ID （选填）
     * @param storeId  店铺ID （选填）
     * @param title    商品标题 （模糊检索）
     * @param pageNum  当前页
     * @param pageSize 每页显示数量
     * @return 商品数据
     */
    @SaIgnore
    @Operation(summary = "商品列表", description = "")
    @PostMapping("listPage")
    public TableDataInfo<StoreProductSpuVo>  selectPage(
        String brandId,
        String cateId,
        String storeId,
        String title,
        @RequestParam(required = false, defaultValue = "1") Integer pageNum,
        @RequestParam(required = false, defaultValue = "10") Integer pageSize){
        PageQuery pageQuery=new PageQuery(pageNum,pageSize);
        StoreProductSpuBo bo=new StoreProductSpuBo();
        bo.setCateId(cateId);
        bo.setBrandId(brandId);
        bo.setTitle(title);
        bo.setStoreId(storeId);
        bo.setStatus(1);
        return baseService.selectListVoPageXml(bo, pageQuery);
    }

    @SaIgnore
    @Operation(summary = "商品详情", description = "")
    @GetMapping("selectById")
    public R<StoreProductSpuVo> selectById(@NonNull String spuid){
        return R.success(baseService.selectVoById(spuid));
    }


 }
