package com.sxpcwlkj.docApi.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sxpcwlkj.common.utils.DataUtil;
import com.sxpcwlkj.common.utils.MapstructUtil;
import com.sxpcwlkj.datasource.entity.page.PageQuery;
import com.sxpcwlkj.datasource.entity.page.TableDataInfo;
import com.sxpcwlkj.datasource.mapper.BaseMapperPlus;
import com.sxpcwlkj.docApi.entity.DocUser;
import com.sxpcwlkj.docApi.entity.bo.DocUserBo;

import com.sxpcwlkj.docApi.entity.vo.DocUserVo;
import com.sxpcwlkj.docApi.enums.DefStaticEnum;
import com.sxpcwlkj.docApi.mapper.DocUserMapper;
import com.sxpcwlkj.docApi.service.DocUserService;
import com.sxpcwlkj.docApi.utils.NicknameGenerator;
import com.sxpcwlkj.framework.sercice.impl.BaseServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * 文档用户-接口实现
 *
* @author 西决
* @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
 */
@Slf4j
@Transactional
@Service("doc_user")
@RequiredArgsConstructor
public class DocUserServiceImpl extends BaseServiceImpl<DocUser, DocUserVo,DocUserBo> implements DocUserService {

   private final DocUserMapper baseMapper;

    @Override
    public BaseMapperPlus<DocUser, DocUserVo> getBaseMapper() {
        return baseMapper;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean insert(DocUserBo bo) {
        try {
            int row;
            bo.setUid(null);
            DocUser obj = MapstructUtil.convert(bo, DocUser.class);
            assert obj != null;
            row = this.getBaseMapper().insert(obj);
            bo.setUid(obj.getUid());
            return row > 0;
        } catch (Exception e) {
            log.error("文档用户,insert 操作失败", e);
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
            log.error("文档用户,deleteById 操作失败", e);
            throw e;
        }
    }

    @Override
    public Boolean updateById(DocUserBo bo) {
        try {
            int row;
            DocUser obj = MapstructUtil.convert(bo, DocUser.class);
            row = this.getBaseMapper().updateById(obj);
            return row > 0;
        } catch (Exception e) {
            log.error("文档用户,updateById 操作失败", e);
            throw e;
        }
    }

    @Override
    public DocUserVo selectVoById(Serializable id) {
        return this.getBaseMapper().selectVoById(id);

    }
    @Override
    public TableDataInfo<DocUserVo> selectListVoPage(DocUserBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<DocUser> lqw = buildQueryWrapper(bo);
        Page<DocUserVo> page = baseMapper.selectVoPage(pageQuery.build(),lqw);
        return TableDataInfo.build(page);
    }

    private LambdaQueryWrapper<DocUser> buildQueryWrapper(DocUserBo query){
        if(query==null){
            query=new DocUserBo();
        }
        LambdaQueryWrapper<DocUser> wrapper = Wrappers.lambdaQuery();
        return wrapper;
    }

    @Override
    public DocUser bindingOpenId(String openId) {
        DocUser docUser= baseMapper.selectOne(new LambdaQueryWrapper<DocUser>().eq(DocUser::getOpenId,openId));
        if(docUser!=null){
            return docUser;
        }else {
            docUser= new DocUser();
            docUser.setNickname(NicknameGenerator.generateRandomNickname());
            docUser.setType("usr");
            docUser.setAvatar(DefStaticEnum.MEMBER_DEF_HEADER_IMG.getValue());
            docUser.setCtime(new Date());
            docUser.setMtime(new Date());
            docUser.setOpenId(openId);
            baseMapper.insert(docUser);
        }
        return docUser;
    }
}
