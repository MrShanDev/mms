package com.sxpcwlkj.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sxpcwlkj.common.utils.BeanCopyUtil;
import com.sxpcwlkj.system.entity.SysTenant;
import com.sxpcwlkj.system.entity.vo.SysTenantVo;
import com.sxpcwlkj.system.mapper.SysTenantMapper;
import com.sxpcwlkj.system.service.SysTenantService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author mmsAdmin
 */
@RequiredArgsConstructor
@Service("sysTenant")
@Slf4j
public class SysTenantServiceImpl implements SysTenantService {

    private final SysTenantMapper sysTenantMapper;

    @Override
    public List<SysTenantVo> selectOpenList() {
        List<SysTenant> sysTenants = sysTenantMapper.selectList(new QueryWrapper<SysTenant>().eq("status", 0));
        return BeanCopyUtil.convert(sysTenants, SysTenantVo.class);
    }

    @Override
    public SysTenant selectById(String tenantId) {
        return sysTenantMapper.selectById(tenantId);
    }
}
