package com.heidelpay.payment.business.paymenttypes;

/*-
 * #%L
 * Heidelpay Java SDK
 * %%
 * Copyright (C) 2018 Heidelpay GmbH
 * %%
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 * #L%
 */

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Currency;

import org.junit.Test;

import com.heidelpay.payment.Authorization;
import com.heidelpay.payment.Charge;
import com.heidelpay.payment.Heidelpay;
import com.heidelpay.payment.Payment;
import com.heidelpay.payment.PaymentException;
import com.heidelpay.payment.business.AbstractPaymentTest;
import com.heidelpay.payment.communication.HttpCommunicationException;
import com.heidelpay.payment.paymenttypes.Card;

public class CardTest extends AbstractPaymentTest {

	
	@Test(expected=PaymentException.class)
	public void testCreateCardWithMerchantNotPCIDSSCompliant() throws HttpCommunicationException {
		Card card = new Card("4444333322221111", "03/20");
		card.setCvc("123");
		Heidelpay heidelpay = new Heidelpay("s-priv-2a107CYZMp3UbyVPAuqWoxQHi9nFyeiW");
		card = heidelpay.createPaymentType(card);
		assertNotNull(card.getId());
	}

	@Test
	public void testCreateCardType() throws HttpCommunicationException {
		Card card = new Card("4444333322221111", "03/20");
		card.setCvc("123");
		card = getHeidelpay().createPaymentType(card);
		assertNotNull(card.getId());
	}

	@Test
	public void testAuthorizeCardType() throws HttpCommunicationException, MalformedURLException {
		Card card = new Card("4444333322221111", "03/20");
		card.setCvc("123");
		card = getHeidelpay().createPaymentType(card);
		Authorization authorization = card.authorize(BigDecimal.ONE, Currency.getInstance("EUR"), new URL("https://www.mpay24.com"));
		assertNotNull(authorization);
		assertNotNull(authorization.getId());
	}

	@Test
	public void testAuthorizeAndPaymentCardType() throws HttpCommunicationException, MalformedURLException {
		Card card = new Card("4444333322221111", "03/20").setCvc("123");
		card.setCvc("123");
		card = getHeidelpay().createPaymentType(card);
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
		card = getHeidelpay().createPaymentType(card);
		Charge charge = card.charge(BigDecimal.ONE, Currency.getInstance("EUR"), new URL("https://www.google.at"));
		assertNotNull(charge);
	}
	
	@Test
	public void testFetchCardType() throws HttpCommunicationException {
		Card card = new Card("4444333322221111", "03/2020");
		card.setCvc("123");
		
		Card createdCard = getHeidelpay().createPaymentType(card);
		assertNotNull(createdCard.getId());
		assertNotNull(createdCard.getId());
		assertEquals(maskString(card.getNumber(), 6, card.getNumber().length()-4, '*'), createdCard.getNumber());
		assertEquals(card.getExpiryDate(), createdCard.getExpiryDate());
		assertNotNull(createdCard.getCvc());

		Card fetchedCard = (Card)getHeidelpay().fetchPaymentType(createdCard.getId());
		assertNotNull(fetchedCard.getId());
		assertEquals(maskString(card.getNumber(), 6, card.getNumber().length()-4, '*'), fetchedCard.getNumber());
		assertEquals(card.getExpiryDate(), fetchedCard.getExpiryDate());
		assertNotNull(fetchedCard.getCvc());
	}

}
