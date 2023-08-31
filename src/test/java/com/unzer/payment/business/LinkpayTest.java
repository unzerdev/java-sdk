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


import static com.unzer.payment.util.Url.unsafeUrl;
import static com.unzer.payment.util.Uuid.generateUuid;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.unzer.payment.BasePaypage;
import com.unzer.payment.Linkpay;
import com.unzer.payment.Unzer;
import com.unzer.payment.communication.HttpCommunicationException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Currency;
import org.junit.jupiter.api.Test;

public class LinkpayTest extends AbstractPaymentTest {
    @Test
    public void fetch_linkpay() {
        Unzer unzer = getUnzer();
        Linkpay newLinkpay = getMaximumLinkpay();
        Linkpay createdLinkpay = unzer.linkpay(newLinkpay);
        assertNotNull(createdLinkpay.getId());

        Linkpay fetchLinkpay = unzer.fetchLinkpay(createdLinkpay.getId());
        assertEquals(createdLinkpay.getId(), fetchLinkpay.getId());
    }

    @Test
    public void charge_MaximumLinkpay() throws HttpCommunicationException {
        Unzer unzer = getUnzer();
        Linkpay request = getMaximumLinkpay();
        request = unzer.linkpay(request);
        Linkpay response = unzer.fetchLinkpay(request.getId());

        assertNull(response.getCard3ds());

        assertNotNull(response);
        assertNotNull(response.getId());
        assertNotNull(response.getRedirectUrl());
        assertNull(response.getPaymentId());

        assertEquals(request.getCurrency(), response.getCurrency());
        assertEquals(request.getReturnUrl(), response.getReturnUrl());
        assertEquals(request.getShopName(), response.getShopName());
        assertEquals(request.getShopDescription(), response.getShopDescription());
        assertEquals(request.getTagline(), response.getTagline());
        assertEquals(request.getTermsAndConditionUrl().toString(), response.getTermsAndConditionUrl().toString());
        assertEquals(request.getPrivacyPolicyUrl().toString(), response.getPrivacyPolicyUrl().toString());
        assertEquals(request.getLogoImage(), response.getLogoImage());
        assertEquals(request.getFullPageImage(), response.getFullPageImage());
        assertEquals(request.getContactUrl().toString(), response.getContactUrl().toString());
        assertEquals(request.getHelpUrl().toString(), response.getHelpUrl().toString());
        assertEquals(request.getImprintUrl().toString(), response.getImprintUrl().toString());
        assertEquals(request.getTermsAndConditionUrl().toString(), response.getTermsAndConditionUrl().toString());
        assertEquals(request.getPrivacyPolicyUrl().toString(), response.getPrivacyPolicyUrl().toString());
        assertEquals(request.getInvoiceId(), response.getInvoiceId());
        assertEquals(request.getOrderId(), response.getOrderId());
        assertEquals(request.getBillingAddressRequired(), response.getBillingAddressRequired());
        assertEquals(request.getShippingAddressRequired(), response.getShippingAddressRequired());
        assertEquals(Arrays.toString(request.getExcludeTypes()), Arrays.toString(response.getExcludeTypes()));
        assertEquals(request.getOneTimeUse(), response.getOneTimeUse());
        assertEquals(request.getIntention(), response.getIntention());
        assertEquals(BasePaypage.Action.CHARGE, response.getAction());

        for (String key : response.getCss().keySet()) {
            assertEquals(request.getCss().get(key), response.getCss().get(key));
        }
    }

