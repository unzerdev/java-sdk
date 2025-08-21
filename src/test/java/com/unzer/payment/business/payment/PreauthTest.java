package com.unzer.payment.business.payment;

import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import com.unzer.payment.Authorization;
import com.unzer.payment.Cancel;
import com.unzer.payment.Preauthorization;
import com.unzer.payment.Unzer;
import com.unzer.payment.communication.HttpClientMock;
import org.junit.jupiter.api.Test;

import java.util.Objects;
import java.util.Scanner;

import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.jsonResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@WireMockTest(httpPort = 8080)
class PreauthTest {
    private static String getResponse(String response) {
        return new Scanner(
                Objects.requireNonNull(
                        PreauthTest.class.getResourceAsStream("/api-response/preauth/" + response)))
                .useDelimiter(
                        "\\A")
                .next();
    }

    @Test
    void preauthorize_fetched_successfully() {
        stubFor(
                post("/v1/payments/preauthorize/").willReturn(
                        jsonResponse(getResponse("fetch-preauthorize.json"), 200))
        );
        stubFor(
                get("/v1/payments/s-pay-123").willReturn(
                        jsonResponse(getResponse("fetch-payment.json"), 200))
        );
        stubFor(
                get("/v1/payments/s-pay-123/preauthorize/s-preaut-1").willReturn(
                        jsonResponse(getResponse("fetch-preauthorize.json"), 200))
        );

        Unzer unzer = new Unzer(new HttpClientMock(), "s-private-key");


        Authorization preauthorization = unzer.preauthorize(new Preauthorization());
        assertNotNull(preauthorization);
        assertInstanceOf(Preauthorization.class, preauthorization);

        // Compare Fetched Preauthorization
        Authorization fetchedPreauth = unzer.fetchAuthorization(preauthorization.getPaymentId());
        assertInstanceOf(Preauthorization.class, fetchedPreauth);

        assertEquals(preauthorization.getId(), fetchedPreauth.getId());
        assertEquals(preauthorization.getAmount(), fetchedPreauth.getAmount());
        assertEquals(preauthorization.getUrl(), fetchedPreauth.getUrl());
        assertEquals(preauthorization.getProcessing().getShortId(), fetchedPreauth.getProcessing().getShortId());
        assertEquals(preauthorization.getProcessing().getUniqueId(), fetchedPreauth.getProcessing().getUniqueId());
        assertEquals(preauthorization.getStatus(), fetchedPreauth.getStatus());
    }

    @Test
    void cancel_preauth_transaction() {
        stubFor(
                post("/v1/payments/s-pay-123/preauthorize/cancels").willReturn(
                        jsonResponse(getResponse("cancel-preauthorize.json"), 200))
        );
        stubFor(
                get("/v1/payments/s-pay-123").willReturn(
                        jsonResponse(getResponse("fetch-canceled-payment.json"), 200))
        );
        stubFor(
                get("/v1/payments/s-pay-123/preauthorize/s-preaut-1").willReturn(
                        jsonResponse(getResponse("fetch-preauthorize.json"), 200))
        );
        stubFor(
                get("/v1/payments/s-pay-123/preauthorize/s-preaut-1/cancels/s-cnl-1").willReturn(
                        jsonResponse(getResponse("fetch-preauthorize.json"), 200))
        );

        Unzer unzer = new Unzer(new HttpClientMock(), "s-private-key");
        Cancel cancel = unzer.cancelPreauthorization("s-pay-123");
        assertNotNull(cancel);
        assertNotNull(cancel.getId());
    }
}
