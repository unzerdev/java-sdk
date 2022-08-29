/*
 * Copyright 2021 Unzer E-Com GmbH
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
package com.unzer.payment.business.paymenttypes;


import com.unzer.payment.Charge;
import com.unzer.payment.business.AbstractPaymentTest;
import com.unzer.payment.communication.HttpCommunicationException;
import com.unzer.payment.paymenttypes.Bancontact;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Currency;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


public class BancontactTest extends AbstractPaymentTest {

    @Test
    public void testCreateBancontactWithoutHolder() throws HttpCommunicationException {
        Bancontact bacncontact = new Bancontact();
        bacncontact = getUnzer().createPaymentType(bacncontact);
        assertNotNull(bacncontact.getId());
    }

    @Test
    public void testCreateBancontactWithHolder() throws HttpCommunicationException {
        Bancontact bancontact = new Bancontact("test holder");
        bancontact = getUnzer().createPaymentType(bancontact);
        assertNotNull(bancontact.getId());
        assertEquals("test holder", bancontact.getHolder());
    }

    @Test
    public void testChargeBancontactType() throws HttpCommunicationException, MalformedURLException {
        Bancontact bancontact = getUnzer().createPaymentType(getBancontact());
        Charge charge = bancontact.charge(BigDecimal.ONE, Currency.getInstance("EUR"), new URL("https://www.unzer.com"));
        assertNotNull(charge);
        assertNotNull(charge.getId());
        assertNotNull(charge.getRedirectUrl());
    }

    @Test
    public void testFetchBancontactType() throws HttpCommunicationException {
        Bancontact bancontact = getUnzer().createPaymentType(getBancontact());
        assertNotNull(bancontact.getId());
        Bancontact fetchedBancontact = (Bancontact) getUnzer().fetchPaymentType(bancontact.getId());
        assertNotNull(fetchedBancontact.getId());
    }

    private Bancontact getBancontact() {
        Bancontact bancontact = new Bancontact();
        return bancontact;
    }
}
