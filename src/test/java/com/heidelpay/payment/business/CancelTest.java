package com.heidelpay.payment.business;

/*-
 * #%L
 * Heidelpay Java SDK
 * %%
 * Copyright (C) 2018 Heidelpay GmbH
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

import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Currency;
import java.util.List;

import org.junit.Test;

import com.heidelpay.payment.Authorization;
import com.heidelpay.payment.Cancel;
import com.heidelpay.payment.Charge;
import com.heidelpay.payment.communication.HttpCommunicationException;

public class CancelTest extends AbstractPaymentTest {

	@Test
	public void testFetchCancelAuthorizationWithHeidelpay() throws MalformedURLException, HttpCommunicationException {
		Authorization authorize = getHeidelpay().authorize(getAuthorization(createPaymentTypeCard().getId(), false));
		Cancel cancelInit = authorize.cancel();
		Cancel cancel = getHeidelpay().fetchCancel(authorize.getPaymentId(),  cancelInit.getId());
		assertNotNull(cancel);
		assertNotNull(cancel.getId());
		assertEquals("COR.000.100.112", cancel.getMessage().getCode());
		assertNotNull(cancel.getMessage().getCustomer());
		assertCancelEquals(cancelInit, cancel);
	}

	@Test
	public void testFetchCancelAuthorizationWithPayment() throws MalformedURLException, HttpCommunicationException {
		Authorization authorize = getHeidelpay().authorize(getAuthorization(createPaymentTypeCard().getId(), false));
		Cancel cancelInit = authorize.cancel();
		assertEquals("COR.000.100.112", cancelInit.getMessage().getCode());
		assertNotNull(cancelInit.getMessage().getCustomer());
		List<Cancel> cancelList = cancelInit.getPayment().getCancelList();
		assertNotNull(cancelList);
		assertEquals(1, cancelList.size());
		assertCancelEquals(cancelInit, cancelList.get(0));
	}

	@Test
	public void testFetchCancelChargeWithHeidelpay() throws MalformedURLException, HttpCommunicationException {
		Charge initCharge = getHeidelpay().charge(BigDecimal.ONE, Currency.getInstance("EUR"), createPaymentTypeCard().getId(), new URL("https://www.google.at"), false);
		Cancel cancelInit = initCharge.cancel();
		assertEquals("COR.000.100.112", cancelInit.getMessage().getCode());
		assertNotNull(cancelInit.getMessage().getCustomer());
		Cancel cancel = getHeidelpay().fetchCancel(initCharge.getPaymentId(), initCharge.getId(), cancelInit.getId());
		assertNotNull(cancel);
		assertNotNull(cancel.getId());
		assertCancelEquals(cancelInit, cancel);
	}
	
	@Test
	public void testCancelChargeWithPayment() throws MalformedURLException, HttpCommunicationException {
		Charge initCharge = getHeidelpay()
				.charge(BigDecimal.ONE, Currency.getInstance("EUR"), createPaymentTypeCard().getId(),
						new URL("https://www.google.at"), false);
		Cancel cancelInit = initCharge.cancel();
		assertEquals("COR.000.100.112", cancelInit.getMessage().getCode());
		assertNotNull(cancelInit.getMessage().getCustomer());
		Cancel cancel = cancelInit.getPayment().getCharge(initCharge.getId())
				.getCancel(cancelInit.getId());
		assertNotNull(cancel);
		assertNotNull(cancel.getId());
		assertCancelEquals(cancelInit, cancel);
	}

	@Test
	public void testCancelChargeWithPaymentReference() throws MalformedURLException, HttpCommunicationException {
		Charge initCharge = getHeidelpay()
				.charge(BigDecimal.ONE, Currency.getInstance("EUR"), createPaymentTypeCard().getId(),
						new URL("https://www.google.at"), false);
		Cancel cancelReq = new Cancel();
		cancelReq.setPaymentReference("pmt-ref");
		cancelReq.setAmount(new BigDecimal(1.0));
		Cancel cancelInit = getHeidelpay().cancelCharge(initCharge.getPaymentId(), initCharge.getId(), cancelReq);
		assertEquals("COR.000.100.112", cancelInit.getMessage().getCode());
		assertNotNull(cancelInit.getMessage().getCustomer());
		assertEquals("pmt-ref", cancelInit.getPaymentReference());
		assertEquals(new BigDecimal(1.0000).setScale(4), cancelInit.getAmount());
		Cancel cancel = cancelInit.getPayment().getCharge(initCharge.getId())
				.getCancel(cancelInit.getId());
		assertNotNull(cancel);
		assertNotNull(cancel.getId());
		assertCancelEquals(cancelInit, cancel);
		assertEquals("pmt-ref", cancel.getPaymentReference());
		assertEquals(new BigDecimal(1.0000).setScale(4), cancel.getAmount());
	}

}
