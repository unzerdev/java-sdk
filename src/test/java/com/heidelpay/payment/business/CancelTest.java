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

import static org.junit.Assert.*;

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
		Authorization authorize = getHeidelpay().authorize(getAuthorization(createPaymentTypeCard().getId()));
		Cancel cancelInit = authorize.cancel();
		Cancel cancel = getHeidelpay().fetchCancel(authorize.getPaymentId(),  cancelInit.getId());
		assertNotNull(cancel);
		assertNotNull(cancel.getId());
		assertCancelEquals(cancelInit, cancel);
	}

	@Test
	public void testFetchCancelAuthorizationWithPayment() throws MalformedURLException, HttpCommunicationException {
		Authorization authorize = getHeidelpay().authorize(getAuthorization(createPaymentTypeCard().getId()));
		Cancel cancelInit = authorize.cancel();
		List<Cancel> cancelList = cancelInit.getPayment().getCancelList();
		assertNotNull(cancelList);
		assertEquals(1, cancelList.size());
		assertCancelEquals(cancelInit, cancelList.get(0));
	}

	@Test
	public void testFetchCancelChargeWithHeidelpay() throws MalformedURLException, HttpCommunicationException {
		Charge initCharge = getHeidelpay().charge(BigDecimal.ONE, Currency.getInstance("EUR"), createPaymentTypeCard().getId(), new URL("https://www.google.at"));
		Cancel cancelInit = initCharge.cancel();
		Cancel cancel = getHeidelpay().fetchCancel(initCharge.getPaymentId(), initCharge.getId(), cancelInit.getId());
		assertNotNull(cancel);
		assertNotNull(cancel.getId());
		assertCancelEquals(cancelInit, cancel);
	}
	
	@Test
	public void testFetchCancelChargeWithPayment() throws MalformedURLException, HttpCommunicationException {
		Charge initCharge = getHeidelpay().charge(BigDecimal.ONE, Currency.getInstance("EUR"), createPaymentTypeCard().getId(), new URL("https://www.google.at"));
		Cancel cancelInit = initCharge.cancel();
		Cancel cancel = cancelInit.getPayment().getCharge(initCharge.getId()).getCancel(cancelInit.getId());
		assertNotNull(cancel);
		assertNotNull(cancel.getId());
		assertCancelEquals(cancelInit, cancel);
	}
}
