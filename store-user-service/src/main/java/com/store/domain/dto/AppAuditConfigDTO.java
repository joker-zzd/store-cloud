package com.store.domain.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AppAuditConfigDTO {
    /**
     * 应用类型：ios/android
     */
    @NotBlank(message = "appType不能为空")
    private String appType;

    /**
     *  应用渠道：ios：appstore；android：xiaomi、huawei、honor、oppo、vivo、yyb、ali、360',
     */
    @NotBlank(message = "channel不能为空")
    private String channel;

    /**
     * 版本号，例如：1.0.3
     */
    @NotBlank(message = "version不能为空")
    private String version;
}
