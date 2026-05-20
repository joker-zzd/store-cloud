package com.store.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("sys_file_info")
public class SysFileInfo {

    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField("business_type")
    private String businessType;

    @TableField("original_name")
    private String originalName;

    @TableField("object_key")
    private String objectKey;

    @TableField("bucket_name")
    private String bucketName;

    @TableField("file_ext")
    private String fileExt;

    @TableField("content_type")
    private String contentType;

    @TableField("file_size")
    private Long fileSize;

    @TableField("file_hash")
    private String fileHash;

    private String etag;

    @TableField("storage_type")
    private String storageType;

    @TableField("uploader_id")
    private Long uploaderId;

    @TableField("uploader_name")
    private String uploaderName;

    private Integer deleted;

    @TableField("create_time")
    private Date createTime;

    @TableField("update_time")
    private Date updateTime;
}
