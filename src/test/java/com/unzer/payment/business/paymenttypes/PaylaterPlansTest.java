package com.unzer.payment.business.paymenttypes;

import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import com.unzer.payment.BaseTransaction;
import com.unzer.payment.PaylaterInstallmentPlans;
import com.unzer.payment.PaylaterPlans;
import com.unzer.payment.Unzer;
import com.unzer.payment.communication.HttpClientMock;
import com.unzer.payment.models.CustomerType;
import com.unzer.payment.models.paylater.InstallmentPlan;
import com.unzer.payment.models.paylater.InstallmentPlansRequest;
import com.unzer.payment.models.paylater.PaylaterPlanType;
import com.unzer.payment.models.paylater.PaylaterPlansRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.EnumSet;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import java.util.stream.Stream;

import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.jsonResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@WireMockTest(httpPort = 8080)
class PaylaterPlansTest {

    private static final BigDecimal AMOUNT = new BigDecimal("99.99");

    private static String fixture(String name) {
        return new Scanner(Objects.requireNonNull(
                PaylaterPlansTest.class.getResourceAsStream("/api-response/paylaterPlans/" + name)))
                .useDelimiter("\\A").next();
    }

    private static String installmentFixture() {
        return new Scanner(Objects.requireNonNull(
                PaylaterPlansTest.class.getResourceAsStream(
                        "/api-response/paylaterInstallment/fetch-installment-plans.json")))
                .useDelimiter("\\A").next();
    }

    private static Unzer stubbedUnzer(String segment, String fixtureBody) {
        stubFor(get("/v1/types/" + segment + "/plans?amount=99.99&currency=EUR&country=DE&customerType=B2C")
                .willReturn(jsonResponse(fixtureBody, 200)));
        return new Unzer(new HttpClientMock(), "s-private-key");
    }

    private static PaylaterPlansRequest defaultRequest() {
        return new PaylaterPlansRequest(AMOUNT, "EUR", "DE", CustomerType.B2C);
    }

    // 1. Request/URL building (parameterized over enum).
    @ParameterizedTest
    @EnumSource(PaylaterPlanType.class)
    void builds_typed_plan_url(PaylaterPlanType type) {
        PaylaterPlansRequest request = new PaylaterPlansRequest(new BigDecimal("33.33"), "EUR", "DE", CustomerType.B2C);

        assertEquals(
                "/v1/types/" + type.getSegment()
                        + "/plans?amount=33.33&currency=EUR&country=DE&customerType=B2C",
                request.getUrl(type));
    }

    // 2. Optional params omitted when null.
    @ParameterizedTest
    @EnumSource(PaylaterPlanType.class)
    void omits_null_optional_params(PaylaterPlanType type) {
        PaylaterPlansRequest request = new PaylaterPlansRequest(new BigDecimal("33.33"), null, null, null);

        assertEquals("/v1/types/" + type.getSegment() + "/plans?amount=33.33", request.getUrl(type));
    }

    // 3. Amount formatting pinned.
    @ParameterizedTest
    @CsvSource({
            "99.99, 99.99",
            "12.3456789, 12.3456789",
            "100, 100"
    })
    void amount_formatting_is_pinned(String amount, String expected) {
        PaylaterPlansRequest request = new PaylaterPlansRequest(new BigDecimal(amount), null, null, null);

        assertEquals("/v1/types/paylater-invoice/plans?amount=" + expected,
                request.getUrl(PaylaterPlanType.INVOICE));
    }

    private static Stream<Arguments> topLevelExpectations() {
        return Stream.of(
                Arguments.of(PaylaterPlanType.INVOICE, "fetch-invoice-plans.json",
                        "s-inv-xyz", new BigDecimal("149.50"), "EUR", 2),
                Arguments.of(PaylaterPlanType.DIRECT_DEBIT, "fetch-direct-debit-plans.json",
                        "s-ddg-xyz", new BigDecimal("200.00"), "CHF", 2)
        );
    }

