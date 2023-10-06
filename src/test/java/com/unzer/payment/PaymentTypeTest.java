package com.unzer.payment;


import com.unzer.payment.business.AbstractPaymentTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class PaymentTypeTest extends AbstractPaymentTest {

    @Test
    public void testForInvalidPaymentType() {
        assertThrows(PaymentException.class, () -> {
            getUnzer().fetchPaymentType("s-xxx-shdshdbshbv");
        });
    }

}
