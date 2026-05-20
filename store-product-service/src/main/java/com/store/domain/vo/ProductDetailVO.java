package com.store.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Schema(description = "商品详情")
public class ProductDetailVO {

    @Schema(description = "商品ID")
    private Long id;

    @Schema(description = "品牌ID")
    private Long brandId;

    @Schema(description = "分类ID")
    private Long categoryId;

    @Schema(description = "商品名称")
    private String name;

    @Schema(description = "商品主图")
    private String pic;

    @Schema(description = "商品编号")
    private String productSn;

    @Schema(description = "上架状态：0=下架，1=上架")
    private Integer publishStatus;

    @Schema(description = "新品状态")
    private Integer newStatus;

    @Schema(description = "推荐状态")
    private Integer recommandStatus;

    @Schema(description = "价格")
    private BigDecimal price;

    @Schema(description = "商品描述")
    private String description;

    @Schema(description = "创建时间")
    private Date createTime;
}

