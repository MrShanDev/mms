package com.sxpcwlkj.system.controller;
import cn.dev33.satoken.annotation.SaCheckLogin;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.sxpcwlkj.authority.LoginObject;
import com.sxpcwlkj.common.utils.R;
import com.sxpcwlkj.common.exception.MmsException;
import com.sxpcwlkj.system.mapper.*;
import com.sxpcwlkj.system.entity.SysNotice;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequiredArgsConstructor
@SaCheckLogin
@RequestMapping("system/home/notices")
public class HomeNoticeController {
    private final SysNoticeMapper notices;
    private final SysNoticeReadMapper reads;
    @GetMapping public R<Map<String,Object>> list() {
        var rows = notices.selectList(new LambdaQueryWrapper<SysNotice>().eq(SysNotice::getStatus,1)
                .orderByDesc(SysNotice::getCreatedTime).last("LIMIT 100"));
        return R.success(Map.of("rows",rows,"readIds",reads.readIds(LoginObject.getLoginTenant(),LoginObject.getLoginId())));
    }
    @GetMapping("/{id}") public R<SysNotice> detail(@PathVariable String id) { return R.success(published(id)); }
    @PostMapping("/{id}/read") public R<Boolean> markRead(@PathVariable String id) {
        published(id); reads.markRead(LoginObject.getLoginTenant(),LoginObject.getLoginId(),id); return R.success(true);
    }
    private SysNotice published(String id) {
        SysNotice notice=notices.selectById(id);
        if (notice==null || !Objects.equals(notice.getStatus(),1)) throw new MmsException("公告不存在或未发布");
        return notice;
    }
}
