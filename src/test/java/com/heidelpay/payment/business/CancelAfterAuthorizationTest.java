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

import com.heidelpay.payment.AbstractPayment;
import com.heidelpay.payment.AbstractTransaction;
import com.heidelpay.payment.Authorization;
import com.heidelpay.payment.Basket;
import com.heidelpay.payment.Cancel;
import com.heidelpay.payment.Payment;
import com.heidelpay.payment.communication.HttpCommunicationException;
import com.heidelpay.payment.marketplace.MarketplaceAuthorization;
import com.heidelpay.payment.marketplace.MarketplaceCancel;
import com.heidelpay.payment.marketplace.MarketplacePayment;
import com.heidelpay.payment.paymenttypes.Card;

public class CancelAfterAuthorizationTest extends AbstractPaymentTest {

	@Test
	public void fetchAuthorization() throws HttpCommunicationException, MalformedURLException {
		Authorization authorize = getHeidelpay().authorize(getAuthorization(createPaymentTypeCard().getId()));
		Authorization authorization = getHeidelpay().fetchAuthorization(authorize.getPaymentId());
		assertNotNull(authorization);
	}

	@Test
	public void fullCancelAfterAuthorization() throws HttpCommunicationException, MalformedURLException {
		Authorization authorize = getHeidelpay().authorize(getAuthorization(createPaymentTypeCard().getId(), false));
		Authorization authorization = getHeidelpay().fetchAuthorization(authorize.getPaymentId());
		Cancel cancel = authorization.cancel();
		assertNotNull(cancel);
		assertNotNull(cancel.getId());
	}

	@Test
	public void partialCancelPayment() throws HttpCommunicationException, MalformedURLException {
		Authorization authorize = getHeidelpay().authorize(getAuthorization(createPaymentTypeCard().getId(), false));
		Payment payment = getHeidelpay().fetchPayment(authorize.getPaymentId());
		Cancel cancel = payment.cancel(new BigDecimal(0.1));
		assertNotNull(cancel);
		assertNotNull(cancel.getId());
		assertEquals(getBigDecimal("0.1000"), cancel.getAmount());
	}

	@Test
	public void partialCancelAfterAuthorization() throws HttpCommunicationException, MalformedURLException {
		Authorization authorize = getHeidelpay().authorize(getAuthorization(createPaymentTypeCard().getId(), false));
		Authorization authorization = getHeidelpay().fetchAuthorization(authorize.getPaymentId());
		Cancel cancel = authorization.cancel(new BigDecimal(0.1));
		assertNotNull(cancel);
		assertNotNull(cancel.getId());
		assertEquals(getBigDecimal("0.1000"), cancel.getAmount());
	}

	@Test
	public void partialCancelAfterAuthorizationHeidelpay() throws HttpCommunicationException, MalformedURLException {
		Authorization authorize = getHeidelpay().authorize(getAuthorization(createPaymentTypeCard().getId(), false));
		Cancel cancel = getHeidelpay().cancelAuthorization(authorize.getPaymentId());
		assertNotNull(cancel);
		assertNotNull(cancel.getId());
		assertEquals(getBigDecimal("10.0000"), cancel.getAmount());
	}

	@Test
	public void fetchCancelHeidelpay() throws HttpCommunicationException, MalformedURLException {
		Authorization authorize = getHeidelpay().authorize(getAuthorization(createPaymentTypeCard().getId(), false));
		Cancel cancelExecuted = getHeidelpay().cancelAuthorization(authorize.getPaymentId());
		assertNotNull(cancelExecuted);
		assertNotNull(cancelExecuted.getId());
		assertEquals(getBigDecimal("10.0000"), cancelExecuted.getAmount());
		Cancel cancel = getHeidelpay().fetchCancel(authorize.getPaymentId(), cancelExecuted.getId());
		assertNotNull(cancel);
		assertNotNull(cancel.getId());
	}

	@Test
	public void partialCancelAfterAuthorizationIsListFilled() throws HttpCommunicationException, MalformedURLException {
		Authorization authorize = getHeidelpay().authorize(getAuthorization(createPaymentTypeCard().getId(), false));
		Authorization authorization = getHeidelpay().fetchAuthorization(authorize.getPaymentId());
		Cancel cancel = authorization.cancel(BigDecimal.ONE);
		assertNotNull(cancel);
		assertNotNull(cancel.getId());
		Cancel cancel2 = authorization.cancel(BigDecimal.ONE);
		assertNotNull(cancel2);
		assertNotNull(cancel2.getId());
		authorization = getHeidelpay().fetchAuthorization(authorize.getPaymentId());
		assertNotNull(authorization.getCancelList());
		assertEquals(2, authorization.getCancelList().size());
		assertEquals(2, authorization.getPayment().getAuthorization().getCancelList().size());
	}

	@Test
	public void cancelWithPaymentReference() throws HttpCommunicationException, MalformedURLException {
		Authorization authorize = getHeidelpay().authorize(getAuthorization(createPaymentTypeCard().getId(), false));
		Cancel cancelReq = new Cancel();
		cancelReq.setPaymentReference("pmt-ref");
		Cancel cancelExecuted = authorize.cancel(cancelReq);
		assertNotNull(cancelExecuted);
		assertNotNull(cancelExecuted.getId());
		assertEquals(getBigDecimal("10.0000"), cancelExecuted.getAmount());
		assertEquals("pmt-ref", cancelExecuted.getPaymentReference());
		Cancel cancel = getHeidelpay().fetchCancel(authorize.getPaymentId(), cancelExecuted.getId());
		assertNotNull(cancel);
		assertNotNull(cancel.getId());
		assertEquals("pmt-ref", cancel.getPaymentReference());
	}

	@Test
	public void testMarketplaceFullAuthorizeCancel() throws MalformedURLException, HttpCommunicationException {
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

		//full cancel
		MarketplacePayment fullCancelPayment = authorize.getPayment().marketplaceFullAuthorizeCancel("test martketplace full cancel");
		assertNotNull(fullCancelPayment);
		assertEquals(Payment.State.CANCELED, fullCancelPayment.getPaymentState());
		assertEquals(2, fullCancelPayment.getAuthorizationsList().size());
		assertEquals(2, fullCancelPayment.getCancelList().size());
	}
	
	@Test
	public void testMarketplacePartialAuthorizeCancel() throws MalformedURLException, HttpCommunicationException {
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
		
		//fetch payment
		MarketplacePayment payment = getHeidelpay(marketplacePrivatekey).fetchMarketplacePayment(authorize.getPaymentId());

		//partial cancel
		MarketplaceCancel cancelRequest = new MarketplaceCancel();
		cancelRequest.setPaymentReference("test marketplace partial cancel");
		cancelRequest.setCanceledBasket(this.buildCancelBasketByParticipant(maxBasket.getBasketItems(), authorize.getProcessing().getParticipantId()));
		
		MarketplaceCancel cancelResponse = authorize.cancel(cancelRequest);
		assertNotNull(cancelResponse);
		assertEquals(AbstractTransaction.Status.SUCCESS, cancelResponse.getStatus());
		assertEquals(authorize.getProcessing().getParticipantId(), cancelResponse.getProcessing().getParticipantId());
		
		//assert payment
		MarketplacePayment paymentAfterCancel = cancelResponse.getPayment();
		assertEquals(AbstractPayment.State.PENDING, payment.getPaymentState());
		assertEquals(payment.getAmountTotal().subtract(cancelResponse.getAmount()), paymentAfterCancel.getAmountTotal());
	}
}
