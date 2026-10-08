package com.sxpcwlkj.system.mapper;
import org.apache.ibatis.annotations.*;
import java.util.List;

@Mapper
public interface SysNoticeReadMapper {
    @Select("SELECT notice_id FROM sys_notice_read WHERE tenant_id=#{tenantId} AND user_id=#{userId}")
    List<String> readIds(@Param("tenantId") String tenantId, @Param("userId") String userId);
    @Insert("INSERT INTO sys_notice_read(tenant_id,user_id,notice_id) VALUES(#{tenantId},#{userId},#{noticeId}) ON DUPLICATE KEY UPDATE read_time=read_time")
    int markRead(@Param("tenantId") String tenantId,@Param("userId") String userId,@Param("noticeId") String noticeId);
}
