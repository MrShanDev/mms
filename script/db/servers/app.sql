create table app_version
(
    id                   bigint auto_increment comment '主键ID'
        primary key,
    app_code             varchar(50)                                         not null comment '应用编码（唯一标识）',
    app_name             varchar(100)                                        not null comment '应用名称',
    platform             enum ('android', 'ios', 'harmony', 'h5', 'flutter') not null comment '平台类型',
    version_code         varchar(20)                                         not null comment '内部版本号（用于比较，如：1001）',
    version_name         varchar(50)                                         not null comment '用户版本号（如：1.2.3）',
    build_number         varchar(50)                                         null comment '构建号',
    download_url         varchar(500)                                        not null comment '下载地址',
    file_size            bigint                                              null comment '文件大小（字节）',
    file_md5             varchar(32)                                         null comment '文件MD5校验值',
    release_notes        text                                                null comment '更新说明',
    is_force_update      tinyint(1)   default 0                              null comment '是否强制更新（0-否，1-是）',
    min_required_version varchar(20)                                         null comment '最小支持版本',
    publish_type         varchar(200) default 'immediate'                    null comment '发布方式',
    publish_status       varchar(200) default 'draft'                        null comment '发布状态',
    publish_time         datetime                                            null comment '发布时间',
    publish_user         varchar(50)                                         null comment '发布人',
    status               tinyint(1)   default 1                              null comment '状态（1-正常，0-停用）',
    sort                 int          default 0                              null comment '排序',
    revision             int          default 1                              null comment '乐观锁',
    tenant_id            varchar(32)  default '0'                            null comment '租户号',
    created_by           varchar(32)                                         null comment '创建者',
    created_time         datetime     default CURRENT_TIMESTAMP              null comment '创建时间',
    updated_by           varchar(32)                                         null comment '更新者',
    updated_time         datetime     default CURRENT_TIMESTAMP              null on update CURRENT_TIMESTAMP comment '更新时间',
    remark               varchar(500)                                        null comment '备注',
    constraint uk_app_platform_version
        unique (app_code, platform, version_code) comment '防止重复版本'
)
    comment 'App版本发布表';

create index idx_platform_status
    on app_version (platform, status)
    comment '平台状态索引';

create index idx_publish_time
    on app_version (publish_time)
    comment '发布时间索引';
