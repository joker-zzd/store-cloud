package com.store.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.store.common.exception.BusinessException;
import com.store.common.page.PageResult;
import com.store.domain.Product;
import com.store.domain.dto.ProductPageQueryDTO;
import com.store.domain.vo.ProductDetailVO;
import com.store.domain.vo.ProductPageVO;
import com.store.redis.DetailRedisConstants;
import com.store.service.ProductService;
import com.store.mapper.ProductMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * @author 19256
 * @description 针对表【pms_product(商品SPU�?】的数据库操作Service实现
 * @createDate 2026-04-11 22:10:13
 */
@Service
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product>
        implements ProductService {
    private final ProductMapper productMapper;
    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;

    public ProductServiceImpl(ProductMapper productMapper,
                              StringRedisTemplate redisTemplate,
                              ObjectMapper objectMapper) {
        this.productMapper = productMapper;
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
    }

    @Override
    public PageResult<ProductPageVO> findByPage(ProductPageQueryDTO queryDTO) {
        Page<Product> page = new Page<>(queryDTO.getPageNum(), queryDTO.getPageSize());

        LambdaQueryWrapper<Product> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.hasText(queryDTO.getName()), Product::getName, queryDTO.getName())
                .eq(queryDTO.getBrandId() != null, Product::getBrandId, queryDTO.getBrandId())
                .eq(queryDTO.getCategoryId() != null, Product::getCategoryId, queryDTO.getCategoryId())
                .eq(queryDTO.getPublishStatus() != null, Product::getPublishStatus, queryDTO.getPublishStatus())
                .orderByDesc(Product::getCreateTime);

        Page<Product> productPage = productMapper.selectPage(page, wrapper);
        List<ProductPageVO> voList = productPage.getRecords().stream().map(product -> {
            ProductPageVO productPageVO = new ProductPageVO();
            BeanUtils.copyProperties(product, productPageVO);
            return productPageVO;
        }).toList();
        Page<ProductPageVO> voPage = new Page<>(productPage.getCurrent(), productPage.getSize(), productPage.getTotal());
        voPage.setRecords(voList);

        return PageResult.of(voPage);
    }

    @Override
    public ProductDetailVO getProductDetail(Long id) {
        if (id == null || id <= 0) {
            throw new BusinessException("商品ID不能为空");
        }
        String cacheKey = DetailRedisConstants.PRODUCT_DETAIL_KEY_PREFIX + id;
        String cacheValue = redisTemplate.opsForValue().get(cacheKey);
        if (StringUtils.hasText(cacheValue)) {
            if (DetailRedisConstants.NULL_VALUE.equals(cacheValue)) {
                return null;
            }
            return readValue(cacheValue);
        }
        Product product = productMapper.selectById(id);
        if (product == null) {
            redisTemplate.opsForValue().set(
                    cacheKey,
                    DetailRedisConstants.NULL_VALUE,
                    DetailRedisConstants.PRODUCT_NULL_TTL
            );
            return null;
        }
        ProductDetailVO detailVO = new ProductDetailVO();
        BeanUtils.copyProperties(product, detailVO);
        redisTemplate.opsForValue().set(
                cacheKey,
                writeValue(detailVO),
                DetailRedisConstants.PRODUCT_DETAIL_TTL
        );
        return detailVO;
    }

    private String writeValue(ProductDetailVO detailVO) {
        try {
            return objectMapper.writeValueAsString(detailVO);
        } catch (JsonProcessingException e) {
            throw new BusinessException("商品详情序列化失败?", e);
        }
    }

    private ProductDetailVO readValue(String json) {
        try {
            return objectMapper.readValue(json, ProductDetailVO.class);
        } catch (JsonProcessingException e) {
            throw new BusinessException("商品详情反序列化失败", e);
        }
    }
}





