package com.unzer.payment.business.paymenttypes;

/*-
 * #%L
 * Unzer Java SDK
 * %%
 * Copyright (C) 2020 Unzer E-Com GmbH
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

import com.unzer.payment.Authorization;
import com.unzer.payment.Cancel;
import com.unzer.payment.Charge;
import com.unzer.payment.Shipment;
import com.unzer.payment.business.AbstractPaymentTest;
import com.unzer.payment.communication.HttpCommunicationException;
import org.apache.commons.lang3.time.DateUtils;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Currency;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class HirePurchaseDirectDebitTest extends AbstractPaymentTest {

	@Test
	public void testRateRetrieval() throws HttpCommunicationException, ParseException {
		BigDecimal effectiveInterestRate = BigDecimal.valueOf(5.5).setScale(4, BigDecimal.ROUND_HALF_UP);
		Date orderDate = getDate("21.06.2019");
		List<HirePurchaseRatePlan> rateList = getUnzer().hirePurchaseRates(BigDecimal.TEN, Currency.getInstance("EUR"), effectiveInterestRate, orderDate);
		assertNotNull(rateList);
		assertEquals(6, rateList.size());
		assertRatePlan(effectiveInterestRate, orderDate, rateList.get(0));
	}

	@Test
	public void testCreateHirePurchaseTypeWithIbanInvoiceDate() throws HttpCommunicationException, ParseException {
		HirePurchaseRatePlan ratePlan = getHirePurchaseRatePlan();
		addIbanInvoiceParameter(ratePlan);
		HirePurchaseRatePlan ratePlanReturned = getUnzer().createPaymentType(ratePlan);
		assertNotNull(ratePlanReturned);
		assertRatePlan(ratePlan, ratePlanReturned);
	}
	
	@Test
	public void testCreateHirePurchaseTypeIbanLater() throws HttpCommunicationException, ParseException {
		HirePurchaseRatePlan ratePlan = getHirePurchaseRatePlan();
		HirePurchaseRatePlan ratePlanReturned = getUnzer().createPaymentType(ratePlan);
		assertNotNull(ratePlanReturned);
		assertRatePlan(ratePlan, ratePlanReturned);
		
		addIbanInvoiceParameter(ratePlanReturned);
		HirePurchaseRatePlan updatedPlan = getUnzer().updatePaymentType(ratePlanReturned);
		
		assertNotNull(updatedPlan);
		assertRatePlan(ratePlanReturned, updatedPlan);
	}
	
	@Test
	public void testAuthorizeViaTypeWithIban() throws HttpCommunicationException, ParseException, MalformedURLException {
		HirePurchaseRatePlan ratePlan = getHirePurchaseRatePlan();
		addIbanInvoiceParameter(ratePlan);
		HirePurchaseRatePlan ratePlanReturned = createHirePurchaseType(ratePlan);
		assertNotNull(ratePlanReturned);
		assertRatePlan(ratePlan, ratePlanReturned);
		
		Authorization authorization = ratePlanReturned.authorize(new BigDecimal(370.48), Currency.getInstance("EUR"), new URL("https://www.unzer.com"), createMaximumCustomerSameAddress().getId(), createBasket().getId(), ratePlan.getEffectiveInterestRate());
		assertValidAuthorize(ratePlan, authorization);
	}

	@Test
	public void testAuthorizeViaUnzerTypeIdWithIban() throws HttpCommunicationException, ParseException, MalformedURLException {
		HirePurchaseRatePlan ratePlan = getHirePurchaseRatePlan();
		addIbanInvoiceParameter(ratePlan);
		HirePurchaseRatePlan ratePlanReturned = createHirePurchaseType(ratePlan);
		assertNotNull(ratePlanReturned);
		assertRatePlan(ratePlan, ratePlanReturned);
		
		Authorization authorization = getUnzer().authorize(getAuthorization(ratePlanReturned.getId(), createMaximumCustomerSameAddress().getId(), createBasket().getId(), ratePlan.getEffectiveInterestRate()));
		assertValidAuthorize(ratePlan, authorization);
	}
	
	@Test
	public void testChargeViaAuthorize() throws HttpCommunicationException, ParseException, MalformedURLException {
		HirePurchaseRatePlan ratePlan = createHirePurchaseType(getHirePurchaseRatePlan());
		HirePurchaseRatePlan ratePlanReturned = getUnzer().createPaymentType(ratePlan);
		assertNotNull(ratePlanReturned);
		assertRatePlan(ratePlan, ratePlanReturned);
		
		Authorization authorization = ratePlanReturned.authorize(new BigDecimal(370.48), Currency.getInstance("EUR"), new URL("https://www.unzer.com"), createMaximumCustomerSameAddress().getId(), createBasket().getId(), ratePlan.getEffectiveInterestRate());
		assertValidAuthorize(ratePlan, authorization);
		
		Charge charge = authorization.charge();
		assertValidCharge(charge);
	}

	@Test
	public void testFullCancellationBeforeShipment() throws HttpCommunicationException, ParseException, MalformedURLException {
		HirePurchaseRatePlan ratePlan = createHirePurchaseType(getHirePurchaseRatePlan());
		HirePurchaseRatePlan ratePlanReturned = getUnzer().createPaymentType(ratePlan);
		assertNotNull(ratePlanReturned);
		assertRatePlan(ratePlan, ratePlanReturned);
		
		Authorization authorization = ratePlanReturned.authorize(new BigDecimal(370.48), Currency.getInstance("EUR"), new URL("https://www.unzer.com"), createMaximumCustomerSameAddress().getId(), createBasket().getId(), ratePlan.getEffectiveInterestRate());
		assertValidAuthorize(ratePlan, authorization);
		
		Charge charge = authorization.charge();
		assertValidCharge(charge);
		
		Cancel cancel = charge.cancel();
		assertValidCancel(cancel, getAmount());
	}

	@Test
	public void testPartialCancellationBeforeShipment() throws HttpCommunicationException, ParseException, MalformedURLException {
		HirePurchaseRatePlan ratePlan = createHirePurchaseType(getHirePurchaseRatePlan());
		HirePurchaseRatePlan ratePlanReturned = getUnzer().createPaymentType(ratePlan);
		assertNotNull(ratePlanReturned);
		assertRatePlan(ratePlan, ratePlanReturned);
		
		Authorization authorization = ratePlanReturned.authorize(new BigDecimal(370.48), Currency.getInstance("EUR"), new URL("https://www.unzer.com"), createMaximumCustomerSameAddress().getId(), createBasket().getId(), ratePlan.getEffectiveInterestRate());
		assertValidAuthorize(ratePlan, authorization);
		
		Charge charge = authorization.charge();
		assertValidCharge(charge);

		Cancel cancelReq = new Cancel();
		cancelReq.setAmountGross(BigDecimal.TEN);
		cancelReq.setAmountNet(BigDecimal.TEN);
		cancelReq.setAmountVat(BigDecimal.TEN);
		Cancel cancel = charge.cancel(cancelReq);

		assertValidCancel(cancel, getBigDecimalFourDigits(370.48));
	}

	@Test
	public void testShipment() throws HttpCommunicationException, ParseException, MalformedURLException {
		HirePurchaseRatePlan ratePlan = getHirePurchaseRatePlan();
		addIbanInvoiceParameter(ratePlan);
		HirePurchaseRatePlan ratePlanReturned = getUnzer().createPaymentType(ratePlan);
		assertNotNull(ratePlanReturned);
		assertRatePlan(ratePlan, ratePlanReturned);
		
		Authorization authorization = ratePlanReturned.authorize(new BigDecimal(370.48), Currency.getInstance("EUR"), new URL("https://www.unzer.com"), createMaximumCustomerSameAddress().getId(), createBasket().getId(), ratePlan.getEffectiveInterestRate());
		assertValidAuthorize(ratePlan, authorization);
		
		Charge charge = authorization.charge();
		assertValidCharge(charge);
		
		Shipment shipment = getUnzer().shipment(charge.getPaymentId());
		assertValidShipment(shipment);		
	}

	@Test
	public void testFullCancelAfterShipment() throws HttpCommunicationException, ParseException, MalformedURLException {
		HirePurchaseRatePlan ratePlan = getHirePurchaseRatePlan();
		addIbanInvoiceParameter(ratePlan);
		HirePurchaseRatePlan ratePlanReturned = getUnzer().createPaymentType(ratePlan);
		assertNotNull(ratePlanReturned);
		assertRatePlan(ratePlan, ratePlanReturned);
		
		Authorization authorization = ratePlanReturned.authorize(new BigDecimal(370.48), Currency.getInstance("EUR"), new URL("https://www.unzer.com"), createMaximumCustomerSameAddress().getId(), createBasket().getId(), ratePlan.getEffectiveInterestRate());
		assertValidAuthorize(ratePlan, authorization);
		
		Charge charge = authorization.charge();
		assertValidCharge(charge);
		
		Shipment shipment = getUnzer().shipment(charge.getPaymentId());
		assertValidShipment(shipment);		

		Cancel cancel = charge.cancel();
		assertValidCancel(cancel, getAmount());
	}

	private void assertValidShipment(Shipment shipment) {
		assertNotNull(shipment);
	}
	
	private void assertValidCancel(Cancel cancel, BigDecimal cancellationAmount) {
		assertNotNull(cancel);
		assertNotNull(cancel.getProcessing().getUniqueId());
		assertNotNull(cancel.getProcessing().getShortId());
		assertNumberEquals(cancellationAmount, cancel.getAmount());
		assertEquals(Cancel.Status.SUCCESS, cancel.getStatus());
	}
	
	private void assertValidCharge(Charge charge) {
		assertNotNull(charge);
		assertNotNull(charge.getProcessing().getExternalOrderId());
		assertNotNull(charge.getProcessing().getUniqueId());
		assertNotNull(charge.getProcessing().getShortId());
		assertNumberEquals(getAmount(), charge.getAmount());
		assertEquals(Charge.Status.SUCCESS, charge.getStatus());
	}

	
	protected Authorization getAuthorization(String typeId, String customerId, String basketId, BigDecimal effectiveInterestRate) throws MalformedURLException {
		Authorization authorization = new Authorization();
		authorization
		.setAmount(getAmount())
		.setCurrency(Currency.getInstance("EUR"))
		.setTypeId(typeId)
		.setReturnUrl(new URL("https://www.unzer.com"))
		.setCustomerId(customerId)
		.setBasketId(basketId);
		authorization.setEffectiveInterestRate(effectiveInterestRate);
		return authorization;
	}

	private BigDecimal getAmount() {
		BigDecimal amount = new BigDecimal(370.4800);
		return amount.setScale(2, RoundingMode.HALF_UP);
	}	
	private HirePurchaseRatePlan createHirePurchaseType(HirePurchaseRatePlan ratePlan) throws ParseException, HttpCommunicationException {
		return getUnzer().createPaymentType(ratePlan);
	}

	private void addIbanInvoiceParameter(HirePurchaseRatePlan ratePlan) {
		ratePlan.setIban("DE89370400440532013000");
		ratePlan.setBic("COBADEFFXXX");
		ratePlan.setAccountHolder("Max Mustermann");
		ratePlan.setInvoiceDate(DateUtils.addDays(new Date(), 0));
		ratePlan.setInvoiceDueDate(DateUtils.addDays(new Date(), 10));
	}

	private HirePurchaseRatePlan getHirePurchaseRatePlan() throws ParseException, HttpCommunicationException {
		BigDecimal effectiveInterestRate = BigDecimal.valueOf(5.5);
		Date orderDate = getDate("21.06.2019");
		List<HirePurchaseRatePlan> rateList = getUnzer().hirePurchaseRates(BigDecimal.TEN, Currency.getInstance("EUR"), effectiveInterestRate, orderDate);
		HirePurchaseRatePlan ratePlan = rateList.get(0);
		return ratePlan;
	}

	private void assertValidAuthorize(HirePurchaseRatePlan ratePlan, Authorization authorization) {
		assertNotNull(authorization);
		assertNumberEquals(getAmount(), authorization.getAmount());
		assertEquals(Authorization.Status.SUCCESS, authorization.getStatus());
		assertNumberEquals(ratePlan.getEffectiveInterestRate(), authorization.getEffectiveInterestRate());
		assertNotNull(authorization.getProcessing().getPdfLink());
		assertNotNull(authorization.getProcessing().getExternalOrderId());
		assertNotNull(authorization.getProcessing().getExternalOrderId());
		assertNotNull(authorization.getProcessing().getZgReferenceId());

		assertNotNull(authorization.getPaymentId());
		assertNotNull(authorization.getCustomerId());
		assertNotNull(authorization.getBasketId());
		assertNotNull(authorization.getTypeId());
	}
	
	
	
	private void assertRatePlan(HirePurchaseRatePlan ratePlan, HirePurchaseRatePlan ratePlanReturned) {
		assertEquals(ratePlan.getAccountHolder(), ratePlanReturned.getAccountHolder());
		assertEquals(ratePlan.getBic(), ratePlanReturned.getBic());
		assertNumberEquals(ratePlan.getEffectiveInterestRate(), ratePlanReturned.getEffectiveInterestRate());
		assertNumberEquals(ratePlan.getFeeFirstRate(), ratePlanReturned.getFeeFirstRate());
		assertNumberEquals(ratePlan.getFeePerRate(), ratePlanReturned.getFeePerRate());
		assertEquals(ratePlan.getIban(), ratePlanReturned.getIban());
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
		if (!getDate(expected).equals(getDate(actual))) throw new AssertionError("expected: '" + getDate(expected) + "', actual: '" + getDate(actual) + "'");

	}
	
	private String getDate(Date expected) {
		return new SimpleDateFormat("dd.MM.yyyy").format(expected);
	}

	private void assertRatePlan(BigDecimal effectiveInterestRate, Date orderDate, HirePurchaseRatePlan ratePlan) {
		assertEquals(3, ratePlan.getNumberOfRates());
		assertEquals(effectiveInterestRate, ratePlan.getEffectiveInterestRate());
		assertEquals(new BigDecimal("10.0").setScale(4, BigDecimal.ROUND_HALF_UP), ratePlan.getTotalPurchaseAmount());
		assertEquals(getBigDecimalFourDigits(0.08), ratePlan.getTotalInterestAmount());
		assertEquals(ratePlan.getTotalAmount().setScale(4, BigDecimal.ROUND_HALF_UP), ratePlan.getTotalInterestAmount().add(ratePlan.getTotalPurchaseAmount()));
		assertEquals(getBigDecimalFourDigits(3.37), ratePlan.getMonthlyRate());
		assertEquals(getBigDecimalFourDigits(3.34), ratePlan.getLastRate());
		assertEquals(getBigDecimalFourDigits(5.40), getBigDecimalFourDigits(ratePlan.getNominalInterestRate().doubleValue()));
		assertEquals(orderDate, ratePlan.getOrderDate());
	}

	private BigDecimal getBigDecimalFourDigits(double number) {
		return new BigDecimal(number).setScale(4, BigDecimal.ROUND_HALF_UP);
	}

}
