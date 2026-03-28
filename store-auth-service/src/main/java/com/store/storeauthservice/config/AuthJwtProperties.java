package com.store.storeauthservice.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
@ConfigurationProperties(prefix = "store.security.jwt")
public class AuthJwtProperties {
    private String secret;
    private Duration accessTtl = Duration.ofMinutes(30);
    private Duration refreshTtl = Duration.ofDays(7);

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public Duration getAccessTtl() {
        return accessTtl;
    }

    public void setAccessTtl(Duration accessTtl) {
        this.accessTtl = accessTtl;
    }

    public Duration getRefreshTtl() {
        return refreshTtl;
    }

    public void setRefreshTtl(Duration refreshTtl) {
        this.refreshTtl = refreshTtl;
    }
}
