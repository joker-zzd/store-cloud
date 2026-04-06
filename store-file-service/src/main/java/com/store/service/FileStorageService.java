package com.store.service;

public interface FileStorageService {

    StoredFile upload(UploadCommand command);

    String generateSignedGetUrl(String objectKey, long expireSeconds);

    void delete(String objectKey);

    record UploadCommand(
            byte[] bytes,
            String originalFilename,
            String contentType,
            String businessType
    ) {
    }

    record StoredFile(
            String objectKey,
            String fileExt,
            long fileSize,
            String fileHash,
            String eTag,
            String storageType,
            String bucketName
    ) {
    }
}
