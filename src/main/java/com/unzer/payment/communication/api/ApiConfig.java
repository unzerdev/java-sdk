package com.unzer.payment.communication.api;

import lombok.Getter;

@Getter
public class ApiConfig {

    public enum AuthMethod {
        BASIC,
        BEARER;
    }

    private final String baseUrl;
    private final String testBaseUrl;
    private final AuthMethod authMethod;

    public ApiConfig(String baseUrl, String testBaseUrl) {
        this(baseUrl, testBaseUrl, AuthMethod.BASIC);
    }

    public ApiConfig(String baseUrl, String testBaseUrl, AuthMethod authMethod) {
        this.baseUrl = baseUrl;
        this.testBaseUrl = testBaseUrl;
        this.authMethod = authMethod;
    }
}
