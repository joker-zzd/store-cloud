package com.store.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

/**
 * 商品SPU表
 * @TableName pms_product
 */
@TableName(value ="pms_product")
@Data
public class Product {
    /**
     * 商品ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 品牌ID
     */
    private Long brandId;

    /**
     * 分类ID
     */
    private Long categoryId;

    /**
     * 商品名称
     */
    private String name;

    /**
     * 商品主图
     */
    private String pic;

    /**
     * 商品编号
     */
    private String productSn;

    /**
     * 上架状态:0=下架,1=上架
     */
    private Integer publishStatus;

    /**
     * 新品状态
     */
    private Integer newStatus;

    /**
     * 推荐状态
     */
    private Integer recommendStatus;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 价格
     */
    private BigDecimal price;

    /**
     * 商品详情
     */
    private String description;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;
}