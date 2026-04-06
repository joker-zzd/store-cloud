package com.store.service.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectRequest;
import com.aliyun.oss.model.PutObjectResult;
import com.store.common.exception.BusinessException;
import com.store.config.AliyunOssProperties;
import com.store.service.FileStorageService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.ByteArrayInputStream;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HexFormat;
import java.util.Locale;
import java.util.UUID;

@Service
public class AliyunOssStorageService implements FileStorageService {

    private final OSS ossClient;
    private final AliyunOssProperties ossProperties;

    public AliyunOssStorageService(OSS ossClient, AliyunOssProperties ossProperties) {
        this.ossClient = ossClient;
        this.ossProperties = ossProperties;
    }

    @Override
    public StoredFile upload(UploadCommand command) {
        String businessType = sanitizeBusinessType(command.businessType());
        String fileExt = getExtension(command.originalFilename());
        String objectKey = buildObjectKey(businessType, fileExt);

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(command.bytes().length);
        metadata.setContentType(command.contentType());
        metadata.addUserMetadata("originalName", command.originalFilename());

        PutObjectRequest request = new PutObjectRequest(
                ossProperties.getBucketName(),
                objectKey,
                new ByteArrayInputStream(command.bytes()),
                metadata
        );

        try {
            PutObjectResult result = ossClient.putObject(request);
            return new StoredFile(
                    objectKey,
                    fileExt,
                    command.bytes().length,
                    sha256Hex(command.bytes()),
                    result.getETag(),
                    "ALIYUN_OSS",
                    ossProperties.getBucketName()
            );
        } catch (Exception exception) {
            throw new BusinessException("Failed to upload file to OSS", exception);
        }
    }

    @Override
    public String generateSignedGetUrl(String objectKey, long expireSeconds) {
        try {
            long safeExpireSeconds = expireSeconds > 0 ? expireSeconds : ossProperties.getSignedUrlExpireSeconds();
            Date expiration = new Date(System.currentTimeMillis() + safeExpireSeconds * 1000L);
            URL url = ossClient.generatePresignedUrl(ossProperties.getBucketName(), objectKey, expiration);
            return url.toString();
        } catch (Exception exception) {
            throw new BusinessException("Failed to generate signed OSS URL", exception);
        }
    }

    @Override
    public void delete(String objectKey) {
        try {
            if (StringUtils.hasText(objectKey)) {
                ossClient.deleteObject(ossProperties.getBucketName(), objectKey);
            }
        } catch (Exception ignored) {
        }
    }

    private String buildObjectKey(String businessType, String fileExt) {
        String datePath = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        String fileName = UUID.randomUUID().toString().replace("-", "");
        if (StringUtils.hasText(fileExt)) {
            fileName = fileName + "." + fileExt;
        }
        String prefix = trimSlashes(ossProperties.getKeyPrefix());
        return prefix + "/" + businessType + "/" + datePath + "/" + fileName;
    }

    private String sanitizeBusinessType(String businessType) {
        String value = StringUtils.hasText(businessType) ? businessType.trim() : "common";
        value = value.replaceAll("[^a-zA-Z0-9_-]", "_");
        return StringUtils.hasText(value) ? value : "common";
    }

    private String trimSlashes(String value) {
        String result = value == null ? "" : value.trim();
        while (result.startsWith("/")) {
            result = result.substring(1);
        }
        while (result.endsWith("/")) {
            result = result.substring(0, result.length() - 1);
        }
        return result;
    }

    private String getExtension(String filename) {
        if (!StringUtils.hasText(filename)) {
            return "";
        }
        int index = filename.lastIndexOf('.');
        if (index < 0 || index == filename.length() - 1) {
            return "";
        }
        return filename.substring(index + 1).toLowerCase(Locale.ROOT);
    }

    private String sha256Hex(byte[] bytes) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            return HexFormat.of().formatHex(digest.digest(bytes));
        } catch (NoSuchAlgorithmException exception) {
            throw new BusinessException("Failed to calculate file hash", exception);
        }
    }
}
