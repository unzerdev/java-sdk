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
import com.heidelpay.payment.paymenttypes.Prepayment;

public class PrepaymentTest extends AbstractPaymentTest {

	@Test
	public void testCreatePrepaymentManatoryType() throws HttpCommunicationException {
		Prepayment prepayment = new Prepayment();
		prepayment = getHeidelpay().createPaymentType(prepayment);
		assertNotNull(prepayment.getId());
	}

	@Test
	public void testAuthorizeSddType() throws HttpCommunicationException, MalformedURLException {
		Prepayment prepayment = getHeidelpay().createPaymentType(getPrepayment());
		Authorization authorization = prepayment.authorize(BigDecimal.ONE, Currency.getInstance("EUR"), new URL("https://www.mpay24.com"));		
		assertNotNull(authorization);
		assertNotNull(authorization.getId());
	}

	@Test
	public void testFetchPrepaymentType() throws HttpCommunicationException {
		Prepayment prepayment = getHeidelpay().createPaymentType(getPrepayment());
		assertNotNull(prepayment.getId());
		Prepayment fetchedPrepayment = (Prepayment) getHeidelpay().fetchPaymentType(prepayment.getId());
		assertNotNull(fetchedPrepayment.getId());
	}

	
	private Prepayment getPrepayment() {
		Prepayment prepayment = new Prepayment();
		return prepayment;
	}


}
