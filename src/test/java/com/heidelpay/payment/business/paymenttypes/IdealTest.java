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
import com.heidelpay.payment.paymenttypes.Ideal;

public class IdealTest extends AbstractPaymentTest {

	@Test
	public void testCreateIdealManatoryType() throws HttpCommunicationException {
		Ideal ideal = (Ideal) getHeidelpay().createPaymentType(getIdeal());
		assertNotNull(ideal.getId());
	}

	@Test
	public void testChargeIdealType() throws HttpCommunicationException, MalformedURLException {
		Ideal ideal = getHeidelpay().createPaymentType(getIdeal());
		Charge charge = ideal.charge(BigDecimal.ONE, Currency.getInstance("EUR"), new URL("https://www.google.at"));
		assertNotNull(charge);
		assertNotNull(charge.getId());
		assertNotNull(charge.getRedirectUrl());
	}

	@Test
	public void testFetchIdealType() throws HttpCommunicationException {
		Ideal ideal = getHeidelpay().createPaymentType(getIdeal());
		assertNotNull(ideal.getId());
		Ideal fetchedIdeal = (Ideal) getHeidelpay().fetchPaymentType(ideal.getId());
		assertNotNull(fetchedIdeal.getId());
	}

	
	private Ideal getIdeal() {
		Ideal ideal = new Ideal().setBic("RABONL2U");
		return ideal;
	}


}
