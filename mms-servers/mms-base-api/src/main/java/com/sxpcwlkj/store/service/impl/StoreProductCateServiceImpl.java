package com.sxpcwlkj.store.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sxpcwlkj.common.code.entity.ThreeQueryBo;
import com.sxpcwlkj.common.enums.SystemCommonEnum;
import com.sxpcwlkj.common.utils.DataUtil;
import com.sxpcwlkj.common.utils.MapstructUtil;
import com.sxpcwlkj.common.utils.StringUtil;
import com.sxpcwlkj.datasource.entity.page.PageQuery;
import com.sxpcwlkj.datasource.entity.page.TableDataInfo;
import com.sxpcwlkj.datasource.mapper.BaseMapperPlus;
import com.sxpcwlkj.framework.service.impl.BaseServiceImpl;
import com.sxpcwlkj.store.entity.StoreProductCate;
import com.sxpcwlkj.store.entity.bo.StoreProductCateBo;
import com.sxpcwlkj.store.entity.export.StoreProductCateExport;
import com.sxpcwlkj.store.entity.vo.StoreProductCateVo;
import com.sxpcwlkj.store.mapper.StoreProductCateMapper;
import com.sxpcwlkj.store.service.StoreProductCateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
/**
 * 商品分类-接口实现
 *
* @author mmsAdmin
* @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
 */
@Slf4j
@Transactional
@Service("store_product_cate")
@RequiredArgsConstructor
public class StoreProductCateServiceImpl extends BaseServiceImpl<StoreProductCate, StoreProductCateVo, StoreProductCateBo> implements StoreProductCateService {

   private final StoreProductCateMapper baseMapper;

    @Override
    public BaseMapperPlus<StoreProductCate, StoreProductCateVo> getBaseMapper() {
        return baseMapper;
    }

    @Override
    public List<StoreProductCateVo> queryTree(ThreeQueryBo bo) {
    List<StoreProductCateVo> queryTrees = baseMapper.selectVoList(new LambdaQueryWrapper<StoreProductCate>()
            .eq(!bo.getIsAll(),StoreProductCate::getStatus, SystemCommonEnum.SYS_COMMON_STATE_OPEN.getValue())
            .orderByAsc(StoreProductCate::getSort));
        // 处理 formId，默认为 "0"
        String parentId = StringUtil.isNotBlank(bo.getFatherId()) ? bo.getFatherId() : "0";

        // 调用递归方法生成树形结构
        return formatTree(queryTrees, parentId, bo.getShowLevel(), 0);
    }

//    @Override
//    public List<StoreProductCateVo> queryTreeByFatherId(String fatherId) {
//        List<StoreProductCateVo> queryTrees = baseMapper.selectVoList(new LambdaQueryWrapper<StoreProductCate>()
//            .eq(StoreProductCate::getStatus, SystemCommonEnum.SYS_COMMON_STATE_OPEN.getValue())
//            .orderByAsc(StoreProductCate::getSort));
//        return formatTree(queryTrees, fatherId,0,0);
//    }

