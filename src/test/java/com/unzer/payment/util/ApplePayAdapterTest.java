package com.unzer.payment.util;


import static com.unzer.payment.util.ApplePayAdapterUtil.doesUrlContainValidDomainName;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.net.URISyntaxException;
import java.util.Collections;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;


@Execution(ExecutionMode.SAME_THREAD)
public class ApplePayAdapterTest {
    @BeforeEach
    public void setDefaultValues() {
        ApplePayAdapterUtil.setCustomAppleValidationUrls(ApplePayAdapterUtil.DEFAULT_VALIDATION_URLS);
    }

    @Test
    public void ifAllParametersAreNullThrowError() {
        assertThrows(NullPointerException.class, () -> ApplePayAdapterUtil.validateApplePayMerchant(
                null,
                null,
                null,
                null));
    }

    @Test
    public void doesUrlsContainValidDomainName() throws URISyntaxException {
        assertTrue(doesUrlContainValidDomainName("https://www.apple-pay-gateway.apple.com/"));
        assertTrue(doesUrlContainValidDomainName("https://cn-apple-pay-gateway.apple.com/"));

        assertFalse(doesUrlContainValidDomainName("https://www.google.com/"));
        assertFalse(doesUrlContainValidDomainName("https://www.amazon.com/"));
    }

    @Test
    public void customValidationUrls() throws URISyntaxException {
        ApplePayAdapterUtil.setCustomAppleValidationUrls(Collections.singletonList("google.com"));
        assertTrue(doesUrlContainValidDomainName("https://www.google.com"));
        assertFalse(doesUrlContainValidDomainName("https://www.apple-pay-gateway.apple.com/"));
    }
}
