package com.unzer.payment.business.paymenttypes;

/*-
 * #%L
 * Unzer Java SDK
 * %%
 * Copyright (C) 2020 Unzer E-Com GmbH
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

import com.unzer.payment.Authorization;
import com.unzer.payment.Charge;
import com.unzer.payment.business.AbstractPaymentTest;
import com.unzer.payment.communication.HttpCommunicationException;
import com.unzer.payment.paymenttypes.Applepay;
import com.unzer.payment.paymenttypes.ApplepayHeader;
import com.unzer.payment.paymenttypes.PaymentType;
import org.junit.Test;

import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Currency;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ApplepayTest extends AbstractPaymentTest {

    @Test
    public void testCreateApplepayType() throws HttpCommunicationException {
        Applepay applepay = getApplePay();

        Applepay response = getUnzer().createPaymentType(applepay);
        assertNotNull(response.getId());
        assertNotNull(response.getApplicationExpirationDate());
        assertNotNull(response.getApplicationPrimaryAccountNumber());
        assertEquals("481852******2027", response.getApplicationPrimaryAccountNumber());
        assertEquals("12/2023", response.getApplicationExpirationDate());

    }

    @Test
    public void testAuthorizeApplePayType() throws HttpCommunicationException, MalformedURLException {
        Applepay applepay = getUnzer().createPaymentType(getApplePay());
        Authorization authorization = applepay.authorize(BigDecimal.ONE, Currency.getInstance("EUR"), new URL("https://www.meinShop.de"));
        assertNotNull(authorization);
        assertNotNull(authorization.getId());
        assertEquals(Authorization.Status.SUCCESS, authorization.getStatus());
    }

    @Test
    public void testAuthorizeApplePayTypeId() throws HttpCommunicationException, MalformedURLException {
        Applepay applepay = getUnzer().createPaymentType(getApplePay());
        Authorization authorization = getUnzer().authorize(BigDecimal.ONE, Currency.getInstance("EUR"), applepay.getId(), new URL("https://www.meinShop.de"));
        assertNotNull(authorization);
        assertNotNull(authorization.getId());
        assertEquals(Authorization.Status.SUCCESS, authorization.getStatus());
    }

    @Test
    public void testChargeApplePayType() throws HttpCommunicationException, MalformedURLException {
        Applepay applepay = getUnzer().createPaymentType(getApplePay());
        Charge charge = applepay.charge(BigDecimal.ONE, Currency.getInstance("EUR"), new URL("https://www.meinShop.de"));
        assertNotNull(charge);
        assertNotNull(charge.getId());
        assertEquals(Authorization.Status.SUCCESS, charge.getStatus());
    }

    @Test
    public void testChargeApplePayTypeId() throws HttpCommunicationException, MalformedURLException {
        Applepay applepay = getUnzer().createPaymentType(getApplePay());
        Charge charge = getUnzer().charge(BigDecimal.ONE, Currency.getInstance("EUR"), applepay.getId(), new URL("https://www.meinShop.de"));
        assertNotNull(charge);
        assertNotNull(charge.getId());
        assertEquals(Authorization.Status.SUCCESS, charge.getStatus());
    }

    @Test
    public void testApplePay() {
        Applepay applepay = getApplePay();

        assertThat(applepay, instanceOf(PaymentType.class));
        assertThat(applepay.getHeader(), instanceOf(ApplepayHeader.class));
        assertThat(applepay.getCurrencyCode(), instanceOf(String.class));
        assertThat(applepay.getApplicationPrimaryAccountNumber(), instanceOf(String.class));
        assertThat(applepay.getData(), instanceOf(String.class));
        assertThat(applepay.getSignature(), instanceOf(String.class));
        assertThat(applepay.getTransactionAmount(), instanceOf(BigDecimal.class));
        assertThat(applepay.getApplicationExpirationDate(), instanceOf(String.class));
        assertThat(applepay.getTypeUrl(), instanceOf(String.class));
        assertThat(applepay.getVersion(), instanceOf(String.class));
    }

    private Applepay getApplePay() {
        Applepay applepay = new Applepay(
                "EC_v1",
                "an5v89lUanUNZZc2997RRxWPTvUYETVla9a778jQKuDdOq7VyMWfik5Y/ZM4irBBW+cOPSRquDaZpVgEDKou62u12y823k7omYlv4seg0RVBrVXCxKKlYTGS+9Aj0aUloCzX0teznQacdi7iRsFgewhquRhqQxQC/DR7Brf3mQmOWFDwh/6NOVmnHlYJnybHOsActKAxENPEHeLB0uAAvkAYMPoQOn3Ao7SAw1VRKSDwJc007kd80hDAcnInpiW741ZGepL65GL1NTUL2SgZHxbP3q6LlvoaoWuHfnjF0XOz6/unehckR1DJMwG3oXjKAgxR1kdqdGqIe1t1pYlcngZszGFvax8kmqv2lp7LAbyxRlwLFpR7FYz9dc2sInPLLFyVhVrvAoVpjNWhBrclbXER7VP+f1V9t3/xFANCXy2IHilY4ZUHfm7cNvHz7NOT978VZn12xwLKzSiq",
                "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"
        );
        applepay.setApplicationExpirationDate("");
        applepay.setApplicationPrimaryAccountNumber("");
        applepay.setTransactionAmount(BigDecimal.ONE);
        applepay.setCurrencyCode("EUR");

        ApplepayHeader header = new ApplepayHeader();
        header.setEphemeralPublicKey("MFkwEwYHKoZIzj0CAQYIKoZIzj0DAQcDQgAEWegdcpR2PSrxm3tDG90lbiCxdC4DxqN20eFVgMZ15xOvbuSYR3DJCtgrjxH6T7zddKVo59U3DSYqoC3aQP90iw==");
        header.setPublicKeyHash("zqO5Y3ldWWm4NnIkfGCvJILw30rp3y46Jsf21gE8CNg=");
        header.setTransactionId("13743d8c88a56a8daa5eb3b517fb578c52e577174e02c417a2c22c474ee6005e");
        applepay.setHeader(header);
        return applepay;
    }

}
