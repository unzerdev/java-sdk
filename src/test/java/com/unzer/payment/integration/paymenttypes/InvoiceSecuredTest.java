package com.unzer.payment.integration.paymenttypes;


import com.unzer.payment.*;
import com.unzer.payment.business.AbstractPaymentTest;
import com.unzer.payment.business.Keys;
import com.unzer.payment.communication.HttpCommunicationException;
import com.unzer.payment.communication.JsonParser;
import com.unzer.payment.communication.impl.HttpClientBasedRestCommunication;
import com.unzer.payment.communication.json.ApiIdObject;
import com.unzer.payment.paymenttypes.InvoiceSecured;
import com.unzer.payment.service.PaymentService;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;

import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.Currency;
import java.util.Date;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.unzer.payment.business.BasketV1TestData.getMaxTestBasketV1;
import static com.unzer.payment.business.BasketV1TestData.getMinTestBasketV1;
import static com.unzer.payment.business.BasketV2TestData.getMaxTestBasketV2;
import static com.unzer.payment.business.BasketV2TestData.getMinTestBasketV2;
import static com.unzer.payment.util.Url.unsafeUrl;
import static com.unzer.payment.util.Uuid.generateUuid;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

public class InvoiceSecuredTest extends AbstractPaymentTest {

    @Test
    @Deprecated
    public void testChargeTypeWithInvoiceIdBasketV1()
            throws HttpCommunicationException, MalformedURLException {
        InvoiceSecured invoice = getUnzer(Keys.LEGACY_PRIVATE_KEY).createPaymentType(getInvoiceSecured());
        Basket basket = getMinTestBasketV1();
        String invoiceId = getRandomInvoiceId();
        Charge charge = invoice.charge(basket.getAmountTotalGross(), Currency.getInstance("EUR"),
                new URL("https://www.meinShop.de"), getMaximumCustomerSameAddress(generateUuid()), basket,
                invoiceId);
        assertNotNull(charge);
        assertNotNull(charge.getPaymentId());
        assertEquals(invoiceId, charge.getInvoiceId());
    }

    @Test
    public void testChargeTypeWithInvoiceIdBasketV2() throws HttpCommunicationException, MalformedURLException  {
        InvoiceSecured invoice = getUnzer(Keys.LEGACY_PRIVATE_KEY).createPaymentType(getInvoiceSecured());
        Basket basket = getMinTestBasketV2();
        String invoiceId = getRandomInvoiceId();
        Charge charge = invoice.charge(basket.getTotalValueGross(), Currency.getInstance("EUR"),
                new URL("https://www.meinShop.de"), getMaximumCustomerSameAddress(generateUuid()), basket,
                invoiceId);
        assertNotNull(charge);
        assertNotNull(charge.getPaymentId());
        assertEquals(invoiceId, charge.getInvoiceId());
    }

    @Test
    public void testCreateInvoiceSecuredMandatoryType() throws HttpCommunicationException {
        InvoiceSecured invoice = getUnzer(Keys.LEGACY_PRIVATE_KEY).createPaymentType(getInvoiceSecured());
        assertNotNull(invoice.getId());
    }

    @Test
    @Deprecated
    public void testChargeTypeBasketV1() throws HttpCommunicationException, MalformedURLException {
        InvoiceSecured invoice = getUnzer(Keys.LEGACY_PRIVATE_KEY).createPaymentType(getInvoiceSecured());
        Basket basket = getMinTestBasketV1();
        Charge chargeResult = invoice.charge(basket.getAmountTotalGross(), Currency.getInstance("EUR"), new URL("https://www.meinShop.de"), getMaximumCustomerSameAddress(generateUuid()), basket, invoice.getId());
        assertNotNull(chargeResult);
    }

    @Test
    public void testChargeTypeBasketV2() throws HttpCommunicationException, MalformedURLException  {
        InvoiceSecured invoice = getUnzer(Keys.LEGACY_PRIVATE_KEY).createPaymentType(getInvoiceSecured());
        Basket basket = getMinTestBasketV2();
        Charge chargeResult = invoice.charge(basket.getTotalValueGross(), Currency.getInstance("EUR"), new URL("https://www.meinShop.de"), getMaximumCustomerSameAddress(generateUuid()), basket, invoice.getId());
        assertNotNull(chargeResult);
    }


    @Test
    @Deprecated
    public void testChargeTypeWithInvalidCurrencyBasketV1() throws HttpCommunicationException {
        InvoiceSecured invoice = getUnzer(Keys.LEGACY_PRIVATE_KEY).createPaymentType(getInvoiceSecured());
        Basket basket = getMinTestBasketV1();

        assertThrows(PaymentException.class, () -> {
            invoice.charge(basket.getAmountTotalGross(), Currency.getInstance("PLN"), new URL("https://www.meinShop.de"), getMaximumCustomerSameAddress(generateUuid()), basket, invoice.getId());
        });
    }

