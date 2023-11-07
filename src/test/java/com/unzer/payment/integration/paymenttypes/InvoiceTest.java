package com.unzer.payment.integration.paymenttypes;


import com.unzer.payment.Charge;
import com.unzer.payment.Unzer;
import com.unzer.payment.business.AbstractPaymentTest;
import com.unzer.payment.paymenttypes.Invoice;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Currency;

import static com.unzer.payment.business.Keys.LEGACY_PRIVATE_KEY;
import static com.unzer.payment.util.Url.unsafeUrl;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class InvoiceTest extends AbstractPaymentTest {

    @Test
    public void testCreateInvoiceMandatoryType() {
        Invoice invoice = getUnzer(LEGACY_PRIVATE_KEY).createPaymentType(new Invoice());
        assertNotNull(invoice.getId());
    }

    @Test
    public void testChargeType() {
        Invoice invoice = getUnzer(LEGACY_PRIVATE_KEY).createPaymentType(new Invoice());
        Charge charge = invoice.charge(BigDecimal.ONE, Currency.getInstance("EUR"), unsafeUrl("https://www.meinShop.de"));
        assertNotNull(charge);
        assertNotNull(charge.getId());
    }

    @Test
    public void testFetchInvoiceType() {
        Unzer unzer = getUnzer(LEGACY_PRIVATE_KEY);
        Invoice invoice = unzer.createPaymentType(new Invoice());
        assertNotNull(invoice.getId());
        Invoice fetchedInvoice = (Invoice) unzer.fetchPaymentType(invoice.getId());
        assertNotNull(fetchedInvoice.getId());
    }


}
