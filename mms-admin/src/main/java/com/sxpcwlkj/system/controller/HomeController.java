package com.sxpcwlkj.system.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateRange;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.sxpcwlkj.MmsAdminApplication;
import com.sxpcwlkj.authority.LoginObject;
import com.sxpcwlkj.common.code.controller.BaseController;
import com.sxpcwlkj.common.utils.R;
import com.sxpcwlkj.system.entity.vo.SysConfigVo;
import com.sxpcwlkj.system.entity.vo.SysFunctionVo;
import com.sxpcwlkj.system.service.SysConfigService;
import com.sxpcwlkj.system.service.SysFunctionService;
import com.sxpcwlkj.system.service.SysNoticeService;
import com.sxpcwlkj.system.service.SysUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.*;

/**
 * 首页控制台
 * @module 系统管理模块
 * @author mmsAdmin
 * @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("system/home")
public class HomeController extends BaseController {

    private final SysUserService sysUserService;
    private final SysFunctionService functionService;
    private final SysConfigService configService;
    private final MmsAdminApplication mmsAdminApplication;
    private final SysNoticeService sysNoticeService;

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
        map.put("systemInfo",mmsAdminApplication);
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


    //1:会员总人数 ，今日新增
    //2:代发货总数量 ，  今日数量
    //3:待还机总数量 ，  今到期数量
    //4:分期付款总订单 ， 今日付款数量


    /**
     * 待处理任务
     */
    @SaCheckLogin
    @GetMapping("/info")
    public R<Object> homeInfo() {
        String loginId = LoginObject.getLoginId();
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Map<String, Object> map = new HashMap<>();
        //总人数
        Long memberCount = 0L;
        map.put("num1", memberCount);
        //今日数量
        Long todayMemberCount = 0L;
        map.put("num2", todayMemberCount);
        map.put("num3", "会员人数");
        //https://icones.js.org
        map.put("num4", "iconoir:accessibility");
        map.put("num5", "人");
        map.put("color1", "#FF6462");
        map.put("color2", "--next-color-primary-lighter");
        map.put("color3", "--el-color-primary");
        list.add(map);

        Map<String, Object> map2 = new HashMap<>();
        //待发货
        Long orderCount = 0L;
        map2.put("num1", orderCount);
        //今日数量
        Long todayOrderCount = 0L;
        map2.put("num2", todayOrderCount);
        map2.put("num3", "订单发货");
        map2.put("num4", "octicon:copilot-16");
        map2.put("num5", "件");
        map2.put("color1", "#6690F9");
        map2.put("color2", "--next-color-success-lighter");
        map2.put("color3", "--el-color-success");
        list.add(map2);

        Map<String, Object> map3 = new HashMap<>();
        //待发货
        Long orderCountGuiHai = 0L;
        map3.put("num1", orderCountGuiHai);
        //今日数量
        Long todayOrderCountGuiHai = 0L;
        map3.put("num2", todayOrderCountGuiHai);
        map3.put("num3", "到期待还");
        map3.put("num4", "emojione-monotone:alembic");
        map3.put("num5", "件");
        map3.put("color1", "#6690F9");
        map3.put("color2", "--next-color-warning-lighter");
        map3.put("color3", "--el-color-warning");
        list.add(map3);

        Map<String, Object> map4 = new HashMap<>();
        //待发货
        Long orderCountFenQi = 0L;
        map4.put("num1", orderCountFenQi);
        //今日数量
        Long todayOrderCountFenQi = 0L;
        map4.put("num2", todayOrderCountFenQi);
        map4.put("num3", "分期待付");
        map4.put("num4", "emojione-monotone:bear-face");
        map4.put("num5", "个");
        map4.put("color1", "#FF6462");
        map4.put("color2", "--next-color-danger-lighter");
        map4.put("color3", "--el-color-danger");
        list.add(map4);

        return R.success(list);
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