    @Test
    public void testChargeTypeWithInvalidCurrencyBasketV2() throws HttpCommunicationException {
        InvoiceSecured invoice = getUnzer(Keys.LEGACY_PRIVATE_KEY).createPaymentType(getInvoiceSecured());
        Basket basket = getMinTestBasketV2();
        assertThrows(PaymentException.class, () -> {
            invoice.charge(basket.getTotalValueGross(), Currency.getInstance("PLN"), new URL("https://www.meinShop.de"), getMaximumCustomerSameAddress(generateUuid()), basket, invoice.getId());
        });
    }


    @Test
    public void testChargeTypeDifferentAddresses() throws HttpCommunicationException {
        InvoiceSecured invoice = getUnzer(Keys.LEGACY_PRIVATE_KEY).createPaymentType(getInvoiceSecured());
        assertThrows(PaymentException.class, () -> {
            invoice.charge(BigDecimal.TEN, Currency.getInstance("EUR"), new URL("https://www.meinShop.de"), getMaximumCustomer(generateUuid()));
        });
    }

    @Test
    @Deprecated
    public void testShipmentInvoiceSecuredTypeBasketV1() throws HttpCommunicationException, MalformedURLException  {
        InvoiceSecured invoice = getUnzer(Keys.LEGACY_PRIVATE_KEY).createPaymentType(getInvoiceSecured());
        Basket basket = getMinTestBasketV1();
        String invoiceId = new Date().getTime() + "";
        Charge charge = getUnzer(Keys.LEGACY_PRIVATE_KEY).charge(basket.getAmountTotalGross(), Currency.getInstance("EUR"), invoice, new URL("https://www.meinShop.de"), getMaximumCustomerSameAddress(generateUuid()), basket);
        Shipment shipment = getUnzer(Keys.LEGACY_PRIVATE_KEY).shipment(charge.getPaymentId(), invoiceId);
        assertNotNull(shipment);
        assertNotNull(shipment.getId());
        assertEquals(invoiceId, shipment.getInvoiceId());
    }

    @Test
    public void testShipmentInvoiceSecuredTypeBasketV2() throws HttpCommunicationException, MalformedURLException  {
        InvoiceSecured invoice = getUnzer(Keys.LEGACY_PRIVATE_KEY).createPaymentType(getInvoiceSecured());
        Basket basket = getMinTestBasketV2();
        String invoiceId = new Date().getTime() + "";
        Charge charge = getUnzer(Keys.LEGACY_PRIVATE_KEY).charge(basket.getTotalValueGross(), Currency.getInstance("EUR"), invoice, new URL("https://www.meinShop.de"), getMaximumCustomerSameAddress(generateUuid()), basket);
        Shipment shipment = getUnzer(Keys.LEGACY_PRIVATE_KEY).shipment(charge.getPaymentId(), invoiceId);
        assertNotNull(shipment);
        assertNotNull(shipment.getId());
        assertEquals(invoiceId, shipment.getInvoiceId());
    }


    @Test
    public void testFetchInvoiceSecuredType() throws HttpCommunicationException {
        InvoiceSecured invoice = getUnzer(Keys.LEGACY_PRIVATE_KEY).createPaymentType(getInvoiceSecured());
        assertNotNull(invoice.getId());
        InvoiceSecured fetchedInvoiceSecured = (InvoiceSecured) getUnzer(Keys.LEGACY_PRIVATE_KEY).fetchPaymentType(invoice.getId());
        assertNotNull(fetchedInvoiceSecured.getId());
    }

    @Test
    public void testChargeInvoiceGuaranteed() throws HttpCommunicationException, MalformedURLException  {
        Unzer unzer = getUnzer(Keys.LEGACY_PRIVATE_KEY);
        HttpClientBasedRestCommunication restCommunication = new HttpClientBasedRestCommunication();
        JsonParser jsonParser = new JsonParser();
        PaymentService paymentService = new PaymentService(unzer, restCommunication);

        String response = restCommunication.httpPost("https://api.unzer.com/v1/types/invoice-guaranteed", unzer.getPrivateKey(), new InvoiceSecured());
        ApiIdObject jsonResponse = jsonParser.fromJson(response, ApiIdObject.class);
        InvoiceSecured invoiceSecured = paymentService.fetchPaymentType(jsonResponse.getId());

        boolean matches = invoiceSecured.getId().matches("s-ivg-\\w*");
        assertTrue(matches);

        Charge charge = invoiceSecured.charge(BigDecimal.TEN, Currency.getInstance("EUR"), new URL("https://www.meinShop.de"), getMaximumCustomerSameAddress(generateUuid()));
        assertNotNull(charge.getPaymentId());
    }

