package com.store.controller;

import com.store.common.resultvo.ResultVO;
import com.store.domain.dto.WechatContractOrderDTO;
import com.store.domain.vo.WechatContractOrderVO;
import com.store.service.PayNotificationCallbackService;
import com.store.service.PaySignContractService;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 微信支付相关接口。
 */
@RestController
@RequestMapping("/api/pay/wechat")
public class WechatPayController {

    private final PaySignContractService paySignContractService;
    private final PayNotificationCallbackService payNotificationCallbackService;

    public WechatPayController(PaySignContractService paySignContractService,
                               PayNotificationCallbackService payNotificationCallbackService) {
        this.paySignContractService = paySignContractService;
        this.payNotificationCallbackService = payNotificationCallbackService;
    }

    /**
     * 微信支付中签约。
     *
     * <p>APP 调用该接口后，服务端会创建首期会员支付订单和待签约协议，
     * 再返回微信 OpenSDK 拉起支付所需参数。用户完成首期支付后，微信会分别回调
     * payNotifyUrl 和 contractNotifyUrl。</p>
     */
    @PostMapping("/contract/order")
    public ResultVO<WechatContractOrderVO> contractOrder(@RequestBody @Valid WechatContractOrderDTO dto) {
        return ResultVO.success(paySignContractService.contractOrder(dto));
    }

    /**
     * 微信支付结果回调。
     *
     * <p>该地址对应配置项 wechat.subscription.pay-notify-url。微信会反复通知，
     * 所以内部处理必须保证幂等。</p>
     */
    @PostMapping(value = "/pay/notify",
            consumes = {MediaType.APPLICATION_XML_VALUE, MediaType.TEXT_XML_VALUE},
            produces = MediaType.APPLICATION_XML_VALUE)
    public String payNotify(@RequestBody String rawXml) {
        return payNotificationCallbackService.handleWechatPayNotify(rawXml);
    }

    /**
     * 微信签约结果回调。
     *
     * <p>该地址对应配置项 wechat.subscription.contract-notify-url。只有收到签约成功回调后，
     * 本地协议才会从待签约变成已签约。</p>
     */
    @PostMapping(value = "/contract/notify",
            consumes = {MediaType.APPLICATION_XML_VALUE, MediaType.TEXT_XML_VALUE},
            produces = MediaType.APPLICATION_XML_VALUE)
    public String contractNotify(@RequestBody String rawXml) {
        return payNotificationCallbackService.handleWechatContractNotify(rawXml);
    }
}
