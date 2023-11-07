package com.unzer.payment.integration.paymenttypes;

import com.unzer.payment.*;
import com.unzer.payment.business.AbstractPaymentTest;
import com.unzer.payment.business.Keys;
import com.unzer.payment.business.paymenttypes.InstallmentSecuredRatePlan;
import com.unzer.payment.communication.HttpCommunicationException;
import com.unzer.payment.communication.JsonParser;
import com.unzer.payment.communication.impl.HttpClientBasedRestCommunication;
import com.unzer.payment.communication.json.ApiIdObject;
import com.unzer.payment.service.PaymentService;
import org.apache.commons.lang3.time.DateUtils;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

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
import static com.unzer.payment.util.Uuid.generateUuid;
import static org.junit.jupiter.api.Assertions.*;


public class InstallmentSecuredTest extends AbstractPaymentTest {

    @Test
    public void testRateRetrieval() {
        BigDecimal effectiveInterestRate = new BigDecimal("5.5").setScale(4, RoundingMode.HALF_UP);
        Date orderDate = getDate("21.06.2019");
        Unzer unzer = getUnzer(Keys.LEGACY_PRIVATE_KEY);
        List<InstallmentSecuredRatePlan> rateList =
                unzer.installmentSecuredRates(BigDecimal.TEN, Currency.getInstance("EUR"),
                        effectiveInterestRate, orderDate);
        assertNotNull(rateList);
        assertTrue(rateList.size() > 0);
        assertInstallmentSecuredRatePlan(effectiveInterestRate, orderDate, rateList.get(0));
    }

    private void assertInstallmentSecuredRatePlan(BigDecimal effectiveInterestRate, Date orderDate,
                                                  InstallmentSecuredRatePlan ratePlan) {
        assertEquals(3, ratePlan.getNumberOfRates());
        assertEquals(effectiveInterestRate, ratePlan.getEffectiveInterestRate());
        assertEquals(new BigDecimal("10.0").setScale(4, RoundingMode.HALF_UP),
                ratePlan.getTotalPurchaseAmount());
        assertEquals(getBigDecimalFourDigits(0.08), ratePlan.getTotalInterestAmount());
        assertEquals(ratePlan.getTotalAmount().setScale(4, RoundingMode.HALF_UP),
                ratePlan.getTotalInterestAmount().add(ratePlan.getTotalPurchaseAmount()));
        assertEquals(getBigDecimalFourDigits(3.37), ratePlan.getMonthlyRate());
        assertEquals(getBigDecimalFourDigits(3.34), ratePlan.getLastRate());
        assertEquals(getBigDecimalFourDigits(5.40),
                getBigDecimalFourDigits(ratePlan.getNominalInterestRate().doubleValue()));
        assertEquals(orderDate, ratePlan.getOrderDate());
    }

    private BigDecimal getBigDecimalFourDigits(double number) {
        return new BigDecimal(number).setScale(4, RoundingMode.HALF_UP);
    }

    @Test

    public void testCreateInstallmentSecuredTypeWithIbanInvoiceDate()
            throws HttpCommunicationException, ParseException {
        InstallmentSecuredRatePlan ratePlan = getInstallmentSecuredRatePlan();
        addIbanInvoiceParameter(ratePlan);
        InstallmentSecuredRatePlan ratePlanReturned =
                getUnzer(Keys.LEGACY_PRIVATE_KEY).createPaymentType(ratePlan);
        assertNotNull(ratePlanReturned);
        assertRatePlan(ratePlan, ratePlanReturned);
    }

    private InstallmentSecuredRatePlan getInstallmentSecuredRatePlan()
            throws ParseException, HttpCommunicationException {
        BigDecimal effectiveInterestRate = new BigDecimal("5.5");
        Date orderDate = getDate("21.06.2019");
        List<InstallmentSecuredRatePlan> rateList =
                getUnzer(Keys.LEGACY_PRIVATE_KEY).installmentSecuredRates(BigDecimal.TEN,
                        Currency.getInstance("EUR"),
                        effectiveInterestRate, orderDate);
        InstallmentSecuredRatePlan ratePlan = rateList.get(0);
        return ratePlan;
    }

