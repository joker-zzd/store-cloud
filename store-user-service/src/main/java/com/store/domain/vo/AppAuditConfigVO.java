package com.store.domain.vo;

import lombok.Data;

@Data
public class AppAuditConfigVO {
    /**
     * 是否审核模式：true审核模式，false正常模式
     */
    private Boolean auditMode;
}
