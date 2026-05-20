package com.store.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.store.domain.AppAuditConfig;
import com.store.domain.dto.AppAuditConfigDTO;
import com.store.domain.vo.AppAuditConfigVO;
import com.store.service.AppAuditConfigService;
import com.store.mapper.AppAuditConfigMapper;
import io.netty.util.internal.StringUtil;
import org.springframework.stereotype.Service;

/**
* @author 19256
* @description 针对表【app_audit_config(APP应用商店审核配置表)】的数据库操作Service实现
* @createDate 2026-05-04 22:29:14
*/
@Service
public class AppAuditConfigServiceImpl extends ServiceImpl<AppAuditConfigMapper, AppAuditConfig>
    implements AppAuditConfigService{
    private final AppAuditConfigMapper appAuditConfigMapper;

    public AppAuditConfigServiceImpl(AppAuditConfigMapper appAuditConfigMapper) {
        this.appAuditConfigMapper = appAuditConfigMapper;
    }

    @Override
    public AppAuditConfigVO getAppAuditConfig(AppAuditConfigDTO appAuditConfigDTO) {
        LambdaQueryWrapper<AppAuditConfig> queryWrapper = new LambdaQueryWrapper<AppAuditConfig>()
                .eq(StringUtils.isNotEmpty(appAuditConfigDTO.getAppType()),AppAuditConfig::getAppType, appAuditConfigDTO.getAppType())
                .eq(StringUtils.isNotEmpty(appAuditConfigDTO.getChannel()),AppAuditConfig::getChannel, appAuditConfigDTO.getChannel())
                .eq(StringUtils.isNotEmpty(appAuditConfigDTO.getVersion()),AppAuditConfig::getVersion, appAuditConfigDTO.getVersion())
                .last("limit 1");

        AppAuditConfig config = appAuditConfigMapper.selectOne(queryWrapper);
        AppAuditConfigVO vo=new AppAuditConfigVO();
        if(config == null){
            vo.setAuditMode( false);
            return vo;
        }
        vo.setAuditMode( config.getAuditMode() != null && Integer.valueOf(1).equals(config.getAuditMode()));

        return vo;
    }
}




