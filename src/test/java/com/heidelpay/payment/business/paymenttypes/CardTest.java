package com.heidelpay.payment.business.paymenttypes;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Currency;

import org.junit.Test;

import com.heidelpay.payment.Authorization;
import com.heidelpay.payment.Charge;
import com.heidelpay.payment.Payment;
import com.heidelpay.payment.business.AbstractPaymentTest;
import com.heidelpay.payment.communication.HttpCommunicationException;
import com.heidelpay.payment.paymenttypes.Card;

public class CardTest extends AbstractPaymentTest {

	@Test
	public void testCreateCardType() throws HttpCommunicationException {
		Card card = new Card("4444333322221111", "03/20");
		card.setCvc("123");
		card = (Card)getHeidelpay().createPaymentType(card);
		assertNotNull(card.getId());
	}

	@Test
	public void testAuthorizeCardType() throws HttpCommunicationException, MalformedURLException {
		Card card = new Card("4444333322221111", "03/20");
		card.setCvc("123");
		card = (Card)getHeidelpay().createPaymentType(card);
		Authorization authorization = card.authorize(BigDecimal.ONE, Currency.getInstance("EUR"), new URL("https://www.mpay24.com"));
		assertNotNull(authorization);
		assertNotNull(authorization.getId());
	}

	@Test
	public void testAuthorizeAndPaymentCardType() throws HttpCommunicationException, MalformedURLException {
		Card card = new Card("4444333322221111", "03/20");
		card.setCvc("123");
		card = (Card)getHeidelpay().createPaymentType(card);
		Authorization authorization = card.authorize(BigDecimal.ONE, Currency.getInstance("EUR"), new URL("https://www.mpay24.com"));
		Payment payment = authorization.getPayment();
		assertNotNull(authorization);
		assertNotNull(payment);
		assertNotNull(authorization.getId());
	}

	@Test
	public void testChargeCardType() throws HttpCommunicationException, MalformedURLException {
		Card card = new Card("4444333322221111", "03/20");
		card.setCvc("123");
		card = (Card)getHeidelpay().createPaymentType(card);
		Charge charge = card.charge(BigDecimal.ONE, Currency.getInstance("EUR"), new URL("https://www.google.at"));
		assertNotNull(charge);
	}
	
	@Test
	public void testFetchCardType() throws HttpCommunicationException {
		Card card = new Card("4444333322221111", "03/20");
		card.setCvc("123");
		card = (Card)getHeidelpay().createPaymentType(card);
		assertNotNull(card.getId());
		Card fetchedCard = (Card)getHeidelpay().fetchPaymentType(card.getId());
		assertNotNull(fetchedCard.getId());
		assertEquals(card.getNumber(), fetchedCard.getNumber());
		assertEquals(card.getExpiryDate(), fetchedCard.getExpiryDate());
		assertEquals(card.getCvc(), fetchedCard.getCvc());
	}

}
