package com.sxpcwlkj.datasource.entity.page;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sxpcwlkj.common.exception.MmsException;
import com.sxpcwlkj.common.utils.SqlUtil;
import com.sxpcwlkj.common.utils.StringUtil;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 分页查询实体类
 *
 * @author mmsAdmin
 */

@Data
public class PageQuery implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 分页大小
     */
    @JsonIgnore
    @TableField(exist = false)
    private Integer pageSize;

    /**
     * 当前页数
     */
    @JsonIgnore
    @TableField(exist = false)
    private Integer pageNum;

    /**
     * 排序列
     */
    @JsonIgnore
    @TableField(exist = false)
    private String orderByColumn;

    /**
     * 排序的方向desc或者asc
     */
    @JsonIgnore
    @TableField(exist = false)
    private String isAsc;

    /**
     * 当前记录起始索引 默认值
     */
    @TableField(exist = false)
    public static final int DEFAULT_PAGE_NUM = 1;

    /**
     * 每页显示记录数 默认值 默认查全部
     */
    @TableField(exist = false)
    public static final int DEFAULT_PAGE_SIZE = Integer.MAX_VALUE;
    public PageQuery() {}
    @JsonIgnore
    public PageQuery getPageQuery() {
        return this;
    }
    public PageQuery( Integer pageNum,Integer pageSize) {
        this.pageSize = pageSize;
        this.pageNum = pageNum;
    }
    public <T> Page<T> build() {
        Integer pageNum = ObjectUtil.defaultIfNull(getPageNum(), DEFAULT_PAGE_NUM);
        Integer pageSize = ObjectUtil.defaultIfNull(getPageSize(), DEFAULT_PAGE_SIZE);
        if (pageNum <= 0) {
            pageNum = DEFAULT_PAGE_NUM;
        }
        Page<T> page = new Page<>(pageNum, pageSize);
        List<OrderItem> orderItems = buildOrderItem();
        if (CollUtil.isNotEmpty(orderItems)) {
            page.addOrder(orderItems);
        }
        return page;
    }

    /**
     * 构建排序
     * <p>
     * 支持的用法如下:
     * {isAsc:"asc",orderByColumn:"id"} order by id asc
     * {isAsc:"asc",orderByColumn:"id,createTime"} order by id asc,create_time asc
     * {isAsc:"desc",orderByColumn:"id,createTime"} order by id desc,create_time desc
     * {isAsc:"asc,desc",orderByColumn:"id,createTime"} order by id asc,create_time desc
     */
    private List<OrderItem> buildOrderItem() {
        if (StringUtil.isBlank(orderByColumn) || StringUtil.isBlank(isAsc)) {
            return null;
        }
        String orderBy = SqlUtil.escapeOrderBySql(orderByColumn);
        orderBy = StringUtil.toUnderScoreCase(orderBy);

        // 兼容前端排序类型
        isAsc = StringUtil.replaceEach(isAsc, new String[]{"ascending", "descending"}, new String[]{"asc", "desc"});

        String[] orderByArr = orderBy.split(StringUtil.SEPARATOR);
        String[] isAscArr = isAsc.split(StringUtil.SEPARATOR);
        if (isAscArr.length != 1 && isAscArr.length != orderByArr.length) {
            throw new MmsException("排序参数有误");
        }

        List<OrderItem> list = new ArrayList<>();
        // 每个字段各自排序
        for (int i = 0; i < orderByArr.length; i++) {
            String orderByStr = orderByArr[i];
            String isAscStr = isAscArr.length == 1 ? isAscArr[0] : isAscArr[i];
            if ("asc".equals(isAscStr)) {
                list.add(OrderItem.asc(orderByStr));
            } else if ("desc".equals(isAscStr)) {
                list.add(OrderItem.desc(orderByStr));
            } else {
                throw new MmsException("排序参数有误");
            }
        }
        return list;
    }

}

