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
import java.text.ParseException;
import java.util.Currency;

import org.junit.Test;

import com.heidelpay.payment.Authorization;
import com.heidelpay.payment.Cancel;
import com.heidelpay.payment.Charge;
import com.heidelpay.payment.Customer;
import com.heidelpay.payment.Payment;
import com.heidelpay.payment.PaymentException;
import com.heidelpay.payment.communication.HttpCommunicationException;
import com.heidelpay.payment.paymenttypes.Card;

public class PaymentTest extends AbstractPaymentTest {

	@Test
	public void testFetchPaymentWithAuthorization() throws MalformedURLException, HttpCommunicationException {
		Authorization authorize = getHeidelpay().authorize(getAuthorization(createPaymentTypeCard().getId()));
		Payment payment = getHeidelpay().fetchPayment(authorize.getPayment().getId());
		assertNotNull(payment);
		assertNotNull(payment.getId());
		assertNotNull(payment.getAuthorization());
		assertNotNull(payment.getAuthorization().getId());
		assertNotNull(payment.getPaymentState());
	}

	@Test
	public void testFullChargeAfterAuthorize() throws MalformedURLException, HttpCommunicationException {
		Authorization authorize = getHeidelpay().authorize(getAuthorization(createPaymentTypeCard().getId(), false));		
		Payment payment = getHeidelpay().fetchPayment(authorize.getPayment().getId());
		Charge charge = payment.charge();
		assertNotNull(charge);
		assertNotNull(charge.getId());
	}

	@Test
	public void testFetchPaymentWithCharges() throws MalformedURLException, HttpCommunicationException {
		Authorization authorize = getHeidelpay().authorize(getAuthorization(createPaymentTypeCard().getId(), false));
		Payment payment = getHeidelpay().fetchPayment(authorize.getPayment().getId());
		assertNotNull(payment);
		assertNotNull(payment.getId());
		assertNotNull(payment.getAuthorization());
		assertNotNull(payment.getAuthorization().getId());
		Charge charge = payment.charge();
		payment = charge.getPayment();
		assertNotNull(payment.getChargesList());
		assertEquals(1, payment.getChargesList().size());
		assertEquals(charge.getAmount(), payment.getCharge(0).getAmount());
		assertEquals(charge.getCurrency(), payment.getCharge(0).getCurrency());
		assertEquals(charge.getId(), payment.getCharge(0).getId());
		assertEquals(charge.getReturnUrl(), payment.getCharge(0).getReturnUrl());
		
	}

	@Test
	public void testPartialChargeAfterAuthorize() throws MalformedURLException, HttpCommunicationException {
		Authorization authorize = getHeidelpay().authorize(getAuthorization(createPaymentTypeCard().getId(), false));
		Payment payment = getHeidelpay().fetchPayment(authorize.getPayment().getId());
		Charge charge = payment.charge(BigDecimal.ONE);
		assertNotNull(charge);
		assertEquals("s-chg-1", charge.getId());
		assertEquals(getBigDecimal("1.0000"), charge.getAmount());
	}

	@Test(expected=PaymentException.class)
	public void testFullCancelAuthorize() throws MalformedURLException, HttpCommunicationException {
		Authorization authorize = getHeidelpay().authorize(getAuthorization(createPaymentTypeCard().getId()));
		Payment payment = getHeidelpay().fetchPayment(authorize.getPayment().getId());
		Cancel cancel = payment.getAuthorization().cancel();
		assertNotNull(cancel);
		assertEquals("s-cnl-1", cancel.getId());
		assertEquals(authorize.getAmount(), cancel.getAmount());
		payment.cancel();
	}

	@Test
	public void testPartialCancelAuthorize() throws MalformedURLException, HttpCommunicationException {
		Authorization authorize = getHeidelpay().authorize(getAuthorization(createPaymentTypeCard().getId(), false));
		Payment payment = getHeidelpay().fetchPayment(authorize.getPayment().getId());
		Cancel cancel = payment.cancel(BigDecimal.ONE);
		assertNotNull(cancel);
		assertEquals("s-cnl-1", cancel.getId());
		assertEquals(getBigDecimal("1.0000"), cancel.getAmount());
	}

