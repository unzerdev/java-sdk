package com.unzer.payment.integration.paymenttypes;


import com.unzer.payment.Charge;
import com.unzer.payment.Unzer;
import com.unzer.payment.business.AbstractPaymentTest;
import com.unzer.payment.paymenttypes.Invoice;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Currency;

import static com.unzer.payment.business.Keys.LEGACY_PRIVATE_KEY;
import static com.unzer.payment.util.Types.unsafeUrl;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class InvoiceTest extends AbstractPaymentTest {

    @Test
    void testCreateInvoiceMandatoryType() {
        Invoice invoice = getUnzer(LEGACY_PRIVATE_KEY).createPaymentType(new Invoice());
        assertNotNull(invoice.getId());
    }

    @Test
    void testChargeType() {
        Invoice invoice = getUnzer(LEGACY_PRIVATE_KEY).createPaymentType(new Invoice());
        Charge charge = invoice.charge(BigDecimal.ONE, Currency.getInstance("EUR"), unsafeUrl("https://www.meinShop.de"));
        assertNotNull(charge);
        assertNotNull(charge.getId());
    }

    @Test
    void testFetchInvoiceType() {
        Unzer unzer = getUnzer(LEGACY_PRIVATE_KEY);
        Invoice invoice = unzer.createPaymentType(new Invoice());
        assertNotNull(invoice.getId());
        Invoice fetchedInvoice = (Invoice) unzer.fetchPaymentType(invoice.getId());
        assertNotNull(fetchedInvoice.getId());
    }


}
