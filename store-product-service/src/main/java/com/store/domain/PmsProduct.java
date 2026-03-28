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
public class PmsProduct {
    /**
     * 商品ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 品牌ID
     */
    @TableField(value = "brand_id")
    private Long brandId;

    /**
     * 分类ID
     */
    @TableField(value = "category_id")
    private Long categoryId;

    /**
     * 商品名称
     */
    @TableField(value = "name")
    private String name;

    /**
     * 商品主图
     */
    @TableField(value = "pic")
    private String pic;

    /**
     * 商品编号
     */
    @TableField(value = "product_sn")
    private String productSn;

    /**
     * 上架状态:0=下架,1=上架
     */
    @TableField(value = "publish_status")
    private Integer publishStatus;

    /**
     * 新品状态
     */
    @TableField(value = "new_status")
    private Integer newStatus;

    /**
     * 推荐状态
     */
    @TableField(value = "recommend_status")
    private Integer recommendStatus;

    /**
     * 排序
     */
    @TableField(value = "sort")
    private Integer sort;

    /**
     * 价格
     */
    @TableField(value = "price")
    private BigDecimal price;

    /**
     * 商品详情
     */
    @TableField(value = "description")
    private String description;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField(value = "update_time")
    private Date updateTime;
}