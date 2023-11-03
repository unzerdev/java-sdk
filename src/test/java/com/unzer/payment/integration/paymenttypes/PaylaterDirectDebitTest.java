package com.unzer.payment.integration.paymenttypes;

import com.unzer.payment.*;
import com.unzer.payment.business.AbstractPaymentTest;
import com.unzer.payment.paymenttypes.PaylaterDirectDebit;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Currency;

import static com.unzer.payment.util.Url.unsafeUrl;
import static com.unzer.payment.util.Uuid.generateUuid;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


public class PaylaterDirectDebitTest extends AbstractPaymentTest {
    @Test
    public void create_and_fetch_payment_type_ok() {
        Unzer unzer = getUnzer();
        PaylaterDirectDebit type = new PaylaterDirectDebit(
                "DE89370400440532013000",
                "Max Mustermann"
        );
        PaylaterDirectDebit response = unzer.createPaymentType(type);
        assertNotNull(response);
        assertNotNull(response.getId());

        PaylaterDirectDebit fetched = (PaylaterDirectDebit) unzer.fetchPaymentType(response.getId());
        assertNotNull(fetched);
        assertEquals(response.getId(), fetched.getId());
    }

    @Test
    public void authorize_and_charge_ok() {
        Unzer unzer = getUnzer();
        PaylaterDirectDebit type = unzer.createPaymentType(
                new PaylaterDirectDebit(
                        "DE89370400440532013000",
                        "Max Mustermann"
                )
        );
        assertNotNull(type.getId());

        Customer customer = unzer.createCustomer(getMaximumCustomer(generateUuid()));
        assertNotNull(customer.getId());

        Authorization authorize = unzer.authorize((Authorization) new Authorization()
                .setAmount(BigDecimal.TEN)
                .setCurrency(Currency.getInstance("EUR"))
                .setTypeId(type.getId())
                .setCustomerId(customer.getId())
                .setReturnUrl(unsafeUrl("https://unzer.com")));

        assertNotNull(authorize);
        assertNotNull(authorize.getId());
        assertNotNull(authorize.getPaymentId());
        assertEquals(BaseTransaction.Status.SUCCESS, authorize.getStatus());

        Charge charge = unzer.chargeAuthorization(authorize.getPaymentId());
        assertNotNull(charge.getId());
        assertEquals(BaseTransaction.Status.SUCCESS, authorize.getStatus());
    }
}
