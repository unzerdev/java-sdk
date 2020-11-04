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

import com.google.gson.GsonBuilder;
import com.heidelpay.payment.*;
import com.heidelpay.payment.communication.HttpCommunicationException;
import com.heidelpay.payment.communication.JsonFieldIgnoreStragegy;
import com.heidelpay.payment.marketplace.MarketplaceCharge;
import com.heidelpay.payment.marketplace.MarketplacePayment;
import com.heidelpay.payment.paymenttypes.Card;
import com.heidelpay.payment.paymenttypes.SepaDirectDebit;
import com.heidelpay.payment.paymenttypes.Sofort;
import org.junit.Test;

import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.Currency;

import static org.junit.Assert.*;

public class ChargeTest extends AbstractPaymentTest {

	@Test
	public void testChargeWithTypeIdAndEndPoint() throws MalformedURLException, HttpCommunicationException {
		Card card = getPaymentTypeCard("4444333322221111");
		card = (Card)getHeidelpayWithEndPoint("https://dev-api.heidelpay.com/v1/").createPaymentType(card);

		Charge charge = getHeidelpayWithEndPoint("https://dev-api.heidelpay.com/v1/").charge(BigDecimal.ONE, Currency.getInstance("EUR"), card.getId(), new URL("https://integration.splitit.com//gateways/Proxy/Execute?publicToken=9e517919-9e3d-4d5f-825e-99f7712eefd1"), false);
		assertNotNull(charge);
		assertNotNull(charge.getId());
		assertEquals("COR.000.100.112", charge.getMessage().getCode());
		assertNotNull(charge.getMessage().getCustomer());
	}

	@Test
	public void testChargeWithTypeId() throws MalformedURLException, HttpCommunicationException {
		Charge charge = getHeidelpay().charge(BigDecimal.ONE, Currency.getInstance("EUR"), createPaymentTypeCard().getId(), new URL("https://integration.splitit.com//gateways/Proxy/Execute?publicToken=9e517919-9e3d-4d5f-825e-99f7712eefd1"), false);
		assertNotNull(charge);
		assertNotNull(charge.getId());
		assertEquals("COR.000.100.112", charge.getMessage().getCode());
		assertNotNull(charge.getMessage().getCustomer());
	}
	
	@Test
	public void testChargeIsSuccess() throws MalformedURLException, HttpCommunicationException {
		Charge charge = getHeidelpay().charge(BigDecimal.ONE, Currency.getInstance("EUR"), createPaymentTypeCard().getId(), new URL("https://www.google.at"), false);
		assertNotNull(charge);
		assertNotNull(charge.getId());
		assertEquals("COR.000.100.112", charge.getMessage().getCode());
		assertNotNull(charge.getMessage().getCustomer());
		assertEquals(Charge.Status.SUCCESS, charge.getStatus());
		assertNotNull(charge.getTraceId());
	}

	@Test
	public void testChargeWIthInvoiceAndOrderIdIsSuccess() throws MalformedURLException, HttpCommunicationException {
		Charge requestCharge = getCharge("1234");
		requestCharge.setCard3ds(Boolean.FALSE);
		requestCharge.setInvoiceId("4567");
		Charge charge = getHeidelpay().charge(requestCharge);
		assertNotNull(charge);
		assertNotNull(charge.getId());
		assertEquals("COR.000.100.112", charge.getMessage().getCode());
		assertNotNull(charge.getMessage().getCustomer());
		assertEquals(Charge.Status.SUCCESS, charge.getStatus());
		assertEquals(requestCharge.getInvoiceId(), charge.getInvoiceId());
		assertEquals(requestCharge.getOrderId(), charge.getOrderId());
	}

	@Test
	public void testChargeWithPaymentType() throws MalformedURLException, HttpCommunicationException {
		LocalDate locaDateNow = LocalDate.now();
		Card card = new Card("4444333322221111", "12/" + (locaDateNow.getYear() + 1));
		Charge charge = getHeidelpay().charge(BigDecimal.ONE, Currency.getInstance("EUR"), card, new URL("https://www.google.at"), false);
		assertNotNull(charge);
		assertEquals("COR.000.100.112", charge.getMessage().getCode());
		assertNotNull(charge.getMessage().getCustomer());
		assertNotNull(charge.getId());
	}

