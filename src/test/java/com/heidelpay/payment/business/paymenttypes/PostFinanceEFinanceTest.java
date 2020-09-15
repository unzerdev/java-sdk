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
import com.heidelpay.payment.paymenttypes.PostFinanceEFinance;
import org.junit.Test;

import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Currency;

import static org.junit.Assert.assertNotNull;

public class PostFinanceEFinanceTest extends AbstractPaymentTest {

	@Test
	public void testCreatePostFinanceEFinanceMandatoryType() throws HttpCommunicationException {
		PostFinanceEFinance pfEFinance = new PostFinanceEFinance();
		pfEFinance = getHeidelpay().createPaymentType(pfEFinance);
		assertNotNull(pfEFinance.getId());
	}

	@Test
	public void testChargePostFinanceEFinanceType() throws HttpCommunicationException, MalformedURLException {
		PostFinanceEFinance pfEFinance = getHeidelpay().createPaymentType(getPostFinanceEFinance());
		Charge charge = pfEFinance.charge(BigDecimal.ONE, Currency.getInstance("CHF"), new URL("https://www.google.at"));
		assertNotNull(charge);
		assertNotNull(charge.getId());
		assertNotNull(charge.getRedirectUrl());
	}

	@Test
	public void testFetchPostFinanceEFinanceType() throws HttpCommunicationException {
		PostFinanceEFinance pfEFinance = getHeidelpay().createPaymentType(getPostFinanceEFinance());
		assertNotNull(pfEFinance.getId());
		PostFinanceEFinance fetchedPostFinanceEFinance = (PostFinanceEFinance) getHeidelpay().fetchPaymentType(pfEFinance.getId());
		assertNotNull(fetchedPostFinanceEFinance.getId());
	}

	
	private PostFinanceEFinance getPostFinanceEFinance() {
		PostFinanceEFinance pfEFinance = new PostFinanceEFinance();
		return pfEFinance;
	}


}
