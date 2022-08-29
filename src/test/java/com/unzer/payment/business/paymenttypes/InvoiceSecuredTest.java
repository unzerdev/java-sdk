package com.unzer.payment.business.paymenttypes;


import com.unzer.payment.*;
import com.unzer.payment.business.AbstractPaymentTest;
import com.unzer.payment.communication.HttpCommunicationException;
import com.unzer.payment.communication.JsonParser;
import com.unzer.payment.communication.impl.HttpClientBasedRestCommunication;
import com.unzer.payment.communication.json.JsonIdObject;
import com.unzer.payment.paymenttypes.InvoiceSecured;
import com.unzer.payment.service.PaymentService;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.util.Currency;
import java.util.Date;

import static com.unzer.payment.business.BasketV1TestData.getMaxTestBasketV1;
import static com.unzer.payment.business.BasketV1TestData.getMinTestBasketV1;
import static com.unzer.payment.business.BasketV2TestData.getMaxTestBasketV2;
import static com.unzer.payment.business.BasketV2TestData.getMinTestBasketV2;
import static com.unzer.payment.util.Url.unsafeUrl;
import static com.unzer.payment.util.Uuid.generateUuid;
import static org.junit.jupiter.api.Assertions.*;

public class InvoiceSecuredTest extends AbstractPaymentTest {

