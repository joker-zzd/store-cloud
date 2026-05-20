/*
 Navicat Premium Data Transfer

 Source Server         : store
 Source Server Type    : MySQL
 Source Server Version : 80032 (8.0.32)
 Source Host           : localhost:3306
 Source Schema         : store

 Target Server Type    : MySQL
 Target Server Version : 80032 (8.0.32)
 File Encoding         : 65001

 Date: 06/05/2026 22:16:51
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for app_audit_config
-- ----------------------------
DROP TABLE IF EXISTS `app_audit_config`;
CREATE TABLE `app_audit_config`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `app_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '应用类型：ios/android',
  `channel` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT ' 应用渠道：ios：appstore；android：xiaomi、huawei、honor、oppo、vivo、yyb、ali、360\',',
  `version` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '版本号，例如：1.0.3',
  `audit_mode` tinyint NOT NULL DEFAULT 0 COMMENT '是否审核模式：0否，1是',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_app_channel_version`(`app_type` ASC, `channel` ASC, `version` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = 'APP应用商店审核配置表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for cms_subject
-- ----------------------------
DROP TABLE IF EXISTS `cms_subject`;
CREATE TABLE `cms_subject`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '专题ID',
  `category_id` bigint NULL DEFAULT NULL COMMENT '分类ID',
  `title` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '专题标题',
  `pic` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '专题图片',
  `recommend_status` tinyint NULL DEFAULT 1 COMMENT '推荐状态',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '专题内容',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_category_id`(`category_id` ASC) USING BTREE,
  INDEX `idx_recommend_status`(`recommend_status` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '专题表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for coupon
-- ----------------------------
DROP TABLE IF EXISTS `coupon`;
CREATE TABLE `coupon`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '优惠券ID',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '优惠券名称',
  `type` tinyint NOT NULL DEFAULT 1 COMMENT '优惠券类型:1=普通券',
  `discount_type` tinyint NOT NULL COMMENT '折扣类型:1=满减,2=每满减,3=折扣,4=无门槛',
  `specific_scope` tinyint NOT NULL DEFAULT 0 COMMENT '限定范围:0=不限定,1=限定',
  `discount_value` int NOT NULL DEFAULT 1 COMMENT '折扣值',
  `threshold_amount` int NOT NULL DEFAULT 0 COMMENT '使用门槛',
  `max_discount_amount` int NOT NULL DEFAULT 0 COMMENT '最大优惠金额',
  `obtain_way` tinyint NOT NULL DEFAULT 0 COMMENT '获取方式:1=手动,2=兑换码',
  `issue_begin_time` datetime NULL DEFAULT NULL COMMENT '发放开始时间',
  `issue_end_time` datetime NULL DEFAULT NULL COMMENT '发放结束时间',
  `term_days` int NOT NULL DEFAULT 0 COMMENT '有效天数',
  `term_begin_time` datetime NULL DEFAULT NULL COMMENT '有效期开始',
  `term_end_time` datetime NULL DEFAULT NULL COMMENT '有效期结束',
  `status` tinyint NULL DEFAULT 1 COMMENT '状态:1=待发放,2=未开始,3=进行中,4=已结束,5=暂停',
  `total_num` int NOT NULL DEFAULT 0 COMMENT '总量',
  `issue_num` int NOT NULL DEFAULT 0 COMMENT '已发放数量',
  `used_num` int NOT NULL DEFAULT 0 COMMENT '已使用数量',
  `user_limit` int NOT NULL DEFAULT 1 COMMENT '每人限领',
  `ext_param` json NULL COMMENT '扩展参数',
  `creater` bigint NOT NULL COMMENT '创建人ID',
  `updater` bigint NOT NULL COMMENT '更新人ID',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_type`(`type` ASC) USING BTREE,
  INDEX `idx_status`(`status` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '优惠券表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for coupon_category_relation
-- ----------------------------
DROP TABLE IF EXISTS `coupon_category_relation`;
CREATE TABLE `coupon_category_relation`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '关联ID',
  `coupon_id` bigint NOT NULL COMMENT '优惠券ID',
  `category_id` bigint NOT NULL COMMENT '商品分类ID',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_coupon_id`(`coupon_id` ASC) USING BTREE,
  INDEX `idx_category_id`(`category_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '优惠券分类关联表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for coupon_product_relation
-- ----------------------------
DROP TABLE IF EXISTS `coupon_product_relation`;
CREATE TABLE `coupon_product_relation`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '关联ID',
  `coupon_id` bigint NOT NULL COMMENT '优惠券ID',
  `product_id` bigint NOT NULL COMMENT '商品ID',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_coupon_id`(`coupon_id` ASC) USING BTREE,
  INDEX `idx_product_id`(`product_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '优惠券与商品关联表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for member
-- ----------------------------
DROP TABLE IF EXISTS `member`;
CREATE TABLE `member`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '会员ID',
  `level_id` bigint NOT NULL COMMENT '等级ID',
  `username` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户名',
  `password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '密码',
  `nickname` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '昵称',
  `mobile` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '手机号',
  `gender` tinyint NULL DEFAULT 0 COMMENT '性别:0=未知,1=男,2=女',
  `birthday` date NULL DEFAULT NULL COMMENT '生日',
  `growth` int NULL DEFAULT 0 COMMENT '成长值',
  `status` tinyint NULL DEFAULT 1 COMMENT '状态:1=启用,0=禁用',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uniq_username`(`username` ASC) USING BTREE,
  UNIQUE INDEX `uniq_mobile`(`mobile` ASC) USING BTREE,
  INDEX `idx_level_id`(`level_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '会员表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for member_address
-- ----------------------------
DROP TABLE IF EXISTS `member_address`;
CREATE TABLE `member_address`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '收货地址ID',
  `member_id` bigint NOT NULL COMMENT '会员ID',
  `name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '收货人姓名',
  `phone` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '联系电话',
  `post_code` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '邮编',
  `province` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '省份',
  `city` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '城市',
  `region` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '区域',
  `detail_address` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '详细地址',
  `default_status` tinyint NULL DEFAULT 0 COMMENT '是否默认地址',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_member_id`(`member_id` ASC) USING BTREE,
  INDEX `idx_default_status`(`default_status` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '会员收货地址表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for member_level
-- ----------------------------
DROP TABLE IF EXISTS `member_level`;
CREATE TABLE `member_level`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '会员等级ID',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '等级名称',
  `growth_point` int NULL DEFAULT 0 COMMENT '所需成长值',
  `default_status` tinyint NULL DEFAULT 0 COMMENT '是否默认等级',
  `discount_rate` decimal(10, 2) NULL DEFAULT 1.00 COMMENT '折扣率',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uniq_name`(`name` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '会员等级表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for member_plan
-- ----------------------------
DROP TABLE IF EXISTS `member_plan`;
CREATE TABLE `member_plan`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `plan_code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '套餐编码，如 VIP、SVIP',
  `plan_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '套餐名称',
  `price` decimal(10, 2) NOT NULL DEFAULT 0.00 COMMENT '套餐价格',
  `renew_price` decimal(10, 2) NOT NULL DEFAULT 0.00 COMMENT '续费价格',
  `duration_days` int NOT NULL DEFAULT 30 COMMENT '有效天数',
  `status` tinyint NOT NULL DEFAULT 1 COMMENT '状态：0禁用 1启用',
  `sort` int NOT NULL DEFAULT 0 COMMENT '排序',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_plan_code`(`plan_code` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '会员套餐表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for member_subscription
-- ----------------------------
DROP TABLE IF EXISTS `member_subscription`;
CREATE TABLE `member_subscription`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `subscription_no` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '订阅编号',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `member_plan_id` bigint NOT NULL COMMENT '会员套餐ID',
  `member_plan_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '会员套餐名称',
  `member_level_id` bigint NULL DEFAULT NULL COMMENT '会员等级ID',
  `member_level_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '会员等级名称',
  `subscribe_scene` tinyint NOT NULL DEFAULT 1 COMMENT '订阅场景：1会员订阅',
  `pay_channel` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '支付渠道：alipay/wechat',
  `pay_way` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '支付方式：alipay_auto/wechat_auto',
  `subscribe_type` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '订阅类型：month/year/season',
  `period_count` int NOT NULL DEFAULT 1 COMMENT '每周期数量，例如1个月、1年',
  `period_unit` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '周期单位：DAY/MONTH/YEAR',
  `status` tinyint NOT NULL DEFAULT 0 COMMENT '订阅状态：0待签约 1生效中 2暂停中 3已取消 4已到期 5签约失败',
  `auto_renew_status` tinyint NOT NULL DEFAULT 0 COMMENT '自动续费状态：0未开通 1开启中 2已关闭 3续费失败待重试',
  `contract_status` tinyint NOT NULL DEFAULT 0 COMMENT '签约状态：0未签约 1已签约 2已解约 3签约失败',
  `agreement_no` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '支付协议号/签约号',
  `external_contract_no` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '商户侧签约编号',
  `open_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '渠道侧用户标识，例如微信openid/支付宝用户标识',
  `contract_time` datetime NULL DEFAULT NULL COMMENT '签约时间',
  `cancel_contract_time` datetime NULL DEFAULT NULL COMMENT '解约时间',
  `first_order_no` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '首单订单号',
  `last_order_no` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '最近一次成功续费订单号',
  `first_pay_amount` decimal(10, 2) NOT NULL DEFAULT 0.00 COMMENT '首次支付金额',
  `renew_pay_amount` decimal(10, 2) NOT NULL DEFAULT 0.00 COMMENT '续费金额',
  `discount_amount` decimal(10, 2) NOT NULL DEFAULT 0.00 COMMENT '优惠金额',
  `currency` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'CNY' COMMENT '币种',
  `start_time` datetime NULL DEFAULT NULL COMMENT '订阅生效时间',
  `expire_time` datetime NULL DEFAULT NULL COMMENT '当前到期时间',
  `next_renew_time` datetime NULL DEFAULT NULL COMMENT '下一次续费时间',
  `last_renew_time` datetime NULL DEFAULT NULL COMMENT '最近一次续费时间',
  `fail_count` int NOT NULL DEFAULT 0 COMMENT '连续续费失败次数',
  `max_fail_count` int NOT NULL DEFAULT 3 COMMENT '最大失败次数',
  `last_fail_reason` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '最近一次失败原因',
  `cancel_reason` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '取消原因',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `version` int NOT NULL DEFAULT 0 COMMENT '版本号',
  `is_deleted` tinyint NOT NULL DEFAULT 0 COMMENT '是否删除：0否 1是',
  `create_by` bigint NULL DEFAULT NULL COMMENT '创建人',
  `update_by` bigint NULL DEFAULT NULL COMMENT '更新人',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_subscription_no`(`subscription_no` ASC) USING BTREE,
  UNIQUE INDEX `uk_external_contract_no`(`external_contract_no` ASC) USING BTREE,
  INDEX `idx_user_id`(`user_id` ASC) USING BTREE,
  INDEX `idx_member_plan_id`(`member_plan_id` ASC) USING BTREE,
  INDEX `idx_agreement_no`(`agreement_no` ASC) USING BTREE,
  INDEX `idx_status`(`status` ASC) USING BTREE,
  INDEX `idx_auto_renew_status`(`auto_renew_status` ASC) USING BTREE,
  INDEX `idx_next_renew_time`(`next_renew_time` ASC) USING BTREE,
  INDEX `idx_expire_time`(`expire_time` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '会员订阅主表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for member_subscription_notify_log
-- ----------------------------
DROP TABLE IF EXISTS `member_subscription_notify_log`;
CREATE TABLE `member_subscription_notify_log`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `notify_no` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '通知记录编号',
  `subscription_id` bigint NULL DEFAULT NULL COMMENT '订阅ID',
  `order_id` bigint NULL DEFAULT NULL COMMENT '订单ID',
  `notify_type` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '通知类型：sign/pay/refund/unsign',
  `pay_channel` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '支付渠道：alipay/wechat',
  `agreement_no` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '协议号',
  `out_trade_no` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '商户支付单号',
  `trade_no` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '支付平台交易号',
  `notify_content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '通知原文',
  `verify_status` tinyint NOT NULL DEFAULT 0 COMMENT '验签状态：0未验签 1成功 2失败',
  `process_status` tinyint NOT NULL DEFAULT 0 COMMENT '处理状态：0待处理 1处理成功 2处理失败 3重复通知',
  `error_msg` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '错误信息',
  `notify_time` datetime NULL DEFAULT NULL COMMENT '通知时间',
  `process_time` datetime NULL DEFAULT NULL COMMENT '处理时间',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_notify_no`(`notify_no` ASC) USING BTREE,
  INDEX `idx_subscription_id`(`subscription_id` ASC) USING BTREE,
  INDEX `idx_order_id`(`order_id` ASC) USING BTREE,
  INDEX `idx_out_trade_no`(`out_trade_no` ASC) USING BTREE,
  INDEX `idx_trade_no`(`trade_no` ASC) USING BTREE,
  INDEX `idx_agreement_no`(`agreement_no` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '会员订阅回调记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for member_subscription_order
-- ----------------------------
DROP TABLE IF EXISTS `member_subscription_order`;
CREATE TABLE `member_subscription_order`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `order_no` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '订阅订单号',
  `subscription_id` bigint NOT NULL COMMENT '订阅ID',
  `subscription_no` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '订阅编号',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `order_type` tinyint NOT NULL COMMENT '订单类型：1首开订单 2续费订单 3补扣订单 4升级补差订单',
  `period_no` int NOT NULL DEFAULT 1 COMMENT '第几期',
  `biz_type` tinyint NOT NULL DEFAULT 1 COMMENT '业务类型：1会员订阅',
  `pay_channel` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '支付渠道：alipay/wechat',
  `pay_way` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '支付方式：alipay_auto/wechat_auto',
  `agreement_no` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '协议号',
  `out_trade_no` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '商户支付单号',
  `transaction_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '渠道流水号',
  `trade_no` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '支付平台交易号',
  `order_amount` decimal(10, 2) NOT NULL DEFAULT 0.00 COMMENT '订单金额',
  `discount_amount` decimal(10, 2) NOT NULL DEFAULT 0.00 COMMENT '优惠金额',
  `pay_amount` decimal(10, 2) NOT NULL DEFAULT 0.00 COMMENT '实际支付金额',
  `currency` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'CNY' COMMENT '币种',
  `order_status` tinyint NOT NULL DEFAULT 0 COMMENT '订单状态：0待支付 1支付中 2支付成功 3支付失败 4已关闭 5已退款',
  `notify_status` tinyint NOT NULL DEFAULT 0 COMMENT '回调状态：0未回调 1已回调 2回调处理中 3回调失败',
  `retry_status` tinyint NOT NULL DEFAULT 0 COMMENT '重试状态：0不需要 1待重试 2重试成功 3重试终止',
  `retry_count` int NOT NULL DEFAULT 0 COMMENT '重试次数',
  `pay_time` datetime NULL DEFAULT NULL COMMENT '支付时间',
  `notify_time` datetime NULL DEFAULT NULL COMMENT '回调时间',
  `close_time` datetime NULL DEFAULT NULL COMMENT '关闭时间',
  `fail_reason` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '失败原因',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `is_deleted` tinyint NOT NULL DEFAULT 0 COMMENT '是否删除：0否 1是',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_order_no`(`order_no` ASC) USING BTREE,
  UNIQUE INDEX `uk_out_trade_no`(`out_trade_no` ASC) USING BTREE,
  INDEX `idx_subscription_id`(`subscription_id` ASC) USING BTREE,
  INDEX `idx_subscription_no`(`subscription_no` ASC) USING BTREE,
  INDEX `idx_user_id`(`user_id` ASC) USING BTREE,
  INDEX `idx_agreement_no`(`agreement_no` ASC) USING BTREE,
  INDEX `idx_order_status`(`order_status` ASC) USING BTREE,
  INDEX `idx_notify_status`(`notify_status` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '会员订阅订单表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for oms_cart_item
-- ----------------------------
DROP TABLE IF EXISTS `oms_cart_item`;
CREATE TABLE `oms_cart_item`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '购物车ID',
  `product_id` bigint NULL DEFAULT NULL COMMENT '商品ID',
  `product_sku_id` bigint NULL DEFAULT NULL COMMENT 'SKU ID',
  `member_id` bigint NULL DEFAULT NULL COMMENT '会员ID',
  `quantity` int NULL DEFAULT 1 COMMENT '购买数量',
  `price` decimal(10, 2) NULL DEFAULT NULL COMMENT '商品价格',
  `product_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '商品名称',
  `sp_data` json NULL COMMENT '规格信息(JSON)',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_member_id`(`member_id` ASC) USING BTREE,
  INDEX `idx_product_id`(`product_id` ASC) USING BTREE,
  INDEX `idx_product_sku_id`(`product_sku_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '购物车表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for oms_order
-- ----------------------------
DROP TABLE IF EXISTS `oms_order`;
CREATE TABLE `oms_order`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '订单ID',
  `member_id` bigint NOT NULL COMMENT '会员ID',
  `coupon_id` bigint NULL DEFAULT NULL COMMENT '优惠券ID',
  `order_sn` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '订单编号',
  `total_amount` decimal(10, 2) NOT NULL COMMENT '订单总额',
  `pay_amount` decimal(10, 2) NOT NULL COMMENT '实际支付金额',
  `freight_amount` decimal(10, 2) NULL DEFAULT 0.00 COMMENT '运费',
  `promotion_amount` decimal(10, 2) NULL DEFAULT 0.00 COMMENT '促销优惠金额',
  `pay_type` tinyint NULL DEFAULT 0 COMMENT '支付方式:1=支付宝,2=微信',
  `status` tinyint NULL DEFAULT 0 COMMENT '订单状态:0=待付款,...',
  `order_type` tinyint NULL DEFAULT 0 COMMENT '订单类型:0=正常,1=秒杀',
  `delivery_company` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '物流公司',
  `delivery_sn` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '物流单号',
  `receiver_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '收货人姓名',
  `receiver_phone` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '收货人电话',
  `receiver_detail_address` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '收货详细地址',
  `note` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '订单备注',
  `payment_time` datetime NULL DEFAULT NULL COMMENT '支付时间',
  `delivery_time` datetime NULL DEFAULT NULL COMMENT '发货时间',
  `receive_time` datetime NULL DEFAULT NULL COMMENT '收货时间',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uniq_order_sn`(`order_sn` ASC) USING BTREE,
  INDEX `idx_member_id`(`member_id` ASC) USING BTREE,
  INDEX `idx_status`(`status` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '订单表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for oms_order_item
-- ----------------------------
DROP TABLE IF EXISTS `oms_order_item`;
CREATE TABLE `oms_order_item`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '订单项ID',
  `order_id` bigint NOT NULL COMMENT '订单ID',
  `order_sn` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '订单编号',
  `product_id` bigint NULL DEFAULT NULL COMMENT '商品ID',
  `product_sku_id` bigint NULL DEFAULT NULL COMMENT 'SKU ID',
  `product_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '商品名称',
  `product_price` decimal(10, 2) NULL DEFAULT NULL COMMENT '商品单价',
  `product_quantity` int NULL DEFAULT NULL COMMENT '购买数量',
  `sp_data` json NULL COMMENT '规格信息(JSON)',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_order_id`(`order_id` ASC) USING BTREE,
  INDEX `idx_product_id`(`product_id` ASC) USING BTREE,
  INDEX `idx_product_sku_id`(`product_sku_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '订单项表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for oms_order_operate_history
-- ----------------------------
DROP TABLE IF EXISTS `oms_order_operate_history`;
CREATE TABLE `oms_order_operate_history`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '操作ID',
  `order_id` bigint NOT NULL COMMENT '订单ID',
  `operate_man` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '操作人',
  `order_status` tinyint NULL DEFAULT NULL COMMENT '订单状态',
  `note` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '操作备注',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '操作时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_order_id`(`order_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '订单操作历史表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for oms_order_return_apply
-- ----------------------------
DROP TABLE IF EXISTS `oms_order_return_apply`;
CREATE TABLE `oms_order_return_apply`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '退货申请ID',
  `order_id` bigint NOT NULL COMMENT '订单ID',
  `product_sku_id` bigint NOT NULL COMMENT 'SKU ID',
  `member_username` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '申请会员用户名',
  `return_amount` decimal(10, 2) NULL DEFAULT NULL COMMENT '退货金额',
  `return_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '退货人姓名',
  `return_phone` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '退货人电话',
  `status` tinyint NULL DEFAULT 0 COMMENT '状态:0=待处理,1=退货中,2=已完成,3=已拒绝',
  `handle_time` datetime NULL DEFAULT NULL COMMENT '处理时间',
  `reason` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '退货原因',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_order_id`(`order_id` ASC) USING BTREE,
  INDEX `idx_status`(`status` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '订单退货申请表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for pms_attribute
-- ----------------------------
DROP TABLE IF EXISTS `pms_attribute`;
CREATE TABLE `pms_attribute`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '属性ID',
  `category_id` bigint NOT NULL COMMENT '所属分类ID',
  `name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '属性名称',
  `type` tinyint NULL DEFAULT 0 COMMENT '属性类型:0=规格,1=参数',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_category_id`(`category_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '商品属性参数表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for pms_brand
-- ----------------------------
DROP TABLE IF EXISTS `pms_brand`;
CREATE TABLE `pms_brand`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '品牌ID',
  `name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '品牌名称',
  `logo` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '品牌LOGO',
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '品牌描述',
  `show_status` tinyint NULL DEFAULT 1 COMMENT '显示状态:1=显示,0=隐藏',
  `sort` int NULL DEFAULT 0 COMMENT '排序',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uniq_name`(`name` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '品牌表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for pms_category
-- ----------------------------
DROP TABLE IF EXISTS `pms_category`;
CREATE TABLE `pms_category`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '分类ID',
  `parent_id` bigint NULL DEFAULT 0 COMMENT '父分类ID',
  `name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '分类名称',
  `level` tinyint NULL DEFAULT 1 COMMENT '分类等级',
  `show_status` tinyint NULL DEFAULT 1 COMMENT '显示状态:1=显示,0=隐藏',
  `icon` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '分类图标',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_parent_id`(`parent_id` ASC) USING BTREE,
  INDEX `idx_level`(`level` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '商品分类表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for pms_comment
-- ----------------------------
DROP TABLE IF EXISTS `pms_comment`;
CREATE TABLE `pms_comment`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '评论ID',
  `product_id` bigint NULL DEFAULT NULL COMMENT '商品ID',
  `member_nickname` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '会员昵称',
  `star` tinyint NULL DEFAULT 5 COMMENT '评分',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '评论内容',
  `pics` json NULL COMMENT '评论图片',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_product_id`(`product_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '商品评价表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for pms_product
-- ----------------------------
DROP TABLE IF EXISTS `pms_product`;
CREATE TABLE `pms_product`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '商品ID',
  `brand_id` bigint NULL DEFAULT NULL COMMENT '品牌ID',
  `category_id` bigint NULL DEFAULT NULL COMMENT '分类ID',
  `name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '商品名称',
  `pic` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '商品主图',
  `product_sn` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '商品编号',
  `publish_status` tinyint NULL DEFAULT 0 COMMENT '上架状态:0=下架,1=上架',
  `new_status` tinyint NULL DEFAULT 0 COMMENT '新品状态',
  `recommand_status` tinyint NULL DEFAULT 0 COMMENT '推荐状态',
  `sort` int NULL DEFAULT 0 COMMENT '排序',
  `price` decimal(10, 2) NULL DEFAULT NULL COMMENT '价格',
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '商品详情',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uniq_product_sn`(`product_sn` ASC) USING BTREE,
  INDEX `idx_brand_id`(`brand_id` ASC) USING BTREE,
  INDEX `idx_category_id`(`category_id` ASC) USING BTREE,
  INDEX `idx_publish_status`(`publish_status` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '商品SPU表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for pms_sku_stock
-- ----------------------------
DROP TABLE IF EXISTS `pms_sku_stock`;
CREATE TABLE `pms_sku_stock`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'SKU ID',
  `product_id` bigint NOT NULL COMMENT '商品ID',
  `sku_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'SKU编号',
  `price` decimal(10, 2) NULL DEFAULT 0.00 COMMENT '价格',
  `stock` int NULL DEFAULT 0 COMMENT '库存数量',
  `low_stock` int NULL DEFAULT 0 COMMENT '预警库存',
  `sp_data` json NULL COMMENT '规格数据(JSON)',
  `pic` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'SKU图片',
  `sale` int NULL DEFAULT 0 COMMENT '销量',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uniq_sku_code`(`sku_code` ASC) USING BTREE,
  INDEX `idx_product_id`(`product_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = 'SKU库存表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for sms_home_advertise
-- ----------------------------
DROP TABLE IF EXISTS `sms_home_advertise`;
CREATE TABLE `sms_home_advertise`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '广告ID',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '广告名称',
  `type` tinyint NULL DEFAULT 0 COMMENT '轮播位置:0=PC首页,1=APP首页',
  `pic` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '广告图片',
  `start_time` datetime NULL DEFAULT NULL COMMENT '开始时间',
  `end_time` datetime NULL DEFAULT NULL COMMENT '结束时间',
  `status` tinyint NULL DEFAULT 1 COMMENT '状态:1=启用,0=禁用',
  `click_count` int NULL DEFAULT 0 COMMENT '点击量',
  `url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '链接地址',
  `sort` int NULL DEFAULT 0 COMMENT '排序',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_type`(`type` ASC) USING BTREE,
  INDEX `idx_status`(`status` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '首页广告表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for sys_dept
-- ----------------------------
DROP TABLE IF EXISTS `sys_dept`;
CREATE TABLE `sys_dept`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '部门ID',
  `parent_id` bigint NULL DEFAULT 0 COMMENT '父部门ID',
  `name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '部门名称',
  `sort` int NULL DEFAULT 0 COMMENT '排序字段',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uniq_name`(`name` ASC) USING BTREE,
  INDEX `idx_parent_id`(`parent_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '部门表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for sys_file_info
-- ----------------------------
DROP TABLE IF EXISTS `sys_file_info`;
CREATE TABLE `sys_file_info`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `business_type` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '业务类型',
  `original_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '原始文件名',
  `object_key` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'OSS对象Key',
  `bucket_name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'Bucket名称',
  `file_ext` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '文件扩展名',
  `content_type` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '文件MIME类型',
  `file_size` bigint NOT NULL COMMENT '文件大小(字节)',
  `file_hash` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '文件哈希',
  `etag` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'OSS返回的ETag',
  `storage_type` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '存储类型',
  `uploader_id` bigint NULL DEFAULT NULL COMMENT '上传人ID',
  `uploader_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '上传人用户名',
  `deleted` tinyint NOT NULL DEFAULT 0 COMMENT '逻辑删除',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_sys_file_info_business_type`(`business_type` ASC) USING BTREE,
  INDEX `idx_sys_file_info_uploader_id`(`uploader_id` ASC) USING BTREE,
  INDEX `idx_sys_file_info_deleted`(`deleted` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '文件元数据表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '菜单ID',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '菜单名称',
  `parent_id` bigint NULL DEFAULT 0 COMMENT '父菜单ID',
  `path` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '路由地址',
  `component` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '组件路径',
  `perms` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '权限标识',
  `menu_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT 'M=目录, C=菜单, F=按钮',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_parent_id`(`parent_id` ASC) USING BTREE,
  INDEX `idx_menu_type`(`menu_type` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '菜单权限表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for sys_oper_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_oper_log`;
CREATE TABLE `sys_oper_log`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '日志ID',
  `title` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '操作标题',
  `business_type` int NULL DEFAULT 0 COMMENT '业务类型',
  `method` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '请求方法',
  `oper_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '操作人',
  `oper_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '请求URL',
  `oper_ip` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '操作IP',
  `oper_param` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '请求参数',
  `status` tinyint NULL DEFAULT 0 COMMENT '操作状态:0正常,1异常',
  `error_msg` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '错误信息',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_business_type`(`business_type` ASC) USING BTREE,
  INDEX `idx_oper_name`(`oper_name` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '系统操作日志表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '角色ID',
  `name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '角色名称',
  `role_key` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '角色权限字符串',
  `sort` int NULL DEFAULT 0 COMMENT '排序字段',
  `status` tinyint NULL DEFAULT 1 COMMENT '状态: 1=正常,0=停用',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uniq_role_key`(`role_key` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '角色信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for sys_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu`  (
  `role_id` bigint NOT NULL COMMENT '角色ID',
  `menu_id` bigint NOT NULL COMMENT '菜单ID',
  PRIMARY KEY (`role_id`, `menu_id`) USING BTREE,
  INDEX `idx_menu_id`(`menu_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '角色和菜单关联表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `dept_id` bigint NULL DEFAULT NULL COMMENT '部门ID',
  `username` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户名',
  `nickname` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '昵称',
  `password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '密码',
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '头像地址',
  `email` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '邮箱',
  `phone` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '手机号',
  `status` tinyint NULL DEFAULT 1 COMMENT '状态: 1=正常,0=停用',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uniq_username`(`username` ASC) USING BTREE,
  INDEX `idx_dept_id`(`dept_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '后台用户信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role`  (
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `role_id` bigint NOT NULL COMMENT '角色ID',
  PRIMARY KEY (`user_id`, `role_id`) USING BTREE,
  INDEX `idx_role_id`(`role_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户和角色关联表' ROW_FORMAT = DYNAMIC;

SET FOREIGN_KEY_CHECKS = 1;
