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

import com.heidelpay.payment.Authorization;
import com.heidelpay.payment.business.AbstractPaymentTest;
import com.heidelpay.payment.communication.HttpCommunicationException;
import com.heidelpay.payment.paymenttypes.Prepayment;

public class PrepaymentTest extends AbstractPaymentTest {

	@Test
	public void testCreatePrepaymentManatoryType() throws HttpCommunicationException {
		Prepayment prepayment = new Prepayment();
		prepayment = getHeidelpay().createPaymentType(prepayment);
		assertNotNull(prepayment.getId());
	}

	@Test
	public void testAuthorizeSddType() throws HttpCommunicationException, MalformedURLException {
		Prepayment prepayment = getHeidelpay().createPaymentType(getPrepayment());
		Authorization authorization = prepayment.authorize(BigDecimal.ONE, Currency.getInstance("EUR"), new URL("https://www.mpay24.com"));		
		assertNotNull(authorization);
		assertNotNull(authorization.getId());
	}

	@Test
	public void testFetchPrepaymentType() throws HttpCommunicationException {
		Prepayment prepayment = getHeidelpay().createPaymentType(getPrepayment());
		assertNotNull(prepayment.getId());
		Prepayment fetchedPrepayment = (Prepayment) getHeidelpay().fetchPaymentType(prepayment.getId());
		assertNotNull(fetchedPrepayment.getId());
	}

	
	private Prepayment getPrepayment() {
		Prepayment prepayment = new Prepayment();
		return prepayment;
	}


}
