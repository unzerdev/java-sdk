package com.unzer.payment.business.paymenttypes;


import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import com.unzer.payment.Charge;
import com.unzer.payment.Unzer;
import com.unzer.payment.communication.HttpClientMock;
import com.unzer.payment.communication.JsonParser;
import com.unzer.payment.models.googlepay.IntermediateSigningKey;
import com.unzer.payment.models.googlepay.SignedKey;
import com.unzer.payment.models.googlepay.SignedMessage;
import com.unzer.payment.paymenttypes.GooglePay;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Currency;
import java.util.Objects;
import java.util.Scanner;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@WireMockTest(httpPort = 8080)
class GooglePayTest {
    private static String getResponse(String response) {
        return new Scanner(Objects.requireNonNull(
                GooglePayTest.class.getResourceAsStream("/api-response/googlepay/" + response))).useDelimiter("\\A")
                .next();
    }

    @Test
    void test_googlepay_json_matches_expected_request_structure() {
        String expectedJsonBody = getResponse("googlepay-example-request.json");

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

        stubFor(
                post("/v1/types/googlepay/")
                        .willReturn(jsonResponse(getResponse("type.json"), 200))
        );

        stubFor(
                get("/v1/types/googlepay/s-gop-q0nucec6itwe")
                        .willReturn(jsonResponse(getResponse("type.json"), 200))
        );

        GooglePay googlepay = unzer.createPaymentType(new GooglePay());

        assertEquals("518834******0003", googlepay.getNumber());
        assertEquals("10/2025", googlepay.getExpiryDate());
        assertEquals("s-gop-q0nucec6itwe", googlepay.getId());
        assertEquals(false, googlepay.getRecurring());
        assertEquals("0:0:0:0:0:0:0:1", googlepay.getGeoLocation().getClientIp());
        assertNull(googlepay.getGeoLocation().getCountryIsoA2());
    }

    @Test
    void test_charge_ok() {
        Unzer unzer = new Unzer(new HttpClientMock(), "s-private-key");

        stubFor(
                post("/v1/types/googlepay/")
                        .willReturn(jsonResponse(getResponse("type.json"), 200))
        );

        stubFor(
                get("/v1/types/googlepay/s-gop-q0nucec6itwe")
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


        GooglePay type = new GooglePay()
                .setSignature("MEYCIQDjT3+Gyf38msn7Jrqdg7LONQ6eOJjLP2iOvw9eOHlDsgIhANz4KQWFUWFZjKsCDErkaNfnTQByHEMVJwXXxoeQh6Xj")
                .setIntermediateSigningKey(new IntermediateSigningKey()
                        .setSignedKey(
                                new SignedKey()
                                        .setKeyValue("MFkwEwYHKoZIzj0CAQYIKoZIzj0DAQcDQgAE+jgMCZadNmANgugCZl0TNnoNRrYkra6CglM6EsjGXMo6bauh1bdRS59SqO8TDGWx36f1irI6GmZQ5aKzirkZfQ==")
                                        .setKeyExpiration("1707521878074")
                        )
                        .setSignatures(Collections.singletonList("MEYCIQCASTOWcSJyMjNe15yLZ8MZmi6d+4NCu77L2/my5zji6AIhANgYOMqE4kUHM45vZGuj9dsyZxwsRkCRmXfb1U9ZQqcs"))
                )
                .setProtocolVersion("ECv2")
                .setSignedMessage(
                        new SignedMessage()
                                .setEncryptedMessage("U6iWYaAIS+zdMTUYgWii1P0Zf4itOTs8JS/MVKWgiynHggsDIEgVcIR016g93ReVn3NlWLsErrWKAbH/MMN1OAa1ymMzoBDG29/Z8LO1NwUjJOf3TzAr7G0YJZJbgA2XuwttZqAwZZX0esEzXmYJcWVrW4R4dZFZ1Z05UeRnJTjqjNoeLUEDF21QlTa8/MgPCX6LsmPch+Q/2DDg3GvCxxSotEEeuJYcwo9TjXJFNX8fEIWg+EUu7pRWpkddBeNOwxkMIh4oC/ABwc1VhgV0M/iMn+GGIK5aQptn2TbsGxaGC+6oh789Ake69V2g9yvhBACoITmNVF1pDpifbGPnJjeT7IUexi0BME9pdlMBjUSqBmbWua8qZO9aD9vbpAz3wMW7sYqY5eW9/W6meJULsjM51IjLzhQM6yoOCv6plVHR6LB1Fcd4/ee7PLwWC1P+yb9V83FnlYSCp2mcdzf/6oL3KucjMQV07rDXG+yWpOoI6DJHnxmpj7/Lc2fxtm4oiyyPe+RbnHbXTvnWhx/WxgRIp8pUX3hrL5lWBvW20RxUgHLIVm3knbbz5bZL4fFP5U94qR5VSA5p946PtOF+qWDG78Id3LV5m3+vz5JNdw==")
                                .setEphemeralPublicKey("BNtwnjzJINb9UN5kSkCMNy65bvGq3jqm7vkLRFQAhmHROuMGBOig4Fl4yhSpBkGSIzzb/aKVxGoc0gxg1sNhddI=")
                                .setTag("gp7zcAvOzT9Ml93f5vBgFsiOPNe8lGflV1jt75c5apk=")
                );

        Charge charge = unzer.charge(BigDecimal.TEN, Currency.getInstance("EUR"), type);
    }
}