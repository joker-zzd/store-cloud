package com.store.domain.dto;

import lombok.Data;

/**
 * Android APP 纯签约预签约请求。
 */
@Data
public class WechatAppPreSignDTO {
    /** 会员套餐ID */
    private Integer levelId;

    /**
     * 签约页面展示账号。
     * 例如：手机号尾号、用户ID、会员账号。
     * 注意：微信文档要求不要传微信昵称，不要传表情等特殊字符。
     */
    private String contractDisplayAccount;

    /** 备注，可选 */
    private String remark;
}
