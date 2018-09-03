package com.heidelpay.payment.business;

import static org.junit.Assert.*;

import java.math.BigDecimal;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CancelAfterAuthorizationTest extends AbstractPaymentTest {

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
	public void fullCancelAfterAuthorization() {
		Authorization authorization = getHeidelpay().fetchAuthorization("s-pay-1");
		Cancel cancel = authorization.cancel();
		assertNotNull(cancel);
	}

	@Test
	public void partialCancelAfterAuthorization() {
		Authorization authorization = getHeidelpay().fetchAuthorization("s-pay-1");
		Cancel cancel = authorization.cancel(new BigDecimal(0.1));
		assertNotNull(cancel);
	}
}
