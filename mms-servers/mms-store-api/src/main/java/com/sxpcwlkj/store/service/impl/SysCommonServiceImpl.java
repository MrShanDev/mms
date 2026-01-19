package com.sxpcwlkj.store.service.impl;

import com.sxpcwlkj.store.mapper.SysCommonMapper;
import com.sxpcwlkj.store.service.SysCommonService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
@Slf4j
@Transactional
@Service("sys_common")
@RequiredArgsConstructor
public class SysCommonServiceImpl implements SysCommonService {
    private final SysCommonMapper sysCommonMapper;
    @Override
    public List<Map<String, String>> selectDictByCode(String code) {
        //code 转大写
        code = code.toUpperCase();
        return sysCommonMapper.selectList("select d.* from sys_dict_data dd left join sys_dict d  on d.field_name=dd.field_name where UPPER(d.field_name)='"+code+"' and d.status=1 group by dd.id order by d.sort asc");
    }
}
