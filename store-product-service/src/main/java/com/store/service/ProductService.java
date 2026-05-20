package com.store.service;

import com.store.common.page.PageResult;
import com.store.domain.Product;
import com.baomidou.mybatisplus.extension.service.IService;
import com.store.domain.dto.ProductPageQueryDTO;
import com.store.domain.vo.ProductDetailVO;
import com.store.domain.vo.ProductPageVO;

/**
 * @author 19256
 * @description 针对表【pms_product(商品SPU表)】的数据库操作Service
 * @createDate 2026-04-11 22:10:13
 */
public interface ProductService extends IService<Product> {
    /**
     * 分页查询商品
     */
    PageResult<ProductPageVO> findByPage(ProductPageQueryDTO queryDTO);

    /**
     * 查询商品详情
     */
    ProductDetailVO getProductDetail(Long id);

}
