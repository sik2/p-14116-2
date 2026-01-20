package com.back.global.auth;

import com.back.standard.ut.Util;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class SystemAuthTokenProvider {
    @Value("${custom.jwt.secretKey}")
    private String jwtSecretKey;

    @Value("${custom.accessToken.expirationSeconds}")
    private int accessTokenExpirationSeconds;

    @Value("${custom.system.apiKey}")
    private String systemApiKey;

    public String getSystemApiKey() {
        return systemApiKey;
    }

    public String getSystemAccessToken() {
        return Util.jwt.toString(
                jwtSecretKey,
                accessTokenExpirationSeconds,
                Map.of("id", 1, "username", "system", "nickname", "시스템")
        );
    }

    public String getAuthorizationHeader() {
        return "Bearer " + systemApiKey + " " + getSystemAccessToken();
    }
}
