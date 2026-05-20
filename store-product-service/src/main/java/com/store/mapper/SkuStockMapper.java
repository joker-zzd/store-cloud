package com.store.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.store.domain.SkuStock;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

/**
 * SKU库存Mapper
 */
public interface SkuStockMapper extends BaseMapper<SkuStock> {

    /**
     * 扣减SKU库存。
     * where stock >= quantity 用于防止并发下单导致库存被扣成负数。
     */
    @Update("update pms_sku_stock "
            + "set stock = stock - #{quantity}, sale = sale + #{quantity} "
            + "where id = #{skuId} and stock >= #{quantity}")
    int deductStock(@Param("skuId") Long skuId, @Param("quantity") Integer quantity);
}
