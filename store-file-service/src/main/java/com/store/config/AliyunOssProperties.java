package com.store.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@Data
@ConfigurationProperties(prefix = "store.oss")
public class AliyunOssProperties {

    /**
     * OSS Endpoint，例如 https://oss-cn-hangzhou.aliyuncs.com
     */
    private String endpoint;

    /**
     * Region，例如 cn-hangzhou。V4 签名模式下必须显式指定。
     */
    private String region;

    /**
     * Bucket 名称。
     */
    private String bucketName;

    /**
     * 对象统一前缀，例如 store-cloud。
     */
    private String keyPrefix = "store-cloud";

    /**
     * 下载签名地址有效期，单位秒。
     */
    private long signedUrlExpireSeconds = 600L;

    /**
     * 单文件最大大小。
     */
    private long maxSize = 10 * 1024 * 1024L;

    /**
     * 扩展名白名单。
     */
    private List<String> allowedExtensions = List.of(
            "jpg", "jpeg", "png", "gif", "webp", "pdf", "doc", "docx", "xls", "xlsx"
    );

    /**
     * MIME 类型白名单。
     */
    private List<String> allowedContentTypes = List.of(
            "image/jpeg", "image/png", "image/gif", "image/webp",
            "application/pdf",
            "application/msword",
            "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
            "application/vnd.ms-excel",
            "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
    );
}
