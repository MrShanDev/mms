package com.sxpcwlkj.system.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateRange;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.sxpcwlkj.authority.LoginObject;
import com.sxpcwlkj.common.code.controller.BaseController;
import com.sxpcwlkj.common.properties.MmsAdminProperties;
import com.sxpcwlkj.common.utils.R;
import com.sxpcwlkj.system.entity.SysNotice;
import com.sxpcwlkj.system.entity.SysRole;
import com.sxpcwlkj.system.entity.SysTenant;
import com.sxpcwlkj.system.entity.SysUser;
import com.sxpcwlkj.system.entity.vo.SysConfigVo;
import com.sxpcwlkj.system.entity.vo.SysFunctionVo;
import com.sxpcwlkj.system.mapper.SysNoticeMapper;
import com.sxpcwlkj.system.mapper.SysRoleMapper;
import com.sxpcwlkj.system.mapper.SysTenantMapper;
import com.sxpcwlkj.system.mapper.SysUserMapper;
import com.sxpcwlkj.system.service.SysConfigService;
import com.sxpcwlkj.system.service.SysFunctionService;
import com.sxpcwlkj.system.service.SysNoticeService;
import com.sxpcwlkj.system.service.SysUserService;
import com.sxpcwlkj.system.service.SystemRuntimeInfoService;
import com.sxpcwlkj.system.entity.vo.SystemRuntimeInfoVo;
import com.sxpcwlkj.system.entity.vo.SystemRuntimeTrendVo;
import com.sxpcwlkj.system.monitor.SystemRuntimeSseHub;
import com.sxpcwlkj.system.service.SystemRuntimeTrendService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.math.BigDecimal;
import java.util.*;

/**
 * 首页控制台
 * @module 系统管理模块
 * @author mmsAdmin
 * @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
 */
@Tag(name = "系统管理模块-首页控制台",description = "系统管理模块-首页控制台")
@RestController
@RequiredArgsConstructor
@RequestMapping("system/home")
public class HomeController extends BaseController {

    private final SysUserService sysUserService;
    private final SysFunctionService functionService;
    private final SysConfigService configService;
    private final MmsAdminProperties mmsAdminProperties;
    private final SysNoticeService sysNoticeService;
    private final SysUserMapper sysUserMapper;
    private final SysRoleMapper sysRoleMapper;
    private final SysTenantMapper sysTenantMapper;
    private final SysNoticeMapper sysNoticeMapper;
    private final SystemRuntimeInfoService systemRuntimeInfoService;
    private final SystemRuntimeTrendService systemRuntimeTrendService;
    private final SystemRuntimeSseHub systemRuntimeSseHub;

    /**
     * 控制台默认数据
     * @return 数据
     */
    @SaCheckLogin
    @GetMapping("/homeInit")
    public R<Object> homeInit(){
        Map<String,Object> map=new HashMap<>();
        map.put("userName",sysUserService.selectVoById(LoginObject.getLoginId()).getUserName());
        int week = DateUtil.dayOfWeek(new Date());
        map.put("week",week==1?"星期日":week==2?"星期一":week==3?"星期二":week==4?"星期三":week==5?"星期四":week==6?"星期五":"星期六");
        List<SysFunctionVo> fastList= functionService.selectIsFast(9);
        map.put("fastList",fastList);
        List<SysConfigVo> eventList = configService.selectEventList(12);
        map.put("eventList",eventList);
        map.put("systemInfo",mmsAdminProperties);
        map.put("userTool",sysUserService.selectTool());
        map.put("newsTool",sysNoticeService.selectTool());
        map.put("task","0/0");
        return R.success(map);
    }

    /**
     * 会员类别
     */
    @SaCheckLogin
    @GetMapping("/memberSex")
    public R<Object> memberSex() {
        List<Number> list = new ArrayList<>();
        Long count = 0L;
        list.add(count);
        Long count2 = 0L;
        list.add(count2);
        Long count3 = 0L;
        list.add(count3);
        return R.success(list);
    }


