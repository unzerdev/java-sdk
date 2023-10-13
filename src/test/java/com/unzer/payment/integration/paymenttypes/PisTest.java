package com.unzer.payment.integration.paymenttypes;


import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.unzer.payment.Charge;
import com.unzer.payment.business.AbstractPaymentTest;
import com.unzer.payment.communication.HttpCommunicationException;
import com.unzer.payment.paymenttypes.Pis;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Currency;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class PisTest extends AbstractPaymentTest {

    @Test
    public void testCreatePis() throws HttpCommunicationException {
        Pis pis = new Pis();
        pis = getUnzer().createPaymentType(pis);
        assertNotNull(pis.getId());
    }

    @Test
    public void testCreatePisWithIbanBic() throws HttpCommunicationException {
        Pis pis = new Pis("DE69545100670661762678", "SPFKAT2BXXX");
        pis = getUnzer().createPaymentType(pis);
        assertNotNull(pis.getId());
        assertNotNull(pis.getIban());
        assertNotNull(pis.getBic());

        Pis fetchedPis = (Pis) getUnzer().fetchPaymentType(pis.getId());
        assertNotNull(fetchedPis.getId());
        assertNotNull(fetchedPis.getIban());
        assertNotNull(fetchedPis.getBic());
    }

    @Test
    public void testAuthorizeType() throws HttpCommunicationException, MalformedURLException {
        Pis pis = getUnzer().createPaymentType(new Pis());
        Charge charge = pis.charge(BigDecimal.ONE, Currency.getInstance("EUR"), new URL("https://www.meinShop.de"));
        assertNotNull(charge);
        assertNotNull(charge.getId());
        assertNotNull(charge.getRedirectUrl());
    }

    @Test
    public void testFetchPisType() throws HttpCommunicationException {
        Pis pis = getUnzer().createPaymentType(new Pis());
        assertNotNull(pis.getId());
        Pis fetchedPis = (Pis) getUnzer().fetchPaymentType(pis.getId());
        assertNotNull(fetchedPis.getId());
    }

    @Test
    @Disabled("AHC-3615 PIS holder not in response when doing a POST/GET")
    public void testFetchPisTypeWithHolderBicIban() throws HttpCommunicationException {
        Pis fetchedPis = (Pis) getUnzer().fetchPaymentType("s-pis-ivt4ibypi0zk");
        assertNotNull(fetchedPis.getId());
        assertNotNull(fetchedPis.getIban());
        assertNotNull(fetchedPis.getBic());
        assertNotNull(fetchedPis.getHolder());
    }
}
