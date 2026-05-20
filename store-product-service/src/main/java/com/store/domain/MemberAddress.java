package com.store.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * 会员收货地址表
 */
@TableName("member_address")
@Data
public class MemberAddress {

    /**
     * 收货地址ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 会员ID
     */
    private Long memberId;

    /**
     * 收货人姓名
     */
    private String name;

    /**
     * 联系电话
     */
    private String phone;

    /**
     * 邮政编码
     */
    private String postCode;

    /**
     * 省份
     */
    private String province;

    /**
     * 城市
     */
    private String city;

    /**
     * 区域
     */
    private String region;

    /**
     * 详细地址
     */
    private String detailAddress;

    /**
     * 是否默认地址：0=否，1=是
     */
    private Integer defaultStatus;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;
}
