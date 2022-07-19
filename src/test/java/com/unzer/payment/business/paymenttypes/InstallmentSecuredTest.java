package com.unzer.payment.business.paymenttypes;

import com.unzer.payment.*;
import com.unzer.payment.business.AbstractPaymentTest;
import com.unzer.payment.communication.HttpCommunicationException;
import com.unzer.payment.communication.JsonParser;
import com.unzer.payment.communication.impl.HttpClientBasedRestCommunication;
import com.unzer.payment.communication.json.JsonIdObject;
import com.unzer.payment.service.PaymentService;
import org.apache.commons.lang3.time.DateUtils;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.MalformedURLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Currency;
import java.util.Date;
import java.util.List;

import static com.unzer.payment.business.BasketV1TestData.getMaxTestBasketV1;
import static com.unzer.payment.business.BasketV2TestData.getMaxTestBasketV2;
import static com.unzer.payment.util.Url.unsafeUrl;
import static org.junit.Assert.*;

/*-
 * #%L
 * Unzer Java SDK
 * %%
 * Copyright (C) 2020 - today Unzer E-Com GmbH
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
        BigDecimal effectiveInterestRate = new BigDecimal(5.5).setScale(4, RoundingMode.HALF_UP);
        Date orderDate = getDate("21.06.2019");
        List<InstallmentSecuredRatePlan> rateList = getUnzer().installmentSecuredRates(BigDecimal.TEN, Currency.getInstance("EUR"), effectiveInterestRate, orderDate);
        assertNotNull(rateList);
        Assert.assertTrue(rateList.size() > 0);
        assertInstallmentSecuredRatePlan(effectiveInterestRate, orderDate, rateList.get(0));
    }

    @Test
    public void testCreateInstallmentSecuredTypeWithIbanInvoiceDate() throws HttpCommunicationException, ParseException {
        InstallmentSecuredRatePlan ratePlan = getInstallmentSecuredRatePlan();
        addIbanInvoiceParameter(ratePlan);
        InstallmentSecuredRatePlan ratePlanReturned = getUnzer().createPaymentType(ratePlan);
        assertNotNull(ratePlanReturned);
        assertRatePlan(ratePlan, ratePlanReturned);
    }

    @Test
    public void testCreateInstallmentSecuredTypeIbanLater() throws HttpCommunicationException, ParseException {
        InstallmentSecuredRatePlan ratePlan = getInstallmentSecuredRatePlan();
        InstallmentSecuredRatePlan ratePlanReturned = getUnzer().createPaymentType(ratePlan);
        assertNotNull(ratePlanReturned);
        assertRatePlan(ratePlan, ratePlanReturned);

        addIbanInvoiceParameter(ratePlanReturned);
        InstallmentSecuredRatePlan updatedPlan = getUnzer().updatePaymentType(ratePlanReturned);

        assertNotNull(updatedPlan);
        assertRatePlan(ratePlanReturned, updatedPlan);
    }

    @Test
    @Deprecated
    public void testAuthorizeViaTypeWithIbanBasketV1() throws HttpCommunicationException, ParseException {
        InstallmentSecuredRatePlan ratePlan = getInstallmentSecuredRatePlan();
        addIbanInvoiceParameter(ratePlan);
        InstallmentSecuredRatePlan ratePlanReturned = createInstallmentSecuredType(ratePlan);
        assertNotNull(ratePlanReturned);
        assertRatePlan(ratePlan, ratePlanReturned);

        Authorization authorization = ratePlanReturned.authorize(
                new BigDecimal(370.48),
                Currency.getInstance("EUR"),
                unsafeUrl("https://www.unzer.com"),
                createMaximumCustomerSameAddress().getId(),
                createBasket(getMaxTestBasketV1()).getId(),
                ratePlan.getEffectiveInterestRate());
        assertValidAuthorize(ratePlan, authorization);
    }

    @Ignore("https://unz.atlassian.net/browse/AHC-5292")
    @Test
    public void testAuthorizeViaTypeWithIbanBasketV2() throws HttpCommunicationException, ParseException {
        InstallmentSecuredRatePlan ratePlan = getInstallmentSecuredRatePlan();
        addIbanInvoiceParameter(ratePlan);
        InstallmentSecuredRatePlan ratePlanReturned = createInstallmentSecuredType(ratePlan);
        assertNotNull(ratePlanReturned);
        assertRatePlan(ratePlan, ratePlanReturned);

        Authorization authorization = ratePlanReturned.authorize(
                new BigDecimal(370.48),
                Currency.getInstance("EUR"),
                unsafeUrl("https://www.unzer.com"),
                createMaximumCustomerSameAddress().getId(),
                createBasket(getMaxTestBasketV2()).getId(),
                ratePlan.getEffectiveInterestRate());
        assertValidAuthorize(ratePlan, authorization);
    }


    @Test
    @Deprecated
    public void testAuthorizeViaUnzerTypeIdWithIbanBasketV1() throws HttpCommunicationException, ParseException, MalformedURLException {
        InstallmentSecuredRatePlan ratePlan = getInstallmentSecuredRatePlan();
        addIbanInvoiceParameter(ratePlan);
        InstallmentSecuredRatePlan ratePlanReturned = createInstallmentSecuredType(ratePlan);
        assertNotNull(ratePlanReturned);
        assertRatePlan(ratePlan, ratePlanReturned);

        Authorization authorization = getUnzer()
                .authorize(
                        getAuthorization(
                                ratePlanReturned.getId(),
                                createMaximumCustomerSameAddress().getId(),
                                createBasket(getMaxTestBasketV1()).getId(),
                                ratePlan.getEffectiveInterestRate()
                        )
                );
        assertValidAuthorize(ratePlan, authorization);
    }

    @Ignore("https://unz.atlassian.net/browse/AHC-5292")
    @Test
    public void testAuthorizeViaUnzerTypeIdWithIbanBasketV2() throws HttpCommunicationException, ParseException, MalformedURLException {
        InstallmentSecuredRatePlan ratePlan = getInstallmentSecuredRatePlan();
        addIbanInvoiceParameter(ratePlan);
        InstallmentSecuredRatePlan ratePlanReturned = createInstallmentSecuredType(ratePlan);
        assertNotNull(ratePlanReturned);
        assertRatePlan(ratePlan, ratePlanReturned);

        Authorization authorization = getUnzer()
                .authorize(
                        getAuthorization(
                                ratePlanReturned.getId(),
                                createMaximumCustomerSameAddress().getId(),
                                createBasket(getMaxTestBasketV2()).getId(),
                                ratePlan.getEffectiveInterestRate()
                        )
                );
        assertValidAuthorize(ratePlan, authorization);
    }

    @Test
    @Deprecated
    public void testChargeViaAuthorizeBasketV1() throws HttpCommunicationException, ParseException {
        InstallmentSecuredRatePlan ratePlan = createInstallmentSecuredType(getInstallmentSecuredRatePlan());
        InstallmentSecuredRatePlan ratePlanReturned = getUnzer().createPaymentType(ratePlan);
        assertNotNull(ratePlanReturned);
        assertRatePlan(ratePlan, ratePlanReturned);

        Authorization authorization = ratePlanReturned.authorize(
                new BigDecimal(370.48),
                Currency.getInstance("EUR"),
                unsafeUrl("https://www.unzer.com"),
                createMaximumCustomerSameAddress().getId(),
                createBasket(getMaxTestBasketV1()).getId(),
                ratePlan.getEffectiveInterestRate());
        assertValidAuthorize(ratePlan, authorization);

        Charge charge = authorization.charge();
        assertValidCharge(charge);
    }

    @Ignore("https://unz.atlassian.net/browse/AHC-5292")
    @Test
    public void testChargeViaAuthorizeBasketV2() throws HttpCommunicationException, ParseException {
        InstallmentSecuredRatePlan ratePlan = createInstallmentSecuredType(getInstallmentSecuredRatePlan());
        InstallmentSecuredRatePlan ratePlanReturned = getUnzer().createPaymentType(ratePlan);
        assertNotNull(ratePlanReturned);
        assertRatePlan(ratePlan, ratePlanReturned);

        Authorization authorization = ratePlanReturned.authorize(
                new BigDecimal(370.48),
                Currency.getInstance("EUR"),
                unsafeUrl("https://www.unzer.com"),
                createMaximumCustomerSameAddress().getId(),
                createBasket(getMaxTestBasketV2()).getId(),
                ratePlan.getEffectiveInterestRate());
        assertValidAuthorize(ratePlan, authorization);

        Charge charge = authorization.charge();
        assertValidCharge(charge);
    }


    @Test
    @Deprecated
    public void testFullCancellationBeforeShipmentBasketV1() throws HttpCommunicationException, ParseException {
        InstallmentSecuredRatePlan ratePlan = createInstallmentSecuredType(getInstallmentSecuredRatePlan());
        InstallmentSecuredRatePlan ratePlanReturned = getUnzer().createPaymentType(ratePlan);
        assertNotNull(ratePlanReturned);
        assertRatePlan(ratePlan, ratePlanReturned);

        Authorization authorization = ratePlanReturned.authorize(
                new BigDecimal(370.48),
                Currency.getInstance("EUR"),
                unsafeUrl("https://www.unzer.com"),
                createMaximumCustomerSameAddress().getId(),
                createBasket(getMaxTestBasketV1()).getId(),
                ratePlan.getEffectiveInterestRate());
        assertValidAuthorize(ratePlan, authorization);

        Charge charge = authorization.charge();
        assertValidCharge(charge);

        Cancel cancel = charge.cancel();
        assertValidCancel(cancel, getAmount());
    }

    @Ignore("https://unz.atlassian.net/browse/AHC-5292")
    @Test
    public void testFullCancellationBeforeShipmentBasketV2() throws HttpCommunicationException, ParseException {
        InstallmentSecuredRatePlan ratePlan = createInstallmentSecuredType(getInstallmentSecuredRatePlan());
        InstallmentSecuredRatePlan ratePlanReturned = getUnzer().createPaymentType(ratePlan);
        assertNotNull(ratePlanReturned);
        assertRatePlan(ratePlan, ratePlanReturned);

        Authorization authorization = ratePlanReturned.authorize(
                new BigDecimal(370.48),
                Currency.getInstance("EUR"),
                unsafeUrl("https://www.unzer.com"),
                createMaximumCustomerSameAddress().getId(),
                createBasket(getMaxTestBasketV2()).getId(),
                ratePlan.getEffectiveInterestRate());
        assertValidAuthorize(ratePlan, authorization);

        Charge charge = authorization.charge();
        assertValidCharge(charge);

        Cancel cancel = charge.cancel();
        assertValidCancel(cancel, getAmount());
    }


    @Test
    @Deprecated
    public void testPartialCancellationBeforeShipmentBasketV1() throws HttpCommunicationException, ParseException {
        InstallmentSecuredRatePlan ratePlan = createInstallmentSecuredType(getInstallmentSecuredRatePlan());
        InstallmentSecuredRatePlan ratePlanReturned = getUnzer().createPaymentType(ratePlan);
        assertNotNull(ratePlanReturned);
        assertRatePlan(ratePlan, ratePlanReturned);

        Authorization authorization = ratePlanReturned.authorize(
                new BigDecimal(370.48),
                Currency.getInstance("EUR"),
                unsafeUrl("https://www.unzer.com"),
                createMaximumCustomerSameAddress().getId(),
                createBasket(getMaxTestBasketV1()).getId(),
                ratePlan.getEffectiveInterestRate());
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

    @Ignore("https://unz.atlassian.net/browse/AHC-5292")
    @Test
    public void testPartialCancellationBeforeShipmentBasketV2() throws HttpCommunicationException, ParseException {
        InstallmentSecuredRatePlan ratePlan = createInstallmentSecuredType(getInstallmentSecuredRatePlan());
        InstallmentSecuredRatePlan ratePlanReturned = getUnzer().createPaymentType(ratePlan);
        assertNotNull(ratePlanReturned);
        assertRatePlan(ratePlan, ratePlanReturned);

        Authorization authorization = ratePlanReturned.authorize(
                new BigDecimal(370.48),
                Currency.getInstance("EUR"),
                unsafeUrl("https://www.unzer.com"),
                createMaximumCustomerSameAddress().getId(),
                createBasket(getMaxTestBasketV2()).getId(),
                ratePlan.getEffectiveInterestRate());
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
    @Deprecated
    public void testShipmentBasketV1() throws HttpCommunicationException, ParseException {
        InstallmentSecuredRatePlan ratePlan = getInstallmentSecuredRatePlan();
        addIbanInvoiceParameter(ratePlan);
        InstallmentSecuredRatePlan ratePlanReturned = getUnzer().createPaymentType(ratePlan);
        assertNotNull(ratePlanReturned);
        assertRatePlan(ratePlan, ratePlanReturned);

        Authorization authorization = ratePlanReturned.authorize(
                new BigDecimal(370.48),
                Currency.getInstance("EUR"),
                unsafeUrl("https://www.unzer.com"),
                createMaximumCustomerSameAddress().getId(),
                createBasket(getMaxTestBasketV1()).getId(),
                ratePlan.getEffectiveInterestRate());
        assertValidAuthorize(ratePlan, authorization);

        Charge charge = authorization.charge();
        assertValidCharge(charge);

        Shipment shipment = getUnzer().shipment(charge.getPaymentId());
        assertValidShipment(shipment);
    }

    @Ignore("https://unz.atlassian.net/browse/AHC-5292")
    @Test
    public void testShipmentBasketV2() throws HttpCommunicationException, ParseException {
        InstallmentSecuredRatePlan ratePlan = getInstallmentSecuredRatePlan();
        addIbanInvoiceParameter(ratePlan);
        InstallmentSecuredRatePlan ratePlanReturned = getUnzer().createPaymentType(ratePlan);
        assertNotNull(ratePlanReturned);
        assertRatePlan(ratePlan, ratePlanReturned);

        Authorization authorization = ratePlanReturned.authorize(
                new BigDecimal(370.48),
                Currency.getInstance("EUR"),
                unsafeUrl("https://www.unzer.com"),
                createMaximumCustomerSameAddress().getId(),
                createBasket(getMaxTestBasketV2()).getId(),
                ratePlan.getEffectiveInterestRate());
        assertValidAuthorize(ratePlan, authorization);

        Charge charge = authorization.charge();
        assertValidCharge(charge);

        Shipment shipment = getUnzer().shipment(charge.getPaymentId());
        assertValidShipment(shipment);
    }


    @Test
    @Deprecated
    public void testFullCancelAfterShipmentBasketV1() throws HttpCommunicationException, ParseException {
        InstallmentSecuredRatePlan ratePlan = getInstallmentSecuredRatePlan();
        addIbanInvoiceParameter(ratePlan);
        InstallmentSecuredRatePlan ratePlanReturned = getUnzer().createPaymentType(ratePlan);
        assertNotNull(ratePlanReturned);
        assertRatePlan(ratePlan, ratePlanReturned);

        Authorization authorization = ratePlanReturned.authorize(
                new BigDecimal("370.48"),
                Currency.getInstance("EUR"),
                unsafeUrl("https://www.unzer.com"),
                createMaximumCustomerSameAddress().getId(),
                createBasket(getMaxTestBasketV1()).getId(),
                ratePlan.getEffectiveInterestRate());
        assertValidAuthorize(ratePlan, authorization);

        Charge charge = authorization.charge();
        assertValidCharge(charge);

        Shipment shipment = getUnzer().shipment(charge.getPaymentId());
        assertValidShipment(shipment);

        Cancel cancel = charge.cancel();
        assertValidCancel(cancel, getAmount());
    }

    @Ignore("Until https://unz.atlassian.net/browse/AHC-5292 is resolved")
    @Test
    public void testFullCancelAfterShipmentBasketV2() throws HttpCommunicationException, ParseException {
        InstallmentSecuredRatePlan ratePlan = getInstallmentSecuredRatePlan();
        addIbanInvoiceParameter(ratePlan);
        InstallmentSecuredRatePlan ratePlanReturned = getUnzer().createPaymentType(ratePlan);
        assertNotNull(ratePlanReturned);
        assertRatePlan(ratePlan, ratePlanReturned);

        Authorization authorization = ratePlanReturned.authorize(
                new BigDecimal("380.38"),
                Currency.getInstance("EUR"),
                unsafeUrl("https://www.unzer.com"),
                createMaximumCustomerSameAddress().getId(),
                createBasket(getMaxTestBasketV2()).getId(),
                ratePlan.getEffectiveInterestRate());
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
                .setReturnUrl(unsafeUrl("https://www.unzer.com"))
                .setCustomerId(customerId)
                .setBasketId(basketId);
        authorization.setEffectiveInterestRate(effectiveInterestRate);
        return authorization;
    }

    private BigDecimal getAmount() {
        BigDecimal amount = new BigDecimal("370.4800");
        return amount.setScale(2, RoundingMode.HALF_UP);
    }

    private InstallmentSecuredRatePlan createInstallmentSecuredType(InstallmentSecuredRatePlan ratePlan) throws HttpCommunicationException {
        return getUnzer().createPaymentType(ratePlan);
    }

    private void addIbanInvoiceParameter(InstallmentSecuredRatePlan ratePlan) {
        ratePlan.setIban("DE89370400440532013000");
        ratePlan.setBic("COBADEFFXXX");
        ratePlan.setAccountHolder("Max Mustermann");
        ratePlan.setInvoiceDate(DateUtils.addHours(DateUtils.addDays(new Date(), 0), -4));
        ratePlan.setInvoiceDueDate(DateUtils.addDays(new Date(), 10));
    }

    private InstallmentSecuredRatePlan getInstallmentSecuredRatePlan() throws ParseException, HttpCommunicationException {
        BigDecimal effectiveInterestRate = new BigDecimal(5.5);
        Date orderDate = getDate("21.06.2019");
        List<InstallmentSecuredRatePlan> rateList = getUnzer().installmentSecuredRates(BigDecimal.TEN, Currency.getInstance("EUR"), effectiveInterestRate, orderDate);
        InstallmentSecuredRatePlan ratePlan = rateList.get(0);
        return ratePlan;
    }

    private InstallmentSecuredRatePlan getInstallmentSecuredRatePlan(BigDecimal amount) throws ParseException, HttpCommunicationException {
        BigDecimal effectiveInterestRate = new BigDecimal(5.5);
        Date orderDate = getDate("21.06.2019");
        List<InstallmentSecuredRatePlan> rateList = getUnzer().installmentSecuredRates(amount, Currency.getInstance("EUR"), effectiveInterestRate, orderDate);
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
        if (!getDate(expected).equals(getDate(actual)))
            throw new AssertionError("expected: '" + getDate(expected) + "', actual: '" + getDate(actual) + "'");

    }

    private String getDate(Date expected) {
        return new SimpleDateFormat("dd.MM.yyyy").format(expected);
    }

    private void assertInstallmentSecuredRatePlan(BigDecimal effectiveInterestRate, Date orderDate, InstallmentSecuredRatePlan ratePlan) {
        assertEquals(3, ratePlan.getNumberOfRates());
        assertEquals(effectiveInterestRate, ratePlan.getEffectiveInterestRate());
        assertEquals(new BigDecimal("10.0").setScale(4, RoundingMode.HALF_UP), ratePlan.getTotalPurchaseAmount());
        assertEquals(getBigDecimalFourDigits(0.08), ratePlan.getTotalInterestAmount());
        assertEquals(ratePlan.getTotalAmount().setScale(4, RoundingMode.HALF_UP), ratePlan.getTotalInterestAmount().add(ratePlan.getTotalPurchaseAmount()));
        assertEquals(getBigDecimalFourDigits(3.37), ratePlan.getMonthlyRate());
        assertEquals(getBigDecimalFourDigits(3.34), ratePlan.getLastRate());
        assertEquals(getBigDecimalFourDigits(5.40), getBigDecimalFourDigits(ratePlan.getNominalInterestRate().doubleValue()));
        assertEquals(orderDate, ratePlan.getOrderDate());
    }

    @Test
    @Deprecated
    public void testAuthorizeHirePurchaseDirectDebitBasketV1() throws HttpCommunicationException, ParseException {
        Unzer unzer = getUnzer();
        HttpClientBasedRestCommunication restCommunication = new HttpClientBasedRestCommunication();
        JsonParser jsonParser = new JsonParser();
        PaymentService paymentService = new PaymentService(unzer, restCommunication);
        Customer maximumCustomer = createMaximumCustomer();
        Basket basket = createBasket(getMaxTestBasketV1(new BigDecimal(380.48)));

        // FIXME: hardcoded endpoint
        String response = restCommunication.httpPost(
                "https://api.unzer.com/v1/types/hire-purchase-direct-debit",
                unzer.getPrivateKey(),
                getInstallmentSecuredRatePlan(new BigDecimal(380.48))
        );
        JsonIdObject jsonResponse = jsonParser.fromJson(response, JsonIdObject.class);
        InstallmentSecuredRatePlan installmentSecuredRatePlan = paymentService.fetchPaymentType(jsonResponse.getId());

        assertTrue(
                installmentSecuredRatePlan.getId().matches("s-hdd-\\w*")
        );

        Authorization authorize = installmentSecuredRatePlan.authorize(
                new BigDecimal(380.48),
                Currency.getInstance("EUR"),
                unsafeUrl("https://www.meinShop.de"),
                maximumCustomer.getCustomerId(),
                basket.getId(),
                new BigDecimal(5.5));
        assertNotNull(authorize.getPaymentId());
    }


    @Ignore("https://unz.atlassian.net/browse/AHC-5292")
    @Test
    public void testAuthorizeHirePurchaseDirectDebitBasketV2() throws HttpCommunicationException, ParseException {
        Unzer unzer = getUnzer();
        HttpClientBasedRestCommunication restCommunication = new HttpClientBasedRestCommunication();
        JsonParser jsonParser = new JsonParser();
        PaymentService paymentService = new PaymentService(unzer, restCommunication);
        Customer maximumCustomer = createMaximumCustomer();
        Basket basket = createBasket(getMaxTestBasketV1(new BigDecimal(380.48)));

        // FIXME: hardcoded endpoint
        String response = restCommunication.httpPost(
                "https://api.unzer.com/v1/types/hire-purchase-direct-debit",
                unzer.getPrivateKey(),
                getInstallmentSecuredRatePlan(new BigDecimal(380.48))
        );
        JsonIdObject jsonResponse = jsonParser.fromJson(response, JsonIdObject.class);
        InstallmentSecuredRatePlan installmentSecuredRatePlan = paymentService.fetchPaymentType(jsonResponse.getId());

        assertTrue(
                installmentSecuredRatePlan.getId().matches("s-hdd-\\w*")
        );

        Authorization authorize = installmentSecuredRatePlan.authorize(
                new BigDecimal(380.48),
                Currency.getInstance("EUR"),
                unsafeUrl("https://www.meinShop.de"),
                maximumCustomer.getCustomerId(),
                basket.getId(),
                new BigDecimal(5.5));
        assertNotNull(authorize.getPaymentId());
    }

    // TODO: remove or extract to separate class
    private BigDecimal getBigDecimalFourDigits(double number) {
        return new BigDecimal(number).setScale(4, RoundingMode.HALF_UP);
    }
}
