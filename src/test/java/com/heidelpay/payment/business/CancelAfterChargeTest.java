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

import org.junit.Test;

import com.heidelpay.payment.Cancel;
import com.heidelpay.payment.Charge;
import com.heidelpay.payment.communication.HttpCommunicationException;

public class CancelAfterChargeTest extends AbstractPaymentTest {

	@Test
	public void testFetchChargeWithId() throws MalformedURLException, HttpCommunicationException {
		Charge initCharge = getHeidelpay().charge(BigDecimal.ONE, Currency.getInstance("EUR"), createPaymentTypeCard().getId(), new URL("https://www.google.at"), false);
		Charge charge = getHeidelpay().fetchCharge(initCharge.getPaymentId(),  initCharge.getId());
		assertNotNull(charge);
		assertNotNull(charge.getId());
		assertChargeEquals(initCharge, charge);
	}


	@Test
	public void testFullRefundWithId() throws MalformedURLException, HttpCommunicationException {
		Charge initCharge = getHeidelpay().charge(BigDecimal.ONE, Currency.getInstance("EUR"), createPaymentTypeCard().getId(), new URL("https://www.google.at"), false);
		Cancel cancel = getHeidelpay().cancelCharge(initCharge.getPaymentId(), initCharge.getId());
		assertNotNull(cancel);
		assertNotNull(cancel.getId());
	}

	@Test
	public void testFullRefundWithCharge() throws MalformedURLException, HttpCommunicationException {
		Charge initCharge = getHeidelpay().charge(BigDecimal.ONE, Currency.getInstance("EUR"), createPaymentTypeCard().getId(), new URL("https://www.google.at"), false);
		Charge charge = getHeidelpay().fetchCharge(initCharge.getPaymentId(), initCharge.getId());
		Cancel cancel = charge.cancel();
		assertNotNull(cancel);
		assertNotNull(cancel.getId());
	}

	@Test
	public void testPartialRefundWithId() throws MalformedURLException, HttpCommunicationException {
		Charge initCharge = getHeidelpay().charge(BigDecimal.ONE, Currency.getInstance("EUR"), createPaymentTypeCard().getId(), new URL("https://www.google.at"), false);
		Cancel cancel = getHeidelpay().cancelCharge(initCharge.getPaymentId(), initCharge.getId(), new BigDecimal(0.1));
		assertNotNull(cancel);
	}

	@Test
	public void testPartialRefundWithCharge() throws MalformedURLException, HttpCommunicationException {
		Charge initCharge = getHeidelpay().charge(BigDecimal.ONE, Currency.getInstance("EUR"), createPaymentTypeCard().getId(), new URL("https://www.google.at"), false);
		Charge charge = getHeidelpay().fetchCharge(initCharge.getPaymentId(), initCharge.getId());
		Cancel cancelExecuted = charge.cancel(new BigDecimal(0.1));
		assertNotNull(cancelExecuted);
		Cancel cancel = getHeidelpay().fetchCancel(initCharge.getPaymentId(), charge.getId(), cancelExecuted.getId());
		assertNotNull(cancel);
	}

	@Test
	public void testCancelAfterChargeChargeWithPaymentReference() throws MalformedURLException, HttpCommunicationException {
		Charge initCharge = getHeidelpay().charge(BigDecimal.ONE, Currency.getInstance("EUR"), createPaymentTypeCard().getId(), new URL("https://www.google.at"), false);
		Cancel cancelReq = new Cancel();
		cancelReq.setPaymentReference("pmt-ref");
		Cancel cancelInit = initCharge.cancel(cancelReq);
		assertEquals("COR.000.100.112", cancelInit.getMessage().getCode());
		assertNotNull(cancelInit.getMessage().getCustomer());
		assertEquals("pmt-ref", cancelInit.getPaymentReference());
		Cancel cancel = cancelInit.getPayment().getCharge(initCharge.getId()).getCancel(cancelInit.getId());
		assertNotNull(cancel);
		assertNotNull(cancel.getId());
		assertCancelEquals(cancelInit, cancel);
		assertEquals("pmt-ref", cancel.getPaymentReference());
	}

	@Test
	public void testCancelAfterChargeChargeWithCancelObject() throws MalformedURLException, HttpCommunicationException {
		Charge initCharge = getHeidelpay().charge(BigDecimal.ONE, Currency.getInstance("EUR"), createPaymentTypeCard().getId(), new URL("https://www.google.at"), false);
		Cancel cancelReq = new Cancel();
		cancelReq.setPaymentReference("pmt-ref");
		cancelReq.setAmount(new BigDecimal(1.0));
		Cancel cancelInit = initCharge.cancel(cancelReq);
		assertEquals("COR.000.100.112", cancelInit.getMessage().getCode());
		assertNotNull(cancelInit.getMessage().getCustomer());
		assertEquals("pmt-ref", cancelInit.getPaymentReference());
		assertEquals(new BigDecimal(1.0000).setScale(4), cancelInit.getAmount());
		Cancel cancel = cancelInit.getPayment().getCharge(initCharge.getId()).getCancel(cancelInit.getId());
		assertNotNull(cancel);
		assertNotNull(cancel.getId());
		assertCancelEquals(cancelInit, cancel);
		assertEquals("pmt-ref", cancel.getPaymentReference());
		assertEquals(new BigDecimal(1.0000).setScale(4), cancel.getAmount());
	}

}
