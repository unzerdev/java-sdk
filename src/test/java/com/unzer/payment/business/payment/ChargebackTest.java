package com.unzer.payment.business.payment;

import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import com.unzer.payment.*;
import com.unzer.payment.communication.HttpClientMock;
import com.unzer.payment.communication.json.JsonMessage;
import com.unzer.payment.enums.RecurrenceType;
import com.unzer.payment.models.AdditionalTransactionData;
import com.unzer.payment.models.CardTransactionData;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.unzer.payment.util.Types.unsafeUrl;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@WireMockTest(httpPort = 8080)
public class ChargebackTest {
    private static String getResponse(String response) {
        return new Scanner(
                Objects.requireNonNull(
                        ChargebackTest.class.getResourceAsStream("/api-response/payment/" + response)))
                .useDelimiter(
                        "\\A")
                .next();
    }

    @Test
    public void chargeback_fetched_successfully() throws ParseException {
        stubFor(
                get("/v1/payments/s-pay-286").willReturn(
                        jsonResponse(getResponse("fetch-payment.json"), 200))
        );
        stubFor(
                get("/v1/payments/s-pay-286/authorize/s-aut-1").willReturn(
                        jsonResponse(getResponse("fetch-authorize.json"), 200))
        );
        stubFor(
                get("/v1/payments/s-pay-286/chargeback/s-cgb-1").willReturn(
                        jsonResponse(getResponse("fetch-chargeback.json"), 200))
        );

        Unzer unzer = new Unzer(new HttpClientMock(), "s-private-key");

        Payment fetchedPayment = unzer.fetchPayment("s-pay-286");
        assertNotNull(fetchedPayment);

        List<Chargeback> actualChargebacks = fetchedPayment.getChargebackList();
        actualChargebacks.sort(Comparator.comparing(BaseTransaction::getId));

        List<Chargeback> expectedChargeBacks = Collections.singletonList(
                (Chargeback) new Chargeback()
                        .setId("s-cgb-1")
                        .setAmount(BigDecimal.TEN)
                        .setCurrency(Currency.getInstance("EUR"))
                        .setReturnUrl(unsafeUrl("https://unzer.com"))
                        .setTypeId("s-crd-6tg6nwdkrcdk")
                        .setPaymentId("s-pay-286")
                        .setTraceId("b2f9c67bebafa3cd49b3d50f9ff00639")
                        .setCard3ds(Boolean.FALSE)
                        .setMessage(
                                new JsonMessage()
                                        .setCode("COR.000.000.000")
                                        .setCustomer("Your payments have been successfully processed.")
                                        .setMerchant("Transaction succeeded")
                        )
                        .setDate(
                                new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss", Locale.ENGLISH).parse("31-Jan-2023 05:44:41"))
                        .setStatus(BaseTransaction.Status.SUCCESS)
                        .setAdditionalTransactionData(
                                new AdditionalTransactionData()
                                        .setCard(
                                                new CardTransactionData()
                                                        .setRecurrenceType(RecurrenceType.UNSCHEDULED)
                                                        .setLiability(CardTransactionData.Liability.MERCHANT)
                                                        .setExemptionType(CardTransactionData.ExemptionType.LVP)
                                        )
                        )
                        .setResourceUrl(
                                unsafeUrl("https://sbx-api.unzer.com/v1/payments/s-pay-286/chargeback/s-cgb-1")
                        )
                        .setProcessing(
                                new Processing()
                                        .setUniqueId("31HA07BC8150FAE7F0B65A2095CE0A83")
                                        .setShortId("5390.6667.9884")
                                        .setTraceId("b2f9c67bebafa3cd49b3d50f9ff00639")
                        )
        );

        assertIterableEquals(expectedChargeBacks, actualChargebacks);
    }
}
