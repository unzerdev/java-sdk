package com.unzer.payment.business;


import com.unzer.payment.PaymentException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class ShipmentTest extends AbstractPaymentTest {
    @Test
    public void testAuthorizeWithShipmentNotSameAddressWithInvoiceSecured() {
        assertThrows(PaymentException.class, () -> getUnzer()
                .authorize(
                        getAuthorization(
                                createPaymentTypeInvoiceSecured().getId(),
                                createMaximumCustomer().getId()
                        )
                )
        );
    }

}
