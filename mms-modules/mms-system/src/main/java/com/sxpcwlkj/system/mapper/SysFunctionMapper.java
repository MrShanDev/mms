package com.sxpcwlkj.system.mapper;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.sxpcwlkj.datasource.mapper.BaseMapperPlus;
import com.sxpcwlkj.system.entity.SysFunction;
import com.sxpcwlkj.system.entity.vo.SysFunctionVo;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Description 系统资源
 * @Author sxpcwlkj
 * @Date 2024/1/2 17:37
 * @Version v1.0.0
 */
@Mapper
@Repository
public interface SysFunctionMapper extends BaseMapperPlus<SysFunction, SysFunctionVo> {
    @InterceptorIgnore(tenantLine = "true")
    @Select("SELECT f.* FROM `sys_role_function` rf RIGHT JOIN `sys_function` f  ON rf.`function_id`=f.`id` WHERE rf.`role_id`=#{id} and rf.`status`=1 ORDER BY f.`parent_id` ASC ,  f.`sort` DESC")
    List<SysFunction> selectByRoleForIds(@Param("id") String id);

    /** 插件卸载：按 remark + 租户列举功能 id（忽略租户行插件，便于跨租户清理）。 */
    @InterceptorIgnore(tenantLine = "true")
    @Select("SELECT id FROM `sys_function` WHERE remark = #{remark} AND tenant_id = #{tenantId}")
    List<String> selectIdsByRemarkAndTenant(@Param("remark") String remark, @Param("tenantId") String tenantId);

    /** 插件卸载：删除本租户下归属某插件的菜单行。 */
    @InterceptorIgnore(tenantLine = "true")
    @Delete("DELETE FROM `sys_function` WHERE remark = #{remark} AND tenant_id = #{tenantId}")
    int deleteByRemarkAndTenant(@Param("remark") String remark, @Param("tenantId") String tenantId);
}
