package com.sxpcwlkj.framework.mapper;

import com.sxpcwlkj.datasource.mapper.BaseMapperPlus;
import com.sxpcwlkj.framework.entity.SysSign;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * 系统加签
 * @author sxpcwlkj
 */
@Mapper
@Repository
public interface SysSignMapper extends BaseMapperPlus<SysSign,SysSign> {

    @Insert("insert into sys_sign(id,user_id,app_id,secret_key,public_key,private_key,time_out,created_time,created_by) values(#{id},#{userId},#{appId},#{secretKey},#{publicKey},#{privateKey},#{timeOut},#{createdTime},#{createdBy})")
    int initSign(SysSign sysSign);
}
