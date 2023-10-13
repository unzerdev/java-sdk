package com.unzer.payment.integration.paymenttypes;


import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.unzer.payment.Charge;
import com.unzer.payment.business.AbstractPaymentTest;
import com.unzer.payment.communication.HttpCommunicationException;
import com.unzer.payment.paymenttypes.Sofort;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Currency;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class SofortTest extends AbstractPaymentTest {

    @Test
    public void testCreateSofortManatoryType() throws HttpCommunicationException {
        Sofort sofort = new Sofort();
        sofort = getUnzer().createPaymentType(sofort);
        assertNotNull(sofort.getId());
    }

    @Test
    @Disabled("does not work on PAPI")
    public void testChargeSofortType() throws HttpCommunicationException, MalformedURLException {
        Sofort sofort = getUnzer().createPaymentType(new Sofort());
        Charge charge = sofort.charge(BigDecimal.ONE, Currency.getInstance("EUR"), new URL("https://www.unzer.com"));
        assertNotNull(charge);
        assertNotNull(charge.getId());
        assertNotNull(charge.getRedirectUrl());
    }

    @Test
    public void testFetchSofortType() throws HttpCommunicationException {
        Sofort sofort = getUnzer().createPaymentType(new Sofort());
        assertNotNull(sofort.getId());
        Sofort fetchedSofort = (Sofort) getUnzer().fetchPaymentType(sofort.getId());
        assertNotNull(fetchedSofort.getId());
    }
}
