package com.store.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * 支付回调记录表
 * @TableName idnice_pay_notification_callback
 */
@TableName(value ="idnice_pay_notification_callback")
@Data
public class PayNotificationCallback {
    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 系统回调记录号
     */
    private String callbackNo;

    /**
     * 第三方回调/通知唯一标识
     */
    private String thirdCallbackNo;

    /**
     * 支付渠道：1微信 2支付宝 3苹果
     */
    private Integer payChannel;

    /**
     * 事件类型：1签约成功 2签约失败 3解约 4扣款成功 5扣款失败 6关闭
     */
    private Integer eventType;

    /**
     * 用户ID（对应用户表id）
     */
    private Integer userId;

    /**
     * 会员信息ID（关联idnice_user_member_info）
     */
    private Integer memberInfoId;

    /**
     * 会员等级ID（关联idnice_member_level）
     */
    private Integer levelId;

    /**
     * 签约协议ID（关联pay_sign_contract）
     */
    private Integer signContractId;

    /**
     * 扣款单ID（关联pay_deduct_order）
     */
    private Integer deductOrderId;

    /**
     * 商户订单号
     */
    private String outTradeNo;

    /**
     * 第三方通知时间
     */
    private Date notifyTime;

    /**
     * 处理状态：0待处理 1处理中 2处理成功 3处理失败 4重复忽略
     */
    private Integer processStatus;

    /**
     * 处理次数
     */
    private Integer processTimes;

    /**
     * 处理完成时间
     */
    private Date processTime;

    /**
     * 失败原因
     */
    private String failReason;

    /**
     * 原始回调报文
     */
    private String rawBody;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;
}