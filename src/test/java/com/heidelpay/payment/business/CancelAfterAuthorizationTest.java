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

import org.junit.Test;

import com.heidelpay.payment.Authorization;
import com.heidelpay.payment.Cancel;
import com.heidelpay.payment.communication.HttpCommunicationException;

public class CancelAfterAuthorizationTest extends AbstractPaymentTest {


	@Test
	public void fetchAuthorization() throws HttpCommunicationException, MalformedURLException {
		Authorization authorize = getHeidelpay().authorize(getAuthorization(createPaymentTypeCard().getId()));
		Authorization authorization = getHeidelpay().fetchAuthorization(authorize.getPaymentId());
		assertNotNull(authorization);
	}

	@Test
	public void fullCancelAfterAuthorization() throws HttpCommunicationException, MalformedURLException {
		Authorization authorize = getHeidelpay().authorize(getAuthorization(createPaymentTypeCard().getId()));
		Authorization authorization = getHeidelpay().fetchAuthorization(authorize.getPaymentId());
		Cancel cancel = authorization.cancel();
		assertNotNull(cancel);
		assertNotNull(cancel.getId());
	}

	@Test
	public void partialCancelAfterAuthorization() throws HttpCommunicationException, MalformedURLException {
		Authorization authorize = getHeidelpay().authorize(getAuthorization(createPaymentTypeCard().getId()));
		Authorization authorization = getHeidelpay().fetchAuthorization(authorize.getPaymentId());
		Cancel cancel = authorization.cancel(new BigDecimal(0.1));
		assertNotNull(cancel);
		assertNotNull(cancel.getId());
		assertEquals(getBigDecimal("0.1000"), cancel.getAmount());
	}
	
	@Test
	public void partialCancelAfterAuthorizationHeidelpay() throws HttpCommunicationException, MalformedURLException {
		Authorization authorize = getHeidelpay().authorize(getAuthorization(createPaymentTypeCard().getId()));
		Cancel cancel = getHeidelpay().cancelAuthorization(authorize.getPaymentId());
		assertNotNull(cancel);
		assertNotNull(cancel.getId());
		assertEquals(getBigDecimal("10.0000"), cancel.getAmount());
	}

	@Test
	public void fetchCancelHeidelpay() throws HttpCommunicationException, MalformedURLException {
		Authorization authorize = getHeidelpay().authorize(getAuthorization(createPaymentTypeCard().getId()));
		Cancel cancelExecuted = getHeidelpay().cancelAuthorization(authorize.getPaymentId());
		assertNotNull(cancelExecuted);
		assertNotNull(cancelExecuted.getId());
		assertEquals(getBigDecimal("10.0000"), cancelExecuted.getAmount());
		Cancel cancel = getHeidelpay().fetchCancel(authorize.getPaymentId(), cancelExecuted.getId());
		assertNotNull(cancel);
		assertNotNull(cancel.getId());
	}

	@Test
	public void partialCancelAfterAuthorizationIsListFilled() throws HttpCommunicationException, MalformedURLException {
		Authorization authorize = getHeidelpay().authorize(getAuthorization(createPaymentTypeCard().getId()));
		Authorization authorization = getHeidelpay().fetchAuthorization(authorize.getPaymentId());
		Cancel cancel = authorization.cancel(BigDecimal.ONE);
		assertNotNull(cancel);
		assertNotNull(cancel.getId());
		Cancel cancel2 = authorization.cancel(BigDecimal.ONE);
		assertNotNull(cancel2);
		assertNotNull(cancel2.getId());
		authorization = getHeidelpay().fetchAuthorization(authorize.getPaymentId());
		assertNotNull(authorization.getCancelList());
		assertEquals(2, authorization.getCancelList().size());
		assertEquals(2, authorization.getPayment().getAuthorization().getCancelList().size());
	}
}
