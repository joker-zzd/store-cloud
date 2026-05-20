package com.store.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 订单明细表
 */
@TableName("oms_order_item")
@Data
public class OrderItem {

    /**
     * 订单项ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 订单ID
     */
    private Long orderId;

    /**
     * 订单编号
     */
    private String orderSn;

    /**
     * 商品ID
     */
    private Long productId;

    /**
     * SKU ID
     */
    private Long productSkuId;

    /**
     * 商品名称快照
     */
    private String productName;

    /**
     * 下单时商品单价快照
     */
    private BigDecimal productPrice;

    /**
     * 购买数量
     */
    private Integer productQuantity;

    /**
     * 规格信息快照(JSON)
     */
    private Object spData;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;
}
