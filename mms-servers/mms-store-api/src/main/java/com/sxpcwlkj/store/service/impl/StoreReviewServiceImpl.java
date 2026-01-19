package com.sxpcwlkj.store.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sxpcwlkj.common.enums.SystemCommonEnum;
import com.sxpcwlkj.common.exception.MmsException;
import com.sxpcwlkj.common.utils.DataUtil;
import com.sxpcwlkj.common.utils.MapstructUtil;
import com.sxpcwlkj.common.utils.StringUtil;
import com.sxpcwlkj.datasource.entity.page.PageQuery;
import com.sxpcwlkj.datasource.entity.page.TableDataInfo;
import com.sxpcwlkj.datasource.mapper.BaseMapperPlus;
import com.sxpcwlkj.framework.service.impl.BaseServiceImpl;
import com.sxpcwlkj.store.entity.StoreReview;
import com.sxpcwlkj.store.entity.bo.StoreReviewBo;
import com.sxpcwlkj.store.entity.export.StoreReviewExport;
import com.sxpcwlkj.member.entity.vo.StoreMemberVo;
import com.sxpcwlkj.store.entity.vo.StoreOrderVo;
import com.sxpcwlkj.store.entity.vo.StoreProductSpuVo;
import com.sxpcwlkj.store.entity.vo.StoreReviewVo;
import com.sxpcwlkj.store.mapper.StoreReviewMapper;
import com.sxpcwlkj.member.service.StoreMemberService;
import com.sxpcwlkj.store.service.StoreOrderService;
import com.sxpcwlkj.store.service.StoreProductSpuService;
import com.sxpcwlkj.store.service.StoreReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Set;

/**
 * 商品评论表-接口实现
 *
* @author mmsAdmin
* @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
 */
@Slf4j
@Transactional
@Service("store_review")
@RequiredArgsConstructor
public class StoreReviewServiceImpl extends BaseServiceImpl<StoreReview, StoreReviewVo, StoreReviewBo> implements StoreReviewService {

   private final StoreReviewMapper baseMapper;
   private final StoreOrderService storeOrderService;
   private final StoreMemberService storeMemberService;
   private final StoreProductSpuService storeProductSpuService;

    @Override
    public BaseMapperPlus<StoreReview, StoreReviewVo> getBaseMapper() {
        return baseMapper;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean insert(StoreReviewBo bo) {
        try {
            int row;
            bo.setId(null);
            StoreReview obj = MapstructUtil.convert(bo, StoreReview.class);
            assert obj != null;
            row = this.getBaseMapper().insert(obj);
            bo.setId(obj.getId());
            return row > 0;
        } catch (Exception e) {
            log.error("商品评论表,insert 操作失败", e);
            throw e;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteById(Serializable ids) {
        try {
            String[] array = DataUtil.getCatStr(ids.toString(), ",");
            for (String id : array) {
                this.getBaseMapper().update(null,Wrappers.<StoreReview>lambdaUpdate()
                    .eq(StoreReview::getId,id)
                    .set(StoreReview::getStatus,2));
            }
            return true;
            //return this.getBaseMapper().deleteByIds(new ArrayList<>(List.of(array)))>0;
        } catch (Exception e) {
            log.error("商品评论表,deleteById 操作失败", e);
            throw e;
        }
    }

    @Override
    public Boolean updateById(StoreReviewBo bo) {
        try {
            int row;
            StoreReview obj = MapstructUtil.convert(bo, StoreReview.class);
            row = this.getBaseMapper().updateById(obj);
            return row > 0;
        } catch (Exception e) {
            log.error("商品评论表,updateById 操作失败", e);
            throw e;
        }
    }

    @Override
    public StoreReviewVo selectVoById(Serializable id) {
        return this.getBaseMapper().selectVoById(id);

    }
    @Override
    public TableDataInfo<StoreReviewVo> selectListVoPage(StoreReviewBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<StoreReview> lqw = buildQueryWrapper(bo);
        Page<StoreReviewVo> page = baseMapper.selectVoPage(pageQuery.build(),lqw);
        for (StoreReviewVo vo : page.getRecords()) {
          StoreMemberVo memberVo = storeMemberService.selectVoById(vo.getUserId());
           if(memberVo!=null){
                vo.setUserName(memberVo.getNickname());
                vo.setUserAvatar(memberVo.getHeadPortrait());
           }else{
                vo.setUserName("未知用户");
                vo.setUserAvatar(SystemCommonEnum.SYS_USER_AVATAR.getCode());
           }
           StoreProductSpuVo spuVo = storeProductSpuService.selectVoById(vo.getSpuId());
            if(spuVo!=null){
                vo.setSpuTitle(spuVo.getTitle());
            }
        }
        return TableDataInfo.build(page);
    }

    private LambdaQueryWrapper<StoreReview> buildQueryWrapper(StoreReviewBo query){
        if(query==null){
            query=new StoreReviewBo();
        }
        LambdaQueryWrapper<StoreReview> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(StringUtil.isNotBlank(query.getSpuId()),StoreReview::getSpuId, query.getSpuId());
        wrapper.eq(StringUtil.isNotEmpty(query.getStatus())&&query.getStatus()>=0,StoreReview::getStatus,query.getStatus());
        return wrapper;
    }

    @Override
    public Boolean imports(Set<StoreReviewExport> list) {
        return true;
    }

    @Override
    public Boolean addReview(StoreReviewBo bo) {
        StoreOrderVo orderVo = storeOrderService.selectVoById(bo.getOrderId());
        if(orderVo==null){
            throw new MmsException("订单不存在");
        }
        if(!orderVo.getUserId().equals(bo.getUserId())){
            throw new MmsException("订单不属于当前用户");
        }
        bo.setTenantId(orderVo.getTenantId());
        return insert(bo);
    }
}
