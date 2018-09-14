package com.heidelpay.payment.business.paymenttypes;

import static org.junit.Assert.assertNotNull;

import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Currency;

import org.junit.Test;

import com.heidelpay.payment.Authorization;
import com.heidelpay.payment.business.AbstractPaymentTest;
import com.heidelpay.payment.communication.HttpCommunicationException;
import com.heidelpay.payment.paymenttypes.Invoice;

public class InvoiceTest extends AbstractPaymentTest {

	@Test
	public void testCreateInvoiceManatoryType() throws HttpCommunicationException {
		Invoice invoice = (Invoice) getHeidelpay().createPaymentType(getInvoice());
		assertNotNull(invoice.getId());
	}

	@Test
	public void testAuthorizeType() throws HttpCommunicationException, MalformedURLException {
		Invoice invoice = getHeidelpay().createPaymentType(getInvoice());
		Authorization authorization = invoice.authorize(BigDecimal.ONE, Currency.getInstance("EUR"), new URL("https://www.mpay24.com"));		
		assertNotNull(authorization);
		assertNotNull(authorization.getId());
	}

	@Test
	public void testFetchInvoiceType() throws HttpCommunicationException {
		Invoice invoice = getHeidelpay().createPaymentType(getInvoice());
		assertNotNull(invoice.getId());
		Invoice fetchedInvoice = (Invoice) getHeidelpay().fetchPaymentType(invoice.getId());
		assertNotNull(fetchedInvoice.getId());
	}

	
	private Invoice getInvoice() {
		Invoice invoice = new Invoice();
		return invoice;
	}


}
