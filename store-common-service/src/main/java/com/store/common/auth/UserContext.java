package com.store.common.auth;

import com.store.common.auth.dto.CurrentUserInfo;
import org.springframework.stereotype.Component;

@Component
public class UserContext {

    public Long getCurrentUserId() {
        return SecurityUtils.getUserId();
    }

    public CurrentUserInfo getCurrentUser() {
        return SecurityUtils.getCurrentUser();
    }
}