    /**
     * 控制台顶部统计卡片（与 MMS 系统管理模块能力对齐）
     * num1：总量；num2：当日新增（按 createdTime）；num3：标题；num4：mms-ui SvgIcon 名（ele- 前缀）
     */
    @SaCheckLogin
    @GetMapping("/info")
    public R<Object> homeInfo() {
        Date now = new Date();
        Date dayStart = DateUtil.beginOfDay(now);
        Date dayEnd = DateUtil.endOfDay(now);

        List<Map<String, Object>> list = new ArrayList<>();

        long userTotal = sysUserMapper.selectCount(Wrappers.lambdaQuery(SysUser.class));
        long userToday = sysUserMapper.selectCount(Wrappers.lambdaQuery(SysUser.class)
                .ge(SysUser::getCreatedTime, dayStart)
                .le(SysUser::getCreatedTime, dayEnd));
        Map<String, Object> map = new HashMap<>();
        map.put("num1", userTotal);
        map.put("num2", userToday);
        map.put("num3", "系统用户");
        map.put("num4", "ele-User");
        map.put("num5", "人");
        map.put("color1", "#409eff");
        map.put("color2", "--next-color-primary-lighter");
        map.put("color3", "--el-color-primary");
        list.add(map);

        long roleTotal = sysRoleMapper.selectCount(Wrappers.lambdaQuery(SysRole.class));
        long roleToday = sysRoleMapper.selectCount(Wrappers.lambdaQuery(SysRole.class)
                .ge(SysRole::getCreatedTime, dayStart)
                .le(SysRole::getCreatedTime, dayEnd));
        Map<String, Object> map2 = new HashMap<>();
        map2.put("num1", roleTotal);
        map2.put("num2", roleToday);
        map2.put("num3", "系统角色");
        map2.put("num4", "ele-Key");
        map2.put("num5", "个");
        map2.put("color1", "#67c23a");
        map2.put("color2", "--next-color-success-lighter");
        map2.put("color3", "--el-color-success");
        list.add(map2);

        long tenantTotal = sysTenantMapper.selectCount(Wrappers.lambdaQuery(SysTenant.class));
        long tenantToday = sysTenantMapper.selectCount(Wrappers.lambdaQuery(SysTenant.class)
                .ge(SysTenant::getCreatedTime, dayStart)
                .le(SysTenant::getCreatedTime, dayEnd));
        Map<String, Object> map3 = new HashMap<>();
        map3.put("num1", tenantTotal);
        map3.put("num2", tenantToday);
        map3.put("num3", "系统租户");
        map3.put("num4", "ele-OfficeBuilding");
        map3.put("num5", "个");
        map3.put("color1", "#e6a23c");
        map3.put("color2", "--next-color-warning-lighter");
        map3.put("color3", "--el-color-warning");
        list.add(map3);

        long noticeTotal = sysNoticeMapper.selectCount(Wrappers.lambdaQuery(SysNotice.class));
        long noticeToday = sysNoticeMapper.selectCount(Wrappers.lambdaQuery(SysNotice.class)
                .ge(SysNotice::getCreatedTime, dayStart)
                .le(SysNotice::getCreatedTime, dayEnd));
        Map<String, Object> map4 = new HashMap<>();
        map4.put("num1", noticeTotal);
        map4.put("num2", noticeToday);
        map4.put("num3", "系统公告");
        map4.put("num4", "ele-Bell");
        map4.put("num5", "条");
        map4.put("color1", "#f56c6c");
        map4.put("color2", "--next-color-danger-lighter");
        map4.put("color3", "--el-color-danger");
        list.add(map4);

        return R.success(list);
    }

    /**
     * 系统运行信息（实时）：OS/CPU/内存/磁盘/JDK/JVM/数据库等
     */
    @SaCheckLogin
    @GetMapping("/runtimeInfo")
    public R<SystemRuntimeInfoVo> runtimeInfo() {
        return R.success(systemRuntimeInfoService.getRuntimeInfo());
    }

    /**
     * 系统运行趋势（实时采样）：CPU/内存/JVM 内存
     */
    @SaCheckLogin
    @GetMapping("/runtimeTrend")
    public R<SystemRuntimeTrendVo> runtimeTrend(@RequestParam(defaultValue = "120") int limit) {
        return R.success(systemRuntimeTrendService.getTrend(limit));
    }

