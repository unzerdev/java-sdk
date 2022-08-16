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


import com.unzer.payment.Unzer;
import com.unzer.payment.business.AbstractPaymentTest;
import com.unzer.payment.business.paymenttypes.InstallmentSecuredRatePlan;
import com.unzer.payment.communication.HttpCommunicationException;
import com.unzer.payment.paymenttypes.InvoiceSecured;
import com.unzer.payment.paymenttypes.SepaDirectDebitSecured;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UrlUtilTest extends AbstractPaymentTest {

    @Test
    public void testGetUrlForPaymentTypeInvoiceSecured() throws HttpCommunicationException {
        String minimalRestUrl = "https://api.unzer.com/v1/types";
        String maximumRestUrl = "https://api.unzer.com/v1/types/invoice-secured";

        Unzer unzer = getUnzer();
        UrlUtil urlUtil = new UrlUtil(unzer.getEndPoint());
        InvoiceSecured invoiceSecured = createPaymentTypeInvoiceSecured();

        assertEquals(String.format("%s/%s", minimalRestUrl, invoiceSecured.getId()), urlUtil.getHttpGetUrl(invoiceSecured.getId()));
        assertEquals(String.format("%s/%s", maximumRestUrl, invoiceSecured.getId()), urlUtil.getHttpGetUrl(invoiceSecured, invoiceSecured.getId()));
    }

    @Test
    public void testGetUrlForPaymentTypeInstallmentSecured() throws HttpCommunicationException {
        String minimalRestUrl = "https://api.unzer.com/v1/types";
        String maximumRestUrl = "https://api.unzer.com/v1/types/installment-secured";

        Unzer unzer = getUnzer();
        UrlUtil urlUtil = new UrlUtil(unzer.getEndPoint());
        InstallmentSecuredRatePlan installmentSecuredRatePlan = createPaymentTypeInstallmentSecuredRatePlan();

        assertEquals(String.format("%s/%s", minimalRestUrl, installmentSecuredRatePlan.getId()), urlUtil.getHttpGetUrl(installmentSecuredRatePlan.getId()));
        assertEquals(String.format("%s/%s", maximumRestUrl, installmentSecuredRatePlan.getId()), urlUtil.getHttpGetUrl(installmentSecuredRatePlan, installmentSecuredRatePlan.getId()));
    }

    @Test
    public void testGetUrlForPaymentTypeSepaDirectDebitSecured() throws HttpCommunicationException {
        String minimalRestUrl = "https://api.unzer.com/v1/types";
        String maximumRestUrl = "https://api.unzer.com/v1/types/sepa-direct-debit-secured";

        Unzer unzer = getUnzer();
        UrlUtil urlUtil = new UrlUtil(unzer.getEndPoint());
        SepaDirectDebitSecured sepaDirectDebitSecured = createPaymentTypeSepaDirectDebitSecured("DE89370400440532013000");

        assertEquals(String.format("%s/%s", minimalRestUrl, sepaDirectDebitSecured.getId()), urlUtil.getHttpGetUrl(sepaDirectDebitSecured.getId()));
        assertEquals(String.format("%s/%s", maximumRestUrl, sepaDirectDebitSecured.getId()), urlUtil.getHttpGetUrl(sepaDirectDebitSecured, sepaDirectDebitSecured.getId()));
    }
}
