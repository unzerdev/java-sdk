package com.heidelpay.payment.business;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.awaitility.Awaitility.await;
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
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Currency;

import org.apache.http.HttpStatus;
import org.junit.Test;

import com.heidelpay.payment.AbstractPayment;
import com.heidelpay.payment.AbstractTransaction;
import com.heidelpay.payment.Basket;
import com.heidelpay.payment.Cancel;
import com.heidelpay.payment.Charge;
import com.heidelpay.payment.Payment;
import com.heidelpay.payment.communication.HttpCommunicationException;
import com.heidelpay.payment.marketplace.MarketplaceCancel;
import com.heidelpay.payment.marketplace.MarketplaceCharge;
import com.heidelpay.payment.marketplace.MarketplacePayment;
import com.heidelpay.payment.paymenttypes.Card;

public class CancelAfterChargeTest extends AbstractPaymentTest {

	@Test
	public void testFetchChargeWithId() throws MalformedURLException, HttpCommunicationException {
		Charge initCharge = getHeidelpay().charge(BigDecimal.ONE, Currency.getInstance("EUR"), createPaymentTypeCard().getId(), new URL("https://www.google.at"), false);
		Charge charge = getHeidelpay().fetchCharge(initCharge.getPaymentId(),  initCharge.getId());
		assertNotNull(charge);
		assertNotNull(charge.getId());
		assertChargeEquals(initCharge, charge);
	}


	@Test
	public void testFullRefundWithId() throws MalformedURLException, HttpCommunicationException {
		Charge initCharge = getHeidelpay().charge(BigDecimal.ONE, Currency.getInstance("EUR"), createPaymentTypeCard().getId(), new URL("https://www.google.at"), false);
		Cancel cancel = getHeidelpay().cancelCharge(initCharge.getPaymentId(), initCharge.getId());
		assertNotNull(cancel);
		assertNotNull(cancel.getId());
	}

	@Test
	public void testFullRefundWithCharge() throws MalformedURLException, HttpCommunicationException {
		Charge initCharge = getHeidelpay().charge(BigDecimal.ONE, Currency.getInstance("EUR"), createPaymentTypeCard().getId(), new URL("https://www.google.at"), false);
		Charge charge = getHeidelpay().fetchCharge(initCharge.getPaymentId(), initCharge.getId());
		Cancel cancel = charge.cancel();
		assertNotNull(cancel);
		assertNotNull(cancel.getId());
	}

	@Test
	public void testPartialRefundWithId() throws MalformedURLException, HttpCommunicationException {
		Charge initCharge = getHeidelpay().charge(BigDecimal.ONE, Currency.getInstance("EUR"), createPaymentTypeCard().getId(), new URL("https://www.google.at"), false);
		Cancel cancel = getHeidelpay().cancelCharge(initCharge.getPaymentId(), initCharge.getId(), new BigDecimal(0.1));
		assertNotNull(cancel);
	}

	@Test
	public void testPartialRefundWithCharge() throws MalformedURLException, HttpCommunicationException {
		Charge initCharge = getHeidelpay().charge(BigDecimal.ONE, Currency.getInstance("EUR"), createPaymentTypeCard().getId(), new URL("https://www.google.at"), false);
		Charge charge = getHeidelpay().fetchCharge(initCharge.getPaymentId(), initCharge.getId());
		Cancel cancelExecuted = charge.cancel(new BigDecimal(0.1));
		assertNotNull(cancelExecuted);
		Cancel cancel = getHeidelpay().fetchCancel(initCharge.getPaymentId(), charge.getId(), cancelExecuted.getId());
		assertNotNull(cancel);
	}

	@Test
	public void testCancelAfterChargeChargeWithPaymentReference() throws MalformedURLException, HttpCommunicationException {
		Charge initCharge = getHeidelpay().charge(BigDecimal.ONE, Currency.getInstance("EUR"), createPaymentTypeCard().getId(), new URL("https://www.google.at"), false);
		Cancel cancelReq = new Cancel();
		cancelReq.setPaymentReference("pmt-ref");
		Cancel cancelInit = initCharge.cancel(cancelReq);
		assertEquals("COR.000.100.112", cancelInit.getMessage().getCode());
		assertNotNull(cancelInit.getMessage().getCustomer());
		assertEquals("pmt-ref", cancelInit.getPaymentReference());
		Cancel cancel = cancelInit.getPayment().getCharge(initCharge.getId()).getCancel(cancelInit.getId());
		assertNotNull(cancel);
		assertNotNull(cancel.getId());
		assertCancelEquals(cancelInit, cancel);
		assertEquals("pmt-ref", cancel.getPaymentReference());
	}

