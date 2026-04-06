package com.store.domain.vo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FileUploadVO {

    private Long fileId;

    private String originalName;

    private String objectKey;

    private String contentType;

    private Long fileSize;

    private String fileHash;

    private String businessType;

    private String storageType;
}
