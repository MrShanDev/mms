package com.sxpcwlkj.store.entity;

import lombok.Data;

/**
 * @author shanpengnian
 */
@Data
public class StoreToolArea {

    /**
     * 名称
     */
    private String name;
    /**
     * /编码
     */
    private String code;
    /**
     * 父编码
     */
    private String parentCode;
    /**
     * 级别
     */
    private Integer level;
    /**
     * 排序
     */
    private Integer sort;
}
