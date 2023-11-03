package com.unzer.payment.integration.paymenttypes;


import static com.unzer.payment.util.Url.unsafeUrl;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.unzer.payment.Charge;
import com.unzer.payment.Unzer;
import com.unzer.payment.business.AbstractPaymentTest;
import com.unzer.payment.paymenttypes.Pis;
import java.math.BigDecimal;
import java.util.Currency;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class PisTest extends AbstractPaymentTest {

    @Test
    public void testCreatePis() {
        Pis pis = new Pis();
        pis = getUnzer().createPaymentType(pis);
        assertNotNull(pis.getId());
    }

    @Test
    public void testCreatePisWithIbanBic() {
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
    public void testAuthorizeType() {
        Unzer unzer = getUnzer();
        Pis pis = unzer.createPaymentType(new Pis());
        Charge charge = unzer.charge(
            BigDecimal.ONE,
            Currency.getInstance("EUR"),
            pis.getId(),
            unsafeUrl("https://www.meinShop.de")
        );
        assertNotNull(charge);
        assertNotNull(charge.getId());
        assertNotNull(charge.getRedirectUrl());
    }

    @Test
    public void testFetchPisType() {
        Pis pis = getUnzer().createPaymentType(new Pis());
        assertNotNull(pis.getId());
        Pis fetchedPis = (Pis) getUnzer().fetchPaymentType(pis.getId());
        assertNotNull(fetchedPis.getId());
    }

    @Test
    @Disabled("AHC-3615 PIS holder not in response when doing a POST/GET")
    public void testFetchPisTypeWithHolderBicIban() {
        Pis fetchedPis = (Pis) getUnzer().fetchPaymentType("s-pis-ivt4ibypi0zk");
        assertNotNull(fetchedPis.getId());
        assertNotNull(fetchedPis.getIban());
        assertNotNull(fetchedPis.getBic());
        assertNotNull(fetchedPis.getHolder());
    }
}
