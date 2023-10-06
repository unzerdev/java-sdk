package com.unzer.payment.integration.paymenttypes;


import com.unzer.payment.Charge;
import com.unzer.payment.business.AbstractPaymentTest;
import com.unzer.payment.communication.HttpCommunicationException;
import com.unzer.payment.paymenttypes.Eps;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Currency;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class EpsTest extends AbstractPaymentTest {

    @Test
    public void testCreateEpsWithoutBicType() throws HttpCommunicationException {
        Eps eps = new Eps();
        eps = getUnzer().createPaymentType(eps);
        assertNotNull(eps.getId());
    }

    @Test
    public void testCreateEpsWithBicType() throws HttpCommunicationException {
        Eps eps = new Eps("SPFKAT2BXXX");
        eps = getUnzer().createPaymentType(eps);
        assertNotNull(eps.getId());
    }

    @Test
    public void testChargeEpsType() throws HttpCommunicationException, MalformedURLException {
        Eps eps = getUnzer().createPaymentType(getEps());
        Charge charge = eps.charge(BigDecimal.ONE, Currency.getInstance("EUR"), new URL("https://www.unzer.com"));
        assertNotNull(charge);
        assertNotNull(charge.getId());
        assertNotNull(charge.getRedirectUrl());
    }

    @Test
    public void testFetchEpsType() throws HttpCommunicationException {
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
