package com.unzer.payment.integration.paymenttypes;

import com.unzer.payment.Authorization;
import com.unzer.payment.Charge;
import com.unzer.payment.business.AbstractPaymentTest;
import com.unzer.payment.paymenttypes.Paypal;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Currency;

import static com.unzer.payment.util.Types.unsafeUrl;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


class PaypalTest extends AbstractPaymentTest {

    @Test
    void testCreatePaypalMandatoryType() {
        Paypal paypal = new Paypal();
        paypal = getUnzer().createPaymentType(paypal);
        assertNotNull(paypal.getId());
    }

    @Test
    void testCreatePaypalWithEmail() {
        Paypal paypal = new Paypal();
        paypal.setEmail("test.user@email.com");
        paypal = getUnzer().createPaymentType(paypal);
        assertNotNull(paypal.getId());
        assertEquals("test.user@email.com", paypal.getEmail());
    }

    @Test
    void testAuthorizeType() {
        Paypal paypal = getUnzer().createPaymentType(new Paypal());
        Authorization authorization = paypal.authorize(BigDecimal.ONE, Currency.getInstance("EUR"),
                unsafeUrl("https://www.meinShop.de"));
        assertNotNull(authorization);
    }

    @Test
    void testChargePaypalType() {
        Paypal paypal = getUnzer().createPaymentType(new Paypal());
        Charge charge = paypal.charge(BigDecimal.ONE, Currency.getInstance("EUR"),
                unsafeUrl("https://www.unzer.com"));
        assertNotNull(charge);
        assertNotNull(charge.getId());
        assertNotNull(charge.getRedirectUrl());
    }

    @Test
    void testFetchPaypalType() {
        Paypal paypal = getUnzer().createPaymentType(new Paypal());
        assertNotNull(paypal.getId());
        Paypal fetchedPaypal = (Paypal) getUnzer().fetchPaymentType(paypal.getId());
        assertNotNull(fetchedPaypal.getId());
    }
}
