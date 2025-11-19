package com.unzer.payment.business.paymenttypes;

import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import com.unzer.payment.Charge;
import com.unzer.payment.Unzer;
import com.unzer.payment.communication.HttpClientMock;
import com.unzer.payment.communication.JsonParser;
import com.unzer.payment.paymenttypes.OpenBanking;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Objects;
import java.util.Scanner;

import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.jsonResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@WireMockTest(httpPort = 8080)
class OpenBankingTest {

    private static String getResponse(String response) {
        return new Scanner(Objects.requireNonNull(
                OpenBankingTest.class.getResourceAsStream("/api-response/open-banking/" + response))).useDelimiter("\\A")
                .next();
    }

    @Test
    void test_openbanking_json_matches_expected_request_structure() {
        String expectedJsonBody = getResponse("open-banking-example-request.json");

        OpenBanking clickToPay = new OpenBanking("DE");


        String json = new JsonParser().toJson(clickToPay);
        assertEquals(expectedJsonBody, json);
    }

    @Test
    void test_type_creation_and_verify_response() {
        Unzer unzer = new Unzer(new HttpClientMock(), "s-private-key");

        stubFor(
                post("/v1/types/openbanking-pis/")
                        .willReturn(jsonResponse(getResponse("type.json"), 200))
        );

        stubFor(
                get("/v1/types/openbanking-pis/s-obp-q0nucec6itwe")
                        .willReturn(jsonResponse(getResponse("type.json"), 200))
        );

        OpenBanking openBanking = unzer.createPaymentType(new OpenBanking("DE"));

        assertEquals("s-obp-q0nucec6itwe", openBanking.getId());
        assertEquals(false, openBanking.getRecurring());
        assertEquals("0:0:0:0:0:0:0:1", openBanking.getGeoLocation().getClientIp());
        assertNull(openBanking.getGeoLocation().getCountryIsoA2());
    }

    @Test
    void test_charge_ok() {
        Unzer unzer = new Unzer(new HttpClientMock(), "s-private-key");

        stubFor(
                post("/v1/types/openbanking-pis/")
                        .willReturn(jsonResponse(getResponse("type.json"), 200))
        );

        stubFor(
                get("/v1/types/openbanking-pis/s-obp-q0nucec6itwe")
                        .willReturn(jsonResponse(getResponse("type.json"), 200))
        );

        stubFor(
                post("/v1/payments/charges/")
                        .willReturn(jsonResponse(getResponse("charge.json"), 200))
        );

        stubFor(
                get("/v1/payments/s-pay-300")
                        .willReturn(jsonResponse(getResponse("payment-charge.json"), 200))
        );
        stubFor(
                get("/v1/payments/s-pay-300/charges/s-chg-2")
                        .willReturn(jsonResponse(getResponse("charge.json"), 200))
        );


        OpenBanking type = new OpenBanking("DE");


        Charge charge = unzer.charge(BigDecimal.TEN, Currency.getInstance("EUR"), type);
        assertNotNull(charge.getAdditionalTransactionData());
        assertNotNull(charge.getAdditionalTransactionData().getOnlineTransfer());
        assertEquals("2025-12-12 10:40:41", charge.getAdditionalTransactionData().getOnlineTransfer().getTargetDueDate());
    }
}
