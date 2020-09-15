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

import org.apache.http.HttpStatus;
import org.junit.Test;

import com.heidelpay.payment.AbstractTransaction;
import com.heidelpay.payment.Authorization;
import com.heidelpay.payment.Basket;
import com.heidelpay.payment.Charge;
import com.heidelpay.payment.Payment;
import com.heidelpay.payment.communication.HttpCommunicationException;
import com.heidelpay.payment.marketplace.MarketplaceAuthorization;
import com.heidelpay.payment.marketplace.MarketplaceCharge;
import com.heidelpay.payment.marketplace.MarketplacePayment;
import com.heidelpay.payment.paymenttypes.Card;

public class ChargeAfterAuthorizationTest extends AbstractPaymentTest {

	@Test
	public void fetchAuthorization() throws HttpCommunicationException, MalformedURLException {
		Authorization authorize = getHeidelpay().authorize(getAuthorization(createPaymentTypeCard().getId()));
		Authorization authorization = getHeidelpay().fetchAuthorization(authorize.getPaymentId());
		assertNotNull(authorization);
	}

	@Test
	public void fullChargeAfterAuthorization() throws HttpCommunicationException, MalformedURLException {
		String orderId = getRandomId();
		Authorization authorize = getHeidelpay().authorize(getAuthorization(createPaymentTypeCard().getId(), null, orderId, null, null, false));
		Authorization authorization = getHeidelpay().fetchAuthorization(authorize.getPaymentId());
		Charge charge = authorization.charge();
		assertNotNull(charge);
		assertNotNull(charge.getId());
		assertEquals(orderId, authorize.getOrderId());
		assertEquals(orderId, authorization.getOrderId());
		assertEquals(orderId, charge.getOrderId());
	}

	@Test
	public void fullChargeAfterAuthorizationHeidelpay() throws HttpCommunicationException, MalformedURLException {
		Authorization authorize = getHeidelpay().authorize(getAuthorization(createPaymentTypeCard().getId(), false));
		Charge charge = getHeidelpay().chargeAuthorization(authorize.getPaymentId());
		assertNotNull(charge);
		assertNotNull(charge.getId());
	}

	@Test
	public void partialChargeAfterAuthorization() throws HttpCommunicationException, MalformedURLException {
		Authorization authorize = getHeidelpay().authorize(getAuthorization(createPaymentTypeCard().getId(), false));
		Authorization authorization = getHeidelpay().fetchAuthorization(authorize.getPaymentId());
		Charge charge = authorization.charge(new BigDecimal(0.1));
		assertNotNull(charge);
		assertNotNull(charge.getId());
	}

	@Test
	public void chargeAfterAuthorizationWithPaymentReference() throws HttpCommunicationException, MalformedURLException {
		Authorization authorize = getHeidelpay().authorize(getAuthorization(createPaymentTypeCard().getId(), false));
		Charge charge = authorize.charge(new BigDecimal(1.0), "pmt-ref");
		assertNotNull(charge);
		assertNotNull(charge.getId());
		assertEquals("pmt-ref", charge.getPaymentReference());
	}
	
