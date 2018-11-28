package com.heidelpay.payment.business;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.MalformedURLException;
import java.util.Currency;
import java.util.List;

import org.junit.Test;

import com.heidelpay.payment.AbstractPayment;
import com.heidelpay.payment.Authorization;
import com.heidelpay.payment.Basket;
import com.heidelpay.payment.BasketItem;
import com.heidelpay.payment.Charge;
import com.heidelpay.payment.Payment;
import com.heidelpay.payment.PaymentException;
import com.heidelpay.payment.communication.HttpCommunicationException;

public class BasketTest extends AbstractPaymentTest {

	@Test
	public void testCreateFetchBasket() throws MalformedURLException, HttpCommunicationException {
		Basket maxBasket = getMaxTestBasket();
		Basket basket = getHeidelpay().createBasket(maxBasket);
		Basket basketFetched = getHeidelpay().fetchBasket(basket.getId());
		assertNotNull(basketFetched);
		assertNotNull(basketFetched.getId());
		assertBasketEquals(maxBasket, basketFetched);
	}

	@Test
	public void testAuthorizationWithBasket() throws MalformedURLException, HttpCommunicationException {
		String basketId = createMaxTestBasket().getId();
		Authorization authorize = getHeidelpay().authorize(getAuthorization(createPaymentTypeCard().getId(), null, null, null, basketId));
		Payment payment = getHeidelpay().fetchPayment(authorize.getPayment().getId());
		assertNotNull(payment);
		assertNotNull(payment.getId());
		assertNotNull(payment.getAuthorization());
		assertNotNull(payment.getAuthorization().getId());
		// TODO Currently a bug in production
//		assertEquals(basketId, payment.getBasketId());
//		assertEquals(basketId, payment.getAuthorization().getBasketId());
	}

	@Test
	public void testChargeWithBasket() throws MalformedURLException, HttpCommunicationException {
		String basketId = createMaxTestBasket().getId();
		Charge charge = getHeidelpay().charge(getCharge(createPaymentTypeCard().getId(), null, null, null, basketId));
		Payment payment = getHeidelpay().fetchPayment(charge.getPayment().getId());
		assertNotNull(payment);
		assertNotNull(payment.getId());
		assertNotNull(payment.getCharge(0));
		assertNotNull(payment.getCharge(0).getId());
		// TODO Currently a bug in production
//		assertEquals(basketId, payment.getBasketId());
//		assertEquals(basketId, payment.getCharge(0).getBasketId());
	}

	private Basket createMaxTestBasket() throws PaymentException, HttpCommunicationException {
		return getHeidelpay().createBasket(getMaxTestBasket());
	}

	private Basket getMaxTestBasket() {
		Basket basket = new Basket();
		basket.setAmountTotal(new BigDecimal(500.5));
		basket.setAmountTotalDiscount(BigDecimal.TEN);
		basket.setCurrencyCode(Currency.getInstance("EUR"));
		basket.setNote("Mistery shopping");
		basket.setOrderId(getRandomId());
		basket.addBasketItem(getMaxTestBasketItem());
		return basket;
	}

	private BasketItem getMaxTestBasketItem() {
		BasketItem basketItem = new BasketItem();
		basketItem.setBasketItemReferenceId("Artikelnummer 4711");
		basketItem.setAmountDiscount(BigDecimal.ONE);
		basketItem.setAmountGross(new BigDecimal(500.5));
		basketItem.setAmountNet(new BigDecimal(420.1));
		basketItem.setAmountPerUnit(new BigDecimal(100.1));
		basketItem.setAmountVat(new BigDecimal(80.4));
		basketItem.setQuantity(5);
		basketItem.setTitle("Apple iPhone");
		basketItem.setUnit("Pc.");
		basketItem.setVat(19);
		
		return basketItem;
	}
	private void assertBasketEquals(Basket expected, Basket actual) {
		assertBigDecimalEquals(expected.getAmountTotal(), actual.getAmountTotal());
		assertBigDecimalEquals(expected.getAmountTotalDiscount(), actual.getAmountTotalDiscount());
		assertEquals(expected.getCurrencyCode(), actual.getCurrencyCode());
		assertEquals(expected.getNote(), actual.getNote());
		assertEquals(expected.getOrderId(), actual.getOrderId());
		assertEquals(expected.getTypeUrl(), actual.getTypeUrl());
		assertBasketItemsEquals(expected.getBasketItems(), actual.getBasketItems());
	}


	private void assertBigDecimalEquals(BigDecimal expected, BigDecimal actual) {
		expected = expected.setScale(4, RoundingMode.HALF_UP);
		actual = actual.setScale(4, RoundingMode.HALF_UP);
		assertTrue( expected.compareTo(actual) == 0);
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
		assertEquals(expected.getVat(), actual.getVat());
	}


}