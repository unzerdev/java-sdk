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
        ApplePayAdapter.validateApplePayMerchant(null, null, null, null);
    }

    @Test
    public void getPlainDomainName() throws URISyntaxException {
        String url = "https://www.unzer.com/de/";
        String plainDomainName = ApplePayAdapter.getPlainDomainName(url);
        Assert.assertEquals("unzer.com", plainDomainName);
    }

    @Test
    public void doesUrlsContainValidDomainName() throws URISyntaxException {
        String validUrl1 = "https://www.apple-pay-gateway.apple.com/";
        String validUrl2 = "https://cn-apple-pay-gateway.apple.com/";

        Assert.assertTrue(ApplePayAdapter.doesUrlContainValidDomainName(validUrl1));
        Assert.assertTrue(ApplePayAdapter.doesUrlContainValidDomainName(validUrl2));
    }
}