	@Test
	public void testMarketplaceFullAuthorizeCharge() throws MalformedURLException, HttpCommunicationException {
		String participantId_1 = MARKETPLACE_PARTICIPANT_ID_1;
		String participantId_2 = MARKETPLACE_PARTICIPANT_ID_2;

		// create basket
		Basket maxBasket = getMaxTestBasket();
		maxBasket.setAmountTotalDiscount(null);

		maxBasket.getBasketItems().get(0).setParticipantId(participantId_1);
		maxBasket.getBasketItems().get(1).setParticipantId(participantId_2);

		int basketItemCnt = maxBasket.getBasketItems().size();
		for (int i = 0; i < basketItemCnt; i++) {
			maxBasket.getBasketItems().get(i).setAmountDiscount(null);
		}

		Basket basket = getHeidelpay(marketplacePrivatekey).createBasket(maxBasket);

		// create card
		Card card = getPaymentTypeCard(NO_3DS_VISA_CARD_NUMBER); //do not change card number except error case
		card = (Card) getHeidelpay(marketplacePrivatekey).createPaymentType(card);

		// marketplace authorization
		MarketplaceAuthorization authorizeRequest = getMarketplaceAuthorization(card.getId(), null, null, null,
				basket.getId(), null);
		authorizeRequest.setAmount(maxBasket.getAmountTotalGross());

		MarketplaceAuthorization authorize = getHeidelpay(marketplacePrivatekey).marketplaceAuthorize(authorizeRequest);
		assertNotNull(authorize.getId());
		assertNotNull(authorize);
		assertEquals(AbstractTransaction.Status.PENDING, authorize.getStatus());
		assertEquals(participantId_2, authorize.getProcessing().getParticipantId());
		
		//confirm authorization
		int redirectStatus = confirmMarketplacePendingTransaction(authorize.getRedirectUrl().toString());
		await().atLeast(5, SECONDS).atMost(10, SECONDS);
		assertEquals(HttpStatus.SC_MOVED_TEMPORARILY, redirectStatus);

		//get payment
		MarketplacePayment payment = getHeidelpay(marketplacePrivatekey).fetchMarketplacePayment(authorize.getPaymentId());
		assertEquals(2, payment.getAuthorizationsList().size());
		assertEquals(Payment.State.PENDING, payment.getPaymentState());
		
		//full charge authorizations
		MarketplacePayment fullCapturePayment = payment.fullChargeAuthorizations("test full marketplace full capture.");
		assertEquals(payment.getId(), fullCapturePayment.getId());
		assertEquals(2, fullCapturePayment.getAuthorizationsList().size());
		assertEquals(2, fullCapturePayment.getChargesList().size());
		assertEquals(Payment.State.COMPLETED, fullCapturePayment.getPaymentState());
	}
	
	@Test
	public void testMarketplaceAuthorizeCharge() throws MalformedURLException, HttpCommunicationException {
		String participantId_1 = MARKETPLACE_PARTICIPANT_ID_1;
		String participantId_2 = MARKETPLACE_PARTICIPANT_ID_2;

		// create basket
		Basket maxBasket = getMaxTestBasket();
		maxBasket.setAmountTotalDiscount(null);

		maxBasket.getBasketItems().get(0).setParticipantId(participantId_1);
		maxBasket.getBasketItems().get(1).setParticipantId(participantId_2);

		int basketItemCnt = maxBasket.getBasketItems().size();
		for (int i = 0; i < basketItemCnt; i++) {
			maxBasket.getBasketItems().get(i).setAmountDiscount(null);
		}

		Basket basket = getHeidelpay(marketplacePrivatekey).createBasket(maxBasket);

		// create card
		Card card = getPaymentTypeCard(NO_3DS_VISA_CARD_NUMBER); //do not change card number except error case
		card = (Card) getHeidelpay(marketplacePrivatekey).createPaymentType(card);

		// marketplace authorization
		MarketplaceAuthorization authorizeRequest = getMarketplaceAuthorization(card.getId(), null, null, null,
				basket.getId(), null);
		authorizeRequest.setAmount(maxBasket.getAmountTotalGross());

		MarketplaceAuthorization authorize = getHeidelpay(marketplacePrivatekey).marketplaceAuthorize(authorizeRequest);
		assertNotNull(authorize.getId());
		assertNotNull(authorize);
		assertEquals(AbstractTransaction.Status.PENDING, authorize.getStatus());
		assertEquals(participantId_2, authorize.getProcessing().getParticipantId());
		
		//confirm authorization
		int redirectStatus = confirmMarketplacePendingTransaction(authorize.getRedirectUrl().toString());
		await().atLeast(5, SECONDS).atMost(10, SECONDS);
		assertEquals(HttpStatus.SC_MOVED_TEMPORARILY, redirectStatus);

		//charge authorization
		MarketplaceCharge chargeAuthorization = new MarketplaceCharge();
		chargeAuthorization.setAmount(BigDecimal.TEN);
		chargeAuthorization.setPaymentReference("test single marketplace authorize charge");
		chargeAuthorization = authorize.charge(chargeAuthorization);
		
		//get payment
		MarketplacePayment payment = getHeidelpay(marketplacePrivatekey).fetchMarketplacePayment(authorize.getPaymentId());
		assertEquals(2, payment.getAuthorizationsList().size());
		assertEquals(Payment.State.PARTLY, payment.getPaymentState());
		assertEquals(2, payment.getAuthorizationsList().size());
		assertEquals(1, payment.getChargesList().size());
		assertEquals(chargeAuthorization.getProcessing().getUniqueId(), payment.getChargesList().get(0).getProcessing().getUniqueId());
	}
}
