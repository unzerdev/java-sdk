package com.unzer.payment.integration.paymenttypes;


import com.unzer.payment.Charge;
import com.unzer.payment.business.AbstractPaymentTest;
import com.unzer.payment.business.Keys;
import com.unzer.payment.paymenttypes.Ideal;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Currency;

import static com.unzer.payment.util.Url.unsafeUrl;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class IdealTest extends AbstractPaymentTest {

    @Test
    public void testCreateIdealManatoryType() {
        Ideal ideal = getUnzer().createPaymentType(getIdeal());
        assertNotNull(ideal.getId());
    }

    @Test
    public void testChargeIdealType() {
        Ideal ideal = getUnzer(Keys.LEGACY_PRIVATE_KEY).createPaymentType(getIdeal());
        Charge charge = ideal.charge(BigDecimal.ONE, Currency.getInstance("EUR"),
                unsafeUrl("https://www.unzer.com"));
        assertNotNull(charge);
        assertNotNull(charge.getId());
        assertNotNull(charge.getRedirectUrl());
    }

    @Test
    public void testFetchIdealType() {
        Ideal ideal = getUnzer().createPaymentType(getIdeal());
        assertNotNull(ideal.getId());
        Ideal fetchedIdeal = (Ideal) getUnzer().fetchPaymentType(ideal.getId());
        assertNotNull(fetchedIdeal.getId());
    }

    private Ideal getIdeal() {
        return new Ideal().setBic("RABONL2U");
    }
}
