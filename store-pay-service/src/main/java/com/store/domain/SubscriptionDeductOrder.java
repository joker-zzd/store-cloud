package com.store.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

/**
 * 订阅扣款单表
 * @TableName idnice_subscription_deduct_order
 */
@TableName(value ="idnice_subscription_deduct_order")
@Data
public class SubscriptionDeductOrder {
    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 用户ID
     */
    private Integer userId;

    /**
     * 会员信息ID（关联idnice_user_member_info）
     */
    private Integer memberInfoId;

    /**
     * 会员等级ID（关联idnice_member_level，记录本次扣款套餐）
     */
    private Integer levelId;

    /**
     * 签约协议表ID（关联pay_sign_contract）
     */
    private Integer signContractId;

    /**
     * 系统扣款单号
     */
    private String deductOrderNo;

    /**
     * 商户订单号
     */
    private String outTradeNo;

    /**
     * 第三方交易流水号
     */
    private String thirdTradeNo;

    /**
     * 扣款类型：1首期签约支付 2自动续费 3补扣 4人工重试
     */
    private Integer deductType;

    /**
     * 第几期
     */
    private Integer periodNo;

    /**
     * 本期开始时间
     */
    private Date periodStartTime;

    /**
     * 本期结束时间
     */
    private Date periodEndTime;

    /**
     * 计划扣款时间
     */
    private Date planDeductTime;

    /**
     * 实际扣款时间
     */
    private Date actualDeductTime;

    /**
     * 订单金额
     */
    private BigDecimal orderAmount;

    /**
     * 优惠金额
     */
    private BigDecimal discountAmount;

    /**
     * 实付金额
     */
    private BigDecimal payAmount;

    /**
     * 货币类型：1人民币
     */
    private Integer currencyType;

    /**
     * 扣款状态：0待发起 1扣款中 2扣款成功 3扣款失败 4已关闭 5已取消
     */
    private Integer deductStatus;

    /**
     * 失败码
     */
    private String failCode;

    /**
     * 失败信息
     */
    private String failMsg;

    /**
     * 重试次数
     */
    private Integer retryCount;

    /**
     * 下次重试时间
     */
    private Date nextRetryTime;

    /**
     * 渠道回调状态：0未收到 1已收到
     */
    private Integer channelNotifyStatus;

    /**
     * 渠道回调时间
     */
    private Date channelNotifyTime;

    /**
     * 渠道请求报文
     */
    private String channelRequestSnapshot;

    /**
     * 渠道响应报文
     */
    private String channelResponseSnapshot;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 会员购买记录ID/业务订单ID
     */
    private Integer purchaseId;
}