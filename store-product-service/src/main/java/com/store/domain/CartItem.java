package com.store.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 购物车表
 */
@TableName("oms_cart_item")
@Data
public class CartItem {

    /**
     * 购物车项ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 商品ID
     */
    private Long productId;

    /**
     * SKU ID
     */
    private Long productSkuId;

    /**
     * 会员ID
     */
    private Long memberId;

    /**
     * 购买数量
     */
    private Integer quantity;

    /**
     * 加入购物车时的商品价格
     */
    private BigDecimal price;

    /**
     * 商品名称
     */
    private String productName;

    /**
     * 规格信息(JSON)
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
