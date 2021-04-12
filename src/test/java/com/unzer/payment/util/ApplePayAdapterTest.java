package com.unzer.payment.util;

import org.junit.Test;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

public class ApplePayAdapterTest {

    @Test(expected = NullPointerException.class)
    public void ifAllParametersAreNullThrowError() throws NoSuchAlgorithmException, IOException, KeyManagementException {
        ApplePayAdapter.validateApplePayMerchant(null, null, null, null);
    }
}