	@Test
	public void testChargeWithReturnUrl() throws MalformedURLException, HttpCommunicationException {
		Charge charge = getHeidelpay().charge(BigDecimal.ONE, Currency.getInstance("EUR"), createPaymentTypeCard().getId(), new URL("https://www.google.at"), false);
		assertNotNull(charge);
		assertEquals("COR.000.100.112", charge.getMessage().getCode());
		assertNotNull(charge.getMessage().getCustomer());
		assertNotNull(charge.getId());
	}

	@Test
	public void testChargeWithCustomerTypeReturnUrl() throws MalformedURLException, HttpCommunicationException {
		LocalDate locaDateNow = LocalDate.now();
		Card card = new Card("4444333322221111", "12/" + (locaDateNow.getYear() + 1));
		Customer customer = new Customer("Rene", "Felder");
		Charge charge = getHeidelpay().charge(BigDecimal.ONE, Currency.getInstance("EUR"), card, new URL("https://www.google.at"), customer, false);
		assertNotNull(charge);
		assertEquals("COR.000.100.112", charge.getMessage().getCode());
		assertNotNull(charge.getMessage().getCustomer());
		assertNotNull(charge.getId());
	}

	@Test
	public void testChargeWithCustomerIdReturnUrl() throws MalformedURLException, HttpCommunicationException, ParseException {
		Customer customer = getHeidelpay().createCustomer(getMaximumCustomer(getRandomId()));
		Charge charge = getHeidelpay().charge(BigDecimal.ONE, Currency.getInstance("EUR"), createPaymentTypeCard().getId(), new URL("https://www.google.at"), customer.getId(), false);
		assertNotNull(charge);
		assertEquals("COR.000.100.112", charge.getMessage().getCode());
		assertNotNull(charge.getMessage().getCustomer());
	}

	@Test
	public void testChargeReturnPayment() throws MalformedURLException, HttpCommunicationException {
		LocalDate locaDateNow = LocalDate.now();
		Card card = new Card("4444333322221111", "12/" + (locaDateNow.getYear() + 1));
		Charge charge = getHeidelpay().charge(BigDecimal.ONE, Currency.getInstance("EUR"), card, new URL("https://www.google.at"), false);
		assertNotNull(charge);
		assertEquals("COR.000.100.112", charge.getMessage().getCode());
		assertNotNull(charge.getMessage().getCustomer());
		assertNotNull(charge.getId());
		assertNotNull(charge.getPayment());
		assertNotNull(charge.getPayment().getId());
	}

	@Test
	public void testChargeSofort() throws MalformedURLException, HttpCommunicationException {
		Charge charge = getHeidelpay().charge(BigDecimal.ONE, Currency.getInstance("EUR"), new Sofort(), new URL("https://www.google.at"));
		assertNotNull(charge.getRedirectUrl());
		assertNotNull(charge);
		assertEquals("COR.000.100.112", charge.getMessage().getCode());
		assertNotNull(charge.getMessage().getCustomer());
		assertNotNull(charge.getId());
		assertNotNull(charge.getPayment());
		assertNotNull(charge.getPayment().getId());
	}
	
	@Test
	public void testChargeOrderId() throws MalformedURLException, HttpCommunicationException {
		String orderId = getRandomId();
		Charge charge = getHeidelpay().charge(getCharge(orderId, false));
		assertNotNull(charge);
		assertEquals("COR.000.100.112", charge.getMessage().getCode());
		assertNotNull(charge.getMessage().getCustomer());
		assertNotNull(charge.getId());
		assertNotNull(charge.getPayment());
		assertNotNull(charge.getPayment().getId());
		Payment payment = getHeidelpay().fetchPayment(orderId);
		assertNotNull(payment);
		assertEquals(charge.getPayment().getId(), payment.getId());
		assertEquals(orderId, charge.getOrderId());
	}

	@Test
	public void testChargeWith3dsFalse() throws MalformedURLException, HttpCommunicationException {
		String orderId = getRandomId();
		Charge charge = getHeidelpay().charge(getCharge(orderId, false));
		assertNotNull(charge);
		assertNotNull(charge.getId());
		assertEquals("COR.000.100.112", charge.getMessage().getCode());
		assertNotNull(charge.getMessage().getCustomer());
		assertEquals(Charge.Status.SUCCESS, charge.getStatus());
		assertFalse(charge.getCard3ds());
	}

