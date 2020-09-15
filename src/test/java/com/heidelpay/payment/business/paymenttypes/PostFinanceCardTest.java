package com.heidelpay.payment.business.paymenttypes;

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

import com.heidelpay.payment.Charge;
import com.heidelpay.payment.business.AbstractPaymentTest;
import com.heidelpay.payment.communication.HttpCommunicationException;
import com.heidelpay.payment.paymenttypes.PostFinanceCard;
import org.junit.Test;

import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Currency;

import static org.junit.Assert.assertNotNull;

public class PostFinanceCardTest extends AbstractPaymentTest {

	@Test
	public void testCreatePostFinanceCardMandatoryType() throws HttpCommunicationException {
		PostFinanceCard pfCard = new PostFinanceCard();
		pfCard = getHeidelpay().createPaymentType(pfCard);
		assertNotNull(pfCard.getId());
	}

	@Test
	public void testChargePostFinanceCardType() throws HttpCommunicationException, MalformedURLException {
		PostFinanceCard pfCard = getHeidelpay().createPaymentType(getPostFinanceCard());
		Charge charge = pfCard.charge(BigDecimal.ONE, Currency.getInstance("CHF"), new URL("https://www.google.at"));
		assertNotNull(charge);
		assertNotNull(charge.getId());
		assertNotNull(charge.getRedirectUrl());
	}

	@Test
	public void testFetchPostFinanceCardType() throws HttpCommunicationException {
		PostFinanceCard pfCard = getHeidelpay().createPaymentType(getPostFinanceCard());
		assertNotNull(pfCard.getId());
		PostFinanceCard fetchedPostFinanceCard = (PostFinanceCard) getHeidelpay().fetchPaymentType(pfCard.getId());
		assertNotNull(fetchedPostFinanceCard.getId());
	}

	
	private PostFinanceCard getPostFinanceCard() {
		PostFinanceCard pfCard = new PostFinanceCard();
		return pfCard;
	}


}
