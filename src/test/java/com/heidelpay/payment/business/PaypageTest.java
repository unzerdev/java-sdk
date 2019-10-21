package com.heidelpay.payment.business;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import java.net.MalformedURLException;
import org.junit.Test;
import com.heidelpay.payment.Paypage;
import com.heidelpay.payment.communication.HttpCommunicationException;

public class PaypageTest extends AbstractSeleniumTest {

	@Test
	public void testMaximumPaypage() throws MalformedURLException, HttpCommunicationException {
		Paypage request = getMaximumPaypage();
		Paypage response = getHeidelpay().paypage(request);
		assertNull(response.getImpressumUrl());
		assertNull(response.getCard3ds());
		
		assertNotNull(response);
		assertNotNull(response.getId());
		assertNotNull(response.getRedirectUrl());
		assertNotNull(response.getPaymentId());
		
		assertEquals(request.getCurrency(), response.getCurrency());
		assertEquals(request.getReturnUrl(), response.getReturnUrl());
		assertEquals(request.getShopName(), response.getShopName());
		assertEquals(request.getShopDescription(), response.getShopDescription());
		assertEquals(request.getTagline(), response.getTagline());
		assertEquals(request.getTermsAndConditionUrl().toString(), response.getTermsAndConditionUrl().toString());
		assertEquals(request.getPrivacyPolicyUrl().toString(), response.getPrivacyPolicyUrl().toString());
		assertEquals(request.getLogoImage().toString(), response.getLogoImage().toString());
		assertEquals(request.getFullPageImage().toString(), response.getFullPageImage().toString());
		assertEquals(request.getContactUrl().toString(), response.getContactUrl().toString());
		assertEquals(request.getHelpUrl().toString(), response.getHelpUrl().toString());
		assertEquals(request.getImprintUrl().toString(), response.getImprintUrl().toString());
		assertEquals(request.getTermsAndConditionUrl().toString(), response.getTermsAndConditionUrl().toString());
		assertEquals(request.getPrivacyPolicyUrl().toString(), response.getPrivacyPolicyUrl().toString());
		assertEquals(request.getInvoiceId(), response.getInvoiceId());
		assertEquals(request.getOrderId(), response.getOrderId());
		assertEquals(request.getBillingAddressRequired(), response.getBillingAddressRequired());
		assertEquals(request.getShippingAddressRequired(), response.getShippingAddressRequired());
		assertEquals("charge", response.getAction().toLowerCase());
		
		for (String key : response.getCss().keySet()) {
			assertEquals(request.getCss().get(key), response.getCss().get(key));
		}
	}
	
	@Test
	public void testPaypage_WithEmptyCssMap() throws MalformedURLException, HttpCommunicationException {
		Paypage request = getMaximumPaypage();
		request.setCss(null);
		
		Paypage response = getHeidelpay().paypage(request);
		assertNull(response.getImpressumUrl());
		assertNull(response.getCard3ds());
		
		assertNotNull(response);
		assertNotNull(response.getId());
		assertNotNull(response.getRedirectUrl());
		assertNotNull(response.getPaymentId());
		
		assertEquals(request.getCurrency(), response.getCurrency());
		assertEquals(request.getReturnUrl(), response.getReturnUrl());
		assertEquals(request.getShopName(), response.getShopName());
		assertEquals(request.getShopDescription(), response.getShopDescription());
		assertEquals(request.getTagline(), response.getTagline());
		assertEquals(request.getTermsAndConditionUrl().toString(), response.getTermsAndConditionUrl().toString());
		assertEquals(request.getPrivacyPolicyUrl().toString(), response.getPrivacyPolicyUrl().toString());
		assertEquals(request.getLogoImage().toString(), response.getLogoImage().toString());
		assertEquals(request.getFullPageImage().toString(), response.getFullPageImage().toString());
		assertEquals(request.getContactUrl().toString(), response.getContactUrl().toString());
		assertEquals(request.getHelpUrl().toString(), response.getHelpUrl().toString());
		assertEquals(request.getImprintUrl().toString(), response.getImprintUrl().toString());
		assertEquals(request.getTermsAndConditionUrl().toString(), response.getTermsAndConditionUrl().toString());
		assertEquals(request.getPrivacyPolicyUrl().toString(), response.getPrivacyPolicyUrl().toString());
		assertEquals(request.getInvoiceId(), response.getInvoiceId());
		assertEquals(request.getOrderId(), response.getOrderId());
		assertEquals(request.getBillingAddressRequired(), response.getBillingAddressRequired());
		assertEquals(request.getShippingAddressRequired(), response.getShippingAddressRequired());
		assertEquals("charge", response.getAction().toLowerCase());
		
		assertNotNull(response.getCss());
		assertEquals(0, response.getCss().size());
	}
}
