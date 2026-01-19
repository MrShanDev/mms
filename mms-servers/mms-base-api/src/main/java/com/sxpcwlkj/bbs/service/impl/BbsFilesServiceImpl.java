package com.sxpcwlkj.bbs.service.impl;

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
import com.sxpcwlkj.framework.service.impl.BaseServiceImpl;
import com.sxpcwlkj.bbs.entity.BbsFiles;
import com.sxpcwlkj.bbs.entity.bo.BbsFilesBo;
import com.sxpcwlkj.bbs.entity.vo.BbsFilesVo;
import com.sxpcwlkj.bbs.entity.export.BbsFilesExport;
import com.sxpcwlkj.bbs.mapper.BbsFilesMapper;
import com.sxpcwlkj.bbs.service.BbsFilesService;
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
 * 话题附件-接口实现
 *
* @author mmsAdmin
* @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
 */
@Slf4j
@Transactional
@Service("bbs_files")
@RequiredArgsConstructor
public class BbsFilesServiceImpl extends BaseServiceImpl<BbsFiles, BbsFilesVo,BbsFilesBo> implements BbsFilesService {

   private final BbsFilesMapper baseMapper;

    @Override
    public BaseMapperPlus<BbsFiles, BbsFilesVo> getBaseMapper() {
        return baseMapper;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean insert(BbsFilesBo bo) {
        try {
            int row;
            bo.setId(null);
            BbsFiles obj = MapstructUtil.convert(bo, BbsFiles.class);
            assert obj != null;
            row = this.getBaseMapper().insert(obj);
            bo.setId(obj.getId());
            return row > 0;
        } catch (Exception e) {
            log.error("话题附件,insert 操作失败", e);
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
            log.error("话题附件,deleteById 操作失败", e);
            throw e;
        }
    }

    @Override
    public Boolean updateById(BbsFilesBo bo) {
        try {
            int row;
            BbsFiles obj = MapstructUtil.convert(bo, BbsFiles.class);
            row = this.getBaseMapper().updateById(obj);
            return row > 0;
        } catch (Exception e) {
            log.error("话题附件,updateById 操作失败", e);
            throw e;
        }
    }

    @Override
    public BbsFilesVo selectVoById(Serializable id) {
        return this.getBaseMapper().selectVoById(id);

    }
    @Override
    public TableDataInfo<BbsFilesVo> selectListVoPage(BbsFilesBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<BbsFiles> lqw = buildQueryWrapper(bo);
        Page<BbsFilesVo> page = baseMapper.selectVoPage(pageQuery.build(),lqw);
        return TableDataInfo.build(page);
    }

    private LambdaQueryWrapper<BbsFiles> buildQueryWrapper(BbsFilesBo query){
        if(query==null){
            query=new BbsFilesBo();
        }
        LambdaQueryWrapper<BbsFiles> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(StringUtil.isNotEmpty(query.getBbsId()), BbsFiles::getBbsId, query.getBbsId());
        return wrapper;
    }

    @Override
    public Boolean imports(Set<BbsFilesExport> list) {
        return true;
    }
}
