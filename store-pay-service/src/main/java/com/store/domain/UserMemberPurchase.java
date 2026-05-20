package com.store.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

/**
 * 会员购买明细表（关联会员等级表，记录会员购买行为）
 * @TableName idnice_user_member_purchase
 */
@TableName(value ="idnice_user_member_purchase")
@Data
public class UserMemberPurchase {
    /**
     * 会员购买记录ID（自增主键）
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 关联用户ID（对应用户表id）
     */
    private Integer userId;

    /**
     * 会员购买单号（唯一，如：MEMBER20251209001）
     */
    private String purchaseNo;

    /**
     * 关联会员等级ID（对应idnice_member_level.id，标记购买的会员等级）
     */
    private Integer levelId;

    /**
     * 购买的会员月份数（同步idnice_member_level.recharge_months）
     */
    private Integer rechargeMonths;

    /**
     * 会员套餐原价（同步idnice_member_level.original_price）
     */
    private BigDecimal originalPrice;

    /**
     * 实际支付金额（元，即会员套餐现价，同步idnice_user_level.current_price）
     */
    private BigDecimal actualPayAmount;

    /**
     * 购买该会员赠送的积分（同步idnice_member_level.give_integral）
     */
    private Integer giveIntegral;

    /**
     * 赠送积分有效期（天数，同步idnice_member_level.integral_validity_days，0=永久）
     */
    private Integer integralValidityDays;

    /**
     * 支付渠道：public-公众号,mini-小程序，h5-网页支付,wechatIos-微信Ios，wechatAndroid-微信Android,alipay-支付包，alipayApp-支付宝App
     */
    private String payChannel;

    /**
     * 支付方式:weixin,alipay
     */
    private String payType;

    /**
     * 支付状态（success=支付成功，failed=支付失败，pending=待支付）
     */
    private String payStatus;

    /**
     * 实际支付时间（状态为success时必填）
     */
    private Date payTime;

    /**
     * 支付服务方订单号
     */
    private String outTradeNo;

    /**
     * 会员生效时间（支付成功后自动生成，如支付当天）
     */
    private Date memberEffectiveTime;

    /**
     * 会员失效时间（生效时间+购买月份数，如生效日+3个月）
     */
    private Date memberExpireTime;

    /**
     * 会员状态（unused=未生效，effective=生效中，expired=已过期，canceled=已取消）
     */
    private String memberStatus;

    /**
     * 折扣金额
     */
    private BigDecimal discountAmount;

    /**
     * 折扣金额描述
     */
    private String discountAmountDesc;

    /**
     * 发票申请状态：0待开票 1已开票 2已驳回
     */
    private Integer invoiceStatus;

    /**
     * 购买备注
     */
    private String remark;

    /**
     * 记录创建时间
     */
    private Date createTime;

    /**
     * 记录更新时间
     */
    private Date updateTime;
}