package com.heidelpay.payment.business.paymenttypes;

import com.heidelpay.payment.Authorization;
import com.heidelpay.payment.Cancel;
import com.heidelpay.payment.Charge;
import com.heidelpay.payment.Shipment;
import com.heidelpay.payment.business.AbstractPaymentTest;
import com.heidelpay.payment.communication.HttpCommunicationException;
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

public class InstallmentSecuredTest extends AbstractPaymentTest {

	@Test
	public void testRateRetrieval() throws HttpCommunicationException, ParseException {
		BigDecimal effectiveInterestRate = BigDecimal.valueOf(5.5).setScale(4, BigDecimal.ROUND_HALF_UP);
		Date orderDate = getDate("21.06.2019");
		List<InstallmentSecuredRatePlan> rateList = getHeidelpay().installmentSecuredRates(BigDecimal.TEN, Currency.getInstance("EUR"), effectiveInterestRate, orderDate);
		assertNotNull(rateList);
		assertEquals(6, rateList.size());
		assertInstallmentSecuredRatePlan(effectiveInterestRate, orderDate, rateList.get(0));
	}

	@Test
	public void testCreateInstallmentSecuredTypeWithIbanInvoiceDate() throws HttpCommunicationException, ParseException {
		InstallmentSecuredRatePlan ratePlan = getInstallmentSecuredRatePlan();
		addIbanInvoiceParameter(ratePlan);
		InstallmentSecuredRatePlan ratePlanReturned = getHeidelpay().createPaymentType(ratePlan);
		assertNotNull(ratePlanReturned);
		assertRatePlan(ratePlan, ratePlanReturned);
	}
	
	@Test
	public void testCreateInstallmentSecuredTypeIbanLater() throws HttpCommunicationException, ParseException {
		InstallmentSecuredRatePlan ratePlan = getInstallmentSecuredRatePlan();
		InstallmentSecuredRatePlan ratePlanReturned = getHeidelpay().createPaymentType(ratePlan);
		assertNotNull(ratePlanReturned);
		assertRatePlan(ratePlan, ratePlanReturned);
		
		addIbanInvoiceParameter(ratePlanReturned);
		InstallmentSecuredRatePlan updatedPlan = getHeidelpay().updatePaymentType(ratePlanReturned);
		
		assertNotNull(updatedPlan);
		assertRatePlan(ratePlanReturned, updatedPlan);
	}
	
	@Test
	public void testAuthorizeViaTypeWithIban() throws HttpCommunicationException, ParseException, MalformedURLException {
		InstallmentSecuredRatePlan ratePlan = getInstallmentSecuredRatePlan();
		addIbanInvoiceParameter(ratePlan);
		InstallmentSecuredRatePlan ratePlanReturned = createInstallmentSecuredType(ratePlan);
		assertNotNull(ratePlanReturned);
		assertRatePlan(ratePlan, ratePlanReturned);
		
		Authorization authorization = ratePlanReturned.authorize(new BigDecimal(370.48), Currency.getInstance("EUR"), new URL("https://www.heidelpay.com"), createMaximumCustomerSameAddress().getId(), createBasket().getId(), ratePlan.getEffectiveInterestRate());
		assertValidAuthorize(ratePlan, authorization);
	}

	@Test
	public void testAuthorizeViaHeidelpayTypeIdWithIban() throws HttpCommunicationException, ParseException, MalformedURLException {
		InstallmentSecuredRatePlan ratePlan = getInstallmentSecuredRatePlan();
		addIbanInvoiceParameter(ratePlan);
		InstallmentSecuredRatePlan ratePlanReturned = createInstallmentSecuredType(ratePlan);
		assertNotNull(ratePlanReturned);
		assertRatePlan(ratePlan, ratePlanReturned);
		
		Authorization authorization = getHeidelpay().authorize(getAuthorization(ratePlanReturned.getId(), createMaximumCustomerSameAddress().getId(), createBasket().getId(), ratePlan.getEffectiveInterestRate()));
		assertValidAuthorize(ratePlan, authorization);
	}
	
	@Test
	public void testChargeViaAuthorize() throws HttpCommunicationException, ParseException, MalformedURLException {
		InstallmentSecuredRatePlan ratePlan = createInstallmentSecuredType(getInstallmentSecuredRatePlan());
		InstallmentSecuredRatePlan ratePlanReturned = getHeidelpay().createPaymentType(ratePlan);
		assertNotNull(ratePlanReturned);
		assertRatePlan(ratePlan, ratePlanReturned);
		
		Authorization authorization = ratePlanReturned.authorize(new BigDecimal(370.48), Currency.getInstance("EUR"), new URL("https://www.heidelpay.com"), createMaximumCustomerSameAddress().getId(), createBasket().getId(), ratePlan.getEffectiveInterestRate());
		assertValidAuthorize(ratePlan, authorization);
		
		Charge charge = authorization.charge();
		assertValidCharge(charge);
	}

	@Test
	public void testFullCancellationBeforeShipment() throws HttpCommunicationException, ParseException, MalformedURLException {
		InstallmentSecuredRatePlan ratePlan = createInstallmentSecuredType(getInstallmentSecuredRatePlan());
		InstallmentSecuredRatePlan ratePlanReturned = getHeidelpay().createPaymentType(ratePlan);
		assertNotNull(ratePlanReturned);
		assertRatePlan(ratePlan, ratePlanReturned);
		
		Authorization authorization = ratePlanReturned.authorize(new BigDecimal(370.48), Currency.getInstance("EUR"), new URL("https://www.heidelpay.com"), createMaximumCustomerSameAddress().getId(), createBasket().getId(), ratePlan.getEffectiveInterestRate());
		assertValidAuthorize(ratePlan, authorization);
		
		Charge charge = authorization.charge();
		assertValidCharge(charge);
		
		Cancel cancel = charge.cancel();
		assertValidCancel(cancel, getAmount());
	}

	@Test
	public void testPartialCancellationBeforeShipment() throws HttpCommunicationException, ParseException, MalformedURLException {
		InstallmentSecuredRatePlan ratePlan = createInstallmentSecuredType(getInstallmentSecuredRatePlan());
		InstallmentSecuredRatePlan ratePlanReturned = getHeidelpay().createPaymentType(ratePlan);
		assertNotNull(ratePlanReturned);
		assertRatePlan(ratePlan, ratePlanReturned);
		
		Authorization authorization = ratePlanReturned.authorize(new BigDecimal(370.48), Currency.getInstance("EUR"), new URL("https://www.heidelpay.com"), createMaximumCustomerSameAddress().getId(), createBasket().getId(), ratePlan.getEffectiveInterestRate());
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
		InstallmentSecuredRatePlan ratePlan = getInstallmentSecuredRatePlan();
		addIbanInvoiceParameter(ratePlan);
		InstallmentSecuredRatePlan ratePlanReturned = getHeidelpay().createPaymentType(ratePlan);
		assertNotNull(ratePlanReturned);
		assertRatePlan(ratePlan, ratePlanReturned);
		
		Authorization authorization = ratePlanReturned.authorize(new BigDecimal(370.48), Currency.getInstance("EUR"), new URL("https://www.heidelpay.com"), createMaximumCustomerSameAddress().getId(), createBasket().getId(), ratePlan.getEffectiveInterestRate());
		assertValidAuthorize(ratePlan, authorization);
		
		Charge charge = authorization.charge();
		assertValidCharge(charge);
		
		Shipment shipment = getHeidelpay().shipment(charge.getPaymentId());
		assertValidShipment(shipment);		
	}

	@Test
	public void testFullCancelAfterShipment() throws HttpCommunicationException, ParseException, MalformedURLException {
		InstallmentSecuredRatePlan ratePlan = getInstallmentSecuredRatePlan();
		addIbanInvoiceParameter(ratePlan);
		InstallmentSecuredRatePlan ratePlanReturned = getHeidelpay().createPaymentType(ratePlan);
		assertNotNull(ratePlanReturned);
		assertRatePlan(ratePlan, ratePlanReturned);
		
		Authorization authorization = ratePlanReturned.authorize(new BigDecimal(370.48), Currency.getInstance("EUR"), new URL("https://www.heidelpay.com"), createMaximumCustomerSameAddress().getId(), createBasket().getId(), ratePlan.getEffectiveInterestRate());
		assertValidAuthorize(ratePlan, authorization);
		
		Charge charge = authorization.charge();
		assertValidCharge(charge);
		
		Shipment shipment = getHeidelpay().shipment(charge.getPaymentId());
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
		.setReturnUrl(new URL("https://www.heidelpay.com"))
		.setCustomerId(customerId)
		.setBasketId(basketId);
		authorization.setEffectiveInterestRate(effectiveInterestRate);
		return authorization;
	}

	private BigDecimal getAmount() {
		BigDecimal amount = new BigDecimal(370.4800);
		return amount.setScale(2, RoundingMode.HALF_UP);
	}
	private InstallmentSecuredRatePlan createInstallmentSecuredType(InstallmentSecuredRatePlan ratePlan) throws ParseException, HttpCommunicationException {
		return getHeidelpay().createPaymentType(ratePlan);
	}

	private void addIbanInvoiceParameter(InstallmentSecuredRatePlan ratePlan) {
		ratePlan.setIban("DE89370400440532013000");
		ratePlan.setBic("COBADEFFXXX");
		ratePlan.setAccountHolder("Rene Felder");
		ratePlan.setInvoiceDate(DateUtils.addDays(new Date(), 0));
		ratePlan.setInvoiceDueDate(DateUtils.addDays(new Date(), 10));
	}

	private InstallmentSecuredRatePlan getInstallmentSecuredRatePlan() throws ParseException, HttpCommunicationException {
		BigDecimal effectiveInterestRate = BigDecimal.valueOf(5.5);
		Date orderDate = getDate("21.06.2019");
		List<InstallmentSecuredRatePlan> rateList = getHeidelpay().installmentSecuredRates(BigDecimal.TEN, Currency.getInstance("EUR"), effectiveInterestRate, orderDate);
		InstallmentSecuredRatePlan ratePlan = rateList.get(0);
		return ratePlan;
	}

	private void assertValidAuthorize(InstallmentSecuredRatePlan ratePlan, Authorization authorization) {
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
	
	
	
	private void assertRatePlan(InstallmentSecuredRatePlan ratePlan, InstallmentSecuredRatePlan ratePlanReturned) {
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

	private void assertInstallmentSecuredRatePlan(BigDecimal effectiveInterestRate, Date orderDate, InstallmentSecuredRatePlan ratePlan) {
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
