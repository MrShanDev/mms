package com.sxpcwlkj.system.service.impl;

import com.sxpcwlkj.authority.LoginObject;
import com.sxpcwlkj.common.exception.MmsException;
import com.sxpcwlkj.system.controller.SysDictController;
import com.sxpcwlkj.system.entity.SysDept;
import com.sxpcwlkj.system.entity.SysUser;
import com.sxpcwlkj.system.entity.bo.SetUserRoleSuperBo;
import com.sxpcwlkj.system.entity.bo.SysDeptBo;
import com.sxpcwlkj.system.entity.bo.SysConfigBo;
import com.sxpcwlkj.system.entity.vo.SysDeptVo;
import com.sxpcwlkj.system.entity.vo.SysRoleVo;
import com.sxpcwlkj.system.mapper.*;
import com.sxpcwlkj.system.service.SysDictService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.context.support.StaticApplicationContext;
import io.github.linpeilie.Converter;
import com.sxpcwlkj.common.utils.SpringUtil;
import com.sxpcwlkj.system.entity.vo.SysConfigVo;
import org.mockito.MockedStatic;
import org.springframework.context.ApplicationContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class SystemAdminRegressionTest {
    @Test void logNumericFieldsUseNumericConstraints() throws Exception {
        for (String name : List.of("operId", "userId", "costTime")) {
            var field = com.sxpcwlkj.system.entity.bo.SysLogBo.class.getDeclaredField(name);
            assertNotNull(field.getAnnotation(jakarta.validation.constraints.NotNull.class));
            assertNull(field.getAnnotation(jakarta.validation.constraints.NotBlank.class));
        }
    }

    @Test void noticeImportCreatesDisabledDraftWithoutSuppliedId() {
        SysNoticeMapper mapper = mock(SysNoticeMapper.class);
        when(mapper.insert(any(com.sxpcwlkj.system.entity.SysNotice.class))).thenReturn(1);
        var row = new com.sxpcwlkj.system.entity.export.SysNoticeExport();
        row.setId("existing"); row.setTitle("测试"); row.setContent("内容"); row.setType(1);
        assertTrue(new SysNoticeServiceImpl(mapper).imports(Set.of(row)));
        verify(mapper).insert(argThat((com.sxpcwlkj.system.entity.SysNotice value) -> value.getId() == null && value.getStatus() == 0));
    }
    @Test void malformedNoticeBatchWritesNothing() {
        SysNoticeMapper mapper = mock(SysNoticeMapper.class);
        var good = new com.sxpcwlkj.system.entity.export.SysNoticeExport(); good.setTitle("标题"); good.setContent("内容"); good.setType(1);
        var bad = new com.sxpcwlkj.system.entity.export.SysNoticeExport();
        assertThrows(MmsException.class, () -> new SysNoticeServiceImpl(mapper).imports(Set.of(good, bad)));
        verifyNoInteractions(mapper);
    }
    @Test void departmentImportRejectsBatchCycleBeforeWriting() {
        SysDeptMapper mapper = mock(SysDeptMapper.class);
        var a = new com.sxpcwlkj.system.entity.export.SysDeptExport(); a.setDeptId("100"); a.setParentId("101"); a.setDeptName("A");
        var b = new com.sxpcwlkj.system.entity.export.SysDeptExport(); b.setDeptId("101"); b.setParentId("100"); b.setDeptName("B");
        assertThrows(MmsException.class, () -> new SysDeptServiceImpl(mapper, mock(SysUserMapper.class)).imports(Set.of(a,b)));
        verify(mapper, never()).insert(any(com.sxpcwlkj.system.entity.SysDept.class));
    }
    @BeforeAll static void initializeMappingContext() {
        StaticApplicationContext context = new StaticApplicationContext();
        Converter converter = mock(Converter.class);
        when(converter.convert(any(SysConfigBo.class), eq(SysConfigVo.class))).thenAnswer(invocation -> {
            SysConfigBo source = invocation.getArgument(0);
            SysConfigVo result = new SysConfigVo();
            result.setConfigKey(source.getConfigKey()); result.setConfigValue(source.getConfigValue());
            return result;
        });
        context.getBeanFactory().registerSingleton("converter", converter);
        new SpringUtil().setApplicationContext(context);
    }

    @Test void departmentMissingDetailReturnsNull() {
        SysDeptMapper mapper = mock(SysDeptMapper.class);
        assertNull(new SysDeptServiceImpl(mapper, mock(SysUserMapper.class)).selectVoById("missing"));
    }
    @Test void departmentAncestryCycleTerminates() {
        SysDeptMapper mapper = mock(SysDeptMapper.class);
        SysDeptVo vo = new SysDeptVo(); vo.setDeptId("1"); vo.setParentId("1");
        when(mapper.selectVoById("1")).thenReturn(vo);
        List<SysDeptVo> result = new ArrayList<>();
        new SysDeptServiceImpl(mapper, mock(SysUserMapper.class)).queryListSon("1", result);
        assertEquals(1, result.size());
    }
    @Test void departmentCannotMoveUnderDescendant() {
        SysDeptMapper mapper = mock(SysDeptMapper.class);
        SysDept child = new SysDept(); child.setDeptId("child"); child.setParentId("parent");
        when(mapper.selectById("child")).thenReturn(child);
        SysDeptBo bo = new SysDeptBo(); bo.setDeptId("parent"); bo.setParentId("child");
        assertThrows(MmsException.class, () -> new SysDeptServiceImpl(mapper, mock(SysUserMapper.class)).updateByIdBase(bo));
        verify(mapper, never()).updateById(any(SysDept.class));
    }
    @Test void dictionaryMissingDetailAndDeleteAreSafe() {
        SysDictMapper mapper = mock(SysDictMapper.class);
        SysDictDataMapper data = mock(SysDictDataMapper.class);
        SysDictServiceImpl service = new SysDictServiceImpl(mapper, data);
        assertNull(service.selectVoById(123L));
        assertFalse(service.deleteById(123L));
        verifyNoInteractions(data);
    }
    @Test void dictionaryControllerAcceptsBatchIds() {
        SysDictService service = mock(SysDictService.class);
        new SysDictController(service).delete("1,2");
        verify(service).deleteById(1L); verify(service).deleteById(2L);
    }
    @Test void invalidBatchDoesNotDeleteAnyRow() {
        SysDictService service = mock(SysDictService.class);
        assertThrows(NumberFormatException.class, () -> new SysDictController(service).delete("1,bad"));
        verifyNoInteractions(service);
    }
    @Test void configReadDoesNotInsertDefaults() {
        SysConfigMapper mapper = mock(SysConfigMapper.class);
        SysConfigServiceImpl service = new SysConfigServiceImpl(mapper, mock(ApplicationContext.class));
        SysConfigBo bo = new SysConfigBo(); bo.setConfigKey("sys_name"); bo.setConfigValue("MMS");
        assertEquals(1, service.selectByCodes(List.of(bo)).size());
        verify(mapper, never()).insert(any(com.sxpcwlkj.system.entity.SysConfig.class));
    }
    @Test void emptyImportMustNotReportSuccess() {
        assertThrows(MmsException.class, () -> new SysDeptServiceImpl(mock(SysDeptMapper.class), mock(SysUserMapper.class)).imports(Set.of()));
        assertThrows(MmsException.class, () -> new SysNoticeServiceImpl(mock(SysNoticeMapper.class)).imports(Set.of()));
        assertThrows(MmsException.class, () -> new SysLogServiceImpl(mock(SysLogMapper.class)).imports(Set.of()));
    }
    @Test void unknownRoleCannotDeleteExistingAssignment() {
        SysUserMapper users = mock(SysUserMapper.class);
        when(users.selectById("target")).thenReturn(new SysUser());
        SysUserRoleMapper assignments = mock(SysUserRoleMapper.class);
        SysRoleMapper roles = mock(SysRoleMapper.class);
        SysUserServiceImpl service = new SysUserServiceImpl(null, users, null, assignments, roles, null, null, null);
        SetUserRoleSuperBo bo = new SetUserRoleSuperBo(); bo.setUserId("target"); bo.setRoleCodes(List.of("missing"));
        try (MockedStatic<LoginObject> login = mockStatic(LoginObject.class)) {
            login.when(LoginObject::getLoginSuper).thenReturn(true);
            assertThrows(MmsException.class, () -> service.setUserRoleSuper(bo));
        }
        verifyNoInteractions(assignments);
    }
    @Test void lowerLevelUserCannotModifyHigherAccount() {
        SysUserMapper users = mock(SysUserMapper.class);
        when(users.selectById("target")).thenReturn(new SysUser());
        SysRoleMapper roles = mock(SysRoleMapper.class);
        SysRoleVo operator = new SysRoleVo(); operator.setCode("operator"); operator.setLevel(10);
        SysRoleVo admin = new SysRoleVo(); admin.setCode("admin"); admin.setLevel(1);
        when(roles.selectByUserIdList("actor")).thenReturn(List.of(operator));
        when(roles.selectByUserIdList("target")).thenReturn(List.of(admin));
        SysUserRoleMapper assignments = mock(SysUserRoleMapper.class);
        SysUserServiceImpl service = new SysUserServiceImpl(null, users, null, assignments, roles, null, null, null);
        SetUserRoleSuperBo bo = new SetUserRoleSuperBo(); bo.setUserId("target"); bo.setRoleCodes(List.of("operator"));
        try (MockedStatic<LoginObject> login = mockStatic(LoginObject.class)) {
            login.when(LoginObject::getLoginSuper).thenReturn(false);
            login.when(LoginObject::getLoginId).thenReturn("actor");
            assertThrows(MmsException.class, () -> service.setUserRoleSuper(bo));
        }
        verifyNoInteractions(assignments);
    }
}