    private void addIbanInvoiceParameter(InstallmentSecuredRatePlan ratePlan) {
        ratePlan.setIban("DE89370400440532013000");
        ratePlan.setBic("COBADEFFXXX");
        ratePlan.setAccountHolder("Max Mustermann");
        ratePlan.setInvoiceDate(DateUtils.addHours(DateUtils.addDays(new Date(), 0), -4));
        ratePlan.setInvoiceDueDate(DateUtils.addDays(new Date(), 10));
    }

    private void assertRatePlan(InstallmentSecuredRatePlan ratePlan,
                                InstallmentSecuredRatePlan ratePlanReturned) {
        assertEquals(ratePlan.getAccountHolder(), ratePlanReturned.getAccountHolder());
        assertEquals(ratePlan.getBic(), ratePlanReturned.getBic());
        assertNumberEquals(ratePlan.getEffectiveInterestRate(),
                ratePlanReturned.getEffectiveInterestRate());
        assertNumberEquals(ratePlan.getFeeFirstRate(), ratePlanReturned.getFeeFirstRate());
        assertNumberEquals(ratePlan.getFeePerRate(), ratePlanReturned.getFeePerRate());
        assertEquals(ratePlan.getIban(), ratePlanReturned.getIban());
        assertDateEquals(ratePlan.getInvoiceDate(), ratePlanReturned.getInvoiceDate());
        assertDateEquals(ratePlan.getInvoiceDueDate(), ratePlanReturned.getInvoiceDueDate());
        assertNumberEquals(ratePlan.getLastRate(), ratePlanReturned.getLastRate());
        assertNumberEquals(ratePlan.getMonthlyRate(), ratePlanReturned.getMonthlyRate());
        assertNumberEquals(ratePlan.getNominalInterestRate(),
                ratePlanReturned.getNominalInterestRate());
        assertEquals(ratePlan.getNumberOfRates(), ratePlanReturned.getNumberOfRates());
        assertEquals(ratePlan.getOrderDate(), ratePlanReturned.getOrderDate());
        assertEquals(ratePlan.getRateList(), ratePlanReturned.getRateList());
        assertNumberEquals(ratePlan.getTotalAmount(), ratePlanReturned.getTotalAmount());
        assertNumberEquals(ratePlan.getTotalInterestAmount(),
                ratePlanReturned.getTotalInterestAmount());
        assertNumberEquals(ratePlan.getTotalPurchaseAmount(),
                ratePlanReturned.getTotalPurchaseAmount());
    }

    private void assertDateEquals(Date expected, Date actual) {
        if (expected == null && actual == null) {
            return;
        }
        if (expected == null && actual != null) {
            throw new AssertionError("expected is null, but actual is not");
        }
        if (expected != null && actual == null) {
            throw new AssertionError("expected is not null, but actual is null");
        }
        if (!getDate(expected).equals(getDate(actual))) {
            throw new AssertionError(
                    "expected: '" + getDate(expected) + "', actual: '" + getDate(actual) + "'");
        }

    }

    private String getDate(Date expected) {
        return new SimpleDateFormat("dd.MM.yyyy").format(expected);
    }

    @Test

    public void testCreateInstallmentSecuredTypeIbanLater()
            throws HttpCommunicationException, ParseException {
        InstallmentSecuredRatePlan ratePlan = getInstallmentSecuredRatePlan();
        InstallmentSecuredRatePlan ratePlanReturned =
                getUnzer(Keys.LEGACY_PRIVATE_KEY).createPaymentType(ratePlan);
        assertNotNull(ratePlanReturned);
        assertRatePlan(ratePlan, ratePlanReturned);

        addIbanInvoiceParameter(ratePlanReturned);
        InstallmentSecuredRatePlan updatedPlan =
                getUnzer(Keys.LEGACY_PRIVATE_KEY).updatePaymentType(ratePlanReturned);

        assertNotNull(updatedPlan);
        assertRatePlan(ratePlanReturned, updatedPlan);
    }

    @Test

    @Deprecated
    public void testAuthorizeViaTypeWithIbanBasketV1()
            throws HttpCommunicationException, ParseException {
        InstallmentSecuredRatePlan ratePlan = getInstallmentSecuredRatePlan();
        addIbanInvoiceParameter(ratePlan);
        Unzer unzer = getUnzer(Keys.LEGACY_PRIVATE_KEY);
        InstallmentSecuredRatePlan ratePlanReturned =
                unzer.createPaymentType(ratePlan);
        assertNotNull(ratePlanReturned);
        assertRatePlan(ratePlan, ratePlanReturned);

        Authorization authorization = ratePlanReturned.authorize(
                new BigDecimal("370.48"),
                Currency.getInstance("EUR"),
                unsafeUrl("https://www.unzer.com"),
                unzer.createCustomer(getMaximumCustomerSameAddress(generateUuid())).getId(),
                unzer.createBasket(getMaxTestBasketV1()).getId(),
                ratePlan.getEffectiveInterestRate());
        assertValidAuthorize(ratePlan, authorization);
    }

