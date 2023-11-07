package com.unzer.payment.integration.paymenttypes;


import com.unzer.payment.Charge;
import com.unzer.payment.business.AbstractPaymentTest;
import com.unzer.payment.paymenttypes.Eps;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Currency;

import static com.unzer.payment.util.Url.unsafeUrl;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class EpsTest extends AbstractPaymentTest {

    @Test
    public void testCreateEpsWithoutBicType() {
        Eps eps = new Eps();
        eps = getUnzer().createPaymentType(eps);
        assertNotNull(eps.getId());
    }

    @Test
    public void testCreateEpsWithBicType() {
        Eps eps = new Eps("SPFKAT2BXXX");
        eps = getUnzer().createPaymentType(eps);
        assertNotNull(eps.getId());
    }

    @Test
    public void testChargeEpsType() {
        Eps eps = getUnzer().createPaymentType(getEps());
        Charge charge = eps.charge(BigDecimal.ONE, Currency.getInstance("EUR"), unsafeUrl("https://www.unzer.com"));
        assertNotNull(charge);
        assertNotNull(charge.getId());
        assertNotNull(charge.getRedirectUrl());
    }

    @Test
    public void testFetchEpsType() {
        Eps eps = getUnzer().createPaymentType(getEps());
        assertNotNull(eps.getId());
        Eps fetchedEps = (Eps) getUnzer().fetchPaymentType(eps.getId());
        assertNotNull(fetchedEps.getId());
    }


    private Eps getEps() {
        Eps eps = new Eps();
        return eps;
    }


}
