package com.store.common.auth;

import com.store.common.auth.dto.CurrentUserInfo;
import com.store.common.exception.BusinessException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public final class SecurityUtils {

    private SecurityUtils() {
    }

    public static Long getUserId() {
        String userId = getRequiredHeader(AuthConstants.HEADER_USER_ID);
        try {
            return Long.valueOf(userId);
        } catch (NumberFormatException exception) {
            throw new BusinessException("当前用户ID格式错误", exception);
        }
    }

    public static String getUsername() {
        return getRequiredHeader(AuthConstants.HEADER_USERNAME);
    }

    public static String getNickname() {
        return getHeader(AuthConstants.HEADER_NICKNAME);
    }

    public static List<String> getRoles() {
        String roles = getHeader(AuthConstants.HEADER_USER_ROLES);
        if (!StringUtils.hasText(roles)) {
            return List.of();
        }
        return Arrays.stream(roles.split(","))
                .map(String::trim)
                .filter(StringUtils::hasText)
                .distinct()
                .toList();
    }

    public static CurrentUserInfo getCurrentUser() {
        return new CurrentUserInfo(
                getUserId(),
                getUsername(),
                getNickname(),
                getRoles()
        );
    }

    public static String getRequiredHeader(String headerName) {
        String value = getHeader(headerName);
        if (!StringUtils.hasText(value)) {
            throw new BusinessException("当前登录信息不存在，请重新登录");
        }
        return value;
    }

    public static String getHeader(String headerName) {
        String value = getRequest().getHeader(headerName);
        return Objects.isNull(value) ? null : value.trim();
    }

    public static HttpServletRequest getRequest() {
        ServletRequestAttributes attributes =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            throw new BusinessException("无法获取当前请求上下文");
        }
        return attributes.getRequest();
    }
}
