package com.unzer.payment.business.payment;

import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import com.unzer.payment.Authorization;
import com.unzer.payment.Charge;
import com.unzer.payment.Sca;
import com.unzer.payment.Unzer;
import com.unzer.payment.communication.HttpClientMock;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Currency;
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
class ScaTest {
    private static String getResponse(String response) {
        try (InputStream is = Objects.requireNonNull(
                ScaTest.class.getResourceAsStream("/api-response/sca/" + response));
             Scanner scanner = new Scanner(is, StandardCharsets.UTF_8.name())) {
            return scanner.useDelimiter("\\A").next();
        } catch (IOException e) {
            throw new RuntimeException("Failed to read test response file: " + response, e);
        }
    }

    @Test
    void sca_created_successfully() throws MalformedURLException {
        stubFor(post("/v1/payments/sca").willReturn(
                jsonResponse(getResponse("create-sca.json"), 200)));
        stubFor(get("/v1/payments/s-pay-123").willReturn(
                jsonResponse(getResponse("fetch-payment.json"), 200)));
        stubFor(get("/v1/payments/s-pay-123/sca/s-sca-1").willReturn(
                jsonResponse(getResponse("fetch-sca.json"), 200)));

        Unzer unzer = new Unzer(new HttpClientMock(), "s-priv-key");

        Sca sca = new Sca();
        sca.setAmount(BigDecimal.valueOf(100.00));
        sca.setCurrency(Currency.getInstance("EUR"));
        sca.setTypeId("s-crd-123");
        sca.setReturnUrl(new URL("https://unzer.com"));

        Sca result = unzer.sca(sca);

        assertNotNull(result);
        assertNotNull(result.getId());
        assertNotNull(result.getPaymentId());
        assertEquals("s-sca-1", result.getId());
        assertEquals("s-pay-123", result.getPaymentId());
    }

    @Test
    void sca_fetched_successfully() {
        stubFor(get("/v1/payments/s-pay-123/sca/s-sca-1").willReturn(
                jsonResponse(getResponse("fetch-sca.json"), 200)));
        stubFor(get("/v1/payments/s-pay-123").willReturn(
                jsonResponse(getResponse("fetch-payment.json"), 200)));

        Unzer unzer = new Unzer(new HttpClientMock(), "s-private-key");
        Sca sca = unzer.fetchSca("s-pay-123", "s-sca-1");

        assertNotNull(sca);
        assertEquals("s-sca-1", sca.getId());
        assertEquals("s-pay-123", sca.getPaymentId());
    }

    @Test
    void authorizeSca_returns_authorization() {
        stubFor(post("/v1/payments/s-pay-123/sca/authorize").willReturn(
                jsonResponse(getResponse("authorize-with-sca.json"), 200)));
        stubFor(get("/v1/payments/s-pay-123").willReturn(
                jsonResponse(getResponse("fetch-payment.json"), 200)));
        stubFor(get("/v1/payments/s-pay-123/sca/s-sca-1").willReturn(
                jsonResponse(getResponse("fetch-sca.json"), 200)));

        Unzer unzer = new Unzer(new HttpClientMock(), "s-private-key");

        Authorization authorization = new Authorization();
        authorization.setAmount(BigDecimal.valueOf(100.00));

        Authorization auth = unzer.authorizeSca("s-pay-123", authorization);

        assertNotNull(auth);
        assertInstanceOf(Authorization.class, auth);
        assertEquals("s-aut-1", auth.getId());
    }

    @Test
    void chargeSca_returns_charge() {
        stubFor(post("/v1/payments/s-pay-123/sca/charges").willReturn(
                jsonResponse(getResponse("charge-with-sca.json"), 200)));
        stubFor(get("/v1/payments/s-pay-123").willReturn(
                jsonResponse(getResponse("fetch-payment.json"), 200)));
        stubFor(get("/v1/payments/s-pay-123/sca/s-sca-1").willReturn(
                jsonResponse(getResponse("fetch-sca.json"), 200)));

        Unzer unzer = new Unzer(new HttpClientMock(), "s-private-key");

        Charge charge = new Charge();
        charge.setAmount(BigDecimal.valueOf(100.00));

        Charge result = unzer.chargeSca("s-pay-123", charge);

        assertNotNull(result);
        assertInstanceOf(Charge.class, result);
        assertEquals("s-chg-1", result.getId());
    }
}
