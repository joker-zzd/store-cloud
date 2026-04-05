package com.store.domain.vo;

import java.time.LocalDateTime;

public class FileInfoVO {
    /**
     * 文件ID
     */
    private Long fileId;

    /**
     * 原始文件名
     */
    private String originalName;

    /**
     * MIME 类型
     */
    private String contentType;

    /**
     * 文件大小，单位字节
     */
    private Long fileSize;

    /**
     * 文件摘要，建议 SHA-256
     */
    private String fileHash;

    /**
     * 扩展名
     */
    private String fileExt;

    /**
     * 业务类型，如 avatar/product/contract
     */
    private String businessType;

    /**
     * 存储类型：LOCAL/MINIO/OSS
     */
    private String storageType;

    /**
     * 是否公开：1公开 0私有
     */
    private Integer publicFlag;

    /**
     * 访问地址
     */
    private String accessUrl;

    /**
     * 上传人 ID
     */
    private Long uploaderId;

    /**
     * 上传人用户名
     */
    private String uploaderName;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}
