package com.store.controlller;

import com.store.common.resultvo.ResultVO;
import com.store.domain.dto.OrderSubmitDTO;
import com.store.domain.vo.OrderSubmitVO;
import com.store.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/order")
@Tag(name = "订单管理", description = "订单模块接口")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/submit")
    @Operation(summary = "订单提交")
    public ResultVO<OrderSubmitVO> submit(@RequestBody OrderSubmitDTO orderSubmitDTO) {
        return ResultVO.success(orderService.submit(orderSubmitDTO));
    }
}
