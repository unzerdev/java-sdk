package com.heidelpay.payment.business;

import static org.junit.Assert.*;

import java.math.BigDecimal;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.heidelpay.payment.Authorization;
import com.heidelpay.payment.Charge;

public class ChargeAfterAuthorizationTest extends AbstractPaymentTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void fetchAuthorization() {
		Authorization authorization = getHeidelpay().fetchAuthorization("s-pay-1");
		assertNotNull(authorization);
	}

	@Test
	public void fullChargeAfterAuthorization() {
		Authorization authorization = getHeidelpay().fetchAuthorization("s-pay-1");
		Charge charge = authorization.charge();
		assertNotNull(charge);
	}

	@Test
	public void partialChargeAfterAuthorization() {
		Authorization authorization = getHeidelpay().fetchAuthorization("s-pay-1");
		Charge charge = authorization.charge(new BigDecimal(0.1));
		assertNotNull(charge);
	}
}
