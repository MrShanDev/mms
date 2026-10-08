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

        return R.success(map);
    }

    /**
     * 会员类别
     */
    @SaCheckLogin
    @GetMapping("/memberSex")
    public R<Object> memberSex() {
        return R.fail("基础系统未提供此业务统计，请使用对应业务模块");
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
    @cn.dev33.satoken.annotation.SaCheckPermission("system:monitor:query")
    @GetMapping("/runtimeInfo")
    public R<SystemRuntimeInfoVo> runtimeInfo() {
        return R.success(systemRuntimeInfoService.getRuntimeInfo());
    }

    /**
     * 系统运行趋势（实时采样）：CPU/内存/JVM 内存
     */
    @SaCheckLogin
    @cn.dev33.satoken.annotation.SaCheckPermission("system:monitor:query")
    @GetMapping("/runtimeTrend")
    public R<SystemRuntimeTrendVo> runtimeTrend(@RequestParam(defaultValue = "120") int limit) {
        return R.success(systemRuntimeTrendService.getTrend(limit));
    }

    /** Issue a short-lived single-use ticket; the JWT never appears in an SSE URL. */
    @cn.dev33.satoken.annotation.SaCheckPermission("system:monitor:query")
    @org.springframework.web.bind.annotation.PostMapping("/runtimeTicket")
    public R<String> runtimeTicket() {
        String ticket = cn.hutool.core.util.IdUtil.simpleUUID();
        com.sxpcwlkj.redis.RedisUtil.setCacheObject("mms:runtime:ticket:" + ticket,
                cn.dev33.satoken.stp.StpUtil.getTokenValue(), java.time.Duration.ofSeconds(30));
        return R.success("success", ticket);
    }

    @GetMapping(value = "/runtimeSse", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter runtimeSse(@RequestParam("ticket") String ticket) {
        if (!ticket.matches("[0-9a-f]{32}")) throw new com.sxpcwlkj.common.exception.MmsException("监控连接票据无效",401);
        String token = com.sxpcwlkj.redis.RedisUtil.getClient().<String>getBucket("mms:runtime:ticket:" + ticket).getAndDelete();
        return systemRuntimeSseHub.register(token);
    }

    /**
     * 快捷菜单
     *
     */
    @SaCheckLogin
    @GetMapping("/menu")
    public R<Object> homeMenu() {
        return R.success(functionService.selectIsFast(9));
    }


    /**
     * 订单数量
     *
     */
    @SaCheckLogin
    @GetMapping("/orderNum")
    public R<Object> orderNum() {
        return R.fail("基础系统未提供此业务统计，请使用对应业务模块");
    }

    /**
     * 订单走势
     *
     */
    @SaCheckLogin
    @GetMapping("/orderPrice")
    public R<Object> orderPrice() {
        return R.fail("基础系统未提供此业务统计，请使用对应业务模块");
    }


}
