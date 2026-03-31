package com.sxpcwlkj.system.service.impl;

import com.sxpcwlkj.system.entity.SysTenantPackage;
import com.sxpcwlkj.system.mapper.SysTenantPackageMapper;
import com.sxpcwlkj.system.service.SysTenantPackageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class SysTenantPackageServiceImpl implements SysTenantPackageService {

    private final SysTenantPackageMapper sysTenantPackageMapper;

    @Override
    public Optional<SysTenantPackage> findById(String packageId) {
        if (packageId == null || packageId.isBlank()) {
            return Optional.empty();
        }
        return Optional.ofNullable(sysTenantPackageMapper.selectById(packageId.trim()));
    }
}
