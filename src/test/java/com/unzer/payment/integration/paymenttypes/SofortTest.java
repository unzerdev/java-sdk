package com.unzer.payment.integration.paymenttypes;


import com.unzer.payment.Charge;
import com.unzer.payment.business.AbstractPaymentTest;
import com.unzer.payment.paymenttypes.Sofort;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Currency;

import static com.unzer.payment.util.Types.unsafeUrl;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class SofortTest extends AbstractPaymentTest {

    @Test
    void testCreateSofortManatoryType() {
        Sofort sofort = new Sofort();
        sofort = getUnzer().createPaymentType(sofort);
        assertNotNull(sofort.getId());
    }

    @Test
    @Disabled("does not work on PAPI")
    public void testChargeSofortType() {
        Sofort sofort = getUnzer().createPaymentType(new Sofort());
        Charge charge = sofort.charge(BigDecimal.ONE, Currency.getInstance("EUR"), unsafeUrl("https://www.unzer.com"));
        assertNotNull(charge);
        assertNotNull(charge.getId());
        assertNotNull(charge.getRedirectUrl());
    }

    @Test
    void testFetchSofortType() {
        Sofort sofort = getUnzer().createPaymentType(new Sofort());
        assertNotNull(sofort.getId());
        Sofort fetchedSofort = (Sofort) getUnzer().fetchPaymentType(sofort.getId());
        assertNotNull(fetchedSofort.getId());
    }
}
