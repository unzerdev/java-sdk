package com.unzer.payment.integration.paymenttypes;


import com.unzer.payment.Charge;
import com.unzer.payment.business.AbstractPaymentTest;
import com.unzer.payment.business.Keys;
import com.unzer.payment.paymenttypes.Przelewy24;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Currency;

import static com.unzer.payment.util.Url.unsafeUrl;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class Przelewy24Test extends AbstractPaymentTest {

    @Test
    public void testCreatePrzelewy24ManatoryType() {
        Przelewy24 p24 = new Przelewy24();
        p24 = getUnzer().createPaymentType(p24);
        assertNotNull(p24.getId());
    }

    @Test
    public void testChargePrzelewy24Type() {
        Przelewy24 p24 = getUnzer(Keys.LEGACY_PRIVATE_KEY).createPaymentType(getPrzelewy24());
        Charge charge = p24.charge(BigDecimal.ONE, Currency.getInstance("PLN"), unsafeUrl("https://www.unzer.com"));
        assertNotNull(charge);
        assertNotNull(charge.getId());
        assertNotNull(charge.getRedirectUrl());
    }

    @Test
    public void testFetchPrzelewy24Type() {
        Przelewy24 p24 = getUnzer().createPaymentType(getPrzelewy24());
        assertNotNull(p24.getId());
        Przelewy24 fetchedPrzelewy24 = (Przelewy24) getUnzer().fetchPaymentType(p24.getId());
        assertNotNull(fetchedPrzelewy24.getId());
    }


    private Przelewy24 getPrzelewy24() {
        Przelewy24 p24 = new Przelewy24();
        return p24;
    }


}
