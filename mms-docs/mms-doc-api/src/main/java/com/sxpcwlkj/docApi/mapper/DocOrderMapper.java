package com.sxpcwlkj.docApi.mapper;

import com.sxpcwlkj.datasource.mapper.BaseMapperPlus;
import com.sxpcwlkj.docApi.entity.DocOrder;
import com.sxpcwlkj.docApi.entity.vo.DocOrderVo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
* 文档订单-Mapper
*
* @author 西决
* @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
*/
@Mapper
@Repository
public interface DocOrderMapper extends BaseMapperPlus<DocOrder, DocOrderVo> {

}
