package com.unzer.payment.integration.paymenttypes;


import static com.unzer.payment.business.Keys.LEGACY_PRIVATE_KEY;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.unzer.payment.Charge;
import com.unzer.payment.Unzer;
import com.unzer.payment.business.AbstractPaymentTest;
import com.unzer.payment.communication.HttpCommunicationException;
import com.unzer.payment.paymenttypes.Invoice;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Currency;
import org.junit.jupiter.api.Test;

public class InvoiceTest extends AbstractPaymentTest {

    @Test
    public void testCreateInvoiceMandatoryType() throws HttpCommunicationException {
        Invoice invoice = getUnzer(LEGACY_PRIVATE_KEY).createPaymentType(new Invoice());
        assertNotNull(invoice.getId());
    }

    @Test
    public void testChargeType() throws HttpCommunicationException, MalformedURLException {
        Invoice invoice = getUnzer(LEGACY_PRIVATE_KEY).createPaymentType(new Invoice());
        Charge charge = invoice.charge(BigDecimal.ONE, Currency.getInstance("EUR"), new URL("https://www.meinShop.de"));
        assertNotNull(charge);
        assertNotNull(charge.getId());
    }

    @Test
    public void testFetchInvoiceType() throws HttpCommunicationException {
        Unzer unzer = getUnzer(LEGACY_PRIVATE_KEY);
		Invoice invoice = unzer.createPaymentType(new Invoice());
        assertNotNull(invoice.getId());
        Invoice fetchedInvoice = (Invoice) unzer.fetchPaymentType(invoice.getId());
        assertNotNull(fetchedInvoice.getId());
    }



}
