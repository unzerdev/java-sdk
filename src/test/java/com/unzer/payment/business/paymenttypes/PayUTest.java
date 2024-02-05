package com.unzer.payment.business.paymenttypes;


import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import com.unzer.payment.Charge;
import com.unzer.payment.Unzer;
import com.unzer.payment.communication.HttpClientMock;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Objects;
import java.util.Scanner;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.unzer.payment.util.Types.unsafeUrl;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@WireMockTest(httpPort = 8080)
class PayUTest {
    private static String getResponse(String response) {
        return new Scanner(Objects.requireNonNull(
                PayUTest.class.getResourceAsStream("/api-response/payu/" + response))).useDelimiter("\\A")
                .next();
    }

    @Test
    void charge() {
        Unzer unzer = new Unzer(new HttpClientMock(), "s-private-key");
        stubFor(
                post("/v1/payments/charges/")
                        .willReturn(jsonResponse(getResponse("charge.json"), 200))
        );
        stubFor(
                get("/v1/payments/s-pay-286")
                        .willReturn(jsonResponse(getResponse("fetch-payment.json"), 200))
        );
        stubFor(
                get("/v1/payments/s-pay-286/charges/s-chg-1")
                        .willReturn(jsonResponse(getResponse("fetch-charge.json"), 200))
        );

        Charge charge = unzer.charge((Charge) new Charge()
                .setTypeId("s-pyu-6tg6nwdkrcdk")
                .setReturnUrl(unsafeUrl("https://unzer.com"))
                .setAmount(BigDecimal.TEN)
                .setCurrency(Currency.getInstance("EUR"))
                .setOrderId("ord-Hi686u4Q4Y"));

        assertNotNull(charge.getId());
        assertNotNull(charge.getPayment().getId());
    }
}