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
package com.unzer.payment.business.paymenttypes;


import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import com.unzer.payment.Authorization;
import com.unzer.payment.Unzer;
import com.unzer.payment.enums.RecurrenceType;
import com.unzer.payment.models.AdditionalTransactionData;
import com.unzer.payment.models.CardTransactionData;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Objects;
import java.util.Scanner;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.unzer.payment.util.Url.unsafeUrl;
import static org.junit.jupiter.api.Assertions.assertEquals;

@WireMockTest(httpPort = 8080)
class CardTest {
    private static String getResponse(String response) {
        return new Scanner(Objects.requireNonNull(CardTest.class.getResourceAsStream("/api-response/" + response))).useDelimiter("\\A").next();
    }

    @Test
    void test_card_liability() {
        Unzer unzer = new Unzer(new HttpClientTestImpl(), "s-private-key");
        stubFor(post("/v1/payments/authorize").willReturn(jsonResponse(getResponse("card-authorize.json"), 200)));
        stubFor(get("/v1/payments/s-pay-286").willReturn(jsonResponse(getResponse("card-fetch-payment.json"), 200)));
        stubFor(get("/v1/payments/s-pay-286/authorize/s-aut-1").willReturn(jsonResponse(getResponse("card-fetch-authorization.json"), 200)));

        Authorization authorization = unzer.authorize((Authorization) new Authorization()
                .setTypeId("s-crd-6tg6nwdkrcdk")
                .setReturnUrl(unsafeUrl("https://unzer.com"))
                .setAmount(BigDecimal.TEN)
                .setCurrency(Currency.getInstance("EUR"))
                .setOrderId("ord-Hi686u4Q4Y")
                .setAdditionalTransactionData(
                        new AdditionalTransactionData()
                                .setCard(
                                        new CardTransactionData()
                                                .setRecurrenceType(RecurrenceType.UNSCHEDULED)
                                )
                ));
        assertEquals(CardTransactionData.Liability.MERCHANT, authorization.getAdditionalTransactionData().getCard().getLiability());
    }

    @Test
    void test_card_exemption() {
        Unzer unzer = new Unzer(new HttpClientTestImpl(), "s-private-key");
        stubFor(post("/v1/payments/authorize").willReturn(jsonResponse(getResponse("card-authorize.json"), 200)));
        stubFor(get("/v1/payments/s-pay-286").willReturn(jsonResponse(getResponse("card-fetch-payment.json"), 200)));
        stubFor(get("/v1/payments/s-pay-286/authorize/s-aut-1").willReturn(jsonResponse(getResponse("card-fetch-authorization.json"), 200)));

        Authorization authorization = unzer.authorize((Authorization) new Authorization()
                .setTypeId("s-crd-6tg6nwdkrcdk")
                .setReturnUrl(unsafeUrl("https://unzer.com"))
                .setAmount(BigDecimal.TEN)
                .setCurrency(Currency.getInstance("EUR"))
                .setOrderId("ord-Hi686u4Q4Y")
                .setAdditionalTransactionData(
                        new AdditionalTransactionData()
                                .setCard(
                                        new CardTransactionData()
                                                .setRecurrenceType(RecurrenceType.UNSCHEDULED)
                                                .setExemptionType(CardTransactionData.ExemptionType.LVP)
                                )
                ));
        assertEquals(CardTransactionData.ExemptionType.LVP, authorization.getAdditionalTransactionData().getCard().getExemptionType());
    }
}