package com.sxpcwlkj.store.service.impl;

import com.alibaba.excel.util.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sxpcwlkj.member.entity.vo.StoreMemberVo;
import com.sxpcwlkj.member.service.StoreMemberService;
import com.sxpcwlkj.common.utils.DataUtil;
import com.sxpcwlkj.common.utils.MapstructUtil;
import com.sxpcwlkj.datasource.entity.page.PageQuery;
import com.sxpcwlkj.datasource.entity.page.TableDataInfo;
import com.sxpcwlkj.datasource.mapper.BaseMapperPlus;
import com.sxpcwlkj.framework.service.impl.BaseServiceImpl;
import com.sxpcwlkj.store.entity.StoreSearchRes;
import com.sxpcwlkj.store.entity.bo.StoreSearchResBo;
import com.sxpcwlkj.store.entity.export.StoreSearchResExport;
import com.sxpcwlkj.store.entity.vo.StoreSearchResVo;
import com.sxpcwlkj.store.mapper.StoreSearchResMapper;
import com.sxpcwlkj.store.service.StoreSearchResService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
/**
 * 搜索记录-接口实现
 *
* @author mmsAdmin
* @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
 */
@Slf4j
@Transactional
@Service("store_search_res")
@RequiredArgsConstructor
public class StoreSearchResServiceImpl extends BaseServiceImpl<StoreSearchRes, StoreSearchResVo, StoreSearchResBo> implements StoreSearchResService {

   private final StoreSearchResMapper baseMapper;
   private final StoreMemberService storeMemberService;

    @Override
    public BaseMapperPlus<StoreSearchRes, StoreSearchResVo> getBaseMapper() {
        return baseMapper;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean insert(StoreSearchResBo bo) {
        try {
            int row;
            bo.setId(null);
            StoreSearchRes obj = MapstructUtil.convert(bo, StoreSearchRes.class);
            assert obj != null;
            row = this.getBaseMapper().insert(obj);
            bo.setId(obj.getId());
            return row > 0;
        } catch (Exception e) {
            log.error("搜索记录,insert 操作失败", e);
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
            log.error("搜索记录,deleteById 操作失败", e);
            throw e;
        }
    }

    @Override
    public Boolean updateById(StoreSearchResBo bo) {
        try {
            int row;
            StoreSearchRes obj = MapstructUtil.convert(bo, StoreSearchRes.class);
            row = this.getBaseMapper().updateById(obj);
            return row > 0;
        } catch (Exception e) {
            log.error("搜索记录,updateById 操作失败", e);
            throw e;
        }
    }

    @Override
    public StoreSearchResVo selectVoById(Serializable id) {
        StoreSearchResVo vo =this.getBaseMapper().selectVoById(id);
        if(vo!=null){
            if(vo.getMemberId()!=null) {
                StoreMemberVo memberVo = storeMemberService.selectVoById(vo.getMemberId());
                if (memberVo != null) {
                    StringBuilder sb = new StringBuilder();
                    if (StringUtils.isNotBlank(memberVo.getNickname())) {
                        sb.append(memberVo.getNickname());
                    }
                    if (StringUtils.isNotBlank(memberVo.getPhone())) {
                        if (!sb.isEmpty()) {
                            sb.append("|");
                        }
                        sb.append(memberVo.getPhone());
                    }
                    vo.setMemberName(sb.toString());
                }
            }else {
                vo.setMemberName("游客");
            }
        }
        return vo;

    }
    @Override
    public TableDataInfo<StoreSearchResVo> selectListVoPage(StoreSearchResBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<StoreSearchRes> lqw = buildQueryWrapper(bo);
        Page<StoreSearchResVo> page = baseMapper.selectVoPage(pageQuery.build(),lqw);
        for (StoreSearchResVo vo:page.getRecords()){
            if(vo.getMemberId()!=null) {
                StoreMemberVo memberVo = storeMemberService.selectVoById(vo.getMemberId());
                if (memberVo != null) {
                    StringBuilder sb = new StringBuilder();
                    if (StringUtils.isNotBlank(memberVo.getNickname())) {
                        sb.append(memberVo.getNickname());
                    }
                    if (StringUtils.isNotBlank(memberVo.getPhone())) {
                        if (!sb.isEmpty()) {
                            sb.append("|");
                        }
                        sb.append(memberVo.getPhone());
                    }
                    vo.setMemberName(sb.toString());
                }
            }else {
                vo.setMemberName("游客");
            }
        }
        return TableDataInfo.build(page);
    }

    private LambdaQueryWrapper<StoreSearchRes> buildQueryWrapper(StoreSearchResBo query){
        if(query==null){
            query=new StoreSearchResBo();
        }
        LambdaQueryWrapper<StoreSearchRes> wrapper = Wrappers.lambdaQuery();
        wrapper.orderByDesc(StoreSearchRes::getCreatedTime);
        return wrapper;
    }

    @Override
    public Boolean imports(Set<StoreSearchResExport> list) {
        return true;
    }

    @Override
    public void addSearchRes(String keywords, String userId) {
        StoreSearchRes searchRes = new StoreSearchRes();
        if(StringUtils.isNotBlank(userId)){
            searchRes.setMemberId(Long.valueOf(userId));
        }
        searchRes.setSearchKeyword(keywords);
        searchRes.setSearchNum(1);
        baseMapper.insert(searchRes);
    }
}
