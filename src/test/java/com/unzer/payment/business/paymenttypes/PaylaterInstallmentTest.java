package com.unzer.payment.business.paymenttypes;


import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import com.unzer.payment.BaseTransaction;
import com.unzer.payment.PaylaterInstallmentPlans;
import com.unzer.payment.Unzer;
import com.unzer.payment.communication.HttpClientMock;
import com.unzer.payment.models.CustomerType;
import com.unzer.payment.models.paylater.InstallmentPlan;
import com.unzer.payment.models.paylater.InstallmentPlansRequest;
import com.unzer.payment.paymenttypes.PaylaterInstallment;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.*;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.*;

@WireMockTest(httpPort = 8080)
class PaylaterInstallmentTest {
    private static String getPlansresponse(String response) {
        return new Scanner(Objects.requireNonNull(
                PaylaterInstallmentTest.class.getResourceAsStream(
                        "/api-response/paylaterInstallment/" + response))).useDelimiter("\\A")
                .next();
    }

    @Test
    void test_installment_plans() {
        Unzer unzer = new Unzer(new HttpClientMock(), "s-private-key");
        String jsonBody = getPlansresponse("fetch-installment-plans.json");
        stubFor(
                get("/v1/types/paylater-installment/plans?amount=99.99&currency=EUR&country=DE&customerType=B2C").willReturn(
                        jsonResponse(jsonBody, 200)));

        BigDecimal amount = new BigDecimal("99.99");
        InstallmentPlansRequest request = new InstallmentPlansRequest(amount, "EUR", "DE", CustomerType.B2C);

        PaylaterInstallmentPlans installmentPlans = unzer.fetchPaylaterInstallmentPlans(request);

        assertEquals("Tx-xyz", installmentPlans.getId());
        assertEquals(Currency.getInstance("EUR"), installmentPlans.getCurrency());
        assertEquals(new BigDecimal("99.99").setScale(4), installmentPlans.getAmount());
        assertEquals(BaseTransaction.Status.SUCCESS, installmentPlans.getStatus());

        List<InstallmentPlan> plans = installmentPlans.getPlans();
        assertNotNull(plans);
        assertEquals(5, plans.size());

        assertEquals(3, plans.get(0).getNumberOfRates());
        assertEquals(6, plans.get(1).getNumberOfRates());
        assertEquals(9, plans.get(2).getNumberOfRates());
        assertEquals(12, plans.get(3).getNumberOfRates());
        assertEquals(24, plans.get(4).getNumberOfRates());

        assertEquals(new BigDecimal("101.3100"), plans.get(0).getTotalAmount());
        assertEquals(new BigDecimal("110.4000"), plans.get(4).getTotalAmount());

        plans.forEach(plan -> {
            assertNotNull(plan.getSecciUrl());
            assertEquals(plan.getNumberOfRates(), plan.getInstallmentRates().size());
        });
    }

    @Test
    void test_installment_type_creation() {
        Unzer unzer = new Unzer(new HttpClientMock(), "s-private-key");
        String jsonBody = getPlansresponse("create-paylater-installment-type.json");
        stubFor(
                post("/v1/types/paylater-installment/").willReturn(
                        jsonResponse(jsonBody, 200)));
        stubFor(
                get("/v1/types/paylater-installment/s-pit-vs6pjunvuick").willReturn(
                        jsonResponse(jsonBody, 200)));

        PaylaterInstallment paylaterInstallment = unzer.createPaymentType(new PaylaterInstallment()
                .setInquiryId("tx-xyz")
                .setHolder("max mustermann")
                .setIban("Iban")
                .setNumberOfRates(3));

        assertNull(paylaterInstallment.getHolder());
        assertNull(paylaterInstallment.getIban());
        assertNull(paylaterInstallment.getIban());

        assertEquals("s-pit-vs6pjunvuick", paylaterInstallment.getId());
        assertEquals(false, paylaterInstallment.getRecurring());
        assertEquals("123.123.123.123", paylaterInstallment.getGeoLocation().getClientIp());
        assertEquals("US", paylaterInstallment.getGeoLocation().getCountryIsoA2());
    }
}
