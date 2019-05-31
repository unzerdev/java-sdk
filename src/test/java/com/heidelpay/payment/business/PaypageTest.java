package com.heidelpay.payment.business;

import static org.junit.Assert.assertNotNull;

import java.net.MalformedURLException;

import org.junit.Test;

import com.heidelpay.payment.Paypage;
import com.heidelpay.payment.communication.HttpCommunicationException;

public class PaypageTest extends AbstractPaymentTest {
	
	@Test
	public void testPaypage() throws MalformedURLException, HttpCommunicationException {
		Paypage payage = getHeidelpay().paypage(getPaypage());
		assertNotNull(payage);
		assertNotNull(payage.getId());
	}

	private Paypage getPaypage() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
