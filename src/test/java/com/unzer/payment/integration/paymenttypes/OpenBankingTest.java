package com.unzer.payment.integration.paymenttypes;

import com.unzer.payment.Charge;
import com.unzer.payment.business.AbstractPaymentTest;
import com.unzer.payment.paymenttypes.OpenBanking;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Currency;

import static com.unzer.payment.util.Types.unsafeUrl;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class OpenBankingTest extends AbstractPaymentTest {
    @Test
    public void testCreateOpenBankingType() {
        OpenBanking openBanking = new OpenBanking("DE");
        openBanking = getUnzer().createPaymentType(openBanking);
        assertNotNull(openBanking.getId());
    }

    @Test
    public void testChargeOpenBankingType() {
        OpenBanking openBanking = getUnzer().createPaymentType(new OpenBanking("DE"));
        Charge charge = getUnzer().charge(BigDecimal.ONE, Currency.getInstance("EUR"), openBanking.getId(), unsafeUrl("https://www.unzer.com"));
        assertNotNull(charge);
        assertNotNull(charge.getId());
        assertNotNull(charge.getRedirectUrl());
    }

    @Test
    public void testFetchOpenBankingType() {
        OpenBanking openBanking = getUnzer().createPaymentType(new OpenBanking("DE"));
        assertNotNull(openBanking.getId());
        OpenBanking fetchedopenBanking = (OpenBanking) getUnzer().fetchPaymentType(openBanking.getId());
        assertNotNull(fetchedopenBanking.getId());
    }
}
