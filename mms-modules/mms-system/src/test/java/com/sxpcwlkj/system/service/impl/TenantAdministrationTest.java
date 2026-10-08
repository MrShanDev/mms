package com.sxpcwlkj.system.service.impl;
import com.sxpcwlkj.common.properties.TenantProperties;
import com.sxpcwlkj.system.entity.*;
import com.sxpcwlkj.system.entity.bo.*;
import com.sxpcwlkj.system.mapper.*;
import org.junit.jupiter.api.Test;
import java.util.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
class TenantAdministrationTest {
    final SysTenantMapper tenants=mock(SysTenantMapper.class);
    final SysTenantPackageMapper packages=mock(SysTenantPackageMapper.class);
    final SysFunctionMapper functions=mock(SysFunctionMapper.class);
    final TenantAdministrationMapper admin=mock(TenantAdministrationMapper.class);
    final TenantProperties properties=new TenantProperties();
    final TenantAdministrationService service=new TenantAdministrationService(tenants,packages,functions,admin,properties,null);
    SysTenant tenant(){var t=new SysTenant();t.setTenantId("acme");t.setStatus(1);when(tenants.selectById("acme")).thenReturn(t);return t;}
    @Test void defaultsToMultiTenantAndReadsPersistedChoice(){assertTrue(service.enabled());when(admin.mode()).thenReturn("false");assertFalse(service.enabled());when(admin.mode()).thenReturn("true");assertTrue(service.enabled());}
    @Test void defaultApplicationAlwaysRemainsAccessible(){when(admin.mode()).thenReturn("false");assertDoesNotThrow(()->service.checkTenant("000000"));verifyNoInteractions(tenants);}
    @Test void singleApplicationRejectsBusinessTenant(){tenant();when(admin.mode()).thenReturn("false");assertThrows(RuntimeException.class,()->service.checkTenant("acme"));}
    @Test void stoppedAndExpiredTenantAreRejected(){var t=tenant();t.setStatus(0);assertThrows(RuntimeException.class,()->service.checkTenant("acme"));t.setStatus(1);t.setExpireTime(new Date(0));assertThrows(RuntimeException.class,()->service.checkTenant("acme"));t.setExpireTime(new Date(System.currentTimeMillis()+60000));assertDoesNotThrow(()->service.checkTenant("acme"));}
    @Test void missingAndDisabledPackageAreRejected(){var t=tenant();t.setPackageId("p");assertThrows(RuntimeException.class,()->service.checkTenant("acme"));var p=new SysTenantPackage();p.setStatus(0);when(packages.selectById("p")).thenReturn(p);assertThrows(RuntimeException.class,()->service.checkTenant("acme"));p.setStatus(1);assertDoesNotThrow(()->service.checkTenant("acme"));}
    @Test void packageCannotGrantPlatformResources(){var f=new SysFunction();f.setId("1");f.setComponent("system/tenant/index");when(functions.selectForSessionTenant("000000")).thenReturn(List.of(f));var b=new TenantPackageSaveBo();b.setName("bad");b.setStatus(1);b.setFunctionIds(List.of("1"));assertThrows(RuntimeException.class,()->service.savePackage(b));verifyNoInteractions(packages);}
    @Test void platformTenantCannotBeEdited(){var b=new TenantSaveBo();b.setTenantId("000000");assertThrows(RuntimeException.class,()->service.saveTenant(b,false));verifyNoInteractions(tenants);}
    @Test void quotaCannotBeLoweredBelowExistingAccounts(){var t=tenant();t.setAccountCount(5);var p=new SysTenantPackage();p.setStatus(1);when(packages.selectById("p")).thenReturn(p);when(admin.userCount("acme")).thenReturn(4L);var b=new TenantSaveBo();b.setTenantId("acme");b.setPackageId("p");b.setAccountCount(3);assertThrows(RuntimeException.class,()->service.saveTenant(b,false));verify(tenants,never()).updateById(any(SysTenant.class));}
}