    @Override
    public void queryListSon(String id, List<StoreProductCateVo> endList) {
        StoreProductCateVo vo = baseMapper.selectVoById(id);
        if (vo != null) {
            endList.add(vo);
            queryListSon(vo.getParentId(), endList);
        }
    }
    private List<StoreProductCateVo> formatTree(List<StoreProductCateVo> vos, String fid, int level,int currentLevel) {
        List<StoreProductCateVo> endList = new ArrayList<>();
        for (StoreProductCateVo s : vos) {
            if (fid.equals(s.getParentId())) {
                if(level > currentLevel||level==0) {
                    List<StoreProductCateVo> vo = formatTree(vos, s.getId(),level,currentLevel+1);
                        s.setChildren(vo);
                        endList.add(s);
                    }
                }
        }
        return endList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean insert(StoreProductCateBo bo) {
        try {
            int row;
            bo.setId(null);
            StoreProductCate obj = MapstructUtil.convert(bo, StoreProductCate.class);
            assert obj != null;
            // 查询上级分类
            StoreProductCateVo parent = baseMapper.selectVoById(bo.getParentId());
            if (parent != null) {
                obj.setLevel(parent.getLevel() + 1);
            } else {
                obj.setLevel(1);
            }
            row = this.getBaseMapper().insert(obj);
            bo.setId(obj.getId());
            return row > 0;
        } catch (Exception e) {
            log.error("商品分类,insert 操作失败", e);
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
            log.error("商品分类,deleteById 操作失败", e);
            throw e;
        }
    }

    @Override
    public Boolean updateById(StoreProductCateBo bo) {
        try {
            int row;
            StoreProductCate obj = MapstructUtil.convert(bo, StoreProductCate.class);
            assert obj != null;
            // 查询上级分类
            StoreProductCateVo parent = baseMapper.selectVoById(bo.getParentId());
            if (parent != null) {
                obj.setLevel(parent.getLevel() + 1);
            } else {
                obj.setLevel(1);
            }
            row = this.getBaseMapper().updateById(obj);
            return row > 0;
        } catch (Exception e) {
            log.error("商品分类,updateById 操作失败", e);
            throw e;
        }
    }

    @Override
    public StoreProductCateVo selectVoById(Serializable id) {
        StoreProductCateVo vo= this.getBaseMapper().selectVoById(id);
        List<String> end= new ArrayList<>();
            getIds(end,vo.getId());
            Collections.reverse(end);
            vo.setIds(end.toArray(new String[]{}));
        return vo;

    }
    private void getIds(List<String> end, String id) {
        StoreProductCateVo vo = baseMapper.selectVoById(id);
        if (vo != null) {
            end.add(vo.getId());
            getIds(end, vo.getParentId());
        }
    }
    @Override
    public TableDataInfo<StoreProductCateVo> selectListVoPage(StoreProductCateBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<StoreProductCate> lqw = buildQueryWrapper(bo);
        Page<StoreProductCateVo> page = baseMapper.selectVoPage(pageQuery.build(),lqw);
        return TableDataInfo.build(page);
    }

    private LambdaQueryWrapper<StoreProductCate> buildQueryWrapper(StoreProductCateBo query){
        if(query==null){
            query=new StoreProductCateBo();
        }
        LambdaQueryWrapper<StoreProductCate> wrapper = Wrappers.lambdaQuery();
        return wrapper;
    }

    @Override
    public Boolean imports(Set<StoreProductCateExport> list) {
        return true;
    }

    @Override
    public StoreProductCateVo selectByCateId(String cateId) {
        StoreProductCateVo  vo = baseMapper.selectVoById(cateId);
        if (vo != null&&vo.getLevel()==2) {
            List<StoreProductCateVo> vos = baseMapper.selectVoList(Wrappers.<StoreProductCate>lambdaQuery().eq(StoreProductCate::getParentId,vo.getId()));
            vo.setChildren(vos);
            return vo;
        }
        if (vo != null&&vo.getLevel()==3) {
            StoreProductCateVo  vo1 = baseMapper.selectVoById(vo.getParentId());
            List<StoreProductCateVo> vos = baseMapper.selectVoList(Wrappers.<StoreProductCate>lambdaQuery().eq(StoreProductCate::getParentId,vo1.getId()));
            vo1.setChildren(vos);
            return  vo1;
        }
        if (vo != null&&vo.getLevel()==4) {
            StoreProductCateVo  vo2 = baseMapper.selectVoById(vo.getParentId());
            StoreProductCateVo  vo1 = baseMapper.selectVoById(vo2.getParentId());
            List<StoreProductCateVo> vos = baseMapper.selectVoList(Wrappers.<StoreProductCate>lambdaQuery().eq(StoreProductCate::getParentId,vo1.getId()));
            vo1.setChildren(vos);
            return  vo1;
        }

        return null;
    }

}
