package com.store.domain.vo;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class FileInfoVO {

    private Long fileId;

    private String originalName;

    private String objectKey;

    private String bucketName;

    private String fileExt;

    private String contentType;

    private Long fileSize;

    private String fileHash;

    private String etag;

    private String businessType;

    private String storageType;

    private Long uploaderId;

    private String uploaderName;

    private Date createTime;
}
