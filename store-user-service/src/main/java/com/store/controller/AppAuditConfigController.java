package com.store.controller;

import com.store.common.resultvo.ResultVO;
import com.store.domain.dto.AppAuditConfigDTO;
import com.store.domain.vo.AppAuditConfigVO;
import com.store.service.AppAuditConfigService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/app/audit")
@Tag(name = "APP审核管理", description = "APP审核相关接口")
public class AppAuditConfigController {
    private final AppAuditConfigService appAuditConfigService;

    public AppAuditConfigController(AppAuditConfigService appAuditConfigService) {
        this.appAuditConfigService = appAuditConfigService;
    }
    @GetMapping("/config")
    @Operation(summary = "获取APP审核配置")
    public ResultVO<AppAuditConfigVO> getAppAuditConfig(@RequestBody @Valid AppAuditConfigDTO appAuditConfigDTO) {
        return ResultVO.success(appAuditConfigService.getAppAuditConfig(appAuditConfigDTO));
    }
}
