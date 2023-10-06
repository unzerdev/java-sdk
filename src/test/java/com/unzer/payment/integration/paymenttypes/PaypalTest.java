package com.unzer.payment.integration.paymenttypes;

import com.unzer.payment.Authorization;
import com.unzer.payment.Charge;
import com.unzer.payment.business.AbstractPaymentTest;
import com.unzer.payment.communication.HttpCommunicationException;
import com.unzer.payment.paymenttypes.Paypal;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Currency;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


public class PaypalTest extends AbstractPaymentTest {

    @Test
    public void testCreatePaypalMandatoryType() throws HttpCommunicationException {
        Paypal paypal = new Paypal();
        paypal = getUnzer().createPaymentType(paypal);
        assertNotNull(paypal.getId());
    }

    @Test
    public void testCreatePaypalWithEmail() throws HttpCommunicationException {
        Paypal paypal = new Paypal();
        paypal.setEmail("test.user@email.com");
        paypal = getUnzer().createPaymentType(paypal);
        assertNotNull(paypal.getId());
        assertEquals("test.user@email.com", paypal.getEmail());
    }

    @Test
    public void testAuthorizeType() throws HttpCommunicationException, MalformedURLException {
        Paypal paypal = getUnzer().createPaymentType(getPaypal());
        Authorization authorization = paypal.authorize(BigDecimal.ONE, Currency.getInstance("EUR"), new URL("https://www.meinShop.de"));
        assertNotNull(authorization);
    }

    @Test
    public void testChargePaypalType() throws HttpCommunicationException, MalformedURLException {
        Paypal paypal = getUnzer().createPaymentType(getPaypal());
        Charge charge = paypal.charge(BigDecimal.ONE, Currency.getInstance("EUR"), new URL("https://www.unzer.com"));
        assertNotNull(charge);
        assertNotNull(charge.getId());
        assertNotNull(charge.getRedirectUrl());
    }

    @Test
    public void testFetchPaypalType() throws HttpCommunicationException {
        Paypal paypal = getUnzer().createPaymentType(getPaypal());
        assertNotNull(paypal.getId());
        Paypal fetchedPaypal = (Paypal) getUnzer().fetchPaymentType(paypal.getId());
        assertNotNull(fetchedPaypal.getId());
    }

    private Paypal getPaypal() {
        Paypal paypal = new Paypal();
        return paypal;
    }

}
