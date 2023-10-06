package com.unzer.payment.integration.paymenttypes;


import com.unzer.payment.Charge;
import com.unzer.payment.business.AbstractPaymentTest;
import com.unzer.payment.business.Keys;
import com.unzer.payment.communication.HttpCommunicationException;
import com.unzer.payment.paymenttypes.Ideal;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Currency;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class IdealTest extends AbstractPaymentTest {

    @Test
    public void testCreateIdealManatoryType() throws HttpCommunicationException {
        Ideal ideal = getUnzer().createPaymentType(getIdeal());
        assertNotNull(ideal.getId());
    }

    @Test
    public void testChargeIdealType() throws HttpCommunicationException, MalformedURLException {
        Ideal ideal = getUnzer(Keys.LEGACY_PRIVATE_KEY).createPaymentType(getIdeal());
        Charge charge = ideal.charge(BigDecimal.ONE, Currency.getInstance("EUR"), new URL("https://www.unzer.com"));
        assertNotNull(charge);
        assertNotNull(charge.getId());
        assertNotNull(charge.getRedirectUrl());
    }

    @Test
    public void testFetchIdealType() throws HttpCommunicationException {
        Ideal ideal = getUnzer().createPaymentType(getIdeal());
        assertNotNull(ideal.getId());
        Ideal fetchedIdeal = (Ideal) getUnzer().fetchPaymentType(ideal.getId());
        assertNotNull(fetchedIdeal.getId());
    }


    private Ideal getIdeal() {
        Ideal ideal = new Ideal().setBic("RABONL2U");
        return ideal;
    }


}