	@Test
	public void testChargeWith3dsTrue() throws MalformedURLException, HttpCommunicationException {
		String orderId = getRandomId();
		Charge charge = getHeidelpay().charge(getCharge(orderId, true));
		assertNotNull(charge);
		assertNotNull(charge.getId());
		assertEquals("COR.000.200.000", charge.getMessage().getCode());
		assertNotNull(charge.getMessage().getCustomer());
		assertEquals(Charge.Status.PENDING, charge.getStatus());
		assertTrue(charge.getCard3ds());
	}

	@Test
	public void testChargeWithPaymentReference() throws MalformedURLException, HttpCommunicationException {
		String orderId = getRandomId();
		Charge chargeObj = getCharge(orderId, true);
		chargeObj.setPaymentReference("pmt-ref");
		Charge charge = getHeidelpay().charge(chargeObj);
		assertNotNull(charge);
		assertNotNull(charge.getId());
		assertEquals("pmt-ref", charge.getPaymentReference());
	}

	@Test
	public void testChargeWithChargeObject() throws MalformedURLException, HttpCommunicationException {
		String orderId = getRandomId();
		Charge chargeObj = getCharge(orderId, true);
		chargeObj.setPaymentReference("pmt-ref");
		chargeObj.setAmount(new BigDecimal(1.0));
		Charge charge = getHeidelpay().charge(chargeObj);
		assertNotNull(charge);
		assertNotNull(charge.getId());
		assertEquals("pmt-ref", charge.getPaymentReference());
		assertEquals(new BigDecimal(1.0000).setScale(4), charge.getAmount());
	}

	@Test
	public void testChargeObjectIsParsableWithGson() throws HttpCommunicationException, MalformedURLException {
		Charge charge = getHeidelpay().charge(BigDecimal.ONE, Currency.getInstance("EUR"), createPaymentTypeCard().getId(), new URL("https://integration.splitit.com//gateways/Proxy/Execute?publicToken=9e517919-9e3d-4d5f-825e-99f7712eefd1"), false);
		assertEquals(String.class, new GsonBuilder()
				.addSerializationExclusionStrategy(new JsonFieldIgnoreStragegy())
				.create()
				.toJson(charge).getClass());
	}
	
	@Test
	public void testMarketplaceChargeWithCard() throws MalformedURLException, HttpCommunicationException {
		String participantId_1 = MARKETPLACE_PARTICIPANT_ID_1;
		String participantId_2 = MARKETPLACE_PARTICIPANT_ID_2;
		
		//create basket
		Basket maxBasket = getMaxTestBasket();
		maxBasket.setAmountTotalDiscount(null);
		
		maxBasket.getBasketItems().get(0).setParticipantId(participantId_1);
		maxBasket.getBasketItems().get(1).setParticipantId(participantId_2);
		
		int basketItemCnt = maxBasket.getBasketItems().size();
		for(int i=0; i<basketItemCnt; i++) {
			maxBasket.getBasketItems().get(i).setAmountDiscount(null);
		}

		Basket basket = getHeidelpay(marketplacePrivatekey).createBasket(maxBasket);	
		
		//create card
		Card card = getPaymentTypeCard(NO_3DS_VISA_CARD_NUMBER); //do not change card number except error case
		card = (Card)getHeidelpay(marketplacePrivatekey).createPaymentType(card);
		
		//marketplace charge
		MarketplaceCharge chargeRequest = getMarketplaceCharge(card.getId(), null, null, null, basket.getId(), null);
		chargeRequest.setAmount(maxBasket.getAmountTotalGross());
		
		MarketplaceCharge charge = getHeidelpay(marketplacePrivatekey).marketplaceCharge(chargeRequest);
		assertNotNull(charge.getId());
		assertNotNull(charge);
		assertEquals(AbstractTransaction.Status.PENDING, charge.getStatus());
		assertEquals(participantId_2, charge.getProcessing().getParticipantId());
		
		//get marketplace payment
		MarketplacePayment payment = getHeidelpay(marketplacePrivatekey).fetchMarketplacePayment(charge.getPayment().getId());
		assertNotNull(payment);
		assertNotNull(payment.getId());
		assertNotNull(payment.getAuthorizationsList());
		assertEquals(1, payment.getChargesList().size());
		assertEquals(Payment.State.PENDING, payment.getPaymentState());
	}
	
