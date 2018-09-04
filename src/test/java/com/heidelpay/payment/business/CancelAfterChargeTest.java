package com.heidelpay.payment.business;

import static org.junit.Assert.assertNotNull;

import java.math.BigDecimal;
import java.net.MalformedURLException;

import org.junit.Test;

import com.heidelpay.payment.Cancel;
import com.heidelpay.payment.Charge;

public class CancelAfterChargeTest extends AbstractPaymentTest {

	@Test
	public void testFetchChargeWithId() throws MalformedURLException {
		Charge charge = getHeidelpay().fetchCharge("s-pay1",  "s-chg-1");
		assertNotNull(charge);
	}
	
	@Test
	public void testFullRefundWithId() throws MalformedURLException {
		Cancel cancel = getHeidelpay().cancelCharge("s-pay-1", "s-chg-1");
		assertNotNull(cancel);
	}

	@Test
	public void testFullRefundWithCharge() throws MalformedURLException {
		Charge charge = getHeidelpay().fetchCharge("s-pay-1", "s-chg-1");
		Cancel cancel = charge.cancel();
		assertNotNull(cancel);
	}

	@Test
	public void testPartialRefundWithId() throws MalformedURLException {
		Cancel cancel = getHeidelpay().cancelCharge("s-pay-1", "s-chg-1", new BigDecimal(0.1));
		assertNotNull(cancel);
	}

	@Test
	public void testPartialRefundWithCharge() throws MalformedURLException {
		Charge charge = getHeidelpay().fetchCharge("s-pay-1", "s-chg-1");
		Cancel cancel = charge.cancel(new BigDecimal(0.1));
		assertNotNull(cancel);
	}
}
