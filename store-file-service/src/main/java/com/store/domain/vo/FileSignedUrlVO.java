package com.store.domain.vo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FileSignedUrlVO {

    private Long fileId;

    private String objectKey;

    private String signedUrl;

    private Long expireSeconds;
}
