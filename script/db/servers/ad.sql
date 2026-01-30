/**
 * 广告位
 */
create table store_advertising_location
(
    id           varchar(32)             not null comment 'ID'
        primary key,
    name         varchar(255)            null comment '广告位名称',
    height       varchar(255)            null comment '广告位高度',
    width        varchar(255)            null comment '广告位宽度',
    code         varchar(255)            null comment '广告位编码',
    max_num      int         default 1   null comment '最大显示数量',
    status       int         default 0   null comment '状态',
    sort         int         default 0   null comment '排序',
    revision     varchar(32) default '1' null comment '乐观锁',
    tenant_id    varchar(32) default '0' null comment '租户号',
    created_by   varchar(32)             null comment '创建人',
    created_time datetime                null comment '创建时间',
    updated_by   varchar(32)             null comment '更新人',
    updated_time datetime                null comment '更新时间',
    remark       varchar(255)            null comment '备注'
)
    comment '广告位;';


/**
 * 广告
 */
create table store_advertising
(
    id                       varchar(255)            not null comment 'ID'
        primary key,
    advertising_id           varchar(255)            null comment '广告位ID',
    start_time               varchar(255)            null comment '开始时间',
    end_time                 varchar(255)            null comment '到期时间',
    route_url                varchar(255)            null comment '路由地址',
    route_parameter          varchar(255)            null comment '路由参数',
    image_url                varchar(255)            null comment '图片地址',
    extended_parameter_one   varchar(255)            null comment '扩展参数一',
    extended_parameter_two   varchar(255)            null comment '扩展参数二',
    extended_parameter_three varchar(255)            null comment '扩展参数三',
    extended_parameter_four  varchar(255)            null comment '扩展参数四',
    extended_parameter_five  varchar(255)            null comment '扩展参数五',
    status                   int         default 1   null comment '状态',
    push_index               int         default 0   null comment '推送首页',
    sort                     int         default 0   null comment '排序',
    revision                 varchar(32) default '1' null comment '乐观锁',
    tenant_id                varchar(32) default '0' null comment '租户号',
    created_by               varchar(32)             null comment '创建人',
    created_time             datetime                null comment '创建时间',
    updated_by               varchar(32)             null comment '更新人',
    updated_time             datetime                null comment '更新时间',
    remark                   varchar(255)            null comment '备注'
)
    comment '广告;';
