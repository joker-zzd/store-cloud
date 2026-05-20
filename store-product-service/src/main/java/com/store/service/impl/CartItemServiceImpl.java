package com.store.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.store.domain.CartItem;
import com.store.service.CartItemService;
import com.store.mapper.CartItemMapper;
import org.springframework.stereotype.Service;

/**
* @author 19256
* @description 针对表【oms_cart_item(购物车表)】的数据库操作Service实现
* @createDate 2026-05-06 22:44:43
*/
@Service
public class CartItemServiceImpl extends ServiceImpl<CartItemMapper, CartItem>
    implements CartItemService{

}




