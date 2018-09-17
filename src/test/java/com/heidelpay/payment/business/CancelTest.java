package com.heidelpay.payment.business;

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
