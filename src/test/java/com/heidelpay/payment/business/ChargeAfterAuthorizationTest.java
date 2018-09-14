package com.heidelpay.payment.business;

import static org.junit.Assert.assertNotNull;

import java.math.BigDecimal;
import java.net.MalformedURLException;

import org.junit.Test;

import com.heidelpay.payment.Authorization;
import com.heidelpay.payment.Charge;
import com.heidelpay.payment.communication.HttpCommunicationException;

public class ChargeAfterAuthorizationTest extends AbstractPaymentTest {

	@Test
	public void fetchAuthorization() throws HttpCommunicationException, MalformedURLException {
		Authorization authorize = getHeidelpay().authorize(getAuthorization(createPaymentType().getId()));
		Authorization authorization = getHeidelpay().fetchAuthorization(authorize.getPaymentId());
		assertNotNull(authorization);
	}

	@Test
	public void fullChargeAfterAuthorization() throws HttpCommunicationException, MalformedURLException {
		Authorization authorize = getHeidelpay().authorize(getAuthorization(createPaymentType().getId()));
		Authorization authorization = getHeidelpay().fetchAuthorization(authorize.getPaymentId());
		Charge charge = authorization.charge();
		assertNotNull(charge);
		assertNotNull(charge.getId());
	}

	@Test
	public void fullChargeAfterAuthorizationHeidelpay() throws HttpCommunicationException, MalformedURLException {
		Authorization authorize = getHeidelpay().authorize(getAuthorization(createPaymentType().getId()));
		Charge charge = getHeidelpay().chargeAuthorization(authorize.getPaymentId());
		assertNotNull(charge);
		assertNotNull(charge.getId());
	}

	@Test
	public void partialChargeAfterAuthorization() throws HttpCommunicationException, MalformedURLException {
		Authorization authorize = getHeidelpay().authorize(getAuthorization(createPaymentType().getId()));
		Authorization authorization = getHeidelpay().fetchAuthorization(authorize.getPaymentId());
		Charge charge = authorization.charge(new BigDecimal(0.1));
		assertNotNull(charge);
		assertNotNull(charge.getId());
	}
}
