package com.heidelpay.payment.business.paymenttypes;

/*-
 * #%L
 * Heidelpay Java SDK
 * %%
 * Copyright (C) 2018 - 2019 Heidelpay GmbH
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

import com.heidelpay.payment.Authorization;
import com.heidelpay.payment.Charge;
import com.heidelpay.payment.business.AbstractPaymentTest;
import com.heidelpay.payment.communication.HttpCommunicationException;
import com.heidelpay.payment.paymenttypes.Applepay;
import com.heidelpay.payment.paymenttypes.ApplepayHeader;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Currency;
import org.junit.Ignore;
import org.junit.Test;

public class ApplepayTest extends AbstractPaymentTest {

	@Ignore("missing key configuration for apple pay")
	@Test
	public void testCreateApplepayType() throws HttpCommunicationException {
		Applepay applepay = getApplePay();
		
		Applepay response = getHeidelpay().createPaymentType(applepay);
		assertNotNull(response.getId());
		assertNotNull(response.getExpiryDate());
		assertNotNull(response.getNumber());
		assertEquals("481852******2027", response.getNumber());
		assertEquals("12/2023", response.getExpiryDate());
		
	}

	@Ignore("missing key configuration for apple pay")
	@Test
	public void testAuthorizeApplePayType() throws HttpCommunicationException, MalformedURLException {
		Applepay applepay = getHeidelpay().createPaymentType(getApplePay());
		Authorization authorization = applepay.authorize(BigDecimal.ONE, Currency.getInstance("EUR"), new URL("https://www.meinShop.de"));
		assertNotNull(authorization);
		assertNotNull(authorization.getId());
		assertEquals(Authorization.Status.SUCCESS, authorization.getStatus());
	}

	@Ignore("missing key configuration for apple pay")
	@Test
	public void testAuthorizeApplePayTypeId() throws HttpCommunicationException, MalformedURLException {
		Applepay applepay = getHeidelpay().createPaymentType(getApplePay());
		Authorization authorization = getHeidelpay().authorize(BigDecimal.ONE, Currency.getInstance("EUR"), applepay.getId(), new URL("https://www.meinShop.de"));
		assertNotNull(authorization);
		assertNotNull(authorization.getId());
		assertEquals(Authorization.Status.SUCCESS, authorization.getStatus());
	}

	@Ignore("missing key configuration for apple pay")
	@Test
	public void testChargeApplePayType() throws HttpCommunicationException, MalformedURLException {
		Applepay applepay = getHeidelpay().createPaymentType(getApplePay());
		Charge charge = applepay.charge(BigDecimal.ONE, Currency.getInstance("EUR"), new URL("https://www.meinShop.de"));
		assertNotNull(charge);
		assertNotNull(charge.getId());
		assertEquals(Authorization.Status.SUCCESS, charge.getStatus());
	}

	@Ignore("missing key configuration for apple pay")
	@Test
	public void testChargeApplePayTypeId() throws HttpCommunicationException, MalformedURLException {
		Applepay applepay = getHeidelpay().createPaymentType(getApplePay());
		Charge charge = getHeidelpay().charge(BigDecimal.ONE, Currency.getInstance("EUR"), applepay.getId(), new URL("https://www.meinShop.de"));
		assertNotNull(charge);
		assertNotNull(charge.getId());
		assertEquals(Authorization.Status.SUCCESS, charge.getStatus());
	}


	private Applepay getApplePay() {
		Applepay applepay = new Applepay();
		applepay.setVersion("EC_v1");
		applepay.setData("RlUOw14KcaH/85Ln/ROds26cROnVO2dOLBcODQDuwXCkuMogbeRteRoy+TlfZAYKfPyAPopQKPauzDsSEaLaLyns6Nqln28mh8SHilBsBJZUb/pU5xKNfsq2eSmk8obRqkrDiIi8pVsi7UXUIMHF3XkP7DPX84IFTwlelDD8YbDAGsihDYNjTwjg+jX3Q9RJmKpG/DM8lDtE3A2HA8wUruVrOFal/nEITq8c3jK8cnL/GVZMrBDsPiHdGxTuzf0O4XddV0A24GE6M1Y6CWp3nVBUMfyedTHggCg/HGU4sFDlCnoX2A39imwL35tYZ83wUHiPJ36el2nGlNkYR7zQ0SQWxDjS+tS68gD19JBhrNYqdl567b6njMGaP19mXdpkLZXhVr3pWYXDzNZXi6HjmvVUqFR/Z+UwapasulnNbg==");
		applepay.setSignature("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");
		ApplepayHeader header = new ApplepayHeader();
		header.setEphemeralPublicKey("MFkwEwYHKoZIzj0CAQYIKoZIzj0DAQcDQgAEyGsYCP+pw8dG9pgt0s/mrJFGqYD0fYwzyXACXAlpN6ghVZpxDPsdh4jiBQBgd4tvedSFVbw+nH/FsN/bs/hK1Q==");
		header.setPublicKeyHash("M2yzlpBsH3GwH5jTV9GgKC7bAUdeIOIfjwQhoKjg5+s=");
		header.setTransactionId("93a2574ba3e42f24b25160cd91fc8d170c9abe9520997767b51f5099933d96f5");
		applepay.setHeader(header);
		return applepay;
	}
	
}
