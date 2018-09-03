package com.heidelpay.payment.business.paymenttypes;

import static org.junit.Assert.assertNotNull;

import java.math.BigDecimal;
import java.util.Currency;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.heidelpay.payment.business.AbstractPaymentTest;
import com.heidelpay.payment.business.Authorization;
import com.heidelpay.payment.business.Charge;
import com.heidelpay.payment.business.Payment;

public class CardTest extends AbstractPaymentTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testCreateCardType() {
		Card card = new Card("4444333322221111", "03/20");
		card.setCvc("123");
		card = (Card)getHeidelpay().createPaymentType(card);
		assertNotNull(card.getId());
		
		getHeidelpay().authorize(BigDecimal.ONE, Currency.getInstance("EUR"), card.getId());
	}

	@Test
	public void testAuthorizeCardType() {
		Card card = new Card("4444333322221111", "03/20");
		card.setCvc("123");
		card = (Card)getHeidelpay().createPaymentType(card);
		Authorization authorization = card.authorize(BigDecimal.ONE, Currency.getInstance("EUR"));
		Payment payment = authorization.getPayment();
		assertNotNull(authorization);
	}

	@Test
	public void testChargeCardType() {
		Card card = new Card("4444333322221111", "03/20");
		card.setCvc("123");
		card = (Card)getHeidelpay().createPaymentType(card);
		Charge charge = card.charge(BigDecimal.ONE, Currency.getInstance("EUR"));
		assertNotNull(charge);
	}
}
