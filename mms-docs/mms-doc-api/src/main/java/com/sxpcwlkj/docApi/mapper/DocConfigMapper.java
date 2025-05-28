package com.sxpcwlkj.docApi.mapper;

import com.sxpcwlkj.datasource.mapper.BaseMapperPlus;
import com.sxpcwlkj.docApi.entity.DocConfig;
import com.sxpcwlkj.docApi.entity.vo.DocConfigVo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
* 文档配置-Mapper
*
* @author 西决
* @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
*/
@Mapper
@Repository
public interface DocConfigMapper extends BaseMapperPlus<DocConfig, DocConfigVo> {

}
