package com.heidelpay.payment.business.paymenttypes;

/*-
 * #%L
 * Heidelpay Java SDK
 * %%
 * Copyright (C) 2018 Heidelpay GmbH
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License")
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

import static org.junit.Assert.assertNotNull;

import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Currency;

import org.junit.Test;

import com.heidelpay.payment.Charge;
import com.heidelpay.payment.business.AbstractPaymentTest;
import com.heidelpay.payment.communication.HttpCommunicationException;
import com.heidelpay.payment.paymenttypes.Wechatpay;

public class WechatpayTest extends AbstractPaymentTest {

	@Test
	public void testCreateWechatpayManatoryType() throws HttpCommunicationException {
		Wechatpay wechatpay = new Wechatpay();
		wechatpay = getHeidelpay().createPaymentType(wechatpay);
		assertNotNull(wechatpay.getId());
	}

	@Test
	public void testChargeWechatpayType() throws HttpCommunicationException, MalformedURLException {
		Wechatpay wechatpay = getHeidelpay().createPaymentType(getWechatpay());
		Charge charge = wechatpay.charge(BigDecimal.ONE, Currency.getInstance("EUR"), new URL("https://www.google.at"));
		assertNotNull(charge);
		assertNotNull(charge.getId());
		assertNotNull(charge.getRedirectUrl());
	}

	@Test
	public void testFetchWechatpayType() throws HttpCommunicationException {
		Wechatpay wechatpay = getHeidelpay().createPaymentType(getWechatpay());
		assertNotNull(wechatpay.getId());
		Wechatpay fetchedWechatpay = (Wechatpay) getHeidelpay().fetchPaymentType(wechatpay.getId());
		assertNotNull(fetchedWechatpay.getId());
	}

	
	private Wechatpay getWechatpay() {
		Wechatpay wechatpay = new Wechatpay();
		return wechatpay;
	}


}
