/**
 * 文章分类
 */
create table store_article_cate
(
    id           varchar(32)             not null comment 'ID'
        primary key,
    parent_id    varchar(32)             null comment '父ID',
    cate_name    varchar(200)            null comment '分类名称',
    level        int                     null comment '级别',
    icon         varchar(500)            null comment '图标',
    status       int         default 1   null comment '状态',
    sort         int         default 0   null comment '排序',
    revision     int         default 1   null comment '乐观锁',
    tenant_id    varchar(32) default '0' null comment '租户号',
    created_by   varchar(32)             null comment '创建者',
    created_time datetime                null comment '创建时间',
    updated_by   varchar(32)             null comment '更新者',
    updated_time datetime                null comment '更新时间',
    remark       varchar(500)            null comment '备注'
)
    comment '文章分类';

/**
 * 文章
 */
create table store_article
(
    id              varchar(32)             not null comment 'ID'
        primary key,
    title           varchar(500)            null comment '文章标题',
    cover_img       varchar(500)            null comment '封面图片',
    tag             varchar(200)            null comment '标签',
    author          varchar(200)            null comment '作者',
    article_cate_id varchar(32)             null comment '分类ID',
    content         longtext                null comment '内容',
    status          int         default 1   null comment '状态',
    sort            int         default 0   null comment '排序',
    revision        int         default 1   null comment '乐观锁',
    tenant_id       varchar(32) default '0' null comment '租户号',
    created_by      varchar(32)             null comment '创建者',
    created_time    datetime                null comment '创建时间',
    updated_by      varchar(32)             null comment '更新者',
    updated_time    datetime                null comment '更新时间',
    remark          varchar(500)            null comment '备注',
    member_id       varchar(32)             null comment '会员ID'
)
    comment '文章';
