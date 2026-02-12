package com.unzer.payment.service;


import com.unzer.payment.Charge;
import com.unzer.payment.PaymentError;
import com.unzer.payment.PaymentException;
import com.unzer.payment.TestData;
import com.unzer.payment.Unzer;
import com.unzer.payment.communication.HttpCommunicationMockUtils;
import com.unzer.payment.communication.UnzerRestCommunication;
import com.unzer.payment.communication.json.ApiTransaction;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class PaymentServiceTest {


    public PaymentService setUpPaymentService(String json, int status) {
        UnzerRestCommunication rest = HttpCommunicationMockUtils.withFixedResponse(json, status);
        Unzer unzer = new Unzer(rest, "anykey");
        return new PaymentService(unzer, rest);
    }

    @Test
    void testChargeInCaseOfCoreException() {
        PaymentService paymentService = setUpPaymentService(TestData.errorJson(), 500);
        PaymentException exception = null;
        try {
            paymentService.charge(new Charge());
        } catch (PaymentException e) {
            exception = e;
        }

        assertNotNull(exception);
        assertEquals(1, exception.getPaymentErrorList().size());
        PaymentError error = exception.getPaymentErrorList().get(0);
        assertEquals("COR.400.100.101", error.getCode());
        assertEquals("Address untraceable", error.getMerchantMessage());
        assertEquals("The provided address is invalid. Please check your input and try agian.", error.getCustomerMessage());
    }

    @Test
    void test200WithErrorJsonIsConvertedToPaymentException() {
        PaymentService paymentService = setUpPaymentService(TestData.errorJson(), 200);
        PaymentException exception = null;
        try {
            paymentService.charge(new Charge());
        } catch (PaymentException e) {
            exception = e;
        }

        assertNotNull(exception);
        assertEquals(1, exception.getPaymentErrorList().size());
        PaymentError error = exception.getPaymentErrorList().get(0);
        assertEquals("COR.400.100.101", error.getCode());
        assertEquals("Address untraceable", error.getMerchantMessage());
        assertEquals("The provided address is invalid. Please check your input and try agian.", error.getCustomerMessage());
    }

    @Test
    void testUpdateScaFetchUrl_WithMalformedScaUrlStartingWithS() throws Exception {
        // Test URL with double slash and s-sca- prefix
        String malformedUrl = "https://api.unzer.com/v1/payments/s-pay-123//s-sca-456";
        String expectedUrl = "https://api.unzer.com/v1/payments/s-pay-123/sca/s-sca-456";

        String result = invokeUpdateScaFetchUrl(malformedUrl);

        assertEquals(expectedUrl, result, "Should replace //s-sca- with /sca/s-sca-");
    }

    @Test
    void testUpdateScaFetchUrl_WithMalformedScaUrlStartingWithP() throws Exception {
        // Test URL with double slash and p-sca- prefix
        String malformedUrl = "https://api.unzer.com/v1/payments/p-pay-789//p-sca-012";
        String expectedUrl = "https://api.unzer.com/v1/payments/p-pay-789/sca/p-sca-012";

        String result = invokeUpdateScaFetchUrl(malformedUrl);

        assertEquals(expectedUrl, result, "Should replace //p-sca- with /sca/p-sca-");
    }

    @Test
    void testUpdateScaFetchUrl_WithCorrectUrl() throws Exception {
        // Test URL that's already correct - should not be modified
        String correctUrl = "https://api.unzer.com/v1/payments/s-pay-123/sca/s-sca-456";

        String result = invokeUpdateScaFetchUrl(correctUrl);

        assertEquals(correctUrl, result, "Should not modify already correct URL");
    }

    @Test
    void testUpdateScaFetchUrl_WithNonScaUrl() throws Exception {
        // Test URL without -sca- pattern - should not be modified
        String nonScaUrl = "https://api.unzer.com/v1/payments/s-pay-123/charges/s-chg-456";

        String result = invokeUpdateScaFetchUrl(nonScaUrl);

        assertEquals(nonScaUrl, result, "Should not modify non-SCA URLs");
    }

    @Test
    void testUpdateScaFetchUrl_WithUrlNotContainingPayments() throws Exception {
        // Test URL without /payments/ path - should not be modified
        String nonPaymentUrl = "https://api.unzer.com/v1/transactions//s-sca-123";

        String result = invokeUpdateScaFetchUrl(nonPaymentUrl);

        assertEquals(nonPaymentUrl, result, "Should not modify URLs without /payments/ path");
    }

    @Test
    void testUpdateScaFetchUrl_WithInvalidPrefixNotMatched() throws Exception {
        // Test URL with invalid prefix (not s or p) - should not be matched by regex
        String invalidPrefixUrl = "https://api.unzer.com/v1/payments/s-pay-123//x-sca-456";

        String result = invokeUpdateScaFetchUrl(invalidPrefixUrl);

        assertEquals(invalidPrefixUrl, result, "Should not modify URLs with invalid prefix (not s or p)");
    }

    /**
     * Helper method to invoke the private static updateScaFetchUrl method using reflection
     */
    private String invokeUpdateScaFetchUrl(String urlString) throws Exception {
        // Create a mock ApiTransaction with the test URL
        ApiTransaction apiTransaction = new ApiTransaction();
        apiTransaction.setUrl(new URL(urlString));

        // Use reflection to access the private static method
        Method method = PaymentService.class.getDeclaredMethod("updateScaFetchUrl", ApiTransaction.class);
        method.setAccessible(true);

        return (String) method.invoke(null, apiTransaction);
    }
}
