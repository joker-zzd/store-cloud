package com.store.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.store.common.auth.UserContext;
import com.store.common.auth.dto.CurrentUserInfo;
import com.store.common.exception.BusinessException;
import com.store.common.resultvo.ResultVO;
import com.store.config.AliyunOssProperties;
import com.store.domain.SysFileInfo;
import com.store.domain.vo.FileInfoVO;
import com.store.domain.vo.FileSignedUrlVO;
import com.store.domain.vo.FileUploadVO;
import com.store.mapper.SysFileInfoMapper;
import com.store.service.FileService;
import com.store.service.FileStorageService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.Locale;

@Service
public class FileServiceImpl extends ServiceImpl<SysFileInfoMapper, SysFileInfo>
        implements FileService {

    private final SysFileInfoMapper sysFileInfoMapper;
    private final FileStorageService fileStorageService;
    private final AliyunOssProperties properties;
    private final UserContext userContext;

    public FileServiceImpl(SysFileInfoMapper sysFileInfoMapper,
                           FileStorageService fileStorageService,
                           AliyunOssProperties properties,
                           UserContext userContext) {
        this.sysFileInfoMapper = sysFileInfoMapper;
        this.fileStorageService = fileStorageService;
        this.properties = properties;
        this.userContext = userContext;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultVO<FileUploadVO> upload(MultipartFile file, String businessType) {
        validateBeforeUpload(file, businessType);

        CurrentUserInfo currentUser = userContext.getCurrentUser();
        byte[] bytes = readBytes(file);
        FileStorageService.StoredFile storedFile = null;

        try {
            storedFile = fileStorageService.upload(new FileStorageService.UploadCommand(
                    bytes,
                    extractFileName(file.getOriginalFilename()),
                    file.getContentType(),
                    businessType
            ));

            Date now = new Date();
            SysFileInfo sysFileInfo = new SysFileInfo();
            sysFileInfo.setBusinessType(normalizeBusinessType(businessType));
            sysFileInfo.setOriginalName(extractFileName(file.getOriginalFilename()));
            sysFileInfo.setObjectKey(storedFile.objectKey());
            sysFileInfo.setBucketName(storedFile.bucketName());
            sysFileInfo.setFileExt(storedFile.fileExt());
            sysFileInfo.setContentType(file.getContentType());
            sysFileInfo.setFileSize(storedFile.fileSize());
            sysFileInfo.setFileHash(storedFile.fileHash());
            sysFileInfo.setEtag(storedFile.eTag());
            sysFileInfo.setStorageType(storedFile.storageType());
            sysFileInfo.setUploaderId(currentUser.userId());
            sysFileInfo.setUploaderName(currentUser.username());
            sysFileInfo.setDeleted(0);
            sysFileInfo.setCreateTime(now);
            sysFileInfo.setUpdateTime(now);

            sysFileInfoMapper.insert(sysFileInfo);

            return ResultVO.success("Upload success", FileUploadVO.builder()
                    .fileId(sysFileInfo.getId())
                    .originalName(sysFileInfo.getOriginalName())
                    .objectKey(sysFileInfo.getObjectKey())
                    .contentType(sysFileInfo.getContentType())
                    .fileSize(sysFileInfo.getFileSize())
                    .fileHash(sysFileInfo.getFileHash())
                    .businessType(sysFileInfo.getBusinessType())
                    .storageType(sysFileInfo.getStorageType())
                    .build());
        } catch (BusinessException exception) {
            if (storedFile != null) {
                fileStorageService.delete(storedFile.objectKey());
            }
            throw exception;
        } catch (Exception exception) {
            if (storedFile != null) {
                fileStorageService.delete(storedFile.objectKey());
            }
            throw new BusinessException("File upload failed, please retry later", exception);
        }
    }

    @Override
    public ResultVO<FileInfoVO> getFileInfo(Long fileId) {
        SysFileInfo sysFileInfo = getExistingFile(fileId);
        return ResultVO.success(FileInfoVO.builder()
                .fileId(sysFileInfo.getId())
                .originalName(sysFileInfo.getOriginalName())
                .objectKey(sysFileInfo.getObjectKey())
                .bucketName(sysFileInfo.getBucketName())
                .fileExt(sysFileInfo.getFileExt())
                .contentType(sysFileInfo.getContentType())
                .fileSize(sysFileInfo.getFileSize())
                .fileHash(sysFileInfo.getFileHash())
                .etag(sysFileInfo.getEtag())
                .businessType(sysFileInfo.getBusinessType())
                .storageType(sysFileInfo.getStorageType())
                .uploaderId(sysFileInfo.getUploaderId())
                .uploaderName(sysFileInfo.getUploaderName())
                .createTime(sysFileInfo.getCreateTime())
                .build());
    }

    @Override
    public ResultVO<FileSignedUrlVO> getSignedUrl(Long fileId, Long expireSeconds) {
        SysFileInfo sysFileInfo = getExistingFile(fileId);
        long finalExpireSeconds = expireSeconds == null || expireSeconds <= 0
                ? properties.getSignedUrlExpireSeconds()
                : expireSeconds;

        String signedUrl = fileStorageService.generateSignedGetUrl(
                sysFileInfo.getObjectKey(),
                finalExpireSeconds
        );

        return ResultVO.success(FileSignedUrlVO.builder()
                .fileId(sysFileInfo.getId())
                .objectKey(sysFileInfo.getObjectKey())
                .signedUrl(signedUrl)
                .expireSeconds(finalExpireSeconds)
                .build());
    }

    private void validateBeforeUpload(MultipartFile file, String businessType) {
        if (file == null || file.isEmpty()) {
            throw new BusinessException("Uploaded file must not be empty");
        }
        if (!StringUtils.hasText(businessType)) {
            throw new BusinessException("businessType must not be blank");
        }
        if (file.getSize() > properties.getMaxSize()) {
            throw new BusinessException("Uploaded file exceeds the configured size limit");
        }

        String fileName = extractFileName(file.getOriginalFilename());
        String ext = getExtension(fileName).toLowerCase(Locale.ROOT);
        if (!properties.getAllowedExtensions().isEmpty()
                && !properties.getAllowedExtensions().contains(ext)) {
            throw new BusinessException("Unsupported file extension: " + ext);
        }

        String contentType = file.getContentType();
        if (!StringUtils.hasText(contentType)) {
            throw new BusinessException("Unable to detect file content type");
        }
        if (!properties.getAllowedContentTypes().isEmpty()
                && !properties.getAllowedContentTypes().contains(contentType)) {
            throw new BusinessException("Unsupported file content type: " + contentType);
        }
    }

    private SysFileInfo getExistingFile(Long fileId) {
        SysFileInfo sysFileInfo = sysFileInfoMapper.selectOne(
                Wrappers.<SysFileInfo>lambdaQuery()
                        .eq(SysFileInfo::getId, fileId)
                        .eq(SysFileInfo::getDeleted, 0)
                        .last("limit 1")
        );
        if (sysFileInfo == null) {
            throw new BusinessException("File does not exist");
        }
        return sysFileInfo;
    }

    private String extractFileName(String originalFilename) {
        if (!StringUtils.hasText(originalFilename)) {
            return "unknown";
        }
        String normalized = originalFilename.replace("\\", "/");
        return normalized.substring(normalized.lastIndexOf('/') + 1);
    }

    private String normalizeBusinessType(String businessType) {
        return businessType.trim().replaceAll("[^a-zA-Z0-9_-]", "_");
    }

    private String getExtension(String fileName) {
        int index = fileName.lastIndexOf('.');
        if (index < 0 || index == fileName.length() - 1) {
            return "";
        }
        return fileName.substring(index + 1);
    }

    private byte[] readBytes(MultipartFile file) {
        try {
            return file.getBytes();
        } catch (IOException exception) {
            throw new BusinessException("Failed to read uploaded file", exception);
        }
    }
}
