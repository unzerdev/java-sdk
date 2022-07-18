package com.unzer.payment.business.paymenttypes;

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

import com.unzer.payment.*;
import com.unzer.payment.business.AbstractPaymentTest;
import com.unzer.payment.communication.HttpCommunicationException;
import com.unzer.payment.communication.JsonParser;
import com.unzer.payment.communication.impl.HttpClientBasedRestCommunication;
import com.unzer.payment.communication.json.JsonIdObject;
import com.unzer.payment.paymenttypes.InvoiceSecured;
import com.unzer.payment.service.PaymentService;
import com.unzer.payment.service.UrlUtil;
import org.junit.Test;

import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.util.Currency;
import java.util.Date;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class InvoiceSecuredTest extends AbstractPaymentTest {

    @Test
    public void testChargeTypeWithInvoiceId()
            throws HttpCommunicationException, MalformedURLException, ParseException {
        InvoiceSecured invoice = getUnzer().createPaymentType(getInvoiceSecured());
        Basket basket = getMinTestBasketV1();
        String invoiceId = getRandomInvoiceId();
        Charge charge = invoice.charge(basket.getAmountTotalGross(), Currency.getInstance("EUR"),
                new URL("https://www.meinShop.de"), getMaximumCustomerSameAddress(getRandomId()), basket,
                invoiceId);
        assertNotNull(charge);
        assertNotNull(charge.getPaymentId());
        assertEquals(invoiceId, charge.getInvoiceId());
    }

    @Test
    public void testCreateInvoiceSecuredManatoryType() throws HttpCommunicationException {
        InvoiceSecured invoice = getUnzer().createPaymentType(getInvoiceSecured());
        assertNotNull(invoice.getId());
    }

    @Test
    public void testChargeType() throws HttpCommunicationException, MalformedURLException, ParseException {
        InvoiceSecured invoice = getUnzer().createPaymentType(getInvoiceSecured());
        Basket basket = getMinTestBasketV1();
        Charge chargeResult = invoice.charge(basket.getAmountTotalGross(), Currency.getInstance("EUR"), new URL("https://www.meinShop.de"), getMaximumCustomerSameAddress(getRandomId()), basket, invoice.getId());
        assertNotNull(chargeResult);
    }

    @Test(expected = PaymentException.class)
    public void testChargeTypeWithInvalidCurrency() throws HttpCommunicationException, MalformedURLException, ParseException {
        InvoiceSecured invoice = getUnzer().createPaymentType(getInvoiceSecured());
        Basket basket = getMinTestBasketV1();
        invoice.charge(basket.getAmountTotalGross(), Currency.getInstance("PLN"), new URL("https://www.meinShop.de"), getMaximumCustomerSameAddress(getRandomId()), basket, invoice.getId());
    }

    @Test(expected = PaymentException.class)
    public void testChargeTypeDifferentAddresses() throws HttpCommunicationException, MalformedURLException, ParseException {
        InvoiceSecured invoice = getUnzer().createPaymentType(getInvoiceSecured());
        invoice.charge(BigDecimal.TEN, Currency.getInstance("EUR"), new URL("https://www.meinShop.de"), getMaximumCustomer(getRandomId()));
    }

    @Test
    public void testShipmentInvoiceSecuredType() throws HttpCommunicationException, MalformedURLException, ParseException {
        InvoiceSecured invoice = getUnzer().createPaymentType(getInvoiceSecured());
        Basket basket = getMinTestBasketV1();
        String invoiceId = new Date().getTime() + "";
        Charge charge = getUnzer().charge(basket.getAmountTotalGross(), Currency.getInstance("EUR"), invoice, new URL("https://www.meinShop.de"), getMaximumCustomerSameAddress(getRandomId()), basket);
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
        UrlUtil urlUtil = new UrlUtil(unzer.getEndPoint());
        HttpClientBasedRestCommunication restCommunication = new HttpClientBasedRestCommunication();
        JsonParser jsonParser = new JsonParser();
        PaymentService paymentService = new PaymentService(unzer, restCommunication);

        String response = restCommunication.httpPost("https://api.unzer.com/v1/types/invoice-guaranteed", unzer.getPrivateKey(), new InvoiceSecured());
        JsonIdObject jsonResponse = jsonParser.fromJson(response, JsonIdObject.class);
        InvoiceSecured invoiceSecured = paymentService.fetchPaymentType(jsonResponse.getId());

        boolean matches = invoiceSecured.getId().matches("s-ivg-\\w*");
        assertTrue(matches);

        Charge charge = invoiceSecured.charge(BigDecimal.TEN, Currency.getInstance("EUR"), new URL("https://www.meinShop.de"), getMaximumCustomerSameAddress(getRandomId()));
        assertNotNull(charge.getPaymentId());
    }

    @Test
    public void testChargeInvoiceFactoring() throws HttpCommunicationException, MalformedURLException, ParseException {
        Unzer unzer = getUnzer();
        UrlUtil urlUtil = new UrlUtil(unzer.getEndPoint());
        HttpClientBasedRestCommunication restCommunication = new HttpClientBasedRestCommunication();
        JsonParser jsonParser = new JsonParser();
        PaymentService paymentService = new PaymentService(unzer, restCommunication);

        String response = restCommunication.httpPost("https://api.unzer.com/v1/types/invoice-factoring", unzer.getPrivateKey(), new InvoiceSecured());
        JsonIdObject jsonResponse = jsonParser.fromJson(response, JsonIdObject.class);
        InvoiceSecured invoiceSecured = paymentService.fetchPaymentType(jsonResponse.getId());

        boolean matches = invoiceSecured.getId().matches("s-ivf-\\w*");
        assertTrue(matches);

        Charge charge = invoiceSecured.charge(BigDecimal.TEN, Currency.getInstance("EUR"), new URL("https://www.meinShop.de"), getMaximumCustomerSameAddress(getRandomId()), createBasketV1(), getRandomId());
        assertNotNull(charge.getPaymentId());
    }


    private InvoiceSecured getInvoiceSecured() {
        return new InvoiceSecured();
    }

}