    // 4. Fetch + mapping (parameterized invoice + direct-debit) & 5. secciUrl parsing.
    @ParameterizedTest
    @MethodSource("topLevelExpectations")
    void fetches_and_maps_plans(PaylaterPlanType type, String fixture, String expectedId,
                                BigDecimal expectedAmount, String expectedCurrency, int expectedPlanCount) {
        Unzer unzer = stubbedUnzer(type.getSegment(), fixture(fixture));

        PaylaterPlans plans = unzer.fetchPaylaterPlans(defaultRequest(), type);

        assertEquals(expectedId, plans.getId());
        assertEquals(expectedAmount.setScale(4), plans.getAmount());
        assertEquals(Currency.getInstance(expectedCurrency), plans.getCurrency());
        assertEquals(BaseTransaction.Status.SUCCESS, plans.getStatus());

        List<InstallmentPlan> mapped = plans.getPlans();
        assertNotNull(mapped);
        assertEquals(expectedPlanCount, mapped.size());

        mapped.forEach(plan -> {
            assertNotNull(plan.getSecciUrl());
            assertNotNull(plan.getTotalAmount());
            assertNotNull(plan.getInstallmentRates());
            assertEquals(plan.getNumberOfRates(), plan.getInstallmentRates().size());
        });
    }

    // 6. Plans without installmentRates map to empty list, no NPE.
    @Test
    void maps_plan_without_rates_to_empty_list() {
        Unzer unzer = stubbedUnzer(PaylaterPlanType.INVOICE.getSegment(), fixture("fetch-plans-without-rates.json"));

        PaylaterPlans plans = unzer.fetchPaylaterPlans(defaultRequest(), PaylaterPlanType.INVOICE);

        assertEquals(1, plans.getPlans().size());
        InstallmentPlan plan = plans.getPlans().get(0);
        assertNotNull(plan.getInstallmentRates());
        assertTrue(plan.getInstallmentRates().isEmpty());
        assertNotNull(plan.getSecciUrl());
    }

    // 7. Empty plans array maps cleanly.
    @Test
    void maps_empty_plans_array() {
        Unzer unzer = stubbedUnzer(PaylaterPlanType.INVOICE.getSegment(), fixture("fetch-plans-empty.json"));

        PaylaterPlans plans = unzer.fetchPaylaterPlans(defaultRequest(), PaylaterPlanType.INVOICE);

        assertNotNull(plans.getPlans());
        assertTrue(plans.getPlans().isEmpty());
    }

    // 8. Enum segments & completeness.
    @ParameterizedTest
    @CsvSource({
            "INSTALLMENT, paylater-installment",
            "INVOICE, paylater-invoice",
            "DIRECT_DEBIT, paylater-direct-debit"
    })
    void enum_exposes_expected_segment(PaylaterPlanType type, String expectedSegment) {
        assertEquals(expectedSegment, type.getSegment());
    }

    @Test
    void enum_contains_exactly_three_constants() {
        assertEquals(
                EnumSet.of(PaylaterPlanType.INSTALLMENT, PaylaterPlanType.INVOICE, PaylaterPlanType.DIRECT_DEBIT),
                EnumSet.allOf(PaylaterPlanType.class));
    }

    // 9. Backward compatibility.
    @Test
    void fetchPaylaterInstallmentPlans_still_hits_installment_endpoint() {
        stubFor(get("/v1/types/paylater-installment/plans?amount=99.99&currency=EUR&country=DE&customerType=B2C")
                .willReturn(jsonResponse(installmentFixture(), 200)));
        Unzer unzer = new Unzer(new HttpClientMock(), "s-private-key");

        PaylaterInstallmentPlans plans = unzer.fetchPaylaterInstallmentPlans(
                new InstallmentPlansRequest(AMOUNT, "EUR", "DE", CustomerType.B2C));

        assertInstanceOf(PaylaterInstallmentPlans.class, plans);
        assertEquals("Tx-xyz", plans.getId());
        assertFalse(plans.getPlans().isEmpty());
        plans.getPlans().forEach(plan -> assertNotNull(plan.getSecciUrl()));
    }

    // 10. Null enum throws instead of building a malformed URL.
    @Test
    void null_type_throws() {
        Unzer unzer = new Unzer(new HttpClientMock(), "s-private-key");
        PaylaterPlansRequest request = defaultRequest();

        assertThrows(NullPointerException.class, () -> unzer.fetchPaylaterPlans(request, null));
    }
}
