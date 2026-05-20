package com.store.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 微信支付中签约请求。
 */
@Data
public class WechatContractOrderDTO {

    /**
     * 用户选择的会员套餐 ID。
     */
    @NotNull(message = "会员套餐ID不能为空")
    private Integer levelId;

    /**
     * 客户端 IP，对应微信 spbill_create_ip。
     *
     * <p>可不传；服务端会优先从 X-Forwarded-For、X-Real-IP 等请求头解析。</p>
     */
    private String clientIp;

    /**
     * 签约页展示账号。
     *
     * <p>建议传手机号尾号、用户 ID、会员账号等，不要传微信昵称、表情等特殊字符。</p>
     */
    @NotBlank(message = "签约展示账号不能为空")
    private String contractDisplayAccount;

    /**
     * 商品描述；为空时服务端按会员套餐名称生成。
     */
    private String body;

    /**
     * 商品详情，可为空。
     */
    private String detail;

    /**
     * 附加数据，可为空，微信支付回调会原样返回。
     */
    private String attach;

    /**
     * 本地业务备注，只落库，不传给微信。
     */
    private String remark;
}
