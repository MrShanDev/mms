package com.sxpcwlkj.member.service.impl;

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
import com.sxpcwlkj.member.entity.StoreMemberAuthentication;
import com.sxpcwlkj.member.entity.bo.StoreMemberAuthenticationBo;
import com.sxpcwlkj.member.entity.vo.StoreMemberAuthenticationVo;
import com.sxpcwlkj.member.entity.export.StoreMemberAuthenticationExport;
import com.sxpcwlkj.member.mapper.StoreMemberAuthenticationMapper;
import com.sxpcwlkj.member.service.StoreMemberAuthenticationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.io.Serializable;
import java.util.Set;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import java.util.Map;
import java.util.HashMap;
/**
 * 会员认证-接口实现
 *
 * @author mmsAdmin
 * @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
 */
@Slf4j
@Transactional
@Service("store_member_authentication")
@RequiredArgsConstructor
public class StoreMemberAuthenticationServiceImpl extends BaseServiceImpl<StoreMemberAuthentication, StoreMemberAuthenticationVo,StoreMemberAuthenticationBo> implements StoreMemberAuthenticationService {

    private final StoreMemberAuthenticationMapper baseMapper;

    @Override
    public BaseMapperPlus<StoreMemberAuthentication, StoreMemberAuthenticationVo> getBaseMapper() {
        return baseMapper;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean insert(StoreMemberAuthenticationBo bo) {
        try {
            int row;
            bo.setId(null);
            StoreMemberAuthentication obj = MapstructUtil.convert(bo, StoreMemberAuthentication.class);
            assert obj != null;
            row = this.getBaseMapper().insert(obj);
            bo.setId(obj.getId());
            return row > 0;
        } catch (Exception e) {
            log.error("会员认证,insert 操作失败", e);
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
            log.error("会员认证,deleteById 操作失败", e);
            throw e;
        }
    }

    @Override
    public Boolean updateById(StoreMemberAuthenticationBo bo) {
        try {
            int row;
            StoreMemberAuthentication obj = MapstructUtil.convert(bo, StoreMemberAuthentication.class);
            row = this.getBaseMapper().updateById(obj);
            return row > 0;
        } catch (Exception e) {
            log.error("会员认证,updateById 操作失败", e);
            throw e;
        }
    }

    @Override
    public StoreMemberAuthenticationVo selectVoById(Serializable id) {
        return this.getBaseMapper().selectVoById(id);

    }
    @Override
    public TableDataInfo<StoreMemberAuthenticationVo> selectListVoPage(StoreMemberAuthenticationBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<StoreMemberAuthentication> lqw = buildQueryWrapper(bo);
        Page<StoreMemberAuthenticationVo> page = baseMapper.selectVoPage(pageQuery.build(),lqw);
        return TableDataInfo.build(page);
    }

    private LambdaQueryWrapper<StoreMemberAuthentication> buildQueryWrapper(StoreMemberAuthenticationBo query){
        if(query==null){
            query=new StoreMemberAuthenticationBo();
        }
        LambdaQueryWrapper<StoreMemberAuthentication> wrapper = Wrappers.lambdaQuery();
        return wrapper;
    }

    @Override
    public Boolean imports(Set<StoreMemberAuthenticationExport> list) {
        return true;
    }
}
