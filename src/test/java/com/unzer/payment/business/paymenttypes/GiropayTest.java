package com.unzer.payment.business.paymenttypes;


import com.unzer.payment.Charge;
import com.unzer.payment.business.AbstractPaymentTest;
import com.unzer.payment.communication.HttpCommunicationException;
import com.unzer.payment.paymenttypes.Giropay;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Currency;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class GiropayTest extends AbstractPaymentTest {

    @Test
    public void testCreateGiropayManatoryType() throws HttpCommunicationException {
        Giropay giropay = new Giropay();
        giropay = getUnzer().createPaymentType(giropay);
        assertNotNull(giropay.getId());
    }

    @Test
    public void testChargeGiropayType() throws HttpCommunicationException, MalformedURLException {
        Giropay giropay = getUnzer().createPaymentType(getGiropay());
        Charge charge = giropay.charge(BigDecimal.ONE, Currency.getInstance("EUR"), new URL("https://www.unzer.com"));
        assertNotNull(charge);
        assertNotNull(charge.getId());
        assertNotNull(charge.getRedirectUrl());
    }

    @Test
    public void testFetchGiropayType() throws HttpCommunicationException {
        Giropay giropay = getUnzer().createPaymentType(getGiropay());
        assertNotNull(giropay.getId());
        Giropay fetchedGiropay = (Giropay) getUnzer().fetchPaymentType(giropay.getId());
        assertNotNull(fetchedGiropay.getId());
    }

    private Giropay getGiropay() {
        return new Giropay();
    }

}