    @Test
    public void charge_Linkpay_WithEmptyCssMap() throws HttpCommunicationException {
        Unzer unzer = getUnzer();

        Linkpay request = getMaximumLinkpay();
        request.setCss(null);

        request = unzer.linkpay(request);
        Linkpay response = unzer.fetchLinkpay(request.getId());

        assertNull(response.getCard3ds());

        assertNotNull(response);
        assertNotNull(response.getId());
        assertNotNull(response.getRedirectUrl());
        assertNull(response.getPaymentId());

        assertEquals(request.getCurrency(), response.getCurrency());
        assertEquals(request.getReturnUrl(), response.getReturnUrl());
        assertEquals(request.getShopName(), response.getShopName());
        assertEquals(request.getShopDescription(), response.getShopDescription());
        assertEquals(request.getTagline(), response.getTagline());
        assertEquals(request.getTermsAndConditionUrl().toString(), response.getTermsAndConditionUrl().toString());
        assertEquals(request.getPrivacyPolicyUrl().toString(), response.getPrivacyPolicyUrl().toString());
        assertEquals(request.getLogoImage(), response.getLogoImage());
        assertEquals(request.getFullPageImage(), response.getFullPageImage());
        assertEquals(request.getContactUrl().toString(), response.getContactUrl().toString());
        assertEquals(request.getHelpUrl().toString(), response.getHelpUrl().toString());
        assertEquals(request.getImprintUrl().toString(), response.getImprintUrl().toString());
        assertEquals(request.getTermsAndConditionUrl().toString(), response.getTermsAndConditionUrl().toString());
        assertEquals(request.getPrivacyPolicyUrl().toString(), response.getPrivacyPolicyUrl().toString());
        assertEquals(request.getInvoiceId(), response.getInvoiceId());
        assertEquals(request.getOrderId(), response.getOrderId());
        assertEquals(request.getBillingAddressRequired(), response.getBillingAddressRequired());
        assertEquals(request.getShippingAddressRequired(), response.getShippingAddressRequired());
        assertEquals(Arrays.toString(request.getExcludeTypes()), Arrays.toString(response.getExcludeTypes()));
        assertEquals(BasePaypage.Action.CHARGE, response.getAction());
    }

    @Test
    public void testAuthorize() throws HttpCommunicationException {
        Unzer unzer = getUnzer();
        Linkpay request = getMaximumLinkpay();
        request.setAction(BasePaypage.Action.AUTHORIZE);
        Linkpay created = unzer.linkpay(request);

        Linkpay fetched = unzer.fetchLinkpay(created.getId());

        assertEquals("AUTHORIZE", fetched.getAction());
    }

    private Linkpay getMaximumLinkpay() {
        Linkpay linkpay = new Linkpay();
        String[] excludeTypes = {"paypal"};
        linkpay.setExcludeTypes(excludeTypes);
        linkpay.setAmount(BigDecimal.ONE);
        linkpay.setCurrency(Currency.getInstance("EUR"));
        linkpay.setReturnUrl(unsafeUrl("https://unzer.com"));
        linkpay.setShopName("Unzer Demo Shop");
        linkpay.setShopDescription("Unzer Demo Shop Description");
        linkpay.setTagline("Unzer Tagline");
        linkpay.setTermsAndConditionUrl(unsafeUrl("https://www.unzer.com/en/datenschutz/"));
        linkpay.setPrivacyPolicyUrl(unsafeUrl("https://www.unzer.com/en/datenschutz/"));
        linkpay.setCss(getCssMap());

        linkpay.setLogoImage("https://docs.unzer.com/payment-nutshell/payment-in-nutshell.png");
        linkpay.setFullPageImage("https://store.storeimages.cdn-apple.com/4668/as-images.apple.com/is/iphone-12-pro-family-hero");

        linkpay.setContactUrl(unsafeUrl("mailto:support@unzer.com"));
        linkpay.setHelpUrl(unsafeUrl("https://www.unzer.com/en/support/"));
        linkpay.setImprintUrl(unsafeUrl("https://www.unzer.com/en/impressum/"));
        linkpay.setPrivacyPolicyUrl(unsafeUrl("https://www.unzer.com/en/datenschutz/"));
        linkpay.setTermsAndConditionUrl(unsafeUrl("https://www.unzer.com/en/datenschutz/"));

        linkpay.setInvoiceId(generateUuid());
        linkpay.setOrderId(generateUuid());
        return linkpay;
    }
}
