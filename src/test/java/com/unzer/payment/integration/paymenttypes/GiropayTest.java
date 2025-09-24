package com.unzer.payment.integration.paymenttypes;


import com.unzer.payment.Charge;
import com.unzer.payment.business.AbstractPaymentTest;
import com.unzer.payment.paymenttypes.Giropay;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Currency;

import static com.unzer.payment.util.Types.unsafeUrl;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class GiropayTest extends AbstractPaymentTest {

    @Test
    void testCreateGiropayManatoryType() {
        Giropay giropay = new Giropay();
        giropay = getUnzer().createPaymentType(giropay);
        assertNotNull(giropay.getId());
    }

    @Test
    void testChargeGiropayType() {
        Giropay giropay = getUnzer().createPaymentType(getGiropay());
        Charge charge = giropay.charge(BigDecimal.ONE, Currency.getInstance("EUR"), unsafeUrl("https://www.unzer.com"));
        assertNotNull(charge);
        assertNotNull(charge.getId());
        assertNotNull(charge.getRedirectUrl());
    }

    @Test
    void testFetchGiropayType() {
        Giropay giropay = getUnzer().createPaymentType(getGiropay());
        assertNotNull(giropay.getId());
        Giropay fetchedGiropay = (Giropay) getUnzer().fetchPaymentType(giropay.getId());
        assertNotNull(fetchedGiropay.getId());
    }

    private Giropay getGiropay() {
        return new Giropay();
    }

}
