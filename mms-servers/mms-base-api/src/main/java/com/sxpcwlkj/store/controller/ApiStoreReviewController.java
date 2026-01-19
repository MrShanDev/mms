package com.sxpcwlkj.store.controller;

import cn.dev33.satoken.annotation.SaIgnore;
import com.sxpcwlkj.authority.LoginObject;
import com.sxpcwlkj.common.utils.R;
import com.sxpcwlkj.datasource.entity.page.TableDataInfo;
import com.sxpcwlkj.framework.config.ValidatedGroupConfig;
import com.sxpcwlkj.store.entity.bo.StoreReviewBo;
import com.sxpcwlkj.store.entity.vo.StoreReviewVo;
import com.sxpcwlkj.store.service.StoreReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *  店铺订单接口
 *
 */
@Tag(name = "🌳商城模块-评论接口",description = "店铺订单评论功能")
@Slf4j
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("mall-api/v1/common/review")
public class ApiStoreReviewController {

    private final StoreReviewService baseService;


    @Operation(summary = "添加评论", description = "已登录用户添加评论")
    @PostMapping("/addReview")
    public R<String> addReview(@RequestBody @Validated(ValidatedGroupConfig.insert.class) StoreReviewBo bo) {
        bo.setUserId(LoginObject.getLoginId());
        Boolean  flag=baseService.addReview(bo);
        //状态:1-正常,0-审核中,2-已删除,3-违规
        bo.setStatus(0);
        bo.setSort(1);
        bo.setRemark("");
        bo.setTenantId("");
        return  R.ok(flag,flag?"添加成功":"添加失败", bo.getId());
    }


    /**
     * 分页列表-商品评论表
     * @param bo 查询条件
     * @return 分页对象
     */
    @SaIgnore
    @Operation(summary = "分页列表-商品评论表", description = "分页列表-商品评论表")
    @PostMapping("/list")
    public TableDataInfo<StoreReviewVo> listPage(@RequestBody @Validated(ValidatedGroupConfig.query.class) StoreReviewBo bo){
        if(bo.getSpuId()==null){
            throw new RuntimeException("商品ID不能为空");
        }
        bo.setStatus(1);
        return baseService.selectListVoPage(bo, bo.getPageQuery());
    }

}
