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

import org.junit.Ignore;
import org.junit.Test;

import com.heidelpay.payment.Authorization;
import com.heidelpay.payment.Cancel;
import com.heidelpay.payment.Payment;
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
		Authorization authorize = getHeidelpay().authorize(getAuthorization(createPaymentTypeCard().getId(), false));
		Authorization authorization = getHeidelpay().fetchAuthorization(authorize.getPaymentId());
		Cancel cancel = authorization.cancel();
		assertNotNull(cancel);
		assertNotNull(cancel.getId());
	}

	@Test
	public void partialCancelPayment() throws HttpCommunicationException, MalformedURLException {
		Authorization authorize = getHeidelpay().authorize(getAuthorization(createPaymentTypeCard().getId(), false));
		Payment payment = getHeidelpay().fetchPayment(authorize.getPaymentId());
		Cancel cancel = payment.cancel(new BigDecimal(0.1));
		assertNotNull(cancel);
		assertNotNull(cancel.getId());
		assertEquals(getBigDecimal("0.1000"), cancel.getAmount());
	}
	
	@Test
	public void partialCancelAfterAuthorization() throws HttpCommunicationException, MalformedURLException {
		Authorization authorize = getHeidelpay().authorize(getAuthorization(createPaymentTypeCard().getId(), false));
		Authorization authorization = getHeidelpay().fetchAuthorization(authorize.getPaymentId());
		Cancel cancel = authorization.cancel(new BigDecimal(0.1));
		assertNotNull(cancel);
		assertNotNull(cancel.getId());
		assertEquals(getBigDecimal("0.1000"), cancel.getAmount());
	}
	
	@Test
	public void partialCancelAfterAuthorizationHeidelpay() throws HttpCommunicationException, MalformedURLException {
		Authorization authorize = getHeidelpay().authorize(getAuthorization(createPaymentTypeCard().getId(), false));
		Cancel cancel = getHeidelpay().cancelAuthorization(authorize.getPaymentId());
		assertNotNull(cancel);
		assertNotNull(cancel.getId());
		assertEquals(getBigDecimal("10.0000"), cancel.getAmount());
	}

	@Test
	public void fetchCancelHeidelpay() throws HttpCommunicationException, MalformedURLException {
		Authorization authorize = getHeidelpay().authorize(getAuthorization(createPaymentTypeCard().getId(), false));
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
		Authorization authorize = getHeidelpay().authorize(getAuthorization(createPaymentTypeCard().getId(), false));
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

	@Ignore("Currently PAPI doens't return paymentReference in Reversal response")
	public void cancelWithPaymentReference() throws HttpCommunicationException, MalformedURLException {
		Authorization authorize = getHeidelpay().authorize(getAuthorization(createPaymentTypeCard().getId(), false));
		Cancel cancelReq = new Cancel();
		cancelReq.setPaymentReference("pmt-ref");
		Cancel cancelExecuted = authorize.cancel(cancelReq);
		assertNotNull(cancelExecuted);
		assertNotNull(cancelExecuted.getId());
		assertEquals(getBigDecimal("10.0000"), cancelExecuted.getAmount());
		assertEquals("pmt-ref", cancelExecuted.getPaymentReference());
		Cancel cancel = getHeidelpay().fetchCancel(authorize.getPaymentId(), cancelExecuted.getId());
		assertNotNull(cancel);
		assertNotNull(cancel.getId());
		assertEquals("pmt-ref", cancel.getPaymentReference());
	}
}
