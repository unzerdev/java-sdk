package com.unzer.payment.business.paymenttypes;


import com.unzer.payment.Charge;
import com.unzer.payment.business.AbstractPaymentTest;
import com.unzer.payment.communication.HttpCommunicationException;
import com.unzer.payment.paymenttypes.Przelewy24;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Currency;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class Przelewy24Test extends AbstractPaymentTest {

    @Test
    public void testCreatePrzelewy24ManatoryType() throws HttpCommunicationException {
        Przelewy24 p24 = new Przelewy24();
        p24 = getUnzer().createPaymentType(p24);
        assertNotNull(p24.getId());
    }

    @Test
    public void testChargePrzelewy24Type() throws HttpCommunicationException, MalformedURLException {
        Przelewy24 p24 = getUnzer().createPaymentType(getPrzelewy24());
        Charge charge = p24.charge(BigDecimal.ONE, Currency.getInstance("PLN"), new URL("https://www.unzer.com"));
        assertNotNull(charge);
        assertNotNull(charge.getId());
        assertNotNull(charge.getRedirectUrl());
    }

    @Test
    public void testFetchPrzelewy24Type() throws HttpCommunicationException {
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
