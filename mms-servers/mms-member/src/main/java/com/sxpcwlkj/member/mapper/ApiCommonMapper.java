package com.sxpcwlkj.member.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
@Mapper
@Repository
public interface ApiCommonMapper {

    @Select(value = "${sql}")
    Map<String,String> selectMap(String sql);
    @Select(value = "${sql}")
    List<Map<String,String>> selectList(String sql);
}