    @Test
    @Deprecated
    public void testChargeInvoiceFactoringBasketV1() throws HttpCommunicationException, MalformedURLException  {
        Unzer unzer = getUnzer(Keys.LEGACY_PRIVATE_KEY);
        HttpClientBasedRestCommunication restCommunication = new HttpClientBasedRestCommunication();
        JsonParser jsonParser = new JsonParser();
        PaymentService paymentService = new PaymentService(unzer, restCommunication);

        String response = restCommunication.httpPost("https://api.unzer.com/v1/types/invoice-factoring", unzer.getPrivateKey(), new InvoiceSecured());
        ApiIdObject jsonResponse = jsonParser.fromJson(response, ApiIdObject.class);
        InvoiceSecured invoiceSecured = paymentService.fetchPaymentType(jsonResponse.getId());

        boolean matches = invoiceSecured.getId().matches("s-ivf-\\w*");
        assertTrue(matches);

        Charge charge = invoiceSecured.charge(
                BigDecimal.TEN,
                Currency.getInstance("EUR"),
                new URL("https://www.meinShop.de"),
                getMaximumCustomerSameAddress(generateUuid()),
                unzer.createBasket(getMaxTestBasketV1()),
                generateUuid());
        assertNotNull(charge.getPaymentId());
    }

    @Test
    public void testChargeInvoiceFactoringBasketV2() throws HttpCommunicationException {
        Unzer unzer = getUnzer(Keys.LEGACY_PRIVATE_KEY);
        HttpClientBasedRestCommunication restCommunication = new HttpClientBasedRestCommunication();
        JsonParser jsonParser = new JsonParser();
        PaymentService paymentService = new PaymentService(unzer, restCommunication);

        String response = restCommunication.httpPost("https://api.unzer.com/v1/types/invoice-factoring", unzer.getPrivateKey(), new InvoiceSecured());
        ApiIdObject jsonResponse = jsonParser.fromJson(response, ApiIdObject.class);
        InvoiceSecured invoiceSecured = paymentService.fetchPaymentType(jsonResponse.getId());

        boolean matches = invoiceSecured.getId().matches("s-ivf-\\w*");
        assertTrue(matches);

        Charge charge = invoiceSecured.charge(BigDecimal.TEN,
                Currency.getInstance("EUR"),
                unsafeUrl("https://www.meinShop.de"),
                getMaximumCustomerSameAddress(generateUuid()),
                unzer.createBasket(getMaxTestBasketV2()),
                generateUuid());
        assertNotNull(charge.getPaymentId());
    }

    @TestFactory
    public Collection<DynamicTest> testCancelInvoiceSecured() {
        return Stream.of(Cancel.ReasonCode.RETURN, Cancel.ReasonCode.RETURN, Cancel.ReasonCode.CREDIT)
                .map(rc -> dynamicTest(rc.name(), () -> {
                    Unzer unzer = getUnzer(Keys.LEGACY_PRIVATE_KEY);
                    InvoiceSecured paymentType = unzer.createPaymentType(new InvoiceSecured());
                    Basket basket = getMaxTestBasketV2();
                    String invoiceId = getRandomInvoiceId();
                    Charge charge = paymentType.charge(
                            basket.getTotalValueGross(),
                            Currency.getInstance("EUR"),
                            unsafeUrl("https://www.meinShop.de"),
                            getMaximumCustomerSameAddress(generateUuid()),
                            basket,
                            invoiceId
                    );
                    Cancel cancel = unzer.cancelCharge(
                            charge.getPaymentId(),
                            charge.getId(),
                            (Cancel) new Cancel(unzer)
                                    .setReasonCode(rc)
                                    .setAmount(basket.getTotalValueGross())
                    );
                    assertNotNull(cancel);
                    assertNotNull(cancel.getId());
                    assertEquals(Cancel.Status.SUCCESS, cancel.getStatus());
                })).collect(Collectors.toList());
    }

    @Test
    public void testCancelFailsWithoutReasonCode() throws HttpCommunicationException {
        PaymentError expectedError = new PaymentError(
                "Reason code is mandatory for the payment type INVOICE_SECURED",
                "Reason code is mandatory for the payment type INVOICE_SECURED. Please contact us for more information.",
                "API.340.100.024"
        );

        Unzer unzer = getUnzer(Keys.LEGACY_PRIVATE_KEY);
        InvoiceSecured paymentType = unzer.createPaymentType(new InvoiceSecured());
        Basket basket = getMaxTestBasketV2();
        String invoiceId = getRandomInvoiceId();
        Charge charge = paymentType.charge(
                basket.getTotalValueGross(),
                Currency.getInstance("EUR"),
                unsafeUrl("https://www.meinShop.de"),
                getMaximumCustomerSameAddress(generateUuid()),
                basket,
                invoiceId
        );

        PaymentException exception = assertThrows(PaymentException.class, () -> {
            unzer.cancelCharge(
                    charge.getPaymentId(),
                    charge.getId(),
                    (Cancel) new Cancel(unzer)
                            .setAmount(basket.getTotalValueGross())
            );
        });
        assertEquals(1, exception.getPaymentErrorList().size());
        PaymentError actualError = exception.getPaymentErrorList().get(0);

        assertEquals(expectedError, actualError);
    }


    @Deprecated
    private InvoiceSecured getInvoiceSecured() {
        return new InvoiceSecured();
    }

}
