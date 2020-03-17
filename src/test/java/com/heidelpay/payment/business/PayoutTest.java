package com.heidelpay.payment.business;

/*-
 * #%L
 * Heidelpay Java SDK
 * %%
 * Copyright (C) 2018 - 2019 Heidelpay GmbH
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.util.Currency;

import org.junit.Test;

import com.heidelpay.payment.Basket;
import com.heidelpay.payment.PaymentException;
import com.heidelpay.payment.Payout;
import com.heidelpay.payment.communication.HttpCommunicationException;
import com.heidelpay.payment.paymenttypes.Card;

public class PayoutTest extends AbstractPaymentTest {

	@Test
	public void testPayoutCardMinimal() throws MalformedURLException, HttpCommunicationException {
		Card card = createPaymentTypeCard();
		Payout payout = getHeidelpay().payout(BigDecimal.ONE, Currency.getInstance("EUR"), card.getId(), new URL("https://www.heidelpay.com"));
		assertNotNull(payout);

		Payout payoutFetched = getHeidelpay().fetchPayout(payout.getPaymentId(), payout.getId());
		assertPayoutEqual(payout, payoutFetched);
	}
	
	@Test
	public void testPayoutCardWithAllData() throws MalformedURLException, HttpCommunicationException, ParseException {
		Card card = createPaymentTypeCard();
		Payout payout = getHeidelpay().payout(getTestPayout(card.getId()));
		assertNotNull(payout);
		assertNotNull(payout.getId());
		
		Payout payoutFetched = getHeidelpay().fetchPayout(payout.getPaymentId(), payout.getId());
		assertPayoutEqual(payout, payoutFetched);
		
	}

	private void assertPayoutEqual(Payout payout, Payout payoutFetched) {
		assertEquals(payout.getAmount(), payoutFetched.getAmount());
		assertEquals(payout.getBasketId(), payoutFetched.getBasketId());
		assertEquals(payout.getCurrency(), payoutFetched.getCurrency());
		assertEquals(payout.getCustomerId(), payoutFetched.getCustomerId());
		// Date from fetch is different than date from payout: https://heidelpay.atlassian.net/browse/AHC-1727
//		assertEquals(payout.getDate(), payoutFetched.getDate());
		assertEquals(payout.getId(), payoutFetched.getId());
		assertEquals(payout.getMetadataId(), payoutFetched.getMetadataId());
		assertEquals(payout.getOrderId(), payoutFetched.getOrderId());
		assertEquals(payout.getPaymentId(), payoutFetched.getPaymentId());
		assertEquals(payout.getPaymentReference(), payoutFetched.getPaymentReference());
		assertEquals(payout.getRedirectUrl(), payoutFetched.getRedirectUrl());
		assertEquals(payout.getReturnUrl(), payoutFetched.getReturnUrl());
		assertEquals(payout.getStatus(), payoutFetched.getStatus());
		assertEquals(payout.getTypeId(), payoutFetched.getTypeId());
		assertEquals(payout.getPaymentReference(), payoutFetched.getPaymentReference());
	}

	private Payout getTestPayout(String typeId) throws PaymentException, HttpCommunicationException, ParseException, MalformedURLException {
		Payout payout = new Payout();
		payout.setAmount(new BigDecimal(856.4900));
		payout.setCurrency(Currency.getInstance("EUR"));
		payout.setOrderId(getRandomId());
		payout.setPaymentReference("My Payment Reference");
		payout.setReturnUrl(new URL("https://www.heidelpay.com"));
		payout.setTypeId(typeId);
		payout.setBasketId(getHeidelpay().createBasket(getMaxTestBasket()).getId());
		payout.setCustomerId(createMaximumCustomerSameAddress().getId());
		payout.setMetadataId(createTestMetadata().getId());
		return payout;
	}
}
