package com.store.domain.dto;

import com.store.common.page.PageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "商品分页查询参数")
public class ProductPageQueryDTO extends PageQuery {
    @Schema(description = "商品名称")
    private String name;

    @Schema(description = "品牌id")
    private Long brandId;

    @Schema(description = "分类ID")
    private Long categoryId;

    @Schema(description = "上架状态：0=下架，1=上架")
    private Integer publishStatus;
}
