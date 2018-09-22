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
import com.heidelpay.payment.business.AbstractPaymentTest;
import com.heidelpay.payment.communication.HttpCommunicationException;
import com.heidelpay.payment.paymenttypes.Przelewy24;

public class Przelewy24Test extends AbstractPaymentTest {

	@Test
	public void testCreatePrzelewy24ManatoryType() throws HttpCommunicationException {
		Przelewy24 p24 = new Przelewy24();
		p24 = getHeidelpay().createPaymentType(p24);
		assertNotNull(p24.getId());
	}

	@Test
	public void testChargePrzelewy24Type() throws HttpCommunicationException, MalformedURLException {
		Przelewy24 p24 = getHeidelpay().createPaymentType(getPrzelewy24());
		Charge charge = p24.charge(BigDecimal.ONE, Currency.getInstance("PLN"), new URL("https://www.google.at"));
		assertNotNull(charge);
		assertNotNull(charge.getId());
		assertNotNull(charge.getRedirectUrl());
	}

	@Test
	public void testFetchPrzelewy24Type() throws HttpCommunicationException {
		Przelewy24 p24 = getHeidelpay().createPaymentType(getPrzelewy24());
		assertNotNull(p24.getId());
		Przelewy24 fetchedPrzelewy24 = (Przelewy24) getHeidelpay().fetchPaymentType(p24.getId());
		assertNotNull(fetchedPrzelewy24.getId());
	}

	
	private Przelewy24 getPrzelewy24() {
		Przelewy24 p24 = new Przelewy24();
		return p24;
	}


}