	@Test
	public void testFullCancelOnCharge() throws HttpCommunicationException, MalformedURLException {
		Charge charge = getHeidelpay().charge(BigDecimal.ONE, Currency.getInstance("EUR"), createPaymentTypeCard().getId(), new URL("https://www.google.at"), false);
		Payment payment = getHeidelpay().fetchPayment(charge.getPaymentId());
		Cancel cancel = payment.getCharge("s-chg-1").cancel();
		assertNotNull(cancel);
	}

	@Test
	public void testPartialCancelOnCharge() throws HttpCommunicationException, MalformedURLException {
		Charge charge = getHeidelpay().charge(BigDecimal.ONE, Currency.getInstance("EUR"), createPaymentTypeCard().getId(), new URL("https://www.google.at"), false);
		Payment payment = getHeidelpay().fetchPayment(charge.getPaymentId());
		Cancel cancel = payment.getCharge(0).cancel(BigDecimal.ONE);
		assertNotNull(cancel);
	}
	
	@Test
	public void testAuthorize() throws HttpCommunicationException, MalformedURLException{
		// Variant 1
		Payment payment = new Payment(getHeidelpay());
		Authorization authorizationUsingPayment = payment.authorize(BigDecimal.ONE, Currency.getInstance("EUR"), createPaymentTypeCard().getId(), new URL("https://www.google.at"));
		
		// Variant 2
		Authorization authorizationUsingHeidelpay = getHeidelpay().authorize(BigDecimal.ONE, Currency.getInstance("EUR"), createPaymentTypeCard().getId(), new URL("https://www.google.at"));
		authorizationUsingHeidelpay.getPayment();
		
		assertNotNull(authorizationUsingPayment);
		assertNotNull(authorizationUsingHeidelpay);
	}
	
	@Test
	public void testAuthorizeWithExistedCustomer() throws HttpCommunicationException, MalformedURLException, ParseException{
		// Variant 1
		Payment payment = new Payment(getHeidelpay());
		Authorization authorizationUsingPayment = payment.authorize(BigDecimal.ONE, Currency.getInstance("EUR"), createPaymentTypeCard().getId(), new URL("https://www.google.at"), createFactoringOKCustomer().getId());
				
		Authorization authorizationUsingHeidelpay = getHeidelpay().authorize(BigDecimal.ONE, Currency.getInstance("EUR"), createPaymentTypeCard().getId(), new URL("https://www.google.at"), createFactoringOKCustomer().getId());
		authorizationUsingHeidelpay.getPayment();
		
		assertNotNull(authorizationUsingPayment);
		assertNotNull(authorizationUsingHeidelpay);
	}
	
	@Test
	public void testAuthorizeWithNotExistedPaymentTypeAndCustomer() throws HttpCommunicationException, MalformedURLException, ParseException{
		Customer customerRequest = getMaximumCustomer("");
		Card cardRequest = getPaymentTypeCard();
		// Variant 1
		Payment payment = new Payment(getHeidelpay());
		Authorization authorizationUsingPayment = payment.authorize(BigDecimal.ONE, Currency.getInstance("EUR"), cardRequest, new URL("https://www.google.at"), customerRequest);
				
		Authorization authorizationUsingHeidelpay = getHeidelpay().authorize(BigDecimal.ONE, Currency.getInstance("EUR"), cardRequest, new URL("https://www.google.at"), customerRequest);
		authorizationUsingHeidelpay.getPayment();
		
		assertNotNull(authorizationUsingPayment);
		assertNotNull(authorizationUsingHeidelpay);
	}
	
	@Test
	public void testChargeWithoutAuthorize() throws HttpCommunicationException, MalformedURLException {
		Charge chargeUsingPayment = new Payment(getHeidelpay()).charge(BigDecimal.ONE, Currency.getInstance("EUR"), createPaymentTypeCard().getId(), new URL("https://www.google.at"));
		Charge chargeUsingHeidelpay = getHeidelpay().charge(BigDecimal.ONE, Currency.getInstance("EUR"), createPaymentTypeCard().getId(), new URL("https://www.google.at"));
		assertNotNull(chargeUsingPayment);
		assertNotNull(chargeUsingHeidelpay);
	}
}
