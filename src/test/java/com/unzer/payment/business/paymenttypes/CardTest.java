package com.unzer.payment.business.paymenttypes;


import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import com.unzer.payment.Authorization;
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
import static com.unzer.payment.util.Url.unsafeUrl;
import static org.junit.jupiter.api.Assertions.assertEquals;

@WireMockTest(httpPort = 8080)
class CardTest {
    private static String getResponse(String response) {
        return new Scanner(Objects.requireNonNull(
                CardTest.class.getResourceAsStream("/api-response/card/" + response))).useDelimiter("\\A")
                .next();
    }

    @Test
    void test_card_liability() {
        Unzer unzer = new Unzer(new HttpClientMock(), "s-private-key");
        stubFor(post("/v1/payments/authorize/").willReturn(
                jsonResponse(getResponse("card-authorize.json"), 200)));
        stubFor(get("/v1/payments/s-pay-286").willReturn(
                jsonResponse(getResponse("card-fetch-payment.json"), 200)));
        stubFor(get("/v1/payments/s-pay-286/authorize/s-aut-1").willReturn(
                jsonResponse(getResponse("card-fetch-authorization.json"), 200)));

        Authorization authorization = unzer.authorize((Authorization) new Authorization()
                .setTypeId("s-crd-6tg6nwdkrcdk")
                .setReturnUrl(unsafeUrl("https://unzer.com"))
                .setAmount(BigDecimal.TEN)
                .setCurrency(Currency.getInstance("EUR"))
                .setOrderId("ord-Hi686u4Q4Y")
                .setAdditionalTransactionData(
                        new AdditionalTransactionData()
                                .setCard(
                                        new CardTransactionData()
                                                .setRecurrenceType(RecurrenceType.UNSCHEDULED)
                                )
                ));
        assertEquals(CardTransactionData.Liability.MERCHANT,
                authorization.getAdditionalTransactionData().getCard().getLiability());
    }

    @Test
    void test_card_exemption() {
        Unzer unzer = new Unzer(new HttpClientMock(), "s-private-key");
        stubFor(post("/v1/payments/authorize/").willReturn(
                jsonResponse(getResponse("card-authorize.json"), 200)));
        stubFor(get("/v1/payments/s-pay-286").willReturn(
                jsonResponse(getResponse("card-fetch-payment.json"), 200)));
        stubFor(get("/v1/payments/s-pay-286/authorize/s-aut-1").willReturn(
                jsonResponse(getResponse("card-fetch-authorization.json"), 200)));

        Authorization authorization = unzer.authorize((Authorization) new Authorization()
                .setTypeId("s-crd-6tg6nwdkrcdk")
                .setReturnUrl(unsafeUrl("https://unzer.com"))
                .setAmount(BigDecimal.TEN)
                .setCurrency(Currency.getInstance("EUR"))
                .setOrderId("ord-Hi686u4Q4Y")
                .setAdditionalTransactionData(
                        new AdditionalTransactionData()
                                .setCard(
                                        new CardTransactionData()
                                                .setRecurrenceType(RecurrenceType.UNSCHEDULED)
                                                .setExemptionType(CardTransactionData.ExemptionType.LVP)
                                )
                ));
        assertEquals(CardTransactionData.ExemptionType.LVP,
                authorization.getAdditionalTransactionData().getCard().getExemptionType());
    }

}