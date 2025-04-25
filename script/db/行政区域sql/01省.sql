create table store_tool_area
(
    id           bigint auto_increment comment 'ID'
        primary key,
    name         varchar(100)            null comment '名称',
    code         varchar(255)            null comment 'CODE',
    parent_code  varchar(255)            null comment '父CODE',
    level        varchar(255)            null comment '级别',
    status       int         default 0   null comment '状态',
    sort         int         default 0   null comment '排序',
    revision     varchar(32) default '0' null comment '乐观锁',
    tenant_id    varchar(32) default '0' null comment '租户号',
    created_by   varchar(32)             null comment '创建人',
    created_time datetime                null comment '创建时间',
    updated_by   varchar(32)             null comment '更新人',
    updated_time datetime                null comment '更新时间',
    remark       varchar(255)            null comment '备注'
)
    comment '行政区域;';


insert into `store_tool_area` (`code`, `name`, `parent_code`, `level`) values('110000000000','北京市','0','1');
insert into `store_tool_area` (`code`, `name`, `parent_code`, `level`) values('120000000000','天津市','0','1');
insert into `store_tool_area` (`code`, `name`, `parent_code`, `level`) values('130000000000','河北省','0','1');
insert into `store_tool_area` (`code`, `name`, `parent_code`, `level`) values('140000000000','山西省','0','1');
insert into `store_tool_area` (`code`, `name`, `parent_code`, `level`) values('150000000000','内蒙古自治区','0','1');
insert into `store_tool_area` (`code`, `name`, `parent_code`, `level`) values('210000000000','辽宁省','0','1');
insert into `store_tool_area` (`code`, `name`, `parent_code`, `level`) values('220000000000','吉林省','0','1');
insert into `store_tool_area` (`code`, `name`, `parent_code`, `level`) values('230000000000','黑龙江省','0','1');
insert into `store_tool_area` (`code`, `name`, `parent_code`, `level`) values('310000000000','上海市','0','1');
insert into `store_tool_area` (`code`, `name`, `parent_code`, `level`) values('320000000000','江苏省','0','1');
insert into `store_tool_area` (`code`, `name`, `parent_code`, `level`) values('330000000000','浙江省','0','1');
insert into `store_tool_area` (`code`, `name`, `parent_code`, `level`) values('340000000000','安徽省','0','1');
insert into `store_tool_area` (`code`, `name`, `parent_code`, `level`) values('350000000000','福建省','0','1');
insert into `store_tool_area` (`code`, `name`, `parent_code`, `level`) values('360000000000','江西省','0','1');
insert into `store_tool_area` (`code`, `name`, `parent_code`, `level`) values('370000000000','山东省','0','1');
insert into `store_tool_area` (`code`, `name`, `parent_code`, `level`) values('410000000000','河南省','0','1');
insert into `store_tool_area` (`code`, `name`, `parent_code`, `level`) values('420000000000','湖北省','0','1');
insert into `store_tool_area` (`code`, `name`, `parent_code`, `level`) values('430000000000','湖南省','0','1');
insert into `store_tool_area` (`code`, `name`, `parent_code`, `level`) values('440000000000','广东省','0','1');
insert into `store_tool_area` (`code`, `name`, `parent_code`, `level`) values('450000000000','广西壮族自治区','0','1');
insert into `store_tool_area` (`code`, `name`, `parent_code`, `level`) values('460000000000','海南省','0','1');
insert into `store_tool_area` (`code`, `name`, `parent_code`, `level`) values('500000000000','重庆市','0','1');
insert into `store_tool_area` (`code`, `name`, `parent_code`, `level`) values('510000000000','四川省','0','1');
insert into `store_tool_area` (`code`, `name`, `parent_code`, `level`) values('520000000000','贵州省','0','1');
insert into `store_tool_area` (`code`, `name`, `parent_code`, `level`) values('530000000000','云南省','0','1');
insert into `store_tool_area` (`code`, `name`, `parent_code`, `level`) values('540000000000','西藏自治区','0','1');
insert into `store_tool_area` (`code`, `name`, `parent_code`, `level`) values('610000000000','陕西省','0','1');
insert into `store_tool_area` (`code`, `name`, `parent_code`, `level`) values('620000000000','甘肃省','0','1');
insert into `store_tool_area` (`code`, `name`, `parent_code`, `level`) values('630000000000','青海省','0','1');
insert into `store_tool_area` (`code`, `name`, `parent_code`, `level`) values('640000000000','宁夏回族自治区','0','1');
insert into `store_tool_area` (`code`, `name`, `parent_code`, `level`) values('650000000000','新疆维吾尔自治区','0','1');
