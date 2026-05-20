package com.store.service;

import com.store.domain.PayNotificationCallback;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author 19256
* @description 针对表【idnice_pay_notification_callback(支付回调记录表)】的数据库操作Service
* @createDate 2026-05-17 18:12:56
*/
public interface PayNotificationCallbackService extends IService<PayNotificationCallback> {

    /**
     * 处理微信支付结果回调。
     *
     * @param rawXml 微信原始 XML 报文
     * @return 微信 V2 XML 应答
     */
    String handleWechatPayNotify(String rawXml);

    /**
     * 处理微信签约结果回调。
     *
     * @param rawXml 微信原始 XML 报文
     * @return 微信 V2 XML 应答
     */
    String handleWechatContractNotify(String rawXml);
}
