package com.unzer.payment.business.paymenttypes;


import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import com.unzer.payment.Authorization;
import com.unzer.payment.Charge;
import com.unzer.payment.Unzer;
import com.unzer.payment.communication.HttpClientMock;
import com.unzer.payment.enums.RecurrenceType;
import com.unzer.payment.models.AdditionalTransactionData;
import com.unzer.payment.models.CardTransactionData;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Objects;
import java.util.Scanner;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.unzer.payment.util.Types.unsafeUrl;
import static org.junit.jupiter.api.Assertions.assertEquals;

@WireMockTest(httpPort = 8080)
class PaypalExpressTest {
    private static String getResponse(String response) {
        return new Scanner(Objects.requireNonNull(
                PaypalExpressTest.class.getResourceAsStream("/api-response/paypal-express/" + response))).useDelimiter("\\A")
                .next();
    }

    @Test
    void update_charge_ok() {
        Unzer unzer = new Unzer(new HttpClientMock(), "s-private-key");

        stubFor(get("/v1/payments/s-pay-286").willReturn(
                jsonResponse(getResponse("fetch-payment-charge.json"), 200)));
        stubFor(get("/v1/payments/s-pay-286/charges/s-chg-1").willReturn(
                jsonResponse(getResponse("fetch-charge.json"), 200)));

        stubFor(patch("/v1/payments/s-pay-286/charges/").willReturn(
                jsonResponse(getResponse("patch-charge.json"), 200)));

        Charge charge = unzer.fetchCharge("s-pay-286", "s-chg-1");
        unzer.updateCharge(charge);
    }

    @Test
    void update_authorization_ok() {
        Unzer unzer = new Unzer(new HttpClientMock(), "s-private-key");

        stubFor(get("/v1/payments/s-pay-286").willReturn(
                jsonResponse(getResponse("fetch-payment-authorize.json"), 200)));
        stubFor(get("/v1/payments/s-pay-286/authorize/s-aut-1").willReturn(
                jsonResponse(getResponse("fetch-authorization.json"), 200)));

        stubFor(patch("/v1/payments/s-pay-286/authorize/").willReturn(
                jsonResponse(getResponse("patch-authorization.json"), 200)));

        Authorization authorization = unzer.fetchAuthorization("s-pay-286");
        unzer.updateAuthorization(authorization);
    }
}