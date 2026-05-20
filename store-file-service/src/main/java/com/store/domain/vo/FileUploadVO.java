package com.store.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(description = "文件上传结果")
public class FileUploadVO {

    @Schema(description = "文件ID")
    private Long fileId;

    @Schema(description = "原始文件名")
    private String originalName;

    @Schema(description = "OSS对象键")
    private String objectKey;

    @Schema(description = "文件内容类型")
    private String contentType;

    @Schema(description = "文件大小，单位字节")
    private Long fileSize;

    @Schema(description = "文件哈希值")
    private String fileHash;

    @Schema(description = "业务类型")
    private String businessType;

    @Schema(description = "存储类型")
    private String storageType;
}
