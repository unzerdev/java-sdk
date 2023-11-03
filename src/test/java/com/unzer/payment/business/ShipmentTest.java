package com.unzer.payment.business;


import com.unzer.payment.PaymentException;
import com.unzer.payment.Unzer;
import com.unzer.payment.paymenttypes.InvoiceSecured;
import org.junit.jupiter.api.Test;

import static com.unzer.payment.util.Uuid.generateUuid;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ShipmentTest extends AbstractPaymentTest {
    @Test
    public void testAuthorizeWithShipmentNotSameAddressWithInvoiceSecured() {
        assertThrows(PaymentException.class, () -> {
                    Unzer unzer = getUnzer();
                    InvoiceSecured paymentTypeInvoiceSecured = unzer.createPaymentType(new InvoiceSecured());
                    unzer.authorize(
                            getAuthorization(
                                    paymentTypeInvoiceSecured.getId(),
                                    unzer.createCustomer(getMaximumCustomer(generateUuid())).getId()
                            )
                    );
                }
        );
    }

}
