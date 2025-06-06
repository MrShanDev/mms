package com.sxpcwlkj.docAdmin.service.impl;

import cn.hutool.core.util.ArrayUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sxpcwlkj.common.enums.SystemCommonEnum;
import com.sxpcwlkj.common.utils.MapstructUtil;
import com.sxpcwlkj.common.utils.DataUtil;
import com.sxpcwlkj.common.utils.StringUtil;
import com.sxpcwlkj.datasource.entity.page.PageQuery;
import com.sxpcwlkj.datasource.entity.page.TableDataInfo;
import com.sxpcwlkj.datasource.mapper.BaseMapperPlus;
import com.sxpcwlkj.framework.sercice.impl.BaseServiceImpl;
import com.sxpcwlkj.docAdmin.entity.DocConfig;
import com.sxpcwlkj.docAdmin.entity.bo.DocConfigBo;
import com.sxpcwlkj.docAdmin.entity.vo.DocConfigVo;
import com.sxpcwlkj.docAdmin.entity.export.DocConfigExport;
import com.sxpcwlkj.docAdmin.mapper.DocConfigMapper;
import com.sxpcwlkj.docAdmin.service.DocConfigService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.io.Serializable;
import java.util.Set;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
/**
 * 文档配置-接口实现
 *
* @author 西决
* @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
 */
@Slf4j
@Transactional
@Service("doc_config")
@RequiredArgsConstructor
public class DocConfigServiceImpl extends BaseServiceImpl<DocConfig, DocConfigVo,DocConfigBo> implements DocConfigService {

   private final DocConfigMapper baseMapper;

    @Override
    public BaseMapperPlus<DocConfig, DocConfigVo> getBaseMapper() {
        return baseMapper;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean insert(DocConfigBo bo) {
        try {
            int row;
            bo.setId(null);
            DocConfig obj = MapstructUtil.convert(bo, DocConfig.class);
            assert obj != null;
            row = this.getBaseMapper().insert(obj);
            bo.setId(obj.getId());
            return row > 0;
        } catch (Exception e) {
            log.error("文档配置,insert 操作失败", e);
            throw e;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteById(Serializable ids) {
        try {
            String[] array = DataUtil.getCatStr(ids.toString(), ",");
            return this.getBaseMapper().deleteByIds(new ArrayList<>(List.of(array)))>0;
        } catch (Exception e) {
            log.error("文档配置,deleteById 操作失败", e);
            throw e;
        }
    }

    @Override
    public Boolean updateById(DocConfigBo bo) {
        try {
            int row;
            DocConfig obj = MapstructUtil.convert(bo, DocConfig.class);
            row = this.getBaseMapper().updateById(obj);
            return row > 0;
        } catch (Exception e) {
            log.error("文档配置,updateById 操作失败", e);
            throw e;
        }
    }

    @Override
    public DocConfigVo selectVoById(Serializable id) {
        return this.getBaseMapper().selectVoById(id);

    }
    @Override
    public TableDataInfo<DocConfigVo> selectListVoPage(DocConfigBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<DocConfig> lqw = buildQueryWrapper(bo);
        Page<DocConfigVo> page = baseMapper.selectVoPage(pageQuery.build(),lqw);
        return TableDataInfo.build(page);
    }

    private LambdaQueryWrapper<DocConfig> buildQueryWrapper(DocConfigBo query){
        if(query==null){
            query=new DocConfigBo();
        }
        LambdaQueryWrapper<DocConfig> wrapper = Wrappers.lambdaQuery();
        return wrapper;
    }

    @Override
    public Boolean imports(Set<DocConfigExport> list) {
        return true;
    }
}
