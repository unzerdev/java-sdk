package com.heidelpay.payment.business;

/*-
 * #%L
 * Heidelpay Java SDK
 * %%
 * Copyright (C) 2018 Heidelpay GmbH
 * %%
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 * #L%
 */

import static org.junit.Assert.assertNotNull;

import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.util.Currency;

import org.junit.Test;

import com.heidelpay.payment.Charge;
import com.heidelpay.payment.Customer;
import com.heidelpay.payment.communication.HttpCommunicationException;
import com.heidelpay.payment.paymenttypes.Card;

public class ChargeTest extends AbstractPaymentTest {
	
	@Test
	public void testChargeWithTypeId() throws MalformedURLException, HttpCommunicationException {
		Charge charge = getHeidelpay().charge(BigDecimal.ONE, Currency.getInstance("EUR"), createPaymentTypeCard().getId(), new URL("https://www.google.at"));
		assertNotNull(charge);
		assertNotNull(charge.getId());
	}
	
	@Test
	public void testChargeWithPaymentType() throws MalformedURLException, HttpCommunicationException {
		Card card = new Card("4444333322221111", "12/19");
		Charge charge = getHeidelpay().charge(BigDecimal.ONE, Currency.getInstance("EUR"), card, new URL("https://www.google.at"));
		assertNotNull(charge);
		assertNotNull(charge.getId());
	}

	@Test
	public void testChargeWithReturnUrl() throws MalformedURLException, HttpCommunicationException {
		Charge charge = getHeidelpay().charge(BigDecimal.ONE, Currency.getInstance("EUR"), createPaymentTypeCard().getId(), new URL("https://www.google.at"));
		assertNotNull(charge);
		assertNotNull(charge.getId());
	}

	@Test
	public void testChargeWithCustomerTypeReturnUrl() throws MalformedURLException, HttpCommunicationException {
		Card card = new Card("4444333322221111", "12/19");
		Customer customer = new Customer("Rene", "Felder");
		Charge charge = getHeidelpay().charge(BigDecimal.ONE, Currency.getInstance("EUR"), card, new URL("https://www.google.at"), customer);
		assertNotNull(charge);
		assertNotNull(charge.getId());
	}

	@Test
	public void testChargeWithCustomerIdReturnUrl() throws MalformedURLException, HttpCommunicationException, ParseException {
		Customer customer = getHeidelpay().createCustomer(getMaximumCustomer(getRandomId()));
		Charge charge = getHeidelpay().charge(BigDecimal.ONE, Currency.getInstance("EUR"), createPaymentTypeCard().getId(), new URL("https://www.google.at"), customer.getId());
		assertNotNull(charge);
	}

	@Test
	public void testChargeReturnPayment() throws MalformedURLException, HttpCommunicationException {
		Card card = new Card("4444333322221111", "12/19");
		Charge charge = getHeidelpay().charge(BigDecimal.ONE, Currency.getInstance("EUR"), card, new URL("https://www.google.at"));
		assertNotNull(charge);
		assertNotNull(charge.getId());
		assertNotNull(charge.getPayment());
		assertNotNull(charge.getPayment().getId());
	}

}
