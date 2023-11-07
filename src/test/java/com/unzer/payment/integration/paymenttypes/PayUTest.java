package com.unzer.payment.integration.paymenttypes;


import com.unzer.payment.BaseTransaction;
import com.unzer.payment.Charge;
import com.unzer.payment.Unzer;
import com.unzer.payment.business.AbstractPaymentTest;
import com.unzer.payment.paymenttypes.PayU;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.stream.Stream;

import static com.unzer.payment.util.Url.unsafeUrl;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

public class PayUTest extends AbstractPaymentTest {

    @Test
    public void create_payment_type() {
        Unzer unzer = getUnzer();

        PayU payU = new PayU();
        payU = unzer.createPaymentType(payU);
        assertNotNull(payU.getId());

        PayU fetched = (PayU) unzer.fetchPaymentType(payU.getId());
        assertEquals(payU.getId(), fetched.getId());
    }

    @TestFactory
    public Stream<DynamicTest> charge() {
        return Stream.of("CZK", "PLN").map(currency -> dynamicTest(currency, () -> {
            Unzer unzer = getUnzer();
            PayU payU = new PayU();
            payU = unzer.createPaymentType(payU);

            Charge charge = unzer.charge(
                    (Charge) new Charge()
                            .setAmount(BigDecimal.TEN)
                            .setCurrency(Currency.getInstance(currency))
                            .setTypeId(payU.getId())
                            .setReturnUrl(unsafeUrl("https://unzer.com"))
            );

            assertNotNull(charge);
            assertNotNull(charge.getId());
            assertEquals(BaseTransaction.Status.PENDING, charge.getStatus());
        }));
    }
}
