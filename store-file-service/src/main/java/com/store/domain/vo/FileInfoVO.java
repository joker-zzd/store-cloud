package com.store.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
@Schema(description = "文件详情信息")
public class FileInfoVO {

    @Schema(description = "文件ID")
    private Long fileId;

    @Schema(description = "原始文件名")
    private String originalName;

    @Schema(description = "OSS对象键")
    private String objectKey;

    @Schema(description = "Bucket名称")
    private String bucketName;

    @Schema(description = "文件扩展名")
    private String fileExt;

    @Schema(description = "文件内容类型")
    private String contentType;

    @Schema(description = "文件大小，单位字节")
    private Long fileSize;

    @Schema(description = "文件哈希值")
    private String fileHash;

    @Schema(description = "OSS返回的ETag")
    private String etag;

    @Schema(description = "业务类型")
    private String businessType;

    @Schema(description = "存储类型")
    private String storageType;

    @Schema(description = "上传人ID")
    private Long uploaderId;

    @Schema(description = "上传人用户名")
    private String uploaderName;

    @Schema(description = "创建时间")
    private Date createTime;
}
