package com.sxpcwlkj.store.entity.bo;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.sxpcwlkj.store.entity.StoreProductSpu;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

// 辅助类
@Data
@AllArgsConstructor
public  class SearchResult {
    private LambdaQueryWrapper<StoreProductSpu> wrapper;
    private List<String> tokens;
}
