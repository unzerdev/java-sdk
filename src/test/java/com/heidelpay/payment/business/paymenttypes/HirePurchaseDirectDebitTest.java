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
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Currency;
import java.util.Date;
import java.util.List;

import org.junit.Test;

import com.heidelpay.payment.business.AbstractPaymentTest;
import com.heidelpay.payment.communication.HttpCommunicationException;

public class HirePurchaseDirectDebitTest extends AbstractPaymentTest {

	@Test
	public void testRateRetrieval() throws HttpCommunicationException, ParseException {
		BigDecimal effectiveInterestRate = BigDecimal.valueOf(5.5);
		Date orderDate = getDate("21.06.2019");
		List<HirePurchaseRatePlan> rateList = getHeidelpay().hirePurchaseRates(BigDecimal.TEN, Currency.getInstance("EUR"), effectiveInterestRate, orderDate);
		assertNotNull(rateList);
		assertEquals(6, rateList.size());
		assertRatePlan(effectiveInterestRate, orderDate, rateList.get(0));
	}

	
	
	private void assertRatePlan(BigDecimal effectiveInterestRate, Date orderDate, HirePurchaseRatePlan ratePlan) {
		assertEquals(3, ratePlan.getNumberOfRates());
		assertEquals(effectiveInterestRate, ratePlan.getEffectiveInterestRate());
		assertEquals(new BigDecimal("10.0"), ratePlan.getTotalPurchaseAmount());
		assertEquals(getBigDecimalTwoDigits(0.08), ratePlan.getTotalInterestAmount());
		assertEquals(ratePlan.getTotalAmount(), ratePlan.getTotalInterestAmount().add(ratePlan.getTotalPurchaseAmount()));
		assertEquals(getBigDecimalTwoDigits(3.37), ratePlan.getMonthlyRate());
		assertEquals(getBigDecimalTwoDigits(3.34), ratePlan.getLastRate());
		assertEquals(getBigDecimalTwoDigits(1.35), ratePlan.getNominalInterestRate());
		assertEquals(orderDate, ratePlan.getOrderDate());
	}

	private BigDecimal getBigDecimalTwoDigits(double number) {
		return new BigDecimal(number).setScale(2, BigDecimal.ROUND_HALF_UP);
	}

}
