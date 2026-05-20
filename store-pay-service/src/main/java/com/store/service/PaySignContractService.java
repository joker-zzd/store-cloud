package com.store.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.store.domain.PaySignContract;
import com.store.domain.dto.WechatContractOrderDTO;
import com.store.domain.vo.WechatContractOrderVO;

/**
 * 自动续费签约协议 Service。
 */
public interface PaySignContractService extends IService<PaySignContract> {

    /**
     * 创建微信支付中签约订单。
     *
     * @param dto 支付中签约请求参数
     * @return APP 调起微信支付所需参数
     */
    WechatContractOrderVO contractOrder(WechatContractOrderDTO dto);
}
