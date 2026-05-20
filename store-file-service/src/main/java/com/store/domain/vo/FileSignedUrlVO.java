package com.store.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(description = "文件签名访问地址")
public class FileSignedUrlVO {

    @Schema(description = "文件ID")
    private Long fileId;

    @Schema(description = "OSS对象键")
    private String objectKey;

    @Schema(description = "带签名的访问地址")
    private String signedUrl;

    @Schema(description = "签名有效期，单位秒")
    private Long expireSeconds;
}
