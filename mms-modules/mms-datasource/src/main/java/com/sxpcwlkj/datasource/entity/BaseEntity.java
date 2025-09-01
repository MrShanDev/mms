package com.sxpcwlkj.datasource.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.Version;
import com.sxpcwlkj.datasource.entity.page.PageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.util.Date;

/**
 * 基础实体类，包含系统通用字段及分页参数
 * <p>
 * 所有业务实体应继承本类，包含以下通用属性：
 * 1. 审计字段（创建人/时间、更新人/时间）
 * 2. 逻辑控制字段（状态、排序、备注）
 * 3. 多租户支持（租户ID）
 * 4. 乐观锁控制
 *
 * @author mmsAdmin
 * @since 2023-01-01
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class BaseEntity extends PageQuery {

    /**
     * 默认构造方法（兼容MyBatis-Plus实体映射）
     */
    public BaseEntity() {
        // 显式调用父类无参构造
        super();
    }

    @Serial
    private static final long serialVersionUID = 1L;

    /* --------------- 业务状态控制字段 --------------- */

    /**
     * 数据状态（0=禁用，1=启用）
     */
    @TableField(fill = FieldFill.INSERT)
    private Integer status = 0;

    /**
     * 显示排序（升序排列）
     */
    private Integer sort;

    /**
     * 备注说明（最大长度500字符）
     */
    @TableField(fill = FieldFill.INSERT)
    private String remark;

    /* --------------- 多租户隔离字段 --------------- */

    /**
     * 租户ID（系统自动填充）
     */
    @TableField(fill = FieldFill.INSERT)
    private String tenantId;

    /* --------------- 乐观锁控制字段 --------------- */

    /**
     * 版本号（更新时自动递增）
     */
    @Version
    @TableField(fill = FieldFill.INSERT)
    private Long revision = 0L;

    /* --------------- 审计字段 --------------- */

    /**
     * 创建人ID（系统自动填充）
     */
    @TableField(fill = FieldFill.INSERT)
    private Long createdBy;

    /**
     * 创建时间（系统自动填充）
     */
    @TableField(fill = FieldFill.INSERT)
    private Date createdTime;

    /**
     * 更新人ID（系统自动填充）
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updatedBy;

    /**
     * 更新时间（系统自动填充）
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updatedTime;
}
