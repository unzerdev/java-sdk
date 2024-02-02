package com.unzer.payment.business.paymenttypes;


import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import com.unzer.payment.Unzer;
import com.unzer.payment.communication.HttpClientMock;
import com.unzer.payment.communication.JsonParser;
import com.unzer.payment.models.googlepay.IntermediateSigningKey;
import com.unzer.payment.models.googlepay.SignedKey;
import com.unzer.payment.models.googlepay.SignedMessage;
import com.unzer.payment.paymenttypes.GooglePay;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;
import java.util.Scanner;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@WireMockTest(httpPort = 8080)
class GooglePayTest {
    private static String getJsonBody(String file) {
        return new Scanner(Objects.requireNonNull(
                GooglePayTest.class.getResourceAsStream(
                        "/api-response/googlepay/" + file))).useDelimiter("\\A")
                .next();
    }

    @Test
    void test_googlepay_json_matches_expected_request_structure() {
        String expectedJsonBody = getJsonBody("googlepay-example-request.json");

        IntermediateSigningKey intermediateSigningKey = new IntermediateSigningKey()
                .setSignedKey(new SignedKey()
                        .setKeyExpiration("1542394027316")
                        .setKeyValue("key-value-xyz\\u003d\\u003d\"}")
                )
                .setSignatures(Collections.singletonList("signature1"));

        GooglePay googlepay = new GooglePay()
                .setSignature("signature-xyz\u003d")
                .setIntermediateSigningKey(intermediateSigningKey)
                .setProtocolVersion("ECv2")
                .setSignedMessage(new SignedMessage()
                        .setTag("001 Cryptogram 3ds")
                        .setEncryptedMessage("encryptedMessage-xyz\"")
                        .setEphemeralPublicKey("ephemeralPublicKey-xyz\\u003d\"")
                );

        String googlepayJson = new JsonParser().toJson(googlepay);
        assertEquals(expectedJsonBody, googlepayJson);
    }

    @Test
    void test_type_creation_and_verify_response() {
        Unzer unzer = new Unzer(new HttpClientMock(), "s-private-key");
        String jsonBody = getJsonBody("create-googlepay-type.json");
        stubFor(
                post("/v1/types/googlepay/").willReturn(
                        jsonResponse(jsonBody, 200)));
        stubFor(
                get("/v1/types/googlepay/s-gop-q0nucec6itwe").willReturn(
                        jsonResponse(jsonBody, 200)));

        GooglePay googlepay = unzer.createPaymentType(new GooglePay());

        assertEquals("518834******0003", googlepay.getNumber());
        assertEquals("10/2025", googlepay.getExpiryDate());
        assertEquals("s-gop-q0nucec6itwe", googlepay.getId());
        assertEquals(false, googlepay.getRecurring());
        assertEquals("0:0:0:0:0:0:0:1", googlepay.getGeoLocation().getClientIp());
        assertNull(googlepay.getGeoLocation().getCountryIsoA2());
    }
}