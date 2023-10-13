package com.unzer.payment;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.unzer.payment.communication.HttpCommunicationException;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

public class PaymentTest {

    @Test
    public void testIsNotEmpty() {
        Payment payment = new Payment(getUnzer());
        assertFalse(payment.isNotEmpty(""));
        assertFalse(payment.isNotEmpty("   "));
        assertFalse(payment.isNotEmpty(null));
        assertTrue(payment.isNotEmpty("a"));
    }

    @Test
    public void testChargeOnNullAuthorization() throws HttpCommunicationException {
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
