package com.sxpcwlkj.system.service.impl;

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
import com.sxpcwlkj.system.entity.SysNotice;
import com.sxpcwlkj.system.entity.bo.SysNoticeBo;
import com.sxpcwlkj.system.entity.vo.SysNoticeVo;
import com.sxpcwlkj.system.entity.export.SysNoticeExport;
import com.sxpcwlkj.system.mapper.SysNoticeMapper;
import com.sxpcwlkj.system.service.SysNoticeService;
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
 * 系统公告-接口实现
 *
* @author 西决
* @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
 */
@Slf4j
@Service("sys_notice")
@RequiredArgsConstructor
public class SysNoticeServiceImpl extends BaseServiceImpl<SysNotice, SysNoticeVo,SysNoticeBo> implements SysNoticeService {

   private final SysNoticeMapper baseMapper;

    @Override
    public BaseMapperPlus<SysNotice, SysNoticeVo> getBaseMapper() {
        return baseMapper;
    }


    @Override
    public Boolean insert(SysNoticeBo bo) {
        int row;
        bo.setId(null);
        SysNotice obj = MapstructUtil.convert(bo, SysNotice.class);
        row = this.getBaseMapper().insert(obj);
        return row > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteById(Serializable ids) {
        String[] array = DataUtil.getCatStr(ids.toString(), ",");
        return this.getBaseMapper().deleteByIds(new ArrayList<>(List.of(array)))>0;
    }

    @Override
    public Boolean updateById(SysNoticeBo bo) {
        int row;
        SysNotice obj = MapstructUtil.convert(bo, SysNotice.class);
        row = this.getBaseMapper().updateById(obj);
        return row > 0;
    }

    @Override
    public SysNoticeVo selectVoById(Serializable id) {
        return this.getBaseMapper().selectVoById(id);

    }
    @Override
    public TableDataInfo<SysNoticeVo> selectListVoPage(SysNoticeBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<SysNotice> lqw = buildQueryWrapper(bo);
        Page<SysNoticeVo> page = baseMapper.selectVoPage(pageQuery.build(),lqw);
        return TableDataInfo.build(page);
    }

    private LambdaQueryWrapper<SysNotice> buildQueryWrapper(SysNoticeBo query){
        if(query==null){
            query=new SysNoticeBo();
        }
        LambdaQueryWrapper<SysNotice> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(StringUtil.isNotEmpty(query.getId()), SysNotice::getId, query.getId());
        wrapper.eq(StringUtil.isNotEmpty(query.getTitle()), SysNotice::getTitle, query.getTitle());
        wrapper.eq(StringUtil.isNotEmpty(query.getType()), SysNotice::getType, query.getType());
        return wrapper;
    }

    @Override
    public Boolean imports(Set<SysNoticeExport> list) {
        return true;
    }
}
