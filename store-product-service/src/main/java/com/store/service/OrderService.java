package com.store.service;

import com.store.domain.Order;
import com.baomidou.mybatisplus.extension.service.IService;
import com.store.domain.dto.OrderSubmitDTO;
import com.store.domain.vo.OrderSubmitVO;

/**
* @author 19256
* @description 针对表【oms_order(订单表)】的数据库操作Service
* @createDate 2026-05-06 22:37:27
*/
public interface OrderService extends IService<Order> {

    /**
     * 提交订单
     */
    OrderSubmitVO submit(OrderSubmitDTO orderSubmitDTO);
}