    @Test
    @Deprecated
    public void testChargeTypeWithInvoiceIdBasketV1()
            throws HttpCommunicationException, MalformedURLException, ParseException {
        InvoiceSecured invoice = getUnzer().createPaymentType(getInvoiceSecured());
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
    public void testChargeTypeWithInvoiceIdBasketV2() throws HttpCommunicationException, MalformedURLException, ParseException {
        InvoiceSecured invoice = getUnzer().createPaymentType(getInvoiceSecured());
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
        InvoiceSecured invoice = getUnzer().createPaymentType(getInvoiceSecured());
        assertNotNull(invoice.getId());
    }

    @Test
    @Deprecated
    public void testChargeTypeBasketV1() throws HttpCommunicationException, MalformedURLException, ParseException {
        InvoiceSecured invoice = getUnzer().createPaymentType(getInvoiceSecured());
        Basket basket = getMinTestBasketV1();
        Charge chargeResult = invoice.charge(basket.getAmountTotalGross(), Currency.getInstance("EUR"), new URL("https://www.meinShop.de"), getMaximumCustomerSameAddress(generateUuid()), basket, invoice.getId());
        assertNotNull(chargeResult);
    }

    @Test
    public void testChargeTypeBasketV2() throws HttpCommunicationException, MalformedURLException, ParseException {
        InvoiceSecured invoice = getUnzer().createPaymentType(getInvoiceSecured());
        Basket basket = getMinTestBasketV2();
        Charge chargeResult = invoice.charge(basket.getTotalValueGross(), Currency.getInstance("EUR"), new URL("https://www.meinShop.de"), getMaximumCustomerSameAddress(generateUuid()), basket, invoice.getId());
        assertNotNull(chargeResult);
    }


    @Test
    @Deprecated
    public void testChargeTypeWithInvalidCurrencyBasketV1() throws HttpCommunicationException {
        InvoiceSecured invoice = getUnzer().createPaymentType(getInvoiceSecured());
        Basket basket = getMinTestBasketV1();

        assertThrows(PaymentException.class, () -> {
            invoice.charge(basket.getAmountTotalGross(), Currency.getInstance("PLN"), new URL("https://www.meinShop.de"), getMaximumCustomerSameAddress(generateUuid()), basket, invoice.getId());
        });
    }

    @Test
    public void testChargeTypeWithInvalidCurrencyBasketV2() throws HttpCommunicationException {
        InvoiceSecured invoice = getUnzer().createPaymentType(getInvoiceSecured());
        Basket basket = getMinTestBasketV2();
        assertThrows(PaymentException.class, () -> {
            invoice.charge(basket.getTotalValueGross(), Currency.getInstance("PLN"), new URL("https://www.meinShop.de"), getMaximumCustomerSameAddress(generateUuid()), basket, invoice.getId());
        });
    }


    @Test
    public void testChargeTypeDifferentAddresses() throws HttpCommunicationException {
        InvoiceSecured invoice = getUnzer().createPaymentType(getInvoiceSecured());
        assertThrows(PaymentException.class, () -> {
            invoice.charge(BigDecimal.TEN, Currency.getInstance("EUR"), new URL("https://www.meinShop.de"), getMaximumCustomer(generateUuid()));
        });
    }

    @Test
    @Deprecated
    public void testShipmentInvoiceSecuredTypeBasketV1() throws HttpCommunicationException, MalformedURLException, ParseException {
        InvoiceSecured invoice = getUnzer().createPaymentType(getInvoiceSecured());
        Basket basket = getMinTestBasketV1();
        String invoiceId = new Date().getTime() + "";
        Charge charge = getUnzer().charge(basket.getAmountTotalGross(), Currency.getInstance("EUR"), invoice, new URL("https://www.meinShop.de"), getMaximumCustomerSameAddress(generateUuid()), basket);
        Shipment shipment = getUnzer().shipment(charge.getPaymentId(), invoiceId);
        assertNotNull(shipment);
        assertNotNull(shipment.getId());
        assertEquals(invoiceId, shipment.getInvoiceId());
    }

    @Test
    public void testShipmentInvoiceSecuredTypeBasketV2() throws HttpCommunicationException, MalformedURLException, ParseException {
        InvoiceSecured invoice = getUnzer().createPaymentType(getInvoiceSecured());
        Basket basket = getMinTestBasketV2();
        String invoiceId = new Date().getTime() + "";
        Charge charge = getUnzer().charge(basket.getTotalValueGross(), Currency.getInstance("EUR"), invoice, new URL("https://www.meinShop.de"), getMaximumCustomerSameAddress(generateUuid()), basket);
        Shipment shipment = getUnzer().shipment(charge.getPaymentId(), invoiceId);
        assertNotNull(shipment);
        assertNotNull(shipment.getId());
        assertEquals(invoiceId, shipment.getInvoiceId());
    }


    @Test
    public void testFetchInvoiceSecuredType() throws HttpCommunicationException {
        InvoiceSecured invoice = getUnzer().createPaymentType(getInvoiceSecured());
        assertNotNull(invoice.getId());
        InvoiceSecured fetchedInvoiceSecured = (InvoiceSecured) getUnzer().fetchPaymentType(invoice.getId());
        assertNotNull(fetchedInvoiceSecured.getId());
    }

    @Test
    public void testChargeInvoiceGuaranteed() throws HttpCommunicationException, MalformedURLException, ParseException {
        Unzer unzer = getUnzer();
        HttpClientBasedRestCommunication restCommunication = new HttpClientBasedRestCommunication();
        JsonParser jsonParser = new JsonParser();
        PaymentService paymentService = new PaymentService(unzer, restCommunication);

        String response = restCommunication.httpPost("https://api.unzer.com/v1/types/invoice-guaranteed", unzer.getPrivateKey(), new InvoiceSecured());
        JsonIdObject jsonResponse = jsonParser.fromJson(response, JsonIdObject.class);
        InvoiceSecured invoiceSecured = paymentService.fetchPaymentType(jsonResponse.getId());

        boolean matches = invoiceSecured.getId().matches("s-ivg-\\w*");
        assertTrue(matches);

        Charge charge = invoiceSecured.charge(BigDecimal.TEN, Currency.getInstance("EUR"), new URL("https://www.meinShop.de"), getMaximumCustomerSameAddress(generateUuid()));
        assertNotNull(charge.getPaymentId());
    }

    @Test
    @Deprecated
    public void testChargeInvoiceFactoringBasketV1() throws HttpCommunicationException, MalformedURLException, ParseException {
        Unzer unzer = getUnzer();
        HttpClientBasedRestCommunication restCommunication = new HttpClientBasedRestCommunication();
        JsonParser jsonParser = new JsonParser();
        PaymentService paymentService = new PaymentService(unzer, restCommunication);

        String response = restCommunication.httpPost("https://api.unzer.com/v1/types/invoice-factoring", unzer.getPrivateKey(), new InvoiceSecured());
        JsonIdObject jsonResponse = jsonParser.fromJson(response, JsonIdObject.class);
        InvoiceSecured invoiceSecured = paymentService.fetchPaymentType(jsonResponse.getId());

        boolean matches = invoiceSecured.getId().matches("s-ivf-\\w*");
        assertTrue(matches);

        Charge charge = invoiceSecured.charge(
                BigDecimal.TEN,
                Currency.getInstance("EUR"),
                new URL("https://www.meinShop.de"),
                getMaximumCustomerSameAddress(generateUuid()),
                createBasket(getMaxTestBasketV1()),
                generateUuid());
        assertNotNull(charge.getPaymentId());
    }

    @Test
    public void testChargeInvoiceFactoringBasketV2() throws HttpCommunicationException, ParseException {
        Unzer unzer = getUnzer();
        HttpClientBasedRestCommunication restCommunication = new HttpClientBasedRestCommunication();
        JsonParser jsonParser = new JsonParser();
        PaymentService paymentService = new PaymentService(unzer, restCommunication);

        String response = restCommunication.httpPost("https://api.unzer.com/v1/types/invoice-factoring", unzer.getPrivateKey(), new InvoiceSecured());
        JsonIdObject jsonResponse = jsonParser.fromJson(response, JsonIdObject.class);
        InvoiceSecured invoiceSecured = paymentService.fetchPaymentType(jsonResponse.getId());

        boolean matches = invoiceSecured.getId().matches("s-ivf-\\w*");
        assertTrue(matches);

        Charge charge = invoiceSecured.charge(BigDecimal.TEN,
                Currency.getInstance("EUR"),
                unsafeUrl("https://www.meinShop.de"),
                getMaximumCustomerSameAddress(generateUuid()),
                createBasket(getMaxTestBasketV2()),
                generateUuid());
        assertNotNull(charge.getPaymentId());
    }


    private InvoiceSecured getInvoiceSecured() {
        return new InvoiceSecured();
    }

}
