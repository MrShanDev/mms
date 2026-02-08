package com.sxpcwlkj.gen.controller;

import com.sxpcwlkj.common.code.entity.PageResult;
import com.sxpcwlkj.common.exception.MmsException;
import com.sxpcwlkj.common.utils.R;
import com.sxpcwlkj.framework.entity.AesKeyEntity;
import com.sxpcwlkj.framework.entity.SysSign;
import com.sxpcwlkj.framework.service.SysSignService;
import com.sxpcwlkj.framework.utils.SignUtil;
import com.sxpcwlkj.gen.common.GenQueryBo;
import com.sxpcwlkj.gen.config.GenDataSource;
import com.sxpcwlkj.gen.entity.DataSourceEntity;
import com.sxpcwlkj.gen.entity.TableEntity;
import com.sxpcwlkj.gen.service.DataSourceService;
import com.sxpcwlkj.gen.utils.DbUtils;
import com.sxpcwlkj.gen.utils.GenUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * 数据源管理
 *
 * @author mmsAdmin
 * @Doc mmsadmin.cn
 */
@Slf4j
@RestController
@RequestMapping("gen/dataSource")
@AllArgsConstructor
public class DataSourceController {
    private final DataSourceService datasourceService;
    private final SysSignService sysSignService;
    @GetMapping("page")
    public R<PageResult<DataSourceEntity>> page(GenQueryBo query) {
        return R.success(datasourceService.page(query).toPageResult());
    }

    @GetMapping("list")
    public R<List<DataSourceEntity>> list() {
        List<DataSourceEntity> list = datasourceService.getList();

        return R.success(list);
    }

    @GetMapping("{id}")
    public R<DataSourceEntity> get(@PathVariable("id") Long id) {
        DataSourceEntity data = datasourceService.getById(id);
        return R.success(data);
    }

    @GetMapping("test/{id}")
    public R<String> test(@PathVariable("id") Long id) {
        try {
            DataSourceEntity entity = datasourceService.getById(id);
            SysSign sysSign = sysSignService.getSign();
            if (sysSign == null) {
                throw new MmsException("秘钥不存在/请重新登录");
            }
            entity.setPassword(SignUtil.decryptAesCryptoJs(entity.getPassword(),new AesKeyEntity(sysSign.getAppId(), sysSign.getSecretKey())));
            DbUtils.getConnection(new GenDataSource(entity));
            return R.success("连接成功");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return R.fail("连接失败，请检查配置信息");
        }
    }

    @PostMapping
    public R<String> save(@RequestBody DataSourceEntity entity) {
        datasourceService.save(entity);
        return R.success();
    }

    @PutMapping
    public R<String> update(@RequestBody DataSourceEntity entity) {
        datasourceService.updateById(entity);
        return R.success();
    }

    @DeleteMapping
    public R<String> delete(@RequestBody Long[] ids) {
        datasourceService.removeBatchByIds(Arrays.asList(ids));
        return R.success();
    }

    /**
     * 根据数据源ID，获取全部数据表
     *
     * @param id 数据源ID
     */
    @GetMapping("table/list/{id}")
    public R<List<TableEntity>> tableList(@PathVariable("id") Long id) {
        try {
            // 获取数据源
            GenDataSource datasource = datasourceService.get(id);
            // 根据数据源，获取全部数据表
            List<TableEntity> tableList = GenUtils.getTableList(datasource);
            return R.success(tableList);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return R.fail("数据源配置错误，请检查数据源配置！");
        }
    }
}
