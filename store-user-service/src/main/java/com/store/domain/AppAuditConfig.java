package com.store.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * APP应用商店审核配置表
 * @TableName app_audit_config
 */
@TableName(value ="app_audit_config")
@Data
public class AppAuditConfig {
    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 应用类型：ios/android
     */
    private String appType;

    /**
     *  应用渠道：ios：appstore；android：xiaomi、huawei、honor、oppo、vivo、yyb、ali、360',
     */
    private String channel;

    /**
     * 版本号，例如：1.0.3
     */
    private String version;

    /**
     * 是否审核模式：0否，1是
     */
    private Integer auditMode;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;
}