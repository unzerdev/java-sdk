package com.unzer.payment.integration.paymenttypes;

import com.unzer.payment.Authorization;
import com.unzer.payment.Charge;
import com.unzer.payment.business.AbstractPaymentTest;
import com.unzer.payment.paymenttypes.Wero;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.net.URL;
import java.util.Currency;

import static com.unzer.payment.util.Types.unsafeUrl;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class WeroTest extends AbstractPaymentTest {

    @Test
    void testCreateWeroType() {
        Wero wero = new Wero();
        wero = getUnzer().createPaymentType(wero);
        assertNotNull(wero.getId());
    }

    @Test
    void testFetchWeroType() {
        Wero wero = getUnzer().createPaymentType(new Wero());
        assertNotNull(wero.getId());
        Wero fetched = (Wero) getUnzer().fetchPaymentType(wero.getId());
        assertNotNull(fetched.getId());
        assertNotNull(fetched.getGeoLocation());
    }

    @Test
    void testChargeWeroType() {
        Wero wero = getUnzer().createPaymentType(new Wero());
        URL returnUrl = unsafeUrl("https://www.unzer.com");
        Charge charge = getUnzer().charge(BigDecimal.ONE, Currency.getInstance("EUR"), wero, returnUrl);
        assertNotNull(charge);
        assertNotNull(charge.getId());
        assertNotNull(charge.getRedirectUrl());
    }

    @Test
    void testAuthorizeWeroType() {
        Wero wero = getUnzer().createPaymentType(new Wero());
        URL returnUrl = unsafeUrl("https://www.unzer.com");
        Authorization authorization = getUnzer().authorize(BigDecimal.ONE, Currency.getInstance("EUR"), wero, returnUrl);
        assertNotNull(authorization);
        assertNotNull(authorization.getId());
        assertNotNull(authorization.getRedirectUrl());
    }
}
