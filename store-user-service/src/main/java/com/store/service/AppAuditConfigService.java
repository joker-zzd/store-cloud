package com.store.service;

import com.store.domain.AppAuditConfig;
import com.baomidou.mybatisplus.extension.service.IService;
import com.store.domain.dto.AppAuditConfigDTO;
import com.store.domain.vo.AppAuditConfigVO;

/**
* @author 19256
* @description 针对表【app_audit_config(APP应用商店审核配置表)】的数据库操作Service
* @createDate 2026-05-04 22:29:14
*/
public interface AppAuditConfigService extends IService<AppAuditConfig> {

    AppAuditConfigVO getAppAuditConfig( AppAuditConfigDTO appAuditConfigDTO);
}
