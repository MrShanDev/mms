package com.sxpcwlkj.gen.service.impl;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sxpcwlkj.datasource.entity.page.TableDataInfo;
import com.sxpcwlkj.gen.common.GenQueryBo;
import com.sxpcwlkj.gen.common.service.impl.BaseServiceImpl;
import com.sxpcwlkj.gen.entity.BaseClassEntity;
import com.sxpcwlkj.gen.mapper.BaseClassMapper;
import com.sxpcwlkj.gen.service.BaseClassService;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 基类管理
 *
 * @author mmsAdmin
 * @Doc mmsadmin.cn
 */
@Service
public class BaseClassServiceImpl extends BaseServiceImpl<BaseClassMapper, BaseClassEntity> implements BaseClassService {

    @Override
    public TableDataInfo<BaseClassEntity> page(GenQueryBo query) {
        IPage<BaseClassEntity> page = baseMapper.selectPage(
                getPage(query), getWrapper(query)
        );

        return TableDataInfo.build(page);
    }

    @Override
    public List<BaseClassEntity> getList() {
        return baseMapper.selectList(null);
    }

    @Override
    public boolean save(BaseClassEntity entity) {
        entity.setCreateTime(new Date());
        return super.save(entity);
    }
}