	@Test
	public void testMarketplaceChargeWithSepaDirectDebit() throws MalformedURLException, HttpCommunicationException {
		String participantId_1 = MARKETPLACE_PARTICIPANT_ID_1;
		String participantId_2 = MARKETPLACE_PARTICIPANT_ID_2;
		
		//create basket
		Basket maxBasket = getMaxTestBasket();
		maxBasket.setAmountTotalDiscount(null);
		
		maxBasket.getBasketItems().get(0).setParticipantId(participantId_1);
		maxBasket.getBasketItems().get(1).setParticipantId(participantId_2);
		
		int basketItemCnt = maxBasket.getBasketItems().size();
		for(int i=0; i<basketItemCnt; i++) {
			maxBasket.getBasketItems().get(i).setAmountDiscount(null);
		}

		Basket basket = getHeidelpay(marketplacePrivatekey).createBasket(maxBasket);	
		
		//create SepaDirectDebit
		SepaDirectDebit sdd = getSepaDirectDebit();
		sdd = (SepaDirectDebit)getHeidelpay(marketplacePrivatekey).createPaymentType(sdd);
		
		//marketplace charge
		MarketplaceCharge chargeRequest = getMarketplaceCharge(sdd.getId(), null, null, null, basket.getId(), null);
		chargeRequest.setAmount(maxBasket.getAmountTotalGross());
		
		MarketplaceCharge charge = getHeidelpay(marketplacePrivatekey).marketplaceCharge(chargeRequest);
		assertNotNull(charge.getId());
		assertNotNull(charge);
		assertEquals(AbstractTransaction.Status.SUCCESS, charge.getStatus());
		
		//get marketplace payment
		MarketplacePayment payment = getHeidelpay(marketplacePrivatekey).fetchMarketplacePayment(charge.getPayment().getId());
		assertNotNull(payment);
		assertNotNull(payment.getId());
		assertNotNull(payment.getAuthorizationsList());
		assertEquals(1, payment.getChargesList().size());
		assertEquals(Payment.State.COMPLETED, payment.getPaymentState());
	}
	
	@Test
	public void testMarketplaceChargeWithSofort() throws MalformedURLException, HttpCommunicationException {
		String participantId_1 = MARKETPLACE_PARTICIPANT_ID_1;
		String participantId_2 = MARKETPLACE_PARTICIPANT_ID_2;
		
		//create basket
		Basket maxBasket = getMaxTestBasket();
		maxBasket.setAmountTotalDiscount(null);
		
		maxBasket.getBasketItems().get(0).setParticipantId(participantId_1);
		maxBasket.getBasketItems().get(1).setParticipantId(participantId_2);
		
		int basketItemCnt = maxBasket.getBasketItems().size();
		for(int i=0; i<basketItemCnt; i++) {
			maxBasket.getBasketItems().get(i).setAmountDiscount(null);
		}

		Basket basket = getHeidelpay(marketplacePrivatekey).createBasket(maxBasket);	
		
		//create sofort
		Sofort sofort = new Sofort();
		sofort = (Sofort)getHeidelpay(marketplacePrivatekey).createPaymentType(sofort);
		
		//marketplace charge
		MarketplaceCharge chargeRequest = getMarketplaceCharge(sofort.getId(), null, null, null, basket.getId(), null);
		chargeRequest.setAmount(maxBasket.getAmountTotalGross());
		
		MarketplaceCharge charge = getHeidelpay(marketplacePrivatekey).marketplaceCharge(chargeRequest);
		assertNotNull(charge.getId());
		assertNotNull(charge);
		assertEquals(AbstractTransaction.Status.PENDING, charge.getStatus());
		
		//get marketplace payment
		MarketplacePayment payment = getHeidelpay(marketplacePrivatekey).fetchMarketplacePayment(charge.getPayment().getId());
		assertNotNull(payment);
		assertNotNull(payment.getId());
		assertNotNull(payment.getAuthorizationsList());
		assertEquals(1, payment.getChargesList().size());
		assertEquals(Payment.State.PENDING, payment.getPaymentState());
		
		//get marketplace charge
		MarketplaceCharge getCharge = getHeidelpay(marketplacePrivatekey).fetchMarketplaceCharge(charge.getPayment().getId(), charge.getId());
		assertNotNull(getCharge);
		assertEquals(charge.getId(), getCharge.getId());
		assertEquals(AbstractTransaction.Status.PENDING, charge.getStatus());
	}
}
