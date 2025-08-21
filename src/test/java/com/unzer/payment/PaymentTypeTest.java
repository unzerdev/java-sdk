package com.unzer.payment;


import com.unzer.payment.business.AbstractPaymentTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

class PaymentTypeTest extends AbstractPaymentTest {

    @Test
    void testForInvalidPaymentType() {
        assertThrows(PaymentException.class, () -> {
            getUnzer().fetchPaymentType("s-xxx-shdshdbshbv");
        });
    }

}
