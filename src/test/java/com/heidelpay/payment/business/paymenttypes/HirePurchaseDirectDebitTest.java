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
import static org.junit.Assert.assertNotNull;

import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Currency;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.time.DateUtils;
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

	@Test
	public void testCreateHirePurchaseTypeWithIbanInvoiceDate() throws HttpCommunicationException, ParseException {
		BigDecimal effectiveInterestRate = BigDecimal.valueOf(5.5);
		Date orderDate = getDate("21.06.2019");
		List<HirePurchaseRatePlan> rateList = getHeidelpay().hirePurchaseRates(BigDecimal.TEN, Currency.getInstance("EUR"), effectiveInterestRate, orderDate);
		HirePurchaseRatePlan ratePlan = rateList.get(0);
		ratePlan.setIban("DE46940594210000012345");
		ratePlan.setBic("COBADEFFXXX");
		ratePlan.setAccountHolder("Rene Felder");
		ratePlan.setInvoiceDate(DateUtils.addDays(new Date(), -1));
		ratePlan.setInvoiceDueDate(DateUtils.addDays(new Date(), 10));
		HirePurchaseRatePlan ratePlanReturned = getHeidelpay().createPaymentType(ratePlan);
		assertNotNull(ratePlanReturned);
		assertRatePlan(ratePlan, ratePlanReturned);
	}
	
	@Test
	public void testCreateHirePurchaseTypeIbanLater() throws HttpCommunicationException, ParseException {
		BigDecimal effectiveInterestRate = BigDecimal.valueOf(5.5);
		Date orderDate = getDate("21.06.2019");
		List<HirePurchaseRatePlan> rateList = getHeidelpay().hirePurchaseRates(BigDecimal.TEN, Currency.getInstance("EUR"), effectiveInterestRate, orderDate);
		HirePurchaseRatePlan ratePlan = rateList.get(0);
		HirePurchaseRatePlan ratePlanReturned = getHeidelpay().createPaymentType(ratePlan);
		assertNotNull(ratePlanReturned);
		assertRatePlan(ratePlan, ratePlanReturned);
		
		ratePlanReturned.setIban("DE46940594210000012345");
		ratePlanReturned.setBic("COBADEFFXXX");
		ratePlanReturned.setAccountHolder("Rene Felder");
		ratePlanReturned.setInvoiceDate(DateUtils.addDays(new Date(), -1));
		ratePlanReturned.setInvoiceDueDate(DateUtils.addDays(new Date(), 10));
		HirePurchaseRatePlan updatedPlan = getHeidelpay().updatePaymentType(ratePlanReturned);
		
		assertNotNull(updatedPlan);
		assertRatePlan(ratePlanReturned, updatedPlan);
	}
	
	@Test
	public void testAuthorizeViaType() throws HttpCommunicationException, ParseException, MalformedURLException {
		BigDecimal effectiveInterestRate = BigDecimal.valueOf(5.5);
		Date orderDate = getDate("21.06.2019");
		List<HirePurchaseRatePlan> rateList = getHeidelpay().hirePurchaseRates(BigDecimal.TEN, Currency.getInstance("EUR"), effectiveInterestRate, orderDate);
		HirePurchaseRatePlan ratePlan = rateList.get(0);
		ratePlan.setIban("DE46940594210000012345");
		ratePlan.setBic("COBADEFFXXX");
		ratePlan.setAccountHolder("Rene Felder");
		ratePlan.setInvoiceDate(DateUtils.addDays(new Date(), -1));
		ratePlan.setInvoiceDueDate(DateUtils.addDays(new Date(), 10));
		HirePurchaseRatePlan ratePlanReturned = getHeidelpay().createPaymentType(ratePlan);
		assertNotNull(ratePlanReturned);
		assertRatePlan(ratePlan, ratePlanReturned);
		
		ratePlanReturned.authorize(BigDecimal.TEN, Currency.getInstance("EUR"), new URL("https://www.heidelpay.com"), createMaximumCustomerSameAddress().getId(), createBasket().getId(), ratePlan.getEffectiveInterestRate());
	}
	
	
	
	private void assertRatePlan(HirePurchaseRatePlan ratePlan, HirePurchaseRatePlan ratePlanReturned) {
		assertEquals(ratePlan.getAccountHolder(), ratePlanReturned.getAccountHolder());
		assertEquals(ratePlan.getBic(), ratePlanReturned.getBic());
		assertNumberEquals(ratePlan.getEffectiveInterestRate(), ratePlanReturned.getEffectiveInterestRate());
		assertNumberEquals(ratePlan.getFeeFirstRate(), ratePlanReturned.getFeeFirstRate());
		assertNumberEquals(ratePlan.getFeePerRate(), ratePlanReturned.getFeePerRate());
		// IBAN masking will be removed: https://heidelpay.atlassian.net/browse/AHC-1793
//		assertEquals(ratePlan.getIban(), ratePlanReturned.getIban());
		assertDateEquals(ratePlan.getInvoiceDate(), ratePlanReturned.getInvoiceDate());
		assertDateEquals(ratePlan.getInvoiceDueDate(), ratePlanReturned.getInvoiceDueDate());
		assertNumberEquals(ratePlan.getLastRate(), ratePlanReturned.getLastRate());
		assertNumberEquals(ratePlan.getMonthlyRate(), ratePlanReturned.getMonthlyRate());
		assertNumberEquals(ratePlan.getNominalInterestRate(), ratePlanReturned.getNominalInterestRate());
		assertEquals(ratePlan.getNumberOfRates(), ratePlanReturned.getNumberOfRates());
		assertEquals(ratePlan.getOrderDate(), ratePlanReturned.getOrderDate());
		assertEquals(ratePlan.getRateList(), ratePlanReturned.getRateList());
		assertNumberEquals(ratePlan.getTotalAmount(), ratePlanReturned.getTotalAmount());
		assertNumberEquals(ratePlan.getTotalInterestAmount(), ratePlanReturned.getTotalInterestAmount());
		assertNumberEquals(ratePlan.getTotalPurchaseAmount(), ratePlanReturned.getTotalPurchaseAmount());
		assertEquals(ratePlan.getTypeUrl(), ratePlanReturned.getTypeUrl());
	}

	private void assertDateEquals(Date expected, Date actual) {
		if (expected == null && actual == null) return;
		if (expected == null && actual != null) throw new AssertionError("expected is null, but actual is not");
		if (expected != null && actual == null) throw new AssertionError("expected is not null, but actual is null");
		if (!getDateTime(expected).equals(getDateTime(actual))) throw new AssertionError("expected: '" + getDateTime(expected) + "', actual: '" + getDateTime(actual) + "'");

	}

	private String getDateTime(Date expected) {
		return new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(expected);
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
