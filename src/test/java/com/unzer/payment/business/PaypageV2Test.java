package com.unzer.payment.business;


import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import com.unzer.payment.communication.JsonParser;
import com.unzer.payment.communication.json.JsonMessage;
import com.unzer.payment.models.paypage.PaypagePayment;
import com.unzer.payment.resources.PaypageV2;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Objects;
import java.util.Scanner;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

@WireMockTest(httpPort = 8080)
class PaypageV2Test {
    private static String getResponse(String response) {
        return new Scanner(Objects.requireNonNull(
                PaypageV2Test.class.getResourceAsStream("/api-response/paypage-v2/" + response))).useDelimiter("\\A")
                .next();
    }

    @ParameterizedTest(name = "Paypage creation with {0}")
    @MethodSource("paypageFetchResponseProvider")
    void paypagePaymentGetMappedCorrectlyFromJson(Integer paymentCount, String JsonResponse) {
        PaypageV2 fetchedPaypage = new JsonParser().fromJson(JsonResponse, PaypageV2.class);
        assertEquals(paymentCount, fetchedPaypage.getTotal());
        assertEquals(paymentCount, fetchedPaypage.getPayments().length);

        if (paymentCount == 0) {
            return;
        }

        PaypagePayment paypagePayment = fetchedPaypage.getPayments()[0];
        assertEquals("s-pay-0", paypagePayment.getPaymentId());

        JsonMessage[] messages = paypagePayment.getMessages();
        assertEquals(1, messages.length);

        assertEquals("COR.000.000.000", messages[0].getCode());
        assertEquals("Your payments have been successfully processed.", messages[0].getCustomer());
        assertEquals("Transaction succeeded", messages[0].getMerchant());

        if (paymentCount == 1) {
            return;
        }

        PaypagePayment paypagePaymentTwo = fetchedPaypage.getPayments()[1];
        assertEquals("s-pay-1", paypagePaymentTwo.getPaymentId());

        JsonMessage[] messagesTwo = paypagePaymentTwo.getMessages();
        assertEquals(1, messagesTwo.length);

        assertEquals("COR.000.000.000", messagesTwo[0].getCode());
        assertEquals("Your payments have been successfully processed.", messagesTwo[0].getCustomer());
        assertEquals("Transaction succeeded", messagesTwo[0].getMerchant());
    }

    public static Stream<Arguments> paypageFetchResponseProvider() {
        return Stream.of(
                Arguments.of(0, getResponse("fetch-response-without-payments.json")),
                Arguments.of(1, getResponse("fetch-response-one-payment.json")),
                Arguments.of(2, getResponse("fetch-response-with-multiple-payments.json"))
        );
    }
}