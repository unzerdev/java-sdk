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
package com.unzer.payment.business;


import com.unzer.payment.Paypage;
import com.unzer.payment.Unzer;
import com.unzer.payment.communication.HttpCommunicationException;
import com.unzer.payment.service.UrlUtil;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class PaypageTest extends AbstractPaymentTest {

    @Test
    public void testMaximumPaypage() throws HttpCommunicationException {
        Unzer unzer = getUnzer();

        Paypage request = getMaximumPaypage();
        Paypage createdPaypage = unzer.paypage(request);

        Paypage fetchedPaypage = unzer.fetchPaypage(createdPaypage.getId());

        assertNull(fetchedPaypage.getCard3ds());
        assertNotNull(fetchedPaypage);
        assertNotNull(fetchedPaypage.getId());
        assertNotNull(fetchedPaypage.getRedirectUrl());
        assertNotNull(fetchedPaypage.getPaymentId());

        assertEquals(request.getCurrency(), fetchedPaypage.getCurrency());
        assertEquals(request.getReturnUrl(), fetchedPaypage.getReturnUrl());
        assertEquals(request.getShopName(), fetchedPaypage.getShopName());
        assertEquals(request.getShopDescription(), fetchedPaypage.getShopDescription());
        assertEquals(request.getTagline(), fetchedPaypage.getTagline());
        assertEquals(request.getTermsAndConditionUrl().toString(), fetchedPaypage.getTermsAndConditionUrl().toString());
        assertEquals(request.getPrivacyPolicyUrl().toString(), fetchedPaypage.getPrivacyPolicyUrl().toString());
        assertEquals(request.getLogoImage(), fetchedPaypage.getLogoImage());
        assertEquals(request.getFullPageImage(), fetchedPaypage.getFullPageImage());
        assertEquals(request.getContactUrl().toString(), fetchedPaypage.getContactUrl().toString());
        assertEquals(request.getHelpUrl().toString(), fetchedPaypage.getHelpUrl().toString());
        assertEquals(request.getImprintUrl().toString(), fetchedPaypage.getImprintUrl().toString());
        assertEquals(request.getTermsAndConditionUrl().toString(), fetchedPaypage.getTermsAndConditionUrl().toString());
        assertEquals(request.getPrivacyPolicyUrl().toString(), fetchedPaypage.getPrivacyPolicyUrl().toString());
        assertEquals(request.getInvoiceId(), fetchedPaypage.getInvoiceId());
        assertEquals(request.getOrderId(), fetchedPaypage.getOrderId());
        assertEquals(request.getBillingAddressRequired(), fetchedPaypage.getBillingAddressRequired());
        assertEquals(request.getShippingAddressRequired(), fetchedPaypage.getShippingAddressRequired());
        assertEquals(Arrays.toString(request.getExcludeTypes()), Arrays.toString(fetchedPaypage.getExcludeTypes()));
        assertEquals("charge", fetchedPaypage.getAction().toLowerCase());

        for (String key : fetchedPaypage.getCss().keySet()) {
            assertEquals(request.getCss().get(key), fetchedPaypage.getCss().get(key));
        }
    }

    @Test
    public void testPaypage_WithEmptyCssMap() throws HttpCommunicationException {
        Unzer unzer = getUnzer();

        Paypage request = getMaximumPaypage();
        request.setCss(null);

        Paypage createdPaypage = unzer.paypage(request);

        Paypage fetchedPaypage = unzer.fetchPaypage(createdPaypage.getId());

        assertNull(fetchedPaypage.getCard3ds());

        assertNotNull(fetchedPaypage);
        assertNotNull(fetchedPaypage.getId());
        assertNotNull(fetchedPaypage.getRedirectUrl());
        assertNotNull(fetchedPaypage.getPaymentId());

        assertEquals(request.getCurrency(), fetchedPaypage.getCurrency());
        assertEquals(request.getReturnUrl(), fetchedPaypage.getReturnUrl());
        assertEquals(request.getShopName(), fetchedPaypage.getShopName());
        assertEquals(request.getShopDescription(), fetchedPaypage.getShopDescription());
        assertEquals(request.getTagline(), fetchedPaypage.getTagline());
        assertEquals(request.getTermsAndConditionUrl().toString(), fetchedPaypage.getTermsAndConditionUrl().toString());
        assertEquals(request.getPrivacyPolicyUrl().toString(), fetchedPaypage.getPrivacyPolicyUrl().toString());
        assertEquals(request.getLogoImage(), fetchedPaypage.getLogoImage());
        assertEquals(request.getFullPageImage(), fetchedPaypage.getFullPageImage());
        assertEquals(request.getContactUrl().toString(), fetchedPaypage.getContactUrl().toString());
        assertEquals(request.getHelpUrl().toString(), fetchedPaypage.getHelpUrl().toString());
        assertEquals(request.getImprintUrl().toString(), fetchedPaypage.getImprintUrl().toString());
        assertEquals(request.getTermsAndConditionUrl().toString(), fetchedPaypage.getTermsAndConditionUrl().toString());
        assertEquals(request.getPrivacyPolicyUrl().toString(), fetchedPaypage.getPrivacyPolicyUrl().toString());
        assertEquals(request.getInvoiceId(), fetchedPaypage.getInvoiceId());
        assertEquals(request.getOrderId(), fetchedPaypage.getOrderId());
        assertEquals(request.getBillingAddressRequired(), fetchedPaypage.getBillingAddressRequired());
        assertEquals(request.getShippingAddressRequired(), fetchedPaypage.getShippingAddressRequired());
        assertEquals(Arrays.toString(request.getExcludeTypes()), Arrays.toString(fetchedPaypage.getExcludeTypes()));
        assertEquals("charge", fetchedPaypage.getAction().toLowerCase());
    }

    @TestFactory
    public Collection<DynamicTest> testRestUrl() {
        UrlUtil urlUtil = new UrlUtil("any-key");
        class TestCase {
            final String name;
            final String action;
            final String expectedUrlPart;

            public TestCase(String name, String action, String expectedUrlPart) {
                this.name = name;
                this.action = action;
                this.expectedUrlPart = expectedUrlPart;
            }
        }

        return Stream.of(
                new TestCase(
                        "null",
                        null,
                        "paypage/charge"
                ),
                new TestCase(
                        "charge",
                        Paypage.Action.CHARGE,
                        "paypage/charge"
                ),
                new TestCase(
                        "authorize",
                        Paypage.Action.AUTHORIZE,
                        "paypage/authorize"
                )
        ).map(t -> DynamicTest.dynamicTest(t.name, () -> {
            Paypage paypage = getMaximumPaypage()
                    .setAction(t.action);

            String actualUrl = urlUtil.getInitPaypageUrl(paypage);
            String expectedUrl = urlUtil.getRestUrl() + t.expectedUrlPart;

            assertEquals(expectedUrl, actualUrl);
        })).collect(Collectors.toList());
    }

    @Test
    public void testAuthorize() throws HttpCommunicationException {
        Unzer unzer = getUnzer();
        Paypage request = getMaximumPaypage().setAction(Paypage.Action.AUTHORIZE);
        Paypage createdPaypage = unzer.paypage(request);

        Paypage fetchedPaypage = unzer.fetchPaypage(createdPaypage.getId());

        assertEquals("AUTHORIZE", fetchedPaypage.getAction());
    }
}
