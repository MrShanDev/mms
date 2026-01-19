package com.sxpcwlkj.store.service;

import java.util.List;
import java.util.Map;

public interface SysCommonService {

    /**
     *  根据code查询字典
     * @param code 编码
     * @return  List
     */
    List<Map<String,String>> selectDictByCode(String code);
}
