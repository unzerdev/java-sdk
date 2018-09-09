package com.heidelpay.payment.business;

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
		Authorization authorize = getHeidelpay().authorize(getAuthorization(createPaymentType().getId()));
		Authorization authorization = getHeidelpay().fetchAuthorization(authorize.getPaymentId());
		assertNotNull(authorization);
	}

	@Test
	public void fullCancelAfterAuthorization() throws HttpCommunicationException, MalformedURLException {
		Authorization authorize = getHeidelpay().authorize(getAuthorization(createPaymentType().getId()));
		Authorization authorization = getHeidelpay().fetchAuthorization(authorize.getPaymentId());
		Cancel cancel = authorization.cancel();
		assertNotNull(cancel);
		assertNotNull(cancel.getId());
	}

	@Test
	public void partialCancelAfterAuthorization() throws HttpCommunicationException, MalformedURLException {
		Authorization authorize = getHeidelpay().authorize(getAuthorization(createPaymentType().getId()));
		Authorization authorization = getHeidelpay().fetchAuthorization(authorize.getPaymentId());
		Cancel cancel = authorization.cancel(new BigDecimal(0.1));
		assertNotNull(cancel);
		assertNotNull(cancel.getId());
		assertEquals(new BigDecimal(0.1), cancel.getAmount());
	}
}