    private void assertValidAuthorize(InstallmentSecuredRatePlan ratePlan,
                                      Authorization authorization) {
        assertNotNull(authorization);
        assertNumberEquals(getAmount(), authorization.getAmount());
        assertEquals(Authorization.Status.SUCCESS, authorization.getStatus());
        assertNumberEquals(ratePlan.getEffectiveInterestRate(),
                authorization.getEffectiveInterestRate());
        assertNotNull(authorization.getProcessing().getPdfLink());
        assertNotNull(authorization.getProcessing().getExternalOrderId());
        assertNotNull(authorization.getProcessing().getExternalOrderId());
        assertNotNull(authorization.getProcessing().getZgReferenceId());

        assertNotNull(authorization.getPaymentId());
        assertNotNull(authorization.getCustomerId());
        assertNotNull(authorization.getBasketId());
        assertNotNull(authorization.getTypeId());
    }

    private BigDecimal getAmount() {
        BigDecimal amount = new BigDecimal("370.4800");
        return amount.setScale(2, RoundingMode.HALF_UP);
    }

    @Disabled("https://unz.atlassian.net/browse/AHC-5292")
    @Test
    public void testAuthorizeViaTypeWithIbanBasketV2()
            throws HttpCommunicationException, ParseException {
        InstallmentSecuredRatePlan ratePlan = getInstallmentSecuredRatePlan();
        addIbanInvoiceParameter(ratePlan);
        Unzer unzer = getUnzer(Keys.LEGACY_PRIVATE_KEY);
        InstallmentSecuredRatePlan ratePlanReturned =
                unzer.createPaymentType(ratePlan);
        assertNotNull(ratePlanReturned);
        assertRatePlan(ratePlan, ratePlanReturned);

        Authorization authorization = ratePlanReturned.authorize(
                new BigDecimal("370.48"),
                Currency.getInstance("EUR"),
                unsafeUrl("https://www.unzer.com"),
                unzer.createCustomer(getMaximumCustomerSameAddress(generateUuid())).getId(),
                unzer.createBasket(getMaxTestBasketV2()).getId(),
                ratePlan.getEffectiveInterestRate());
        assertValidAuthorize(ratePlan, authorization);
    }

    @Test

    @Deprecated
    public void testAuthorizeViaUnzerTypeIdWithIbanBasketV1()
            throws HttpCommunicationException, ParseException, MalformedURLException {
        Unzer unzer = getUnzer(Keys.LEGACY_PRIVATE_KEY);

        InstallmentSecuredRatePlan ratePlan = getInstallmentSecuredRatePlan();
        addIbanInvoiceParameter(ratePlan);
        InstallmentSecuredRatePlan ratePlanReturned = unzer.createPaymentType(ratePlan);
        assertNotNull(ratePlanReturned);
        assertRatePlan(ratePlan, ratePlanReturned);

        Authorization authorization = unzer
                .authorize(
                        getAuthorization(
                                ratePlanReturned.getId(),
                                unzer.createCustomer(getMaximumCustomerSameAddress(generateUuid())).getId(),
                                unzer.createBasket(getMaxTestBasketV1()).getId(),
                                ratePlan.getEffectiveInterestRate()
                        )
                );
        assertValidAuthorize(ratePlan, authorization);
    }

