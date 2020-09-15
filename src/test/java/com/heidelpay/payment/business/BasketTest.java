package com.heidelpay.payment.business;

/*-
 * #%L
 * Heidelpay Java SDK
 * %%
 * Copyright (C) 2018 Heidelpay GmbH
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.MalformedURLException;
import java.util.List;

import org.junit.Test;

import com.heidelpay.payment.Authorization;
import com.heidelpay.payment.Basket;
import com.heidelpay.payment.BasketItem;
import com.heidelpay.payment.Charge;
import com.heidelpay.payment.Payment;
import com.heidelpay.payment.PaymentException;
import com.heidelpay.payment.communication.HttpCommunicationException;

public class BasketTest extends AbstractPaymentTest {

	@Test
	public void testCreateFetchBasket() throws HttpCommunicationException {
		Basket maxBasket = getMaxTestBasket();
		int basketItemCnt = maxBasket.getBasketItems().size();
		for(int i=0; i<basketItemCnt; i++) {
			maxBasket.getBasketItems().get(i).setParticipantId("testparticipant" + (i + 1));
		}

		Basket basket = getHeidelpay().createBasket(maxBasket);
		Basket basketFetched = getHeidelpay().fetchBasket(basket.getId());
		assertNotNull(basketFetched);
		assertNotNull(basketFetched.getId());
		assertBasketEquals(maxBasket, basketFetched);
	}

	@Test
	public void testCreateFetchMinBasket() throws HttpCommunicationException {
		Basket minBasket = getMinTestBasket();
		Basket basket = getHeidelpay().createBasket(minBasket);
		Basket basketFetched = getHeidelpay().fetchBasket(basket.getId());
		assertNotNull(basketFetched);
		assertNotNull(basketFetched.getId());
		assertBasketEquals(minBasket, basketFetched);
	}

	@Test
	public void testUpdateBasket() throws HttpCommunicationException {
		Basket minBasket = getMinTestBasket();
		Basket basket = getHeidelpay().createBasket(minBasket);
		Basket basketFetched = getHeidelpay().fetchBasket(basket.getId());
		Basket maxBasket = getMaxTestBasket();
		maxBasket.setOrderId(basket.getOrderId());
		Basket updatedBasket = getHeidelpay().updateBasket(maxBasket, basket.getId());
		assertNotNull(basketFetched);
		assertNotNull(basketFetched.getId());
		assertBasketEquals(maxBasket, updatedBasket);
	}

	@Test
	public void testUpdateBasketWithFetched() throws HttpCommunicationException {
		Basket minBasket = getMinTestBasket();
		Basket basket = getHeidelpay().createBasket(minBasket);
		Basket basketFetched = getHeidelpay().fetchBasket(basket.getId());
		basketFetched.setNote("Something changed");
		Basket updatedBasket = getHeidelpay().updateBasket(basketFetched, basket.getId());
		assertNotNull(basketFetched);
		assertNotNull(basketFetched.getId());
		assertBasketEquals(basketFetched, updatedBasket);
	}
	
	@Test
	public void testAuthorizationWithBasket() throws MalformedURLException, HttpCommunicationException {
		Basket basket = createMaxTestBasket();
		Authorization authorization = getAuthorization(createPaymentTypeCard().getId(), null, null, null, basket.getId());
		authorization.setAmount(basket.getAmountTotalGross().subtract(basket.getAmountTotalDiscount()));
		Authorization authorize = getHeidelpay().authorize(authorization);
		Payment payment = getHeidelpay().fetchPayment(authorize.getPayment().getId());
		assertNotNull(payment);
		assertNotNull(payment.getId());
		assertNotNull(payment.getAuthorization());
		assertNotNull(payment.getAuthorization().getId());
		assertEquals(basket.getId(), payment.getBasketId());
		assertEquals(basket.getId(), payment.getAuthorization().getBasketId());
	}

	@Test
	public void testChargeWithBasket() throws MalformedURLException, HttpCommunicationException {
		Basket basket = createMaxTestBasket();
		Charge chargeReq = getCharge(createPaymentTypeCard().getId(), null, null, null, basket.getId());
		chargeReq.setAmount(basket.getAmountTotalGross().subtract(basket.getAmountTotalDiscount()));
		Charge charge = getHeidelpay().charge(chargeReq);
		Payment payment = getHeidelpay().fetchPayment(charge.getPayment().getId());
		assertNotNull(payment);
		assertNotNull(payment.getId());
		assertNotNull(payment.getCharge(0));
		assertNotNull(payment.getCharge(0).getId());
		assertEquals(basket.getId(), payment.getBasketId());
		assertEquals(basket.getId(), payment.getCharge(0).getBasketId());
	}

	private Basket createMaxTestBasket() throws PaymentException, HttpCommunicationException {
		return getHeidelpay().createBasket(getMaxTestBasket());
	}


	private void assertBasketEquals(Basket expected, Basket actual) {
		assertBigDecimalEquals(expected.getAmountTotalGross(), actual.getAmountTotalGross());
		assertBigDecimalEquals(expected.getAmountTotalDiscount(), actual.getAmountTotalDiscount());
		assertBigDecimalEquals(expected.getAmountTotalVat(), actual.getAmountTotalVat());
		assertEquals(expected.getCurrencyCode(), actual.getCurrencyCode());
		assertEquals(expected.getNote(), actual.getNote());
		assertEquals(expected.getOrderId(), actual.getOrderId());
		assertEquals(expected.getTypeUrl(), actual.getTypeUrl());
		assertBasketItemsEquals(expected.getBasketItems(), actual.getBasketItems());
	}


	private void assertBigDecimalEquals(BigDecimal expected, BigDecimal actual) {
		if (expected == null) {
			return;
		}
		expected = expected.setScale(4, RoundingMode.HALF_UP);
		actual = actual.setScale(4, RoundingMode.HALF_UP);
		assertEquals(0, expected.compareTo(actual));
	}



	private void assertBasketItemsEquals(List<BasketItem> expected, List<BasketItem> actual) {
		for (int i=0; i<expected.size(); i++) {
			assertBasketItemEquals(expected.get(i), actual.get(i));
		}
	}


	private void assertBasketItemEquals(BasketItem expected, BasketItem actual) {
		assertBigDecimalEquals(expected.getAmountDiscount(), actual.getAmountDiscount());
		assertBigDecimalEquals(expected.getAmountGross(), actual.getAmountGross());
		assertBigDecimalEquals(expected.getAmountNet(), actual.getAmountNet());
		assertBigDecimalEquals(expected.getAmountPerUnit(), actual.getAmountPerUnit());
		assertBigDecimalEquals(expected.getAmountVat(), actual.getAmountVat());
		assertEquals(expected.getBasketItemReferenceId(), actual.getBasketItemReferenceId());
		assertEquals(expected.getQuantity(), actual.getQuantity());
		assertEquals(expected.getTitle(), actual.getTitle());
		assertEquals(expected.getUnit(), actual.getUnit());
		assertNumberEquals(expected.getVat(), actual.getVat());
		assertEquals(expected.getImageUrl(), actual.getImageUrl());
		assertEquals(expected.getSubTitle(), actual.getSubTitle());
		assertEquals(expected.getType(), actual.getType());
		assertEquals(expected.getParticipantId(), actual.getParticipantId());
	}

	private void assertNumberEquals(Integer expected, Integer actual) {
		if (expected != null) {
			assertEquals(expected, actual);
		}
	}


}
