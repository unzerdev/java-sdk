package com.unzer.payment.business.paymenttypes;

import com.unzer.payment.Charge;
import com.unzer.payment.business.AbstractPaymentTest;
import com.unzer.payment.communication.HttpCommunicationException;
import com.unzer.payment.paymenttypes.PaylaterInvoice;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Currency;

import static org.junit.jupiter.api.Assertions.assertNotNull;


public class PaylaterInvoiceTest extends AbstractPaymentTest {

    @Test
    public void testCreatePaylaterType() throws HttpCommunicationException {
        PaylaterInvoice paylaterInvoice = getUnzer().createPaymentType(new PaylaterInvoice());
        assertNotNull(paylaterInvoice.getId());
    }

    @Test
    public void testChargePaylaterType() throws HttpCommunicationException {
        PaylaterInvoice paylaterInvoice = getUnzer().createPaymentType(new PaylaterInvoice());

        Charge charge = getUnzer().charge(BigDecimal.ONE,
                Currency.getInstance("EUR"),
                paylaterInvoice
        );

        assertNotNull(charge);
        assertNotNull(charge.getId());
        assertNotNull(charge.getRedirectUrl());
    }

    @Test
    public void testFetchPaylaterType() throws HttpCommunicationException {
        PaylaterInvoice paylaterInvoice = getUnzer().createPaymentType(new PaylaterInvoice());
        assertNotNull(paylaterInvoice.getId());

        PaylaterInvoice fetchedPaylaterInvoice = (PaylaterInvoice) getUnzer().fetchPaymentType(paylaterInvoice.getId());
        assertNotNull(fetchedPaylaterInvoice.getId());
    }
}
