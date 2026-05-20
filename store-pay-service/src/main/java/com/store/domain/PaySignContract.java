package com.store.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;

/**
 * 自动续费签约协议表
 *
 * @TableName idnice_pay_sign_contract
 */
@TableName(value = "idnice_pay_sign_contract")
@Data
public class PaySignContract {
    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 用户ID（关联eb_user）
     */
    private Long userId;

    /**
     * 会员信息ID（关联idnice_user_member_info）
     */
    private Integer memberInfoId;

    /**
     * 会员等级ID（关联idnice_member_level，记录签约套餐）
     */
    private Integer levelId;

    /**
     * 签约渠道：1微信 2支付宝 3苹果
     */
    private Integer payChannel;

    /**
     * 系统内部签约协议号/微信contract_code
     */
    private String contractNo;

    /**
     * 第三方协议号：微信contract_id/支付宝agreement_no/苹果original_transaction_id
     */
    private String thirdPartyContractNo;

    /**
     * 第三方用户标识，如支付宝user_id/微信openid等
     */
    private String externalUserId;

    /**
     * 协议状态：0待签约 1已签约 2签约失败 3已解约 4暂停
     */
    private Integer contractStatus;

    /**
     * 自动续费状态：0关闭 1开启
     */
    private Integer autoRenewStatus;

    /**
     * 周期类型：1天 2月 3年
     */
    private Integer periodType;

    /**
     * 周期值
     */
    private Integer periodValue;

    /**
     * 客户端平台：1Android 2iOS 3H5 4小程序
     */
    private Integer clientPlatform;

    /**
     * 首次扣款时间
     */
    private Date firstDeductTime;

    /**
     * 下一次计划扣款时间
     */
    private Date nextDeductTime;

    /**
     * 最近一次扣款时间
     */
    private Date lastDeductTime;

    /**
     * 协议约定金额
     */
    private BigDecimal amount;

    /**
     * 签约成功时间
     */
    private Date signTime;

    /**
     * 解约时间
     */
    private Date unsignTime;

    /**
     * 解约原因
     */
    private String unsignReason;

    /**
     * 渠道签约响应报文
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
     * 微信自动续费模板ID/plan_id
     */
    private String wechatPlanId;

    /**
     * 微信预签约ID
     */
    private String preEntrustwebId;

    /**
     * 预签约本地建议过期时间
     */
    private Date preEntrustExpireTime;

    /**
     * 微信预签约请求序列号
     */
    private String requestSerial;
}