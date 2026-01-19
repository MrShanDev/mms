package com.sxpcwlkj.gen.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.sxpcwlkj.common.exception.MmsException;
import com.sxpcwlkj.datasource.entity.page.TableDataInfo;
import com.sxpcwlkj.framework.entity.AesKeyEntity;
import com.sxpcwlkj.framework.entity.SysSign;
import com.sxpcwlkj.framework.service.SysSignService;
import com.sxpcwlkj.framework.utils.SignUtil;
import com.sxpcwlkj.gen.common.GenQueryBo;
import com.sxpcwlkj.gen.common.service.impl.BaseServiceImpl;
import com.sxpcwlkj.gen.config.DbType;
import com.sxpcwlkj.gen.config.GenDataSource;
import com.sxpcwlkj.gen.entity.DataSourceEntity;
import com.sxpcwlkj.gen.mapper.DataSourceMapper;
import com.sxpcwlkj.gen.service.DataSourceService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;


/**
 * 数据源管理
 *
 * @author mmsAdmin
 * @Doc mmsadmin.cn
 */
@Service
@AllArgsConstructor
@Slf4j
public class DataSourceServiceImpl extends BaseServiceImpl<DataSourceMapper, DataSourceEntity> implements DataSourceService {
    private final DataSource dataSource;
    private final SysSignService sysSignService;
    @Override
    public TableDataInfo<DataSourceEntity> page(GenQueryBo query) {
        IPage<DataSourceEntity> page = baseMapper.selectPage(
                getPage(query),
                getWrapper(query)
        );
        return TableDataInfo.build(page);
    }

    @Override
    public List<DataSourceEntity> getList() {
        return baseMapper.selectList(Wrappers.emptyWrapper());
    }

    @Override
    public String getDatabaseProductName(Long dataSourceId) {
        if (dataSourceId.intValue() == 0) {
            return DbType.MySQL.name();
        } else {
            return getById(dataSourceId).getDbType();
        }
    }

    @Override
    public GenDataSource get(Long datasourceId) {
        // 初始化配置信息
        GenDataSource info = null;
        if (datasourceId.intValue() == 0) {
            try {
                info = new GenDataSource(dataSource.getConnection());
            } catch (SQLException e) {
                log.error(e.getMessage(), e);
            }
        } else {
            DataSourceEntity vo = this.getById(datasourceId);
            try {
                SysSign sysSign = sysSignService.getSign();
                if (sysSign == null) {
                    throw new MmsException("秘钥不存在/请重新登录");
                }
                AesKeyEntity aes= new AesKeyEntity(sysSign.getAppId(), sysSign.getSecretKey());
                String newFieldValue = SignUtil.decryptAesCryptoJs(vo.getPassword(),aes);
                vo.setPassword(newFieldValue);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                throw new RuntimeException(e);
            }
            info = new GenDataSource(vo);
        }
        return info;
    }

    @Override
    public boolean save(DataSourceEntity entity) {
        entity.setCreateTime(new Date());
        return super.save(entity);
    }
}
