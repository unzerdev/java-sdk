package com.unzer.payment.business.paymenttypes;

import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import com.unzer.payment.Unzer;
import com.unzer.payment.communication.HttpClientMock;
import com.unzer.payment.communication.JsonParser;
import com.unzer.payment.paymenttypes.ClickToPay;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Objects;
import java.util.Scanner;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.client.WireMock.jsonResponse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@WireMockTest(httpPort = 8080)
public class ClickToPayTest {

    private static String getResponse(String response) {
        return new Scanner(Objects.requireNonNull(
                GooglePayTest.class.getResourceAsStream("/api-response/clicktopay/" + response))).useDelimiter("\\A")
                .next();
    }

    @Test
    void test_clicktopay_json_matches_expected_request_structure() {
        String expectedJsonBody = getResponse("clicktopay-example-request.json");

        ClickToPay clickToPay = new ClickToPay()
                .setBrand("mastercard")
                .setMcMerchantTransactionId("0a4e0d3.34f4a04b.894125b16ddd1f1b3a58273d63a0894179ac3535")
                .setMcCxFlowId("34f4a04b.5ab95e32-30f7-483f-846f-a08230a6d2ed.1618397078")
                .setMcCorrelationId("corr12345");


        String json = new JsonParser().toJson(clickToPay);
        assertEquals(expectedJsonBody, json);
    }

    @Test
    void test_type_creation_and_verify_response() {
        Unzer unzer = new Unzer(new HttpClientMock(), "s-private-key");

        stubFor(
                post("/v1/types/clicktopay/")
                        .willReturn(jsonResponse(getResponse("type.json"), 200))
        );

        stubFor(
                get("/v1/types/clicktopay/s-ctp-q0nucec6itwe")
                        .willReturn(jsonResponse(getResponse("type.json"), 200))
        );

        ClickToPay clickToPay = unzer.createPaymentType(new ClickToPay());

        assertEquals("s-ctp-q0nucec6itwe", clickToPay.getId());
        assertEquals(false, clickToPay.getRecurring());
        assertEquals("0:0:0:0:0:0:0:1", clickToPay.getGeoLocation().getClientIp());
        assertNull(clickToPay.getGeoLocation().getCountryIsoA2());
    }
}
