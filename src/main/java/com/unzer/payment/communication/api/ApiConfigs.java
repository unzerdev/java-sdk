package com.unzer.payment.communication.api;

public class ApiConfigs {

    public static final ApiConfig PAYMENT_API = new ApiConfig(ApiUrls.PAPI_URL, ApiUrls.PAPI_TEST_URL);
    public static final ApiConfig PAYMENT_API_BEARER_AUTH = new ApiConfig(ApiUrls.PAPI_URL, ApiUrls.PAPI_TEST_URL, ApiConfig.AuthMethod.BEARER);
    public static final ApiConfig TOKEN_SERVICE_API = new ApiConfig(ApiUrls.TOKEN_SERVICE_URL, ApiUrls.TOKEN_SERVICE_TEST_URL);
    public static final ApiConfig PAYPAGE_API = new ApiConfig(ApiUrls.PAYPAGE_URL, ApiUrls.PAYPAGE_TEST_URL, ApiConfig.AuthMethod.BEARER);
}
