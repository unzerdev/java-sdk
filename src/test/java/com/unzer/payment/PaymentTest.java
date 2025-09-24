package com.unzer.payment;


import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PaymentTest {

    @Test
    void testIsNotEmpty() {
        Payment payment = new Payment(getUnzer());
        assertFalse(payment.isNotEmpty(""));
        assertFalse(payment.isNotEmpty("   "));
        assertFalse(payment.isNotEmpty(null));
        assertTrue(payment.isNotEmpty("a"));
    }

    @Test
    void testChargeOnNullAuthorization() {
        Payment payment = new Payment(getUnzer());

        try {
            payment.cancel(BigDecimal.TEN);
        } catch (PaymentException paymentException) {
            assertEquals("Cancel is only possible for an Authorization", paymentException.getMessage());
        }

    }

    private Unzer getUnzer() {
        return null;
    }

}
