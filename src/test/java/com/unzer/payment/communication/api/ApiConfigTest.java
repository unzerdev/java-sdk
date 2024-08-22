package com.unzer.payment.communication.api;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ApiConfigTest {

    @Test
    void getPaymentApiConfig() {
        assertEquals("https://api.unzer.com", ApiConfigs.PAYMENT_API.getBaseUrl());
        assertEquals("https://sbx-api.unzer.com", ApiConfigs.PAYMENT_API.getTestBaseUrl());
    }

    @Test
    void getTokenServiceConfig() {
        assertEquals("https://token.upcgw.com", ApiConfigs.TOKEN_SERVICE_API.getBaseUrl());
        assertEquals("https://token.test.upcgw.com", ApiConfigs.TOKEN_SERVICE_API.getTestBaseUrl());
    }

    @Test
    void getPaypageServiceConfig() {
        assertEquals("https://paypage.unzer.com", ApiConfigs.PAYPAGE_API.getBaseUrl());
        assertEquals("https://paypage.test.unzer.io", ApiConfigs.PAYPAGE_API.getTestBaseUrl());
    }
}
