package com.sxpcwlkj.docAdmin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sxpcwlkj.common.utils.DataUtil;
import com.sxpcwlkj.common.utils.MapstructUtil;
import com.sxpcwlkj.datasource.entity.page.PageQuery;
import com.sxpcwlkj.datasource.entity.page.TableDataInfo;
import com.sxpcwlkj.datasource.mapper.BaseMapperPlus;
import com.sxpcwlkj.docAdmin.entity.DocAuthorizeUser;
import com.sxpcwlkj.docAdmin.entity.bo.DocAuthorizeUserBo;
import com.sxpcwlkj.docAdmin.entity.export.DocAuthorizeUserExport;
import com.sxpcwlkj.docAdmin.entity.vo.DocAuthorizeUserVo;
import com.sxpcwlkj.docAdmin.mapper.DocAuthorizeUserMapper;
import com.sxpcwlkj.docAdmin.service.DocAuthorizeUserService;
import com.sxpcwlkj.framework.sercice.impl.BaseServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
/**
 * 文档授权用户-接口实现
 *
* @author 西决
* @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
 */
@Slf4j
@Transactional
@Service("doc_authorize_user")
@RequiredArgsConstructor
public class DocAuthorizeUserServiceImpl extends BaseServiceImpl<DocAuthorizeUser, DocAuthorizeUserVo,DocAuthorizeUserBo> implements DocAuthorizeUserService {

   private final DocAuthorizeUserMapper baseMapper;

    @Override
    public BaseMapperPlus<DocAuthorizeUser, DocAuthorizeUserVo> getBaseMapper() {
        return baseMapper;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean insert(DocAuthorizeUserBo bo) {
        try {
            int row;
            bo.setId(null);
            DocAuthorizeUser obj = MapstructUtil.convert(bo, DocAuthorizeUser.class);
            assert obj != null;
            row = this.getBaseMapper().insert(obj);
            bo.setId(obj.getId());
            return row > 0;
        } catch (Exception e) {
            log.error("文档授权用户,insert 操作失败", e);
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
            log.error("文档授权用户,deleteById 操作失败", e);
            throw e;
        }
    }

    @Override
    public Boolean updateById(DocAuthorizeUserBo bo) {
        try {
            int row;
            DocAuthorizeUser obj = MapstructUtil.convert(bo, DocAuthorizeUser.class);
            row = this.getBaseMapper().updateById(obj);
            return row > 0;
        } catch (Exception e) {
            log.error("文档授权用户,updateById 操作失败", e);
            throw e;
        }
    }

    @Override
    public DocAuthorizeUserVo selectVoById(Serializable id) {
        return this.getBaseMapper().selectVoById(id);

    }
    @Override
    public TableDataInfo<DocAuthorizeUserVo> selectListVoPage(DocAuthorizeUserBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<DocAuthorizeUser> lqw = buildQueryWrapper(bo);
        Page<DocAuthorizeUserVo> page = baseMapper.selectVoPage(pageQuery.build(),lqw);
        return TableDataInfo.build(page);
    }

    private LambdaQueryWrapper<DocAuthorizeUser> buildQueryWrapper(DocAuthorizeUserBo query){
        if(query==null){
            query=new DocAuthorizeUserBo();
        }
        LambdaQueryWrapper<DocAuthorizeUser> wrapper = Wrappers.lambdaQuery();
        return wrapper;
    }

    @Override
    public Boolean imports(Set<DocAuthorizeUserExport> list) {
        return true;
    }
}
