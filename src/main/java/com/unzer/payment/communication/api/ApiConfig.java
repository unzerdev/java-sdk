package com.unzer.payment.communication.api;

import lombok.Getter;

@Getter
public class ApiConfig {
    enum AUTH_METOD {
        BASIC,
        BEARER;
    }

    private final String baseUrl;
    private final String testBaseUrl;
    private final AUTH_METOD authMethod;

    public ApiConfig(String baseUrl, String testBaseUrl) {
        this(baseUrl, testBaseUrl, AUTH_METOD.BASIC);
    }

    public ApiConfig(String baseUrl, String testBaseUrl, AUTH_METOD authMethod) {
        this.baseUrl = baseUrl;
        this.testBaseUrl = testBaseUrl;
        this.authMethod = authMethod;
    }
}
