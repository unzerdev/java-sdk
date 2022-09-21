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
package com.unzer.payment.business;


import com.unzer.payment.Authorization;
import com.unzer.payment.Cancel;
import com.unzer.payment.Charge;
import com.unzer.payment.communication.HttpCommunicationException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Currency;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class CancelTest extends AbstractPaymentTest {

    @Test
    public void testFetchCancelAuthorizationWithUnzer() throws MalformedURLException, HttpCommunicationException {
        Authorization authorize = getUnzer().authorize(getAuthorization(createPaymentTypeCard(getUnzer(), "4711100000000000").getId(), false));
        Cancel cancelInit = authorize.cancel();
        Cancel cancel = getUnzer().fetchCancel(authorize.getPaymentId(), cancelInit.getId());
        assertNotNull(cancel);
        assertNotNull(cancel.getId());
        assertEquals("COR.000.100.112", cancel.getMessage().getCode());
        assertNotNull(cancel.getMessage().getCustomer());
        assertCancelEquals(cancelInit, cancel);
    }

    @Test
    public void testFetchCancelAuthorizationWithPayment() throws MalformedURLException, HttpCommunicationException {
        Authorization authorize = getUnzer().authorize(getAuthorization(createPaymentTypeCard(getUnzer(), "4711100000000000").getId(), false));
        Cancel cancelInit = authorize.cancel();
        assertEquals("COR.000.100.112", cancelInit.getMessage().getCode());
        assertNotNull(cancelInit.getMessage().getCustomer());
        List<Cancel> cancelList = cancelInit.getPayment().getCancelList();
        assertNotNull(cancelList);
        assertEquals(1, cancelList.size());
        assertCancelEquals(cancelInit, cancelList.get(0));
    }

    @Test
    public void testFetchCancelChargeWithUnzer() throws MalformedURLException, HttpCommunicationException {
        Charge initCharge = getUnzer().charge(BigDecimal.ONE, Currency.getInstance("EUR"), createPaymentTypeCard(getUnzer(), "4711100000000000").getId(), new URL("https://www.unzer.com"), false);
        Cancel cancelInit = initCharge.cancel();
        assertEquals("COR.000.100.112", cancelInit.getMessage().getCode());
        assertNotNull(cancelInit.getMessage().getCustomer());
        Cancel cancel = getUnzer().fetchCancel(initCharge.getPaymentId(), initCharge.getId(), cancelInit.getId());
        assertNotNull(cancel);
        assertNotNull(cancel.getId());
        assertCancelEquals(cancelInit, cancel);
    }

    @Test
    public void testCancelChargeWithPayment() throws MalformedURLException, HttpCommunicationException {
        Charge initCharge = getUnzer()
                .charge(BigDecimal.ONE, Currency.getInstance("EUR"), createPaymentTypeCard(getUnzer(), "4711100000000000").getId(),
                        new URL("https://www.unzer.com"), false);
        Cancel cancelInit = initCharge.cancel();
        assertEquals("COR.000.100.112", cancelInit.getMessage().getCode());
        assertNotNull(cancelInit.getMessage().getCustomer());
        Cancel cancel = cancelInit.getPayment().getCharge(initCharge.getId())
                .getCancel(cancelInit.getId());
        assertNotNull(cancel);
        assertNotNull(cancel.getId());
        assertCancelEquals(cancelInit, cancel);
    }

    @Test
    public void testCancelChargeWithPaymentReference() throws MalformedURLException, HttpCommunicationException {
        Charge initCharge = getUnzer()
                .charge(BigDecimal.ONE, Currency.getInstance("EUR"), createPaymentTypeCard(getUnzer(), "4711100000000000").getId(),
                        new URL("https://www.unzer.com"), false);
        Cancel cancelReq = new Cancel();
        cancelReq.setPaymentReference("pmt-ref");
        cancelReq.setAmount(new BigDecimal(1.0));
        Cancel cancelInit = getUnzer().cancelCharge(initCharge.getPaymentId(), initCharge.getId(), cancelReq);
        assertEquals("COR.000.100.112", cancelInit.getMessage().getCode());
        assertNotNull(cancelInit.getMessage().getCustomer());
        assertEquals("pmt-ref", cancelInit.getPaymentReference());
        assertEquals(new BigDecimal(1.0000).setScale(4), cancelInit.getAmount());
        Cancel cancel = cancelInit.getPayment().getCharge(initCharge.getId())
                .getCancel(cancelInit.getId());
        assertNotNull(cancel);
        assertNotNull(cancel.getId());
        assertCancelEquals(cancelInit, cancel);
        assertEquals("pmt-ref", cancel.getPaymentReference());
        assertEquals(new BigDecimal(1.0000).setScale(4), cancel.getAmount());
    }

}
