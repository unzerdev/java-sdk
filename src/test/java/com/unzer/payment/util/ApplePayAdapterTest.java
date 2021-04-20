package com.unzer.payment.util;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

public class ApplePayAdapterTest {

    @Test(expected = NullPointerException.class)
    public void ifAllParametersAreNullThrowError() throws NoSuchAlgorithmException, IOException, KeyManagementException, URISyntaxException {
        ApplePayAdapterUtil.validateApplePayMerchant(null, null, null, null);
    }

    @Test
    public void getPlainDomainName() throws URISyntaxException {
        String url = "https://www.unzer.com/de/";
        String plainDomainName = ApplePayAdapterUtil.getPlainDomainName(url);
        Assert.assertEquals("unzer.com", plainDomainName);
    }

    @Test
    public void doesUrlsContainValidDomainName() throws URISyntaxException {
        String validUrl1 = "https://www.apple-pay-gateway.apple.com/";
        String validUrl2 = "https://cn-apple-pay-gateway.apple.com/";
        String invalidUrl1 = "https://www.google.com/";
        String invalidUrl2 = "https://www.amazon.com/";

        Assert.assertTrue(ApplePayAdapterUtil.doesUrlContainValidDomainName(validUrl1));
        Assert.assertTrue(ApplePayAdapterUtil.doesUrlContainValidDomainName(validUrl2));
        Assert.assertFalse(ApplePayAdapterUtil.doesUrlContainValidDomainName(invalidUrl1));
        Assert.assertFalse(ApplePayAdapterUtil.doesUrlContainValidDomainName(invalidUrl2));
    }
}
