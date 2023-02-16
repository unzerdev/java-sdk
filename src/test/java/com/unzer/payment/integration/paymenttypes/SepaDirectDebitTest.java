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


import com.unzer.payment.Cancel;
import com.unzer.payment.Charge;
import com.unzer.payment.business.AbstractPaymentTest;
import com.unzer.payment.communication.HttpCommunicationException;
import com.unzer.payment.paymenttypes.SepaDirectDebit;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Currency;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class SepaDirectDebitTest extends AbstractPaymentTest {

    @Test
    public void testCreateSepaDirectDebitManatoryType() throws HttpCommunicationException {
        SepaDirectDebit sdd = new SepaDirectDebit("DE89370400440532013000");
        sdd = getUnzer().createPaymentType(sdd);
        assertNotNull(sdd.getId());
    }

    @Test
    public void testCreateSepaDirectDebitFullType() throws HttpCommunicationException {
        SepaDirectDebit sddOriginal = getSepaDirectDebit();
        SepaDirectDebit sddCreated = getUnzer().createPaymentType(sddOriginal);
        assertSddEquals(sddOriginal, sddCreated);
    }

    @Test
    public void testChargeSepaDirectDebitType() throws HttpCommunicationException, MalformedURLException {
        SepaDirectDebit sdd = createPaymentTypeSepaDirectDebit();
        Charge charge = sdd.charge(BigDecimal.ONE, Currency.getInstance("EUR"), new URL("https://www.unzer.com"));
        assertNotNull(charge);
        assertNotNull(charge.getId());
        assertNotNull(charge.getProcessing());
        assertNotNull(charge.getProcessing().getCreatorId());
        assertNotNull(charge.getProcessing().getIdentification());
        assertNotNull(charge.getProcessing().getTraceId());
    }

    @Test
    public void testFetchSepaDirectDebitType() throws HttpCommunicationException {
        SepaDirectDebit sdd = createPaymentTypeSepaDirectDebit();
        assertNotNull(sdd.getId());
        SepaDirectDebit fetchedSdd = (SepaDirectDebit) getUnzer().fetchPaymentType(sdd.getId());
        assertNotNull(fetchedSdd.getId());
        assertSddEquals(sdd, fetchedSdd);
    }

    @Test
    public void testCancelSepaDirectDebitType() throws HttpCommunicationException, MalformedURLException {
        SepaDirectDebit sdd = createPaymentTypeSepaDirectDebit();
        Charge charge = sdd.charge(BigDecimal.ONE, Currency.getInstance("EUR"), new URL("https://www.unzer.com"));
        assertNotNull(charge);
        assertNotNull(charge.getId());

        Cancel cancel = charge.cancel();
        assertNotNull(cancel);
        assertNotNull(cancel.getId());
    }

    private void assertSddEquals(SepaDirectDebit sddOriginal, SepaDirectDebit sddCreated) {
        assertNotNull(sddCreated.getId());
        assertEquals(sddOriginal.getBic(), sddCreated.getBic());
        assertEquals(sddOriginal.getHolder(), sddCreated.getHolder());
        assertEquals(sddOriginal.getIban(), sddCreated.getIban());
    }

}
