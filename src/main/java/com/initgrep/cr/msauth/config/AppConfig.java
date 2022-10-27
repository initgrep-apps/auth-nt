package com.initgrep.cr.msauth.config;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@RequiredArgsConstructor
@Configuration
@ConfigurationProperties(prefix = "app")
public class AppConfig {

    private final KeyPairConfig keyPair = new KeyPairConfig();

    private final AccessTokenConfig accessToken = new AccessTokenConfig();

    private final RefreshTokenConfig refreshToken = new RefreshTokenConfig();

    @Data
    public static class KeyPairConfig{
        private String directory;
    }
    @Data
    public static class AccessTokenConfig {
        private String publicKeyPath;
        private String privateKeyPath;
        private int expiryDurationMin;
    }

    @Data
    public static class RefreshTokenConfig {
        private String publicKeyPath;
        private String privateKeyPath;
        private int expiryDurationDays;
        private int minRenewalDays;
    }

}
