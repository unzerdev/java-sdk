package com.unzer.payment.business;


import com.unzer.payment.Linkpay;
import com.unzer.payment.communication.HttpCommunicationException;
import org.junit.jupiter.api.Test;

import java.net.MalformedURLException;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public class LinkpayTest extends AbstractSeleniumTest {

    @Test
    public void testMaximumLinkpay() throws MalformedURLException, HttpCommunicationException {
        Linkpay request = getMaximumLinkpay();
        Linkpay response = getUnzer().linkpay(request);
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
        assertEquals("charge", response.getAction().toLowerCase());

        for (String key : response.getCss().keySet()) {
            assertEquals(request.getCss().get(key), response.getCss().get(key));
        }
    }

    @Test
    public void testLinkpay_WithEmptyCssMap() throws MalformedURLException, HttpCommunicationException {
        Linkpay request = getMaximumLinkpay();
        request.setCss(null);

        Linkpay response = getUnzer().linkpay(request);
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
        assertEquals("charge", response.getAction().toLowerCase());
    }
}
