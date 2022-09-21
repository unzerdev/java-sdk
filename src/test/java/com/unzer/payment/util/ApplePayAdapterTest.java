/*
 * Copyright 2020-today Unzer E-Com GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.unzer.payment.util;


import org.junit.jupiter.api.Test;

import java.net.URISyntaxException;

import static com.unzer.payment.util.ApplePayAdapterUtil.doesUrlContainValidDomainName;
import static org.junit.jupiter.api.Assertions.*;

public class ApplePayAdapterTest {
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
        ApplePayAdapterUtil.setCustomAppleValidationUrls("google.com");
        assertTrue(doesUrlContainValidDomainName("https://www.google.com"));
        assertFalse(doesUrlContainValidDomainName("https://www.apple-pay-gateway.apple.com/"));
    }
}
