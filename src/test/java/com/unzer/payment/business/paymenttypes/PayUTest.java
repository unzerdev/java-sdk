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


import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.jsonResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.unzer.payment.util.Url.unsafeUrl;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import com.unzer.payment.Charge;
import com.unzer.payment.Unzer;
import com.unzer.payment.communication.HttpClientMock;
import java.math.BigDecimal;
import java.util.Currency;
import java.util.Objects;
import java.util.Scanner;
import org.junit.jupiter.api.Test;

@WireMockTest(httpPort = 8080)
class PayUTest {
  @Test
  void charge() {
    Unzer unzer = new Unzer(new HttpClientMock(), "s-private-key");
    stubFor(post("/v1/payments/charges").willReturn(
        jsonResponse(getResponse("charge.json"), 200)));
    stubFor(get("/v1/payments/s-pay-286").willReturn(
        jsonResponse(getResponse("fetch-payment.json"), 200)));
    stubFor(get("/v1/payments/s-pay-286/charges/s-chg-1").willReturn(
        jsonResponse(getResponse("fetch-charge.json"), 200)));

    Charge charge = unzer.charge((Charge) new Charge()
        .setTypeId("s-pyu-6tg6nwdkrcdk")
        .setReturnUrl(unsafeUrl("https://unzer.com"))
        .setAmount(BigDecimal.TEN)
        .setCurrency(Currency.getInstance("EUR"))
        .setOrderId("ord-Hi686u4Q4Y"));

    assertNotNull(charge.getId());
    assertNotNull(charge.getPayment().getId());
  }

  private static String getResponse(String response) {
    return new Scanner(Objects.requireNonNull(
        PayUTest.class.getResourceAsStream("/api-response/payu/" + response))).useDelimiter("\\A")
        .next();
  }
}