package com.unzer.payment.communication.api;

public class ApiConfigs {

    public static final ApiConfig PAYMENT_API = new ApiConfig("https://api.unzer.com", "https://stg-api.unzer.com");
    public static final ApiConfig TOKEN_SERVICE_API = new ApiConfig("https://token.upcgw.com", "https://token.int.unzer.io");
    public static final ApiConfig PAYPAGE_API = new ApiConfig("https://paypage.unzer.com", "https://paypage.int.unzer.io", ApiConfig.AuthMethod.BEARER);
}
