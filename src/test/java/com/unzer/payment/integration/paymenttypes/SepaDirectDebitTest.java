package com.unzer.payment.integration.paymenttypes;


import com.unzer.payment.Cancel;
import com.unzer.payment.Charge;
import com.unzer.payment.Unzer;
import com.unzer.payment.business.AbstractPaymentTest;
import com.unzer.payment.business.Keys;
import com.unzer.payment.paymenttypes.SepaDirectDebit;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Currency;

import static com.unzer.payment.util.Url.unsafeUrl;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class SepaDirectDebitTest extends AbstractPaymentTest {

    @Test
    public void testCreateSepaDirectDebitManatoryType() {
        SepaDirectDebit sdd = new SepaDirectDebit("DE89370400440532013000");
        sdd = getUnzer(Keys.LEGACY_PRIVATE_KEY).createPaymentType(sdd);
        assertNotNull(sdd.getId());
    }

    @Test
    public void testCreateSepaDirectDebitFullType() {
        SepaDirectDebit sddOriginal = getSepaDirectDebit();
        SepaDirectDebit sddCreated = getUnzer(Keys.LEGACY_PRIVATE_KEY).createPaymentType(sddOriginal);
        assertSddEquals(sddOriginal, sddCreated);
    }

    @Test
    public void testChargeSepaDirectDebitType() {
        Unzer unzer = getUnzer(Keys.LEGACY_PRIVATE_KEY);
        SepaDirectDebit sdd = unzer.createPaymentType(getSepaDirectDebit());
        Charge charge = sdd.charge(BigDecimal.ONE, Currency.getInstance("EUR"), unsafeUrl("https://www.unzer.com"));
        assertNotNull(charge);
        assertNotNull(charge.getId());
        assertNotNull(charge.getProcessing());
        assertNotNull(charge.getProcessing().getCreatorId());
        assertNotNull(charge.getProcessing().getIdentification());
        assertNotNull(charge.getProcessing().getTraceId());
    }

    @Test
    public void testFetchSepaDirectDebitType() {
        Unzer unzer = getUnzer(Keys.LEGACY_PRIVATE_KEY);
        SepaDirectDebit sdd = unzer.createPaymentType(getSepaDirectDebit());
        assertNotNull(sdd.getId());
        SepaDirectDebit fetchedSdd = (SepaDirectDebit) unzer.fetchPaymentType(sdd.getId());
        assertNotNull(fetchedSdd.getId());
        assertSddEquals(sdd, fetchedSdd);
    }

    @Test
    public void testCancelSepaDirectDebitType() {
        Unzer unzer = getUnzer(Keys.LEGACY_PRIVATE_KEY);
        SepaDirectDebit sdd = unzer.createPaymentType(getSepaDirectDebit());
        Charge charge = sdd.charge(BigDecimal.ONE, Currency.getInstance("EUR"), unsafeUrl("https://www.unzer.com"));
        assertNotNull(charge);
        assertNotNull(charge.getId());

        Cancel cancel = charge.cancel();
        assertNotNull(cancel);
        assertNotNull(cancel.getId());
    }

    private void assertSddEquals(SepaDirectDebit sddOriginal, SepaDirectDebit sddCreated) {
        assertNotNull(sddCreated.getId());
        assertEquals(sddOriginal.getBic(), sddCreated.getBic());
        assertEquals(sddOriginal.getHolder(), sddCreated.getHolder());
        assertEquals(sddOriginal.getIban(), sddCreated.getIban());
    }

}
