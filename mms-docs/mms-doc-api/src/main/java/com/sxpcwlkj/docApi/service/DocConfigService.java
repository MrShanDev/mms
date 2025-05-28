package com.sxpcwlkj.docApi.service;

import com.sxpcwlkj.docApi.entity.DocConfig;
import com.sxpcwlkj.docApi.entity.bo.DocConfigBo;
import com.sxpcwlkj.docApi.entity.vo.DocConfigVo;
import com.sxpcwlkj.framework.sercice.BaseService;

import java.util.Set;

/**
 * 文档配置-接口
 *
 * @author 西决
 * @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
 * @describe  支持自定义扩展,已继承接口：{insert、deleteById、updateById、selectById、getByEntityListPage}（更多查看BaseService接口）
 */
public interface DocConfigService extends BaseService<DocConfig, DocConfigVo, DocConfigBo> {

}
