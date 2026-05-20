package com.store.controlller;

import com.store.common.page.PageResult;
import com.store.common.resultvo.ResultVO;
import com.store.domain.dto.ProductPageQueryDTO;
import com.store.domain.vo.CouponReceiveVO;
import com.store.domain.vo.ProductDetailVO;
import com.store.domain.vo.ProductPageVO;
import com.store.service.CouponService;
import com.store.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/product")
@Tag(name = "商品管理", description = "商品模块接口")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/list")
    @Operation(summary = "分页查询商品列表")
    public ResultVO<PageResult<ProductPageVO>> page(ProductPageQueryDTO queryDTO) {
        return ResultVO.success(productService.findByPage(queryDTO));
    }

    @GetMapping("/detail/{id}")
    @Operation(summary = "查询商品详情")
    public ResultVO<ProductDetailVO> detail(@PathVariable(name = "id") Long id) {
        return ResultVO.success(productService.getProductDetail(id));
    }
}
