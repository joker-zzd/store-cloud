package com.store.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.util.Date;

import lombok.Data;

/**
 * 文件元数据表
 *
 * @TableName sys_file_info
 */
@TableName(value = "sys_file_info")
@Data
public class SysFileInfo {
    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 业务类型，如 avatar/product/contract
     */
    private String businessType;

    /**
     * 原始文件名
     */
    private String originalName;

    /**
     * 存储文件名
     */
    private String storageName;

    /**
     * 相对路径
     */
    private String relativePath;

    /**
     * 访问地址
     */
    private String accessUrl;

    /**
     * 扩展名
     */
    private String fileExt;

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
     * 存储类型：LOCAL/MINIO/OSS
     */
    private String storageType;

    /**
     * 上传人 ID
     */
    private Long uploaderId;

    /**
     * 上传人用户名
     */
    private String uploaderName;

    /**
     * 是否公开：1公开 0私有
     */
    private Integer publicFlag;

    /**
     * 逻辑删除标记
     */
    private Integer deleted;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;
}