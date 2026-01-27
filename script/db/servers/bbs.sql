create table bbs_topic
(
    id           varchar(32)             not null comment 'ID'
        primary key,
    member_id    varchar(32)             null comment '发布者',
    cate_id      varchar(32)             null comment '分类ID',
    title        varchar(300)            null comment '标题',
    content_html text                    null comment '内容',
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
    comment '话题';


create table bbs_leve
(
    id           varchar(32)             null comment 'ID',
    bbs_id       varchar(32)             null comment '话题ID',
    member_id    varchar(32)             null comment '会员ID',
    type         int                     null comment '类型;1:喜欢 2:点赞 3:分享',
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
    comment '话题操作';

create table bbs_files
(
    id           varchar(32)             null comment 'ID',
    bbs_id       varchar(32)             null comment '话题ID',
    type         varchar(100)            null comment '类型',
    height       int                     null comment '高度',
    width        int                     null comment '宽度',
    size         int                     null comment '大小',
    status       int         default 1   null comment '状态',
    url          varchar(500)            null comment '附件地址',
    sort         int         default 0   null comment '排序',
    revision     int         default 1   null comment '乐观锁',
    tenant_id    varchar(32) default '0' null comment '租户号',
    created_by   varchar(32)             null comment '创建者',
    created_time datetime                null comment '创建时间',
    updated_by   varchar(32)             null comment '更新者',
    updated_time datetime                null comment '更新时间',
    remark       varchar(500)            null comment '备注'
)
    comment '话题附件';


create table bbs_comment
(
    id           varchar(32)             not null comment 'ID'
        primary key,
    father_id    varchar(32)             null comment '父ID',
    bbs_id       varchar(32)             null comment '话题ID',
    level        int         default 1   null comment '级别',
    member_id    varchar(32)             null comment '会员ID',
    comment      text                    null comment '评论',
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
    comment '话题评论';


create table bbs_cate
(
    id           varchar(32)             not null comment 'ID'
        primary key,
    father_id    varchar(32)             null comment '父ID',
    name         varchar(300)            null comment '名称',
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
    comment '话题分类';


create table bbs_attention
(
    id           varchar(32)             not null comment 'ID'
        primary key,
    member_id    varchar(32)             null comment '发布者',
    attention_id varchar(32)             null comment '被关注者',
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
    comment '关注作者';
