package com.sxpcwlkj.app.mapper;

import com.sxpcwlkj.app.entity.AppVersion;
import com.sxpcwlkj.app.entity.vo.AppVersionVo;
import com.sxpcwlkj.datasource.mapper.BaseMapperPlus;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * App版本发布Mapper接口
 *
 * @author Qoder
 * @date 2026-01-22
 */
@Mapper
@Repository
public interface AppVersionMapper extends BaseMapperPlus<AppVersion, AppVersionVo> {

}
