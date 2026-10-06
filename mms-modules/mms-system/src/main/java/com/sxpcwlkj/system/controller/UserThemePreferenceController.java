package com.sxpcwlkj.system.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sxpcwlkj.authority.LoginObject;
import com.sxpcwlkj.common.utils.R;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import java.util.Map;
import java.util.Set;

/** 当前用户的外观配置。账户与租户取自登录上下文，不接受客户端指定。 */
@RestController
@RequiredArgsConstructor
@SaCheckLogin
@RequestMapping("system/user/themePreference")
public class UserThemePreferenceController {
    private final JdbcTemplate jdbc;
    private final ObjectMapper json;
    private static final Set<String> TRANSIENT = Set.of("isDrawer", "isFixedHeaderChange", "isShowLogoChange", "__proto__", "constructor", "prototype");

    private static final Set<String> ALLOWED = Set.of("dashboardScene","primary","isIsDark","topBar","topBarColor","isTopBarColorGradual","menuBar","menuBarColor","menuBarActiveColor","isMenuBarColorGradual","columnsMenuBar","columnsMenuBarColor","isColumnsMenuBarColorGradual","isColumnsMenuHoverPreload","isCollapse","isUniqueOpened","isFixedHeader","isClassicSplitMenu","isLockScreen","lockScreenTime","isShowLogo","logoBar","logoBarColor","isBreadcrumb","isTagsview","isBreadcrumbIcon","isTagsviewIcon","isCacheTagsView","isSortableTagsView","isShareTagsView","isFooter","isGrayscale","isInvert","isWartermark","wartermarkText","tagsStyle","animation","columnsAsideStyle","columnsAsideLayout","layout","isRequestRoutes","globalTitle","globalViceTitle","globalViceTitleMsg","globalI18n","globalComponentSize");

    @GetMapping
    public R<Object> get() throws JsonProcessingException {
        var rows = jdbc.queryForList("SELECT theme_json FROM sys_user_theme_preference WHERE tenant_id=? AND user_id=?", LoginObject.getLoginTenant(), LoginObject.getLoginId());
        return R.success(rows.isEmpty() ? Map.of() : json.readValue(String.valueOf(rows.getFirst().get("theme_json")), Map.class));
    }

    @PutMapping
    public R<Object> save(@RequestBody Map<String, Object> preferences) throws JsonProcessingException {
        if (preferences.size() > 100) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "配置字段过多");
        for (var entry : preferences.entrySet()) {
            var value = entry.getValue();
            if (!ALLOWED.contains(entry.getKey()) || !entry.getKey().matches("[A-Za-z][A-Za-z0-9]{0,63}") || TRANSIENT.contains(entry.getKey()) ||
                    !(value instanceof String || value instanceof Boolean || value instanceof Number) ||
                    (value instanceof String text && text.length() > 2048)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "配置格式不正确");
            }
        }
        if (preferences.containsKey("dashboardScene") && !Set.of("base", "mall", "office", "task").contains(String.valueOf(preferences.get("dashboardScene"))))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "控制台方案不正确");
        String encoded = json.writeValueAsString(preferences);
        if (encoded.length() > 16384) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "配置过大");
        jdbc.update("INSERT INTO sys_user_theme_preference(tenant_id,user_id,theme_json) VALUES(?,?,?) ON DUPLICATE KEY UPDATE theme_json=VALUES(theme_json)", LoginObject.getLoginTenant(), LoginObject.getLoginId(), encoded);
        return R.success(Map.of());
    }
}