    protected Authorization getAuthorization(String typeId, String customerId, String basketId,
                                             BigDecimal effectiveInterestRate)
            throws MalformedURLException {
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

    @Disabled("https://unz.atlassian.net/browse/AHC-5292")
    @Test
    public void testAuthorizeViaUnzerTypeIdWithIbanBasketV2()
            throws HttpCommunicationException, ParseException, MalformedURLException {
        InstallmentSecuredRatePlan ratePlan = getInstallmentSecuredRatePlan();
        addIbanInvoiceParameter(ratePlan);
        Unzer unzer = getUnzer(Keys.LEGACY_PRIVATE_KEY);
        InstallmentSecuredRatePlan ratePlanReturned = unzer.createPaymentType(ratePlan);
        assertNotNull(ratePlanReturned);
        assertRatePlan(ratePlan, ratePlanReturned);

        Authorization authorization = unzer
                .authorize(
                        getAuthorization(
                                ratePlanReturned.getId(),
                                unzer.createCustomer(getMaximumCustomerSameAddress(generateUuid())).getId(),
                                unzer.createBasket(getMaxTestBasketV2()).getId(),
                                ratePlan.getEffectiveInterestRate()
                        )
                );
        assertValidAuthorize(ratePlan, authorization);
    }

    @Test

    @Deprecated
    public void testChargeViaAuthorizeBasketV1() throws HttpCommunicationException, ParseException {
        Unzer unzer = getUnzer(Keys.LEGACY_PRIVATE_KEY);
        InstallmentSecuredRatePlan ratePlan = unzer.createPaymentType(getInstallmentSecuredRatePlan());
        InstallmentSecuredRatePlan ratePlanReturned =
                (InstallmentSecuredRatePlan) unzer.fetchPaymentType(ratePlan.getId());
        assertNotNull(ratePlanReturned);
        assertRatePlan(ratePlan, ratePlanReturned);

        Authorization authorization = ratePlanReturned.authorize(
                new BigDecimal("370.48"),
                Currency.getInstance("EUR"),
                unsafeUrl("https://www.unzer.com"),
                unzer.createCustomer(getMaximumCustomerSameAddress(generateUuid())).getId(),
                unzer.createBasket(getMaxTestBasketV1()).getId(),
                ratePlan.getEffectiveInterestRate());
        assertValidAuthorize(ratePlan, authorization);

        Charge charge = authorization.charge();
        assertValidCharge(charge);
    }

    private void assertValidCharge(Charge charge) {
        assertNotNull(charge);
        assertNotNull(charge.getProcessing().getExternalOrderId());
        assertNotNull(charge.getProcessing().getUniqueId());
        assertNotNull(charge.getProcessing().getShortId());
        assertNumberEquals(getAmount(), charge.getAmount());
        assertEquals(Charge.Status.SUCCESS, charge.getStatus());
    }

    @Disabled("https://unz.atlassian.net/browse/AHC-5292")
    @Test
    public void testChargeViaAuthorizeBasketV2() throws HttpCommunicationException, ParseException {
        Unzer unzer = getUnzer(Keys.LEGACY_PRIVATE_KEY);
        InstallmentSecuredRatePlan ratePlan =
                unzer.createPaymentType(getInstallmentSecuredRatePlan());
        InstallmentSecuredRatePlan ratePlanReturned = unzer.createPaymentType(ratePlan);
        assertNotNull(ratePlanReturned);
        assertRatePlan(ratePlan, ratePlanReturned);

        Authorization authorization = ratePlanReturned.authorize(
                new BigDecimal("370.48"),
                Currency.getInstance("EUR"),
                unsafeUrl("https://www.unzer.com"),
                unzer.createCustomer(getMaximumCustomerSameAddress(generateUuid())).getId(),
                unzer.createBasket(getMaxTestBasketV2()).getId(),
                ratePlan.getEffectiveInterestRate());
        assertValidAuthorize(ratePlan, authorization);

        Charge charge = authorization.charge();
        assertValidCharge(charge);
    }

    @Test
    @Deprecated

    public void testFullCancellationBeforeShipmentBasketV1()
            throws HttpCommunicationException, ParseException {
        Unzer unzer = getUnzer(Keys.LEGACY_PRIVATE_KEY);
        InstallmentSecuredRatePlan ratePlan =
                unzer.createPaymentType(getInstallmentSecuredRatePlan());
        InstallmentSecuredRatePlan ratePlanReturned =
                (InstallmentSecuredRatePlan) unzer.fetchPaymentType(ratePlan.getId());
        assertNotNull(ratePlanReturned);
        assertRatePlan(ratePlan, ratePlanReturned);

        Authorization authorization = ratePlanReturned.authorize(
                new BigDecimal("370.48"),
                Currency.getInstance("EUR"),
                unsafeUrl("https://www.unzer.com"),
                unzer.createCustomer(getMaximumCustomerSameAddress(generateUuid())).getId(),
                unzer.createBasket(getMaxTestBasketV1()).getId(),
                ratePlan.getEffectiveInterestRate());
        assertValidAuthorize(ratePlan, authorization);

        Charge charge = authorization.charge();
        assertValidCharge(charge);

        Cancel cancel = charge.cancel();
        assertValidCancel(cancel, getAmount());
    }

    private void assertValidCancel(Cancel cancel, BigDecimal cancellationAmount) {
        assertNotNull(cancel);
        assertNotNull(cancel.getProcessing().getUniqueId());
        assertNotNull(cancel.getProcessing().getShortId());
        assertNumberEquals(cancellationAmount, cancel.getAmount());
        assertEquals(Cancel.Status.SUCCESS, cancel.getStatus());
    }

    @Disabled("https://unz.atlassian.net/browse/AHC-5292")
    @Test
    public void testFullCancellationBeforeShipmentBasketV2()
            throws HttpCommunicationException, ParseException {
        Unzer unzer = getUnzer(Keys.LEGACY_PRIVATE_KEY);
        InstallmentSecuredRatePlan ratePlan =
                unzer.createPaymentType(getInstallmentSecuredRatePlan());
        InstallmentSecuredRatePlan ratePlanReturned = unzer.createPaymentType(ratePlan);
        assertNotNull(ratePlanReturned);
        assertRatePlan(ratePlan, ratePlanReturned);

        Authorization authorization = ratePlanReturned.authorize(
                new BigDecimal("370.48"),
                Currency.getInstance("EUR"),
                unsafeUrl("https://www.unzer.com"),
                unzer.createCustomer(getMaximumCustomerSameAddress(generateUuid())).getId(),
                unzer.createBasket(getMaxTestBasketV2()).getId(),
                ratePlan.getEffectiveInterestRate());
        assertValidAuthorize(ratePlan, authorization);

        Charge charge = authorization.charge();
        assertValidCharge(charge);

        Cancel cancel = charge.cancel();
        assertValidCancel(cancel, getAmount());
    }

    @Test

    @Deprecated
    public void testPartialCancellationBeforeShipmentBasketV1()
            throws HttpCommunicationException, ParseException {
        Unzer unzer = getUnzer(Keys.LEGACY_PRIVATE_KEY);
        InstallmentSecuredRatePlan ratePlan =
                unzer.createPaymentType(getInstallmentSecuredRatePlan());
        InstallmentSecuredRatePlan ratePlanReturned =
                (InstallmentSecuredRatePlan) unzer.fetchPaymentType(ratePlan.getId());
        assertNotNull(ratePlanReturned);
        assertRatePlan(ratePlan, ratePlanReturned);

        Authorization authorization = ratePlanReturned.authorize(
                new BigDecimal("370.48"),
                Currency.getInstance("EUR"),
                unsafeUrl("https://www.unzer.com"),
                unzer.createCustomer(getMaximumCustomerSameAddress(generateUuid())).getId(),
                unzer.createBasket(getMaxTestBasketV1()).getId(),
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

    @Disabled("https://unz.atlassian.net/browse/AHC-5292")
    @Test
    public void testPartialCancellationBeforeShipmentBasketV2()
            throws HttpCommunicationException, ParseException {
        Unzer unzer = getUnzer(Keys.LEGACY_PRIVATE_KEY);
        InstallmentSecuredRatePlan ratePlan =
                unzer.createPaymentType(getInstallmentSecuredRatePlan());
        InstallmentSecuredRatePlan ratePlanReturned = unzer.createPaymentType(ratePlan);
        assertNotNull(ratePlanReturned);
        assertRatePlan(ratePlan, ratePlanReturned);

        Authorization authorization = ratePlanReturned.authorize(
                new BigDecimal("370.48"),
                Currency.getInstance("EUR"),
                unsafeUrl("https://www.unzer.com"),
                unzer.createCustomer(getMaximumCustomerSameAddress(generateUuid())).getId(),
                unzer.createBasket(getMaxTestBasketV2()).getId(),
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
        Unzer unzer = getUnzer(Keys.LEGACY_PRIVATE_KEY);
        InstallmentSecuredRatePlan ratePlanReturned = unzer.createPaymentType(ratePlan);
        assertNotNull(ratePlanReturned);
        assertRatePlan(ratePlan, ratePlanReturned);

        Authorization authorization = ratePlanReturned.authorize(
                new BigDecimal("370.48"),
                Currency.getInstance("EUR"),
                unsafeUrl("https://www.unzer.com"),
                unzer.createCustomer(getMaximumCustomerSameAddress(generateUuid())).getId(),
                unzer.createBasket(getMaxTestBasketV1()).getId(),
                ratePlan.getEffectiveInterestRate());
        assertValidAuthorize(ratePlan, authorization);

        Charge charge = authorization.charge();
        assertValidCharge(charge);

        Shipment shipment = unzer.shipment(charge.getPaymentId());
        assertValidShipment(shipment);
    }

    private void assertValidShipment(Shipment shipment) {
        assertNotNull(shipment);
    }

    @Disabled("https://unz.atlassian.net/browse/AHC-5292")
    @Test
    public void testShipmentBasketV2() throws HttpCommunicationException, ParseException {
        InstallmentSecuredRatePlan ratePlan = getInstallmentSecuredRatePlan();
        addIbanInvoiceParameter(ratePlan);
        Unzer unzer = getUnzer(Keys.LEGACY_PRIVATE_KEY);
        InstallmentSecuredRatePlan ratePlanReturned =
                unzer.createPaymentType(ratePlan);
        assertNotNull(ratePlanReturned);
        assertRatePlan(ratePlan, ratePlanReturned);

        Authorization authorization = ratePlanReturned.authorize(
                new BigDecimal("370.48"),
                Currency.getInstance("EUR"),
                unsafeUrl("https://www.unzer.com"),
                unzer.createCustomer(getMaximumCustomerSameAddress(generateUuid())).getId(),
                unzer.createBasket(getMaxTestBasketV2()).getId(),
                ratePlan.getEffectiveInterestRate());
        assertValidAuthorize(ratePlan, authorization);

        Charge charge = authorization.charge();
        assertValidCharge(charge);

        Shipment shipment = unzer.shipment(charge.getPaymentId());
        assertValidShipment(shipment);
    }

    @Test
    @Deprecated

    public void testFullCancelAfterShipmentBasketV1()
            throws HttpCommunicationException, ParseException {
        InstallmentSecuredRatePlan ratePlan = getInstallmentSecuredRatePlan();
        addIbanInvoiceParameter(ratePlan);
        Unzer unzer = getUnzer(Keys.LEGACY_PRIVATE_KEY);
        InstallmentSecuredRatePlan ratePlanReturned = unzer.createPaymentType(ratePlan);
        assertNotNull(ratePlanReturned);
        assertRatePlan(ratePlan, ratePlanReturned);

        Authorization authorization = ratePlanReturned.authorize(
                new BigDecimal("370.48"),
                Currency.getInstance("EUR"),
                unsafeUrl("https://www.unzer.com"),
                unzer.createCustomer(getMaximumCustomerSameAddress(generateUuid())).getId(),
                unzer.createBasket(getMaxTestBasketV1()).getId(),
                ratePlan.getEffectiveInterestRate());
        assertValidAuthorize(ratePlan, authorization);

        Charge charge = authorization.charge();
        assertValidCharge(charge);

        Shipment shipment = unzer.shipment(charge.getPaymentId());
        assertValidShipment(shipment);

        Cancel cancel = charge.cancel();
        assertValidCancel(cancel, getAmount());
    }

    @Disabled("Until https://unz.atlassian.net/browse/AHC-5292 is resolved")
    @Test
    public void testFullCancelAfterShipmentBasketV2()
            throws HttpCommunicationException, ParseException {
        InstallmentSecuredRatePlan ratePlan = getInstallmentSecuredRatePlan();
        addIbanInvoiceParameter(ratePlan);
        Unzer unzer = getUnzer(Keys.LEGACY_PRIVATE_KEY);
        InstallmentSecuredRatePlan ratePlanReturned =
                unzer.createPaymentType(ratePlan);
        assertNotNull(ratePlanReturned);
        assertRatePlan(ratePlan, ratePlanReturned);

        Authorization authorization = ratePlanReturned.authorize(
                new BigDecimal("380.38"),
                Currency.getInstance("EUR"),
                unsafeUrl("https://www.unzer.com"),
                unzer.createCustomer(getMaximumCustomerSameAddress(generateUuid())).getId(),
                unzer.createBasket(getMaxTestBasketV2()).getId(),
                ratePlan.getEffectiveInterestRate());
        assertValidAuthorize(ratePlan, authorization);

        Charge charge = authorization.charge();
        assertValidCharge(charge);

        Shipment shipment = unzer.shipment(charge.getPaymentId());
        assertValidShipment(shipment);

        Cancel cancel = charge.cancel();
        assertValidCancel(cancel, getAmount());
    }

    @Test

    @Deprecated
    public void testAuthorizeHirePurchaseDirectDebitBasketV1()
            throws HttpCommunicationException, ParseException {
        Unzer unzer = getUnzer(Keys.LEGACY_PRIVATE_KEY);
        HttpClientBasedRestCommunication restCommunication = new HttpClientBasedRestCommunication();
        JsonParser jsonParser = new JsonParser();
        PaymentService paymentService = new PaymentService(unzer, restCommunication);
        Customer maximumCustomer = unzer.createCustomer(getMaximumCustomer(generateUuid()));
        Basket basket = unzer.createBasket(getMaxTestBasketV1(new BigDecimal("380.48")));

        // FIXME: hardcoded endpoint
        String response = restCommunication.httpPost(
                "https://sbx-api.unzer.com/v1/types/hire-purchase-direct-debit",
                Keys.LEGACY_PRIVATE_KEY,
                getInstallmentSecuredRatePlan(new BigDecimal("380.48"))
        );
        ApiIdObject jsonResponse = jsonParser.fromJson(response, ApiIdObject.class);
        InstallmentSecuredRatePlan installmentSecuredRatePlan = paymentService.fetchPaymentType(jsonResponse.getId());

        assertTrue(
                installmentSecuredRatePlan.getId().matches("s-hdd-\\w*")
        );

        Authorization authorize = installmentSecuredRatePlan.authorize(
                new BigDecimal("380.48"),
                Currency.getInstance("EUR"),
                unsafeUrl("https://www.meinShop.de"),
                maximumCustomer.getCustomerId(),
                basket.getId(),
                new BigDecimal("5.5"));
        assertNotNull(authorize.getPaymentId());
    }

    private InstallmentSecuredRatePlan getInstallmentSecuredRatePlan(BigDecimal amount)
            throws ParseException, HttpCommunicationException {
        BigDecimal effectiveInterestRate = new BigDecimal("5.5");
        Date orderDate = getDate("21.06.2019");
        List<InstallmentSecuredRatePlan> rateList =
                getUnzer(Keys.LEGACY_PRIVATE_KEY).installmentSecuredRates(amount,
                        Currency.getInstance("EUR"),
                        effectiveInterestRate, orderDate);
        InstallmentSecuredRatePlan ratePlan = rateList.get(0);
        return ratePlan;
    }

    @Disabled("https://unz.atlassian.net/browse/AHC-5292")
    @Test
    public void testAuthorizeHirePurchaseDirectDebitBasketV2()
            throws HttpCommunicationException, ParseException {
        Unzer unzer = getUnzer(Keys.LEGACY_PRIVATE_KEY);
        HttpClientBasedRestCommunication restCommunication = new HttpClientBasedRestCommunication();
        JsonParser jsonParser = new JsonParser();
        PaymentService paymentService = new PaymentService(unzer, restCommunication);
        Customer maximumCustomer = unzer.createCustomer(getMaximumCustomer(generateUuid()));
        Basket basket = unzer.createBasket(getMaxTestBasketV1(new BigDecimal("380.48")));

        // FIXME: hardcoded endpoint
        String response = restCommunication.httpPost(
                "https://api.unzer.com/v1/types/hire-purchase-direct-debit",
                unzer.getPrivateKey(),
                getInstallmentSecuredRatePlan(new BigDecimal("380.48"))
        );
        ApiIdObject jsonResponse = jsonParser.fromJson(response, ApiIdObject.class);
        InstallmentSecuredRatePlan installmentSecuredRatePlan =
                paymentService.fetchPaymentType(jsonResponse.getId());

        assertTrue(
                installmentSecuredRatePlan.getId().matches("s-hdd-\\w*")
        );

        Authorization authorize = installmentSecuredRatePlan.authorize(
                new BigDecimal("380.48"),
                Currency.getInstance("EUR"),
                unsafeUrl("https://www.meinShop.de"),
                maximumCustomer.getCustomerId(),
                basket.getId(),
                new BigDecimal("5.5"));
        assertNotNull(authorize.getPaymentId());
    }
}
