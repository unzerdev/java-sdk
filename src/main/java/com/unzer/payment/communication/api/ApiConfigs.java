package com.unzer.payment.communication.api;

public class ApiConfigs {

    public static final ApiConfig PAYMENT_API = new ApiConfig("https://api.unzer.com", "https://sbx-api.unzer.com");
    public static final ApiConfig PAYMENT_API_BEARER_AUTH = new ApiConfig("https://api.unzer.com", "https://sbx-api.unzer.com", ApiConfig.AuthMethod.BEARER);
    public static final ApiConfig TOKEN_SERVICE_API = new ApiConfig("https://token.upcgw.com", "https://token.test.upcgw.com");
    public static final ApiConfig PAYPAGE_API = new ApiConfig("https://paypage.unzer.com", "https://paypage.test.unzer.io", ApiConfig.AuthMethod.BEARER);
}
