package com.heidelpay.payment.business;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Currency;

import org.junit.Test;

import com.heidelpay.payment.Cancel;
import com.heidelpay.payment.Charge;
import com.heidelpay.payment.communication.HttpCommunicationException;

public class CancelAfterChargeTest extends AbstractPaymentTest {

	@Test
	public void testFetchChargeWithId() throws MalformedURLException, HttpCommunicationException {
		Charge initCharge = getHeidelpay().charge(BigDecimal.ONE, Currency.getInstance("EUR"), createPaymentType().getId(), new URL("https://www.google.at"));
		Charge charge = getHeidelpay().fetchCharge(initCharge.getPaymentId(),  initCharge.getId());
		assertNotNull(charge);
		assertNotNull(charge.getId());
		assertChargeEquals(initCharge, charge);
	}


	@Test
	public void testFullRefundWithId() throws MalformedURLException, HttpCommunicationException {
		Charge initCharge = getHeidelpay().charge(BigDecimal.ONE, Currency.getInstance("EUR"), createPaymentType().getId(), new URL("https://www.google.at"));
		Cancel cancel = getHeidelpay().cancelCharge(initCharge.getPaymentId(), initCharge.getId());
		assertNotNull(cancel);
		assertNotNull(cancel.getId());
	}

	@Test
	public void testFullRefundWithCharge() throws MalformedURLException, HttpCommunicationException {
		Charge initCharge = getHeidelpay().charge(BigDecimal.ONE, Currency.getInstance("EUR"), createPaymentType().getId(), new URL("https://www.google.at"));
		Charge charge = getHeidelpay().fetchCharge(initCharge.getPaymentId(), initCharge.getId());
		Cancel cancel = charge.cancel();
		assertNotNull(cancel);
		assertNotNull(cancel.getId());
	}

	@Test
	public void testPartialRefundWithId() throws MalformedURLException, HttpCommunicationException {
		Charge initCharge = getHeidelpay().charge(BigDecimal.ONE, Currency.getInstance("EUR"), createPaymentType().getId(), new URL("https://www.google.at"));
		Cancel cancel = getHeidelpay().cancelCharge(initCharge.getPaymentId(), initCharge.getId(), new BigDecimal(0.1));
		assertNotNull(cancel);
	}

	@Test
	public void testPartialRefundWithCharge() throws MalformedURLException, HttpCommunicationException {
		Charge initCharge = getHeidelpay().charge(BigDecimal.ONE, Currency.getInstance("EUR"), createPaymentType().getId(), new URL("https://www.google.at"));
		Charge charge = getHeidelpay().fetchCharge(initCharge.getPaymentId(), initCharge.getId());
		Cancel cancel = charge.cancel(new BigDecimal(0.1));
		assertNotNull(cancel);
	}
	
	
}
