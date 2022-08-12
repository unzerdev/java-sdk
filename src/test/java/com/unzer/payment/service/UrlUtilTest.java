package com.unzer.payment.service;

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


import com.unzer.payment.business.AbstractPaymentTest;
import com.unzer.payment.business.Keys;
import com.unzer.payment.business.paymenttypes.InstallmentSecuredRatePlan;
import com.unzer.payment.communication.HttpCommunicationException;
import com.unzer.payment.paymenttypes.InvoiceSecured;
import com.unzer.payment.paymenttypes.PaymentType;
import com.unzer.payment.paymenttypes.SepaDirectDebitSecured;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class UrlUtilTest extends AbstractPaymentTest {
    private static final String sbxTypesUrl = "https://sbx-api.unzer.com/v1/types/";
    private static final String id = "random-id";

    @Test
    public void testUnknownPaymentType() {
        assertEquals(
                sbxTypesUrl + id,
                new UrlUtil(Keys.KEY_WITHOUT_3DS).getHttpGetUrl(id)
        );
    }

    @Test
    public void testGetUrlForPaymentTypeInvoiceSecured() {
        runTest(new InvoiceSecured(), "invoice-secured/");
    }

    @Test
    public void testGetUrlForPaymentTypeInstallmentSecured() {
        runTest(new InstallmentSecuredRatePlan(), "installment-secured/");
    }

    @Test
    public void testGetUrlForPaymentTypeSepaDirectDebitSecured() throws HttpCommunicationException {
        runTest(new SepaDirectDebitSecured(""), "sepa-direct-debit-secured/");
    }

    private void runTest(PaymentType type, String typePartUrl) {
        assertEquals(
                sbxTypesUrl + typePartUrl + id,
                new UrlUtil(Keys.KEY_WITHOUT_3DS).getHttpGetUrl(type, id)
        );
    }
}
