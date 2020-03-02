package com.heidelpay.payment.business;

/*-
 * #%L
 * Heidelpay Java SDK
 * %%
 * Copyright (C) 2018 - 2020 Heidelpay GmbH
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import java.net.MalformedURLException;
import java.util.Arrays;
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
		assertEquals(Arrays.toString(request.getExcludeTypes()), Arrays.toString(response.getExcludeTypes()));
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
		assertEquals(Arrays.toString(request.getExcludeTypes()), Arrays.toString(response.getExcludeTypes()));
		assertEquals("charge", response.getAction().toLowerCase());
	}
}
