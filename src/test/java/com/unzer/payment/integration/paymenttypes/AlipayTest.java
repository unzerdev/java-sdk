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
import com.unzer.payment.paymenttypes.Alipay;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Currency;

import static org.junit.jupiter.api.Assertions.assertNotNull;


public class AlipayTest extends AbstractPaymentTest {

    @Test
    public void testCreateAlipayManatoryType() throws HttpCommunicationException {
        Alipay alipay = new Alipay();
        alipay = getUnzer().createPaymentType(alipay);
        assertNotNull(alipay.getId());
    }

    @Test
    public void testChargeAlipayType() throws HttpCommunicationException, MalformedURLException {
        Alipay alipay = getUnzer().createPaymentType(getAlipay());
        Charge charge = alipay.charge(BigDecimal.ONE, Currency.getInstance("EUR"), new URL("https://www.unzer.com"));
        assertNotNull(charge);
        assertNotNull(charge.getId());
        assertNotNull(charge.getRedirectUrl());
    }

    @Test
    public void testFetchAlipayType() throws HttpCommunicationException {
        Alipay alipay = getUnzer().createPaymentType(getAlipay());
        assertNotNull(alipay.getId());
        Alipay fetchedAlipay = (Alipay) getUnzer().fetchPaymentType(alipay.getId());
        assertNotNull(fetchedAlipay.getId());
    }


    private Alipay getAlipay() {
        Alipay alipay = new Alipay();
        return alipay;
    }


}
