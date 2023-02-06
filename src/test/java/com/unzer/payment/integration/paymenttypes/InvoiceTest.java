/*
 * Copyright 2020-today Unzer E-Com GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.unzer.payment.integration.paymenttypes;


import com.unzer.payment.Charge;
import com.unzer.payment.Unzer;
import com.unzer.payment.business.AbstractPaymentTest;
import com.unzer.payment.communication.HttpCommunicationException;
import com.unzer.payment.paymenttypes.Invoice;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Currency;

import static com.unzer.payment.business.Keys.KEY_WITH_3DS;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class InvoiceTest extends AbstractPaymentTest {

    @Test
    public void testCreateInvoiceMandatoryType() throws HttpCommunicationException {
        Invoice invoice = getUnzer(KEY_WITH_3DS).createPaymentType(new Invoice());
        assertNotNull(invoice.getId());
    }

    @Test
    public void testChargeType() throws HttpCommunicationException, MalformedURLException {
        Invoice invoice = getUnzer(KEY_WITH_3DS).createPaymentType(new Invoice());
        Charge charge = invoice.charge(BigDecimal.ONE, Currency.getInstance("EUR"), new URL("https://www.meinShop.de"));
        assertNotNull(charge);
        assertNotNull(charge.getId());
    }

    @Test
    public void testFetchInvoiceType() throws HttpCommunicationException {
        Unzer unzer = getUnzer(KEY_WITH_3DS);
		Invoice invoice = unzer.createPaymentType(new Invoice());
        assertNotNull(invoice.getId());
        Invoice fetchedInvoice = (Invoice) unzer.fetchPaymentType(invoice.getId());
        assertNotNull(fetchedInvoice.getId());
    }



}