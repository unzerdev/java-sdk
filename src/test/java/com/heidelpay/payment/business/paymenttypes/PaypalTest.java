package com.heidelpay.payment.business.paymenttypes;

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
import java.util.Currency;

import org.junit.Test;

import com.heidelpay.payment.Charge;
import com.heidelpay.payment.PaymentException;
import com.heidelpay.payment.business.AbstractPaymentTest;
import com.heidelpay.payment.communication.HttpCommunicationException;
import com.heidelpay.payment.paymenttypes.Paypal;

public class PaypalTest extends AbstractPaymentTest {

	@Test
	public void testCreatePaypalManatoryType() throws HttpCommunicationException {
		Paypal paypal = new Paypal();
		paypal = (Paypal) getHeidelpay().createPaymentType(paypal);
		assertNotNull(paypal.getId());
	}

	@Test(expected=PaymentException.class)
	public void testAuthorizeType() throws HttpCommunicationException, MalformedURLException {
		Paypal paypal = getHeidelpay().createPaymentType(getPaypal());
		paypal.authorize(BigDecimal.ONE, Currency.getInstance("EUR"), new URL("https://www.mpay24.com"));		
	}

	@Test
	public void testChargePaypalType() throws HttpCommunicationException, MalformedURLException {
		Paypal paypal = getHeidelpay().createPaymentType(getPaypal());
		Charge charge = paypal.charge(BigDecimal.ONE, Currency.getInstance("EUR"), new URL("https://www.google.at"));
		assertNotNull(charge);
		assertNotNull(charge.getId());
		assertNotNull(charge.getRedirectUrl());
	}

	@Test
	public void testFetchPaypalType() throws HttpCommunicationException {
		Paypal paypal = getHeidelpay().createPaymentType(getPaypal());
		assertNotNull(paypal.getId());
		Paypal fetchedPaypal = (Paypal) getHeidelpay().fetchPaymentType(paypal.getId());
		assertNotNull(fetchedPaypal.getId());
	}

	
	private Paypal getPaypal() {
		Paypal paypal = new Paypal();
		return paypal;
	}


}
