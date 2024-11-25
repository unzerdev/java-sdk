package com.unzer.payment.business.payment;

import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import com.unzer.payment.Authorization;
import com.unzer.payment.Preauthorization;
import com.unzer.payment.Unzer;
import com.unzer.payment.communication.HttpClientMock;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.util.Objects;
import java.util.Scanner;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.*;

@WireMockTest(httpPort = 8080)
public class PreauthTest {
    private static String getResponse(String response) {
        return new Scanner(
                Objects.requireNonNull(
                        PreauthTest.class.getResourceAsStream("/api-response/preauth/" + response)))
                .useDelimiter(
                        "\\A")
                .next();
    }

    @Test
    public void preauthorize_fetched_successfully() throws ParseException {
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
}
