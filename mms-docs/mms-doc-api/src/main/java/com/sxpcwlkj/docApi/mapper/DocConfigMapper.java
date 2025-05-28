package com.sxpcwlkj.docApi.mapper;

import com.sxpcwlkj.datasource.mapper.BaseMapperPlus;
import com.sxpcwlkj.docApi.entity.DocConfig;
import com.sxpcwlkj.docApi.entity.vo.DocConfigVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
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

    @Select("select COALESCE(c.value, 0) AS value from doc_config c left join doc_product p on c.`key`=p.code where p.prod_id=#{prodId}")
    String selectByKey(@Param("prodId") String prodId);
}
