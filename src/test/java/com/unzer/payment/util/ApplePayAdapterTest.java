package com.unzer.payment.util;

/*-
 * #%L
 * Unzer Java SDK
 * %%
 * Copyright (C) 2020 - today Unzer E-Com GmbH
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

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
