package com.store.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.store.domain.PmsProduct;
import com.store.service.PmsProductService;
import com.store.mapper.PmsProductMapper;
import org.springframework.stereotype.Service;

/**
* @author 19256
* @description 针对表【pms_product(商品SPU表)】的数据库操作Service实现
* @createDate 2026-03-23 23:33:59
*/
@Service
public class PmsProductServiceImpl extends ServiceImpl<PmsProductMapper, PmsProduct>
    implements PmsProductService{

}




