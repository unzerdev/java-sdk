package com.unzer.payment.business.paymenttypes;


import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import com.unzer.payment.Charge;
import com.unzer.payment.Unzer;
import com.unzer.payment.communication.HttpClientMock;
import com.unzer.payment.communication.JsonParser;
import com.unzer.payment.paymenttypes.Twint;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Objects;
import java.util.Scanner;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@WireMockTest(httpPort = 8080)
class TwintTest {
    private static String getResponse(String response) {
        return new Scanner(Objects.requireNonNull(
                TwintTest.class.getResourceAsStream("/api-response/twint/" + response))).useDelimiter("\\A")
                .next();
    }

    @Test
    void test_twint_json_matches_expected_request_structure() {
        String expectedJsonBody = "{}";

        Twint twint = new Twint();
        String twintJson = new JsonParser().toJson(twint);
        assertEquals(expectedJsonBody, twintJson);
    }

    @Test
    void test_type_creation_and_verify_response() {
        Unzer unzer = new Unzer(new HttpClientMock(), "s-private-key");

        stubFor(
                post("/v1/types/twint/")
                        .willReturn(jsonResponse(getResponse("type.json"), 200))
        );

        stubFor(
                get("/v1/types/twint/s-twt-q0nucec6itwe")
                        .willReturn(jsonResponse(getResponse("type.json"), 200))
        );

        Twint twint = unzer.createPaymentType(new Twint());

        assertEquals("s-twt-q0nucec6itwe", twint.getId());
        assertEquals(false, twint.getRecurring());
        assertEquals("0:0:0:0:0:0:0:1", twint.getGeoLocation().getClientIp());
        assertEquals("DE", twint.getGeoLocation().getCountryIsoA2());
    }

    @Test
    void test_charge_ok() {
        Unzer unzer = new Unzer(new HttpClientMock(), "s-private-key");

        stubFor(
                post("/v1/types/twint/")
                        .willReturn(jsonResponse(getResponse("type.json"), 200))
        );

        stubFor(
                get("/v1/types/twint/s-twt-q0nucec6itwe")
                        .willReturn(jsonResponse(getResponse("type.json"), 200))
        );

        stubFor(
                post("/v1/payments/charges/")
                        .willReturn(jsonResponse(getResponse("charge.json"), 200))
        );

        stubFor(
                get("/v1/payments/s-pay-290")
                        .willReturn(jsonResponse(getResponse("payment-charge.json"), 200))
        );
        stubFor(
                get("/v1/payments/s-pay-290/charges/s-chg-1")
                        .willReturn(jsonResponse(getResponse("charge.json"), 200))
        );


        Twint type = new Twint();
        Charge charge = unzer.charge(BigDecimal.TEN, Currency.getInstance("EUR"), type);
    }
}