package com.sxpcwlkj.system.service.impl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.sxpcwlkj.common.code.entity.PageResult;
import com.sxpcwlkj.common.exception.MmsException;
import com.sxpcwlkj.datasource.entity.page.TableDataInfo;
import com.sxpcwlkj.system.entity.SysTenant;
import com.sxpcwlkj.system.entity.bo.TenantQueryBo;
import com.sxpcwlkj.system.mapper.SysTenantMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
@Service
@RequiredArgsConstructor
public class TenantQueryService {
    private final SysTenantMapper tenants;
    public PageResult<SysTenant> list(TenantQueryBo query) {
        if (query.getStatus()!=null && query.getStatus()!=0 && query.getStatus()!=1) throw new MmsException("租户状态无效");
        String keyword=query.getKeyword()==null?"":query.getKeyword().trim();
        if (keyword.length()>100) throw new MmsException("查询关键字过长");
        var wrapper=new LambdaQueryWrapper<SysTenant>();
        if (!keyword.isEmpty()) wrapper.and(w -> w.like(SysTenant::getTenantId,keyword).or().like(SysTenant::getName,keyword));
        wrapper.eq(query.getStatus()!=null,SysTenant::getStatus,query.getStatus());
        wrapper.orderByAsc(SysTenant::getTenantId);
        return TableDataInfo.build(tenants.selectPage(query.build(),wrapper)).toPageResult();
    }
}