	@Test
	public void testCancelAfterChargeChargeWithCancelObject() throws MalformedURLException, HttpCommunicationException {
		Charge initCharge = getHeidelpay().charge(BigDecimal.ONE, Currency.getInstance("EUR"), createPaymentTypeCard().getId(), new URL("https://www.google.at"), false);
		Cancel cancelReq = new Cancel();
		cancelReq.setPaymentReference("pmt-ref");
		cancelReq.setAmount(new BigDecimal(1.0));
		Cancel cancelInit = initCharge.cancel(cancelReq);
		assertEquals("COR.000.100.112", cancelInit.getMessage().getCode());
		assertNotNull(cancelInit.getMessage().getCustomer());
		assertEquals("pmt-ref", cancelInit.getPaymentReference());
		assertEquals(new BigDecimal(1.0000).setScale(4), cancelInit.getAmount());
		Cancel cancel = cancelInit.getPayment().getCharge(initCharge.getId()).getCancel(cancelInit.getId());
		assertNotNull(cancel);
		assertNotNull(cancel.getId());
		assertCancelEquals(cancelInit, cancel);
		assertEquals("pmt-ref", cancel.getPaymentReference());
		assertEquals(new BigDecimal(1.0000).setScale(4), cancel.getAmount());
	}

	@Test
	public void testMarketplaceFullCancelChargeWithCard() throws MalformedURLException, HttpCommunicationException {
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
		
		//confirm authorization
		int redirectStatus = confirmMarketplacePendingTransaction(charge.getRedirectUrl().toString());
		await().atLeast(3, SECONDS).atMost(10, SECONDS);
		assertEquals(HttpStatus.SC_MOVED_TEMPORARILY, redirectStatus);
		
		//full cancel
		MarketplacePayment fullCancelPayment = charge.getPayment().marketplaceFullChargesCancel("test martketplace full cancel");
		assertNotNull(fullCancelPayment);
		assertEquals(Payment.State.CANCELED, fullCancelPayment.getPaymentState());
		assertEquals(2, fullCancelPayment.getChargesList().size());
		assertEquals(2, fullCancelPayment.getCancelList().size());
	}
	
	@Test
	public void testMarketplacePartialCancelChargeWithCard() throws MalformedURLException, HttpCommunicationException {
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
		
		//confirm authorization
		int redirectStatus = confirmMarketplacePendingTransaction(charge.getRedirectUrl().toString());
		await().atLeast(3, SECONDS).atMost(10, SECONDS);
		assertEquals(HttpStatus.SC_MOVED_TEMPORARILY, redirectStatus);
		
		//get marketplace payment
		MarketplacePayment payment = getHeidelpay(marketplacePrivatekey).fetchMarketplacePayment(charge.getPayment().getId());
		assertNotNull(payment);
		assertNotNull(payment.getId());
		assertNotNull(payment.getAuthorizationsList());
		assertEquals(2, payment.getChargesList().size());
		assertEquals(Payment.State.COMPLETED, payment.getPaymentState());
		
		//partial cancel
		MarketplaceCancel cancelRequest = new MarketplaceCancel();
		cancelRequest.setPaymentReference("test marketplace partial cancel");
		cancelRequest.setCanceledBasket(this.buildCancelBasketByParticipant(maxBasket.getBasketItems(), charge.getProcessing().getParticipantId()));
				
		MarketplaceCancel cancelResponse = charge.cancel(cancelRequest);
		assertNotNull(cancelResponse);
		assertEquals(AbstractTransaction.Status.SUCCESS, cancelResponse.getStatus());
		
		//assert payment
		MarketplacePayment paymentAfterCancel = cancelResponse.getPayment();
		assertEquals(AbstractPayment.State.COMPLETED, paymentAfterCancel.getPaymentState());
		assertEquals(1, paymentAfterCancel.getCancelList().size());
		assertEquals(payment.getAmountTotal(), paymentAfterCancel.getAmountTotal());
		assertEquals(payment.getAmountCharged().subtract(cancelResponse.getAmount()), paymentAfterCancel.getAmountCharged());
		assertEquals(cancelResponse.getAmount(), paymentAfterCancel.getAmountCanceled());
	}
}
