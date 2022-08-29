package com.unzer.payment.util;


import org.junit.jupiter.api.Test;

import java.net.URISyntaxException;

import static org.junit.jupiter.api.Assertions.*;

public class ApplePayAdapterTest {

    @Test
    public void ifAllParametersAreNullThrowError() {
        assertThrows(NullPointerException.class, () -> {
            ApplePayAdapterUtil.validateApplePayMerchant(null, null, null, null);
        });
    }

    @Test
    public void getPlainDomainName() throws URISyntaxException {
        String url = "https://www.unzer.com/de/";
        String plainDomainName = ApplePayAdapterUtil.getPlainDomainName(url);
        assertEquals("unzer.com", plainDomainName);
    }

    @Test
    public void doesUrlsContainValidDomainName() throws URISyntaxException {
        String validUrl1 = "https://www.apple-pay-gateway.apple.com/";
        String validUrl2 = "https://cn-apple-pay-gateway.apple.com/";
        String invalidUrl1 = "https://www.google.com/";
        String invalidUrl2 = "https://www.amazon.com/";

        assertTrue(ApplePayAdapterUtil.doesUrlContainValidDomainName(validUrl1));
        assertTrue(ApplePayAdapterUtil.doesUrlContainValidDomainName(validUrl2));
        assertFalse(ApplePayAdapterUtil.doesUrlContainValidDomainName(invalidUrl1));
        assertFalse(ApplePayAdapterUtil.doesUrlContainValidDomainName(invalidUrl2));
    }
}
