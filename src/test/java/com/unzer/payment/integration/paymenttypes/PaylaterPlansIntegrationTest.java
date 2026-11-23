package com.unzer.payment.integration.paymenttypes;

import com.unzer.payment.BaseTransaction;
import com.unzer.payment.PaymentError;
import com.unzer.payment.PaymentException;
import com.unzer.payment.PaylaterPlans;
import com.unzer.payment.business.AbstractPaymentTest;
import com.unzer.payment.models.CustomerType;
import com.unzer.payment.models.paylater.InstallmentPlan;
import com.unzer.payment.models.paylater.PaylaterPlanType;
import com.unzer.payment.models.paylater.PaylaterPlansRequest;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Sandbox integration tests for the Paylater invoice and direct-debit {@code /plans} endpoints.
 * Authenticates exactly like {@link PaylaterInstallmentTest} via {@link #getUnzer()} (the
 * {@code defaultPrivateKey} system property / CI secret). Mirrors
 * {@link PaylaterInstallmentTest#testFetchInstallmentPlans()}.
 */
class PaylaterPlansIntegrationTest extends AbstractPaymentTest {

    // 11. Happy path invoice + direct-debit: real plans with non-null secciUrl.
    @ParameterizedTest
    @EnumSource(value = PaylaterPlanType.class, names = {"INVOICE", "DIRECT_DEBIT"})
    void testFetchPlans(PaylaterPlanType type) {
        PaylaterPlansRequest request =
                new PaylaterPlansRequest(new BigDecimal("99.99"), "EUR", "DE", CustomerType.B2C);

        PaylaterPlans plans = getUnzer().fetchPaylaterPlans(request, type);

        assertNotNull(plans.getId());
        assertNumberEquals(new BigDecimal("99.99"), plans.getAmount());
        assertEquals(Currency.getInstance("EUR"), plans.getCurrency());
        assertEquals(BaseTransaction.Status.SUCCESS, plans.getStatus());

        List<InstallmentPlan> mapped = plans.getPlans();
        assertNotNull(mapped);
        assertFalse(mapped.isEmpty());
        mapped.forEach(plan -> {
            assertNotNull(plan.getTotalAmount());
            assertNotNull(plan.getSecciUrl());
            assertNotNull(plan.getInstallmentRates());
        });
    }

    // 12. Invalid-input error paths. Validation is delegated to PAPI; the SDK surfaces its
    // PaymentException unchanged, carrying the standard Unzer error envelope
    // (code + merchantMessage + customerMessage).
    //
    // We assert the contract only: a PaymentException whose error list is non-empty and whose
    // first entry carries a code and a merchant message. We deliberately do NOT pin a specific
    // getCode() value -- the concrete codes are a backend detail and pinning them is too brittle.
    //
    // Example of an observed sandbox envelope (illustrative only, NOT a pinned expectation):
    //   code            = "API.720.100.001"
    //   merchantMessage = "The given payment method plans is not found."
    //   customerMessage = "Invalid payment method. Please contact us for more information."
    @ParameterizedTest
    @EnumSource(value = PaylaterPlanType.class, names = {"INVOICE", "DIRECT_DEBIT"})
    void testNegativeAmountIsRejectedByPapi(PaylaterPlanType type) {
        assertPapiRejects(new PaylaterPlansRequest(new BigDecimal("-10.00"), "EUR", "DE", CustomerType.B2C), type);
    }

    @ParameterizedTest
    @EnumSource(value = PaylaterPlanType.class, names = {"INVOICE", "DIRECT_DEBIT"})
    void testZeroAmountIsRejectedByPapi(PaylaterPlanType type) {
        assertPapiRejects(new PaylaterPlansRequest(BigDecimal.ZERO, "EUR", "DE", CustomerType.B2C), type);
    }

    @ParameterizedTest
    @EnumSource(value = PaylaterPlanType.class, names = {"INVOICE", "DIRECT_DEBIT"})
    void testInvalidCurrencyIsRejectedByPapi(PaylaterPlanType type) {
        assertPapiRejects(new PaylaterPlansRequest(new BigDecimal("99.99"), "XXX", "DE", CustomerType.B2C), type);
    }

    @ParameterizedTest
    @EnumSource(value = PaylaterPlanType.class, names = {"INVOICE", "DIRECT_DEBIT"})
    void testMissingCurrencyIsRejectedByPapi(PaylaterPlanType type) {
        assertPapiRejects(new PaylaterPlansRequest(new BigDecimal("99.99"), null, "DE", CustomerType.B2C), type);
    }

    @ParameterizedTest
    @EnumSource(value = PaylaterPlanType.class, names = {"INVOICE", "DIRECT_DEBIT"})
    void testInvalidCountryIsRejectedByPapi(PaylaterPlanType type) {
        assertPapiRejects(new PaylaterPlansRequest(new BigDecimal("99.99"), "EUR", "ZZ", CustomerType.B2C), type);
    }

    private void assertPapiRejects(PaylaterPlansRequest request, PaylaterPlanType type) {
        PaymentException exception = assertThrows(PaymentException.class,
                () -> getUnzer().fetchPaylaterPlans(request, type));

        List<PaymentError> errors = exception.getPaymentErrorList();
        assertNotNull(errors);
        assertFalse(errors.isEmpty());
        PaymentError error = errors.get(0);
        assertNotNull(error.getCode());
        assertNotNull(error.getMerchantMessage());
    }
}
