package com.unzer.payment.integration.paymenttypes;

import com.unzer.payment.Charge;
import com.unzer.payment.business.AbstractPaymentTest;
import com.unzer.payment.paymenttypes.Twint;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Currency;

import static com.unzer.payment.util.Types.unsafeUrl;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class TwintTest extends AbstractPaymentTest {

    @Test
    public void testCreateTwintManatoryType() {
        Twint twint = new Twint();
        twint = getUnzer().createPaymentType(twint);
        assertNotNull(twint.getId());
    }

    @Test
    public void testChargeTwintType() {
        Twint twint = getUnzer().createPaymentType(new Twint());
        Charge charge = getUnzer().charge(BigDecimal.ONE, Currency.getInstance("CHF"), twint.getId(), unsafeUrl("https://www.unzer.com"));
        assertNotNull(charge);
        assertNotNull(charge.getId());
        assertNotNull(charge.getRedirectUrl());
    }

    @Test
    public void testFetchTwintType() {
        Twint twint = getUnzer().createPaymentType(new Twint());
        assertNotNull(twint.getId());
        Twint fetchedTwint = (Twint) getUnzer().fetchPaymentType(twint.getId());
        assertNotNull(fetchedTwint.getId());
    }
}
