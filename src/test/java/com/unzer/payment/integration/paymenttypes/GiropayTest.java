package com.unzer.payment.integration.paymenttypes;


import static com.unzer.payment.util.Url.unsafeUrl;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.unzer.payment.Charge;
import com.unzer.payment.business.AbstractPaymentTest;
import com.unzer.payment.paymenttypes.Giropay;
import java.math.BigDecimal;
import java.util.Currency;
import org.junit.jupiter.api.Test;

public class GiropayTest extends AbstractPaymentTest {

    @Test
    public void testCreateGiropayManatoryType() {
        Giropay giropay = new Giropay();
        giropay = getUnzer().createPaymentType(giropay);
        assertNotNull(giropay.getId());
    }

    @Test
    public void testChargeGiropayType() {
        Giropay giropay = getUnzer().createPaymentType(getGiropay());
        Charge charge = giropay.charge(BigDecimal.ONE, Currency.getInstance("EUR"), unsafeUrl("https://www.unzer.com"));
        assertNotNull(charge);
        assertNotNull(charge.getId());
        assertNotNull(charge.getRedirectUrl());
    }

    @Test
    public void testFetchGiropayType() {
        Giropay giropay = getUnzer().createPaymentType(getGiropay());
        assertNotNull(giropay.getId());
        Giropay fetchedGiropay = (Giropay) getUnzer().fetchPaymentType(giropay.getId());
        assertNotNull(fetchedGiropay.getId());
    }

    private Giropay getGiropay() {
        return new Giropay();
    }

}
