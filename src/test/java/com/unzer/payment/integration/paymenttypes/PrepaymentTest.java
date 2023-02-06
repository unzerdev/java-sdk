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
import com.unzer.payment.business.AbstractPaymentTest;
import com.unzer.payment.communication.HttpCommunicationException;
import com.unzer.payment.paymenttypes.Prepayment;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Currency;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class PrepaymentTest extends AbstractPaymentTest {

    @Test
    public void testCreatePrepaymentManatoryType() throws HttpCommunicationException {
        Prepayment prepayment = new Prepayment();
        prepayment = getUnzer().createPaymentType(prepayment);
        assertNotNull(prepayment.getId());
    }

    @Test
    public void testChargePrepaymentType() throws HttpCommunicationException, MalformedURLException {
        Prepayment prepayment = getUnzer().createPaymentType(getPrepayment());
        Charge charge = prepayment.charge(BigDecimal.ONE, Currency.getInstance("EUR"), new URL("https://www.meinShop.de"));
        assertNotNull(charge);
        assertNotNull(charge.getId());
        assertNotNull(charge.getProcessing());
        assertNotNull(charge.getProcessing().getIban());
        assertNotNull(charge.getProcessing().getBic());
        assertNotNull(charge.getProcessing().getDescriptor());
        assertNotNull(charge.getProcessing().getHolder());
    }

    @Test
    public void testFetchPrepaymentType() throws HttpCommunicationException {
        Prepayment prepayment = getUnzer().createPaymentType(getPrepayment());
        assertNotNull(prepayment.getId());
        Prepayment fetchedPrepayment = (Prepayment) getUnzer().fetchPaymentType(prepayment.getId());
        assertNotNull(fetchedPrepayment.getId());
    }


    private Prepayment getPrepayment() {
        return new Prepayment();
    }


}
