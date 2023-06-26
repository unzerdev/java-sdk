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

package com.unzer.payment.business.payment;

import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.jsonResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.unzer.payment.util.Url.unsafeUrl;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import com.unzer.payment.AbstractTransaction;
import com.unzer.payment.Chargeback;
import com.unzer.payment.Payment;
import com.unzer.payment.Unzer;
import com.unzer.payment.business.paymenttypes.HttpClientTestImpl;
import com.unzer.payment.communication.json.JsonMessage;
import com.unzer.payment.enums.RecurrenceType;
import com.unzer.payment.models.AdditionalTransactionData;
import com.unzer.payment.models.CardTransactionData;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Currency;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import org.junit.jupiter.api.Test;

@WireMockTest(httpPort = 8080)
public class ChargebackTest {
  @Test
  public void chargeback_fetched_successfully() throws ParseException {
    stubFor(
        get("/v1/payments/s-pay-286").willReturn(
            jsonResponse(getResponse("fetch-payment.json"), 200))
    );
    stubFor(
        get("/v1/payments/s-pay-286/authorize/s-aut-1").willReturn(
            jsonResponse(getResponse("fetch-authorize.json"), 200))
    );
    stubFor(
        get("/v1/payments/s-pay-286/chargeback/s-cgb-1").willReturn(
            jsonResponse(getResponse("fetch-chargeback.json"), 200))
    );

    Unzer unzer = new Unzer(new HttpClientTestImpl(), "s-private-key");

    Payment fetchedPayment = unzer.fetchPayment("s-pay-286");
    assertNotNull(fetchedPayment);

    List<Chargeback> actualChargebacks = fetchedPayment.getChargebackList();
    actualChargebacks.sort(Comparator.comparing(AbstractTransaction::getId));

    List<Chargeback> expectedChargeBacks = Arrays.asList(
        (Chargeback) new Chargeback()
            .setId("s-cgb-1")
            .setAmount(BigDecimal.TEN)
            .setCurrency(Currency.getInstance("EUR"))
            .setReturnUrl(unsafeUrl("https://unzer.com"))
            .setTypeId("s-crd-6tg6nwdkrcdk")
            .setPaymentId("s-pay-286")
            .setTraceId("b2f9c67bebafa3cd49b3d50f9ff00639")
            .setCard3ds(Boolean.FALSE)
            .setMessage(
                new JsonMessage()
                    .setCode("COR.000.000.000")
                    .setCustomer("Your payments have been successfully processed.")
                    .setMerchant("Transaction succeeded")
            )
            .setDate(
                new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss").parse("31-Jan-2023 05:44:41"))
            .setStatus(AbstractTransaction.Status.SUCCESS)
            .setAdditionalTransactionData(
                new AdditionalTransactionData()
                    .setCard(
                        new CardTransactionData()
                            .setRecurrenceType(RecurrenceType.UNSCHEDULED)
                            .setLiability(CardTransactionData.Liability.MERCHANT)
                            .setExemptionType(CardTransactionData.ExemptionType.LVP)
                    )
            )
            .setResourceUrl(
                unsafeUrl("https://sbx-api.unzer.com/v1/payments/s-pay-286/chargeback/s-cgb-1"))
    );

    assertIterableEquals(expectedChargeBacks, actualChargebacks);
  }

  private static String getResponse(String response) {
    return new Scanner(
        Objects.requireNonNull(
            ChargebackTest.class.getResourceAsStream("/api-response/payment/" + response)))
        .useDelimiter(
            "\\A")
        .next();
  }
}
