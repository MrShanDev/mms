package com.sxpcwlkj.article.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaIgnore;
import com.sxpcwlkj.authority.LoginObject;
import com.sxpcwlkj.common.code.controller.BaseController;
import com.sxpcwlkj.common.code.entity.ThreeQueryBo;
import com.sxpcwlkj.common.enums.SystemCommonEnum;
import com.sxpcwlkj.common.utils.R;
import com.sxpcwlkj.datasource.entity.page.PageQuery;
import com.sxpcwlkj.datasource.entity.page.TableDataInfo;
import com.sxpcwlkj.article.entity.bo.StoreArticleBo;
import com.sxpcwlkj.article.entity.vo.StoreArticleCateVo;
import com.sxpcwlkj.article.entity.vo.StoreArticleVo;
import com.sxpcwlkj.article.service.StoreArticleCateService;
import com.sxpcwlkj.article.service.StoreArticleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Tag(name = "🌳商城模块-文章列表",description = "文章列表等一些基础功能")
@Slf4j
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("article/v1")
public class ApiStoreArticleController extends BaseController {

    private final StoreArticleCateService baseService;
    private final StoreArticleService  storeArticleService;

    /**
     *  文章分类列表
     * @return  List<StoreArticleCateVo>
     */
    @SaIgnore
    @PostMapping("/listCate")
    public R<List<StoreArticleCateVo>> listPage() {
        ThreeQueryBo bo = new ThreeQueryBo();
        return success(baseService.queryTree(bo.getIsAll(),bo.getShowLevel()));
    }

    /**
     * 文章列表
     * @param cateId  分类id
     * @param pageSize  每页条数
     * @param pageNum  页码
     * @return  TableDataInfo<StoreArticleVo>
     */
    @SaIgnore
    @PostMapping("/listArticle")
    public TableDataInfo<StoreArticleVo> listPage(
        @NotBlank(message = "分类ID不能为空")
        @RequestParam String cateId,

        @NotNull(message = "每页大小不能为空")
        @Min(value = 1, message = "每页大小不能小于1")
        @Max(value = 100, message = "每页大小不能大于100")
        @RequestParam(defaultValue = "10") Integer pageSize,

        @NotNull(message = "页码不能为空")
        @Min(value = 1, message = "页码不能小于1")
        @RequestParam(defaultValue = "1") Integer pageNum) {

        StoreArticleBo bo = new StoreArticleBo();
        bo.setArticleCateId(cateId);
        bo.setPageSize(pageSize);
        bo.setPageNum(pageNum);
        bo.setStatus(SystemCommonEnum.SYS_COMMON_STATE_OPEN.getValue());
        return storeArticleService.selectListVoPage(bo, bo.getPageQuery());
    }


    /**
     *  根据id查询-文章
     */
    @SaIgnore
    @GetMapping("/queryArticleById")
    public R<StoreArticleVo> queryArticleById(@NotBlank(message = "文章ID不能为空")
                                               @RequestParam String id) {
        return success(storeArticleService.selectVoById(id));
    }

    /**
     * 发布文章【需登录】
     * @param bo 文章信息
     * @return 结果
     */
    @SaCheckLogin
    @Operation(summary = "发布文章", description = "用户发布商城文章，默认为待审核状态")
    @PostMapping("/publishArticle")
    public R<Void> publishArticle(@RequestBody StoreArticleBo bo) {
        //验证，标题最少3个字符，文章内容不能为空不超过500字
        if (bo.getTitle() == null || bo.getTitle().length() < 3) {
            return R.fail("标题最少3个字符");
        }
        if (bo.getContent() == null || bo.getContent().length() > 10000) {
            return R.fail("内容不能为空不超过10000字");
        }
        bo.setArticleCateId("2011427967877816321");
        bo.setMemberId(LoginObject.getLoginId());
        bo.setStatus(SystemCommonEnum.SYS_COMMON_STATE_CLOSE.getValue()); // 0 = 待审核
        return storeArticleService.insert(bo) ? R.success() : R.fail("发布失败");
    }

    /**
     * 文章详情
     * @param id 文章id
     * @return 文章详情
     */
    @SaIgnore
    @Operation(summary = "文章详情", description = "获取商城文章详细信息")
    @GetMapping("/getArticleInfo")
    public R<StoreArticleVo> getArticleInfo(@NotBlank(message = "文章ID不能为空") String id) {
        return R.success(storeArticleService.selectVoById(id));
    }

    /**
     * 编辑文章【需登录】
     * @param bo 文章信息
     * @return
     */
    @SaCheckLogin
    @Operation(summary = "编辑文章", description = "用户编辑自己的商城文章")
    @PostMapping("/editArticle")
    public R<Void> editArticle(@RequestBody StoreArticleBo bo) {
        if (bo.getTitle() == null || bo.getTitle().length() < 3) {
            return R.fail("标题最少3个字符");
        }
        if (bo.getContent() == null || bo.getContent().length() > 10000) {
            return R.fail("内容不能为空不超过10000字");
        }
        bo.setArticleCateId("2011427967877816321");
        StoreArticleVo old = storeArticleService.selectVoById(bo.getId());
        if (old == null) {
            return R.fail("文章不存在");
        }
        if (old.getMemberId() == null || !old.getMemberId().equals(LoginObject.getLoginId())) {
            return R.fail("无权限编辑他人文章");
        }
        bo.setRevision(old.getRevision());
        // 编辑后重新进入待审核状态
        bo.setStatus(SystemCommonEnum.SYS_COMMON_STATE_CLOSE.getValue());
        return storeArticleService.updateById(bo) ? R.success() : R.fail("编辑失败");
    }

    /**
     * 删除文章【需登录】
     * @param id 文章id
     * @return 结果
     */
    @SaCheckLogin
    @Operation(summary = "删除文章", description = "用户删除自己的商城文章")
    @DeleteMapping("/deleteArticle")
    public R<Void> deleteArticle(@NotBlank(message = "文章ID不能为空") String id) {
        StoreArticleVo old = storeArticleService.selectVoById(id);
        if (old == null) {
            return R.fail("文章不存在");
        }
        if (old.getMemberId() == null || !old.getMemberId().equals(LoginObject.getLoginId())) {
            return R.fail("无权限删除他人文章");
        }
        return storeArticleService.deleteById(id) ? R.success() : R.fail("删除失败");
    }

    /**
     * 获取文章统计【需登录】
     * @return 统计信息
     */
    @SaCheckLogin
    @Operation(summary = "获取文章统计", description = "统计当前用户的总商城文章数和待审核文章数")
    @GetMapping("/getArticleCount")
    public R<Map<String, Object>> getArticleCount() {

        Map<String, Object> result = new HashMap<>();

        StoreArticleBo totalBo = new StoreArticleBo();
        totalBo.setStatus(SystemCommonEnum.SYS_COMMON_STATE_OPEN.getValue());
        result.put("total", storeArticleService.count(totalBo));

        StoreArticleBo pendingBo = new StoreArticleBo();
        pendingBo.setStatus(SystemCommonEnum.SYS_COMMON_STATE_CLOSE.getValue());
        result.put("pending", storeArticleService.count(pendingBo));

        return R.success(result);
    }

    /**
     * 我的文章分页【需登录】
     * @param  pageQuery 查询条件
     * @return 分页列表
     */
    @SaCheckLogin
    @Operation(summary = "我的文章分页", description = "获取当前用户的商城文章列表，支持按状态过滤")
    @PostMapping("/myArticlePage")
    public R<TableDataInfo<StoreArticleVo>> myArticlePage(@RequestBody PageQuery pageQuery) {
        StoreArticleBo bo = new StoreArticleBo();
        bo.setPageSize(pageQuery.getPageSize());
        bo.setPageNum(pageQuery.getPageNum());
        bo.setMemberId(LoginObject.getLoginId());
        return R.success(storeArticleService.selectListVoPage(bo, bo.getPageQuery()));
    }
}
