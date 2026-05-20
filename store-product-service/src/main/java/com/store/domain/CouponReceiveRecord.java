package com.store.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * 优惠券领取记录表
 * @TableName coupon_receive_record
 */
@TableName(value ="coupon_receive_record")
@Data
public class CouponReceiveRecord {
    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 优惠券ID
     */
    private Long couponId;

    /**
     * 会员ID
     */
    private Long memberId;

    /**
     * 领取时间
     */
    private Date receiveTime;
}