package com.sxpcwlkj.common.tenant;

/** Checks application mode and tenant validity for an authenticated request. */
public interface TenantRequestPolicy {
    void check();
    void checkPermission(String permission);
}
