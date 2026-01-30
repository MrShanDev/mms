DROP TABLE IF EXISTS doc_user;
CREATE TABLE doc_user(
                         `uid` VARCHAR(32) NOT NULL   COMMENT '用户编号' ,
                         `nickname` VARCHAR(255)    COMMENT '昵称' ,
                         `avatar` VARCHAR(255)    COMMENT '头像' ,
                         `type` VARCHAR(255)    COMMENT '用户类型;usr=普通用户 vip=会员用户' ,
                         `ctime` DATETIME    COMMENT '创建时间' ,
                         `mtime` DATETIME    COMMENT '更新时间' ,
                         `status` INT   DEFAULT 0 COMMENT '状态' ,
                         `sort` INT   DEFAULT 0 COMMENT '排序' ,
                         `revision` VARCHAR(32)   DEFAULT 1 COMMENT '乐观锁' ,
                         `tenant_id` VARCHAR(32)   DEFAULT 000000 COMMENT '租户号' ,
                         `created_by` VARCHAR(32)    COMMENT '创建人' ,
                         `created_time` DATETIME    COMMENT '创建时间' ,
                         `updated_by` VARCHAR(32)    COMMENT '更新人' ,
                         `updated_time` DATETIME    COMMENT '更新时间' ,
                         `remark` VARCHAR(255)    COMMENT '备注' ,
                         PRIMARY KEY (uid)
)  COMMENT = '文档用户;';
DROP TABLE IF EXISTS doc_authorize_user;
CREATE TABLE doc_authorize_user(
                                   `id` VARCHAR(32) NOT NULL   COMMENT 'ID' ,
                                   `uid` VARCHAR(32)    COMMENT '用户编号' ,
                                   `chan` VARCHAR(255)    COMMENT '授权平台' ,
                                   `appid` VARCHAR(255)    COMMENT '授权平台标识' ,
                                   `openid` VARCHAR(255)    COMMENT '授权平台用户ID' ,
                                   `ctime` DATETIME    COMMENT '创建时间' ,
                                   `mtime` DATETIME    COMMENT '更新时间' ,
                                   `status` INT   DEFAULT 0 COMMENT '状态' ,
                                   `sort` INT   DEFAULT 0 COMMENT '排序' ,
                                   `revision` VARCHAR(32)   DEFAULT 1 COMMENT '乐观锁' ,
                                   `tenant_id` VARCHAR(32)   DEFAULT 000000 COMMENT '租户号' ,
                                   `created_by` VARCHAR(32)    COMMENT '创建人' ,
                                   `created_time` DATETIME    COMMENT '创建时间' ,
                                   `updated_by` VARCHAR(32)    COMMENT '更新人' ,
                                   `updated_time` DATETIME    COMMENT '更新时间' ,
                                   `remark` VARCHAR(255)    COMMENT '备注' ,
                                   PRIMARY KEY (id)
)  COMMENT = '文档授权用户;';
DROP TABLE IF EXISTS doc_product;
CREATE TABLE doc_product(
                            `prod_id` VARCHAR(255) NOT NULL   COMMENT '产品编号' ,
                            `prod_name` VARCHAR(255)    COMMENT '产品名称' ,
                            `unit_price` VARCHAR(255)    COMMENT '销售单价' ,
                            `mark_price` VARCHAR(255)    COMMENT '市场价格' ,
                            `type` VARCHAR(255)    COMMENT '产品类型' ,
                            `ctime` VARCHAR(255)    COMMENT '创建时间' ,
                            `mtime` VARCHAR(255)    COMMENT '更新时间' ,
                            `status` INT   DEFAULT 0 COMMENT '商品状态;up：上架 un：下降 rm：删除' ,
                            `sort` INT   DEFAULT 0 COMMENT '排序' ,
                            `revision` VARCHAR(32)   DEFAULT 1 COMMENT '乐观锁' ,
                            `tenant_id` VARCHAR(32)   DEFAULT 000000 COMMENT '租户号' ,
                            `created_by` VARCHAR(32)    COMMENT '创建人' ,
                            `created_time` DATETIME    COMMENT '创建时间' ,
                            `updated_by` VARCHAR(32)    COMMENT '更新人' ,
                            `updated_time` DATETIME    COMMENT '更新时间' ,
                            `remark` VARCHAR(255)    COMMENT '备注' ,
                            PRIMARY KEY (prod_id)
)  COMMENT = '文档商品;';
DROP TABLE IF EXISTS doc_order;
CREATE TABLE doc_order(
                          `order_id` VARCHAR(32) NOT NULL   COMMENT '订单编号' ,
                          `uid` VARCHAR(32)    COMMENT '用户编号' ,
                          `txn_amt` DECIMAL(24,2)    COMMENT '订单金额' ,
                          `pay_mchid` VARCHAR(255)    COMMENT '支付商户号' ,
                          `pay_no` VARCHAR(255)    COMMENT '支付平台流水号' ,
                          `pay_timeout` VARCHAR(255)    COMMENT '支付超时时间' ,
                          `prod_id` VARCHAR(255)    COMMENT '产品编号' ,
                          `prod_name` VARCHAR(255)    COMMENT '产品名称' ,
                          `prod_price` DECIMAL(24,2)    COMMENT '产品价格' ,
                          `prod_type` VARCHAR(255)    COMMENT '产品类型' ,
                          `ctime` VARCHAR(255)    COMMENT '创建时间' ,
                          `mtime` VARCHAR(255)    COMMENT '更新时间' ,
                          `status` INT   DEFAULT 0 COMMENT '订单状态;unpaid：待支付 paysuc：已支付 refund：已退款 cancel：已取消 finish：已完成' ,
                          `sort` INT   DEFAULT 0 COMMENT '排序' ,
                          `revision` VARCHAR(32)   DEFAULT 1 COMMENT '乐观锁' ,
                          `tenant_id` VARCHAR(32)   DEFAULT 000000 COMMENT '租户号' ,
                          `created_by` VARCHAR(32)    COMMENT '创建人' ,
                          `created_time` DATETIME    COMMENT '创建时间' ,
                          `updated_by` VARCHAR(32)    COMMENT '更新人' ,
                          `updated_time` DATETIME    COMMENT '更新时间' ,
                          `remark` VARCHAR(255)    COMMENT '备注' ,
                          PRIMARY KEY (order_id)
)  COMMENT = '文档订单;';
DROP TABLE IF EXISTS doc_config;
CREATE TABLE doc_config(
                           `id` VARCHAR(255) NOT NULL   COMMENT 'ID' ,
                           `key` VARCHAR(255)    COMMENT 'KEY' ,
                           `value` VARCHAR(255)    COMMENT '值' ,
                           `ctime` DATETIME    COMMENT '创建时间' ,
                           `mtime` DATETIME    COMMENT '更新时间' ,
                           `status` INT   DEFAULT 0 COMMENT '状态' ,
                           `sort` INT   DEFAULT 0 COMMENT '排序' ,
                           `revision` VARCHAR(32)   DEFAULT 1 COMMENT '乐观锁' ,
                           `tenant_id` VARCHAR(32)   DEFAULT 000000 COMMENT '租户号' ,
                           `created_by` VARCHAR(32)    COMMENT '创建人' ,
                           `created_time` DATETIME    COMMENT '创建时间' ,
                           `updated_by` VARCHAR(32)    COMMENT '更新人' ,
                           `updated_time` DATETIME    COMMENT '更新时间' ,
                           `remark` VARCHAR(255)    COMMENT '备注' ,
                           PRIMARY KEY (id)
)  COMMENT = '文档配置;';
