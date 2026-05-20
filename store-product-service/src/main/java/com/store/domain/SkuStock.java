package com.store.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * SKU库存表
 */
@TableName("pms_sku_stock")
@Data
public class SkuStock {

    /**
     * SKU ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 商品ID
     */
    private Long productId;

    /**
     * SKU编码
     */
    private String skuCode;

    /**
     * SKU销售价格
     */
    private BigDecimal price;

    /**
     * 当前库存数量
     */
    private Integer stock;

    /**
     * 库存预警数量
     */
    private Integer lowStock;

    /**
     * 规格数据(JSON)
     */
    private Object spData;

    /**
     * SKU图片
     */
    private String pic;

    /**
     * 销量
     */
    private Integer sale;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;
}
