package com.store.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("sys_file_info")
public class SysFileInfo {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String businessType;

    private String originalName;

    private String objectKey;

    private String bucketName;

    private String fileExt;

    private String contentType;

    private Long fileSize;

    private String fileHash;

    private String etag;

    private String storageType;

    private Long uploaderId;

    private String uploaderName;

    private Integer deleted;

    private Date createTime;

    private Date updateTime;
}