    /**
     * SSE：系统运行状态实时推送
     * <p>注意：EventSource 无法自定义 Header，这里通过 query 透传 Authorization token。</p>
     */
    @GetMapping(value = "/runtimeSse", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter runtimeSse(@RequestParam("Authorization") String authorization) {
        SseEmitter emitter = systemRuntimeSseHub.register(authorization);
        return emitter;
    }

    /**
     * 快捷菜单
     *
     */
    @SaCheckLogin
    @GetMapping("/menu")
    public R<Object> homeMenu() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Map<String, Object> map = new HashMap<>();
        map.put("icon", "iconfont icon-yangan");
        map.put("label", "会员管理");
        map.put("path", "/sxpcwlkj/storeMember");
        map.put("iconColor", "#F72B3F");
        Map<String, Object> query = new HashMap<>();
        map.put("query", query);
        list.add(map);

        Map<String, Object> map2 = new HashMap<>();
        map2.put("icon", "iconfont icon-wendu");
        map2.put("label", "实名认证");
        map2.put("path", "/sxpcwlkj/storeMemberAuthentication");
        map2.put("iconColor", "#91BFF8");
        Map<String, Object> query2 = new HashMap<>();
        map2.put("query", query2);
        list.add(map2);

        Map<String, Object> map3 = new HashMap<>();
        map3.put("icon", "iconfont icon-neiqianshujuchucun");
        map3.put("label", "商品列表");
        map3.put("path", "/sxpcwlkj/storeProduct");
        map3.put("iconColor", "#88D565");
        Map<String, Object> query3 = new HashMap<>();
        map3.put("query", query3);
        list.add(map3);

        Map<String, Object> map4 = new HashMap<>();
        map4.put("icon", "iconfont icon-fuwenbenkuang");
        map4.put("label", "文章列表");
        map4.put("path", "/sxpcwlkj/storeArticle");
        map4.put("iconColor", "#88D565");
        Map<String, Object> query4 = new HashMap<>();
        map4.put("query", query4);
        list.add(map4);

        Map<String, Object> map5 = new HashMap<>();
        map5.put("icon", "iconfont icon-yangan");
        map5.put("label", "待付款订单");
        map5.put("path", "/sxpcwlkj/storeOrder");
        map5.put("iconColor", "#FBD4A0");
        Map<String, Object> query5 = new HashMap<>();
        query5.put("status", 10);
        map5.put("query", query5);
        list.add(map5);

        Map<String, Object> map6 = new HashMap<>();
        map6.put("icon", "iconfont icon-shouye_dongtaihui");
        map6.put("label", "待发货订单");
        map6.put("path", "/sxpcwlkj/storeOrder");
        map6.put("iconColor", "#FBD4A0");
        Map<String, Object> query6 = new HashMap<>();
        query6.put("status", 20);
        map6.put("query", query6);
        list.add(map6);

        return R.success(list);
    }


    /**
     * 订单数量
     *
     */
    @SaCheckLogin
    @GetMapping("/orderNum")
    public R<Object> orderNum() {
        Map<String, Object> map = new HashMap<>();
        List<BigDecimal> list1 = new ArrayList<>();
        List<BigDecimal> list2 = new ArrayList<>();
        //月份集合
        // 创建日期范围生成器
        DateTime start = DateUtil.parse("2024-01-01");
        DateTime end = DateUtil.parse("2024-12-31");
        DateRange range = DateUtil.range(start, end, DateField.MONTH);

        for (DateTime date : range) {
            BigDecimal num1 = new BigDecimal(0);
            list1.add(num1 == null ? new BigDecimal(0) : num1);
            BigDecimal num2 = new BigDecimal(0);
            list2.add(num2 == null ? new BigDecimal(0) : num2);
        }
        map.put("list1", list1);
        map.put("list2", list2);

        return R.success(map);
    }

    /**
     * 订单走势
     *
     */
    @SaCheckLogin
    @GetMapping("/orderPrice")
    public R<Object> orderPrice() {
        Map<String, Object> map = new HashMap<>();
        List<Map<String, Object>> list1 = new ArrayList<>();
        List<Map<String, Object>> list2 = new ArrayList<>();
        List<Map<String, Object>> list3 = new ArrayList<>();
        //月份集合
        // 创建日期范围生成器
        DateTime start = DateUtil.parse("2024-01-01");
        DateTime end = DateUtil.parse("2024-12-31");
        DateRange range = DateUtil.range(start, end, DateField.MONTH);

        int i = 1;
        for (DateTime date : range) {
            //{ value: 0, stationName: 's1' },
            Map<String, Object> map1 = new HashMap<>();
            BigDecimal num1 = new BigDecimal(0);
            num1 = num1 == null ? new BigDecimal(0) : num1;
            map1.put("value", num1);
            map1.put("stationName", "s" + i);
            list1.add(map1);

            Map<String, Object> map2 = new HashMap<>();
            BigDecimal num2 = new BigDecimal(0);
            num2 = num2 == null ? new BigDecimal(0) : num2;
            map2.put("value", num2);
            map2.put("stationName", "s" + i);
            list2.add(map2);

            Map<String, Object> map3 = new HashMap<>();
            map3.put("value", num1.add(num2));
            map3.put("stationName", "s" + i);
            list3.add(map3);

            i++;
        }
        map.put("list1", list1);
        map.put("list2", list2);
        map.put("list3", list3);

        return R.success(map);
    }


}
