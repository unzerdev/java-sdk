package com.heidelpay.payment.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

import com.heidelpay.payment.Charge;
import com.heidelpay.payment.Heidelpay;
import com.heidelpay.payment.PaymentError;
import com.heidelpay.payment.PaymentException;
import com.heidelpay.payment.communication.HeidelpayRestCommunication;
import com.heidelpay.payment.communication.HttpCommunicationException;
import com.heidelpay.payment.communication.HttpCommunicationMockUtils;

public class PaymentServiceTest {

	
	public PaymentService setUpPaymentService(String json, int status) {
		HeidelpayRestCommunication rest = HttpCommunicationMockUtils.withFixedResponse(json, status);
		Heidelpay heidelpay = new Heidelpay(rest, "anykey");
		return new PaymentService(heidelpay, rest);
	}
	
	@Test
	public void testChargeInCaseOfCoreException() throws HttpCommunicationException {
		PaymentService paymentService = setUpPaymentService(errorJson(), 500);
		PaymentException exception = null;
		try {
			paymentService.charge(new Charge());	
		} catch(PaymentException e) {
			exception = e;
		}
		
		assertNotNull(exception);
		assertEquals(1,exception.getPaymentErrorList().size());
		PaymentError error = exception.getPaymentErrorList().get(0);
		assertEquals("COR.400.100.101", error.getCode());
		assertEquals("Address untraceable", error.getMerchantMessage());
		assertEquals("The provided address is invalid. Please check your input and try agian.", error.getCustomerMessage());
	}
	
	private String errorJson() {
		return "{" + 
				"  \"id\" : \"s-err-f2ea241e5e8e4eb3b1513fab12c\"," + 
				"  \"url\" : \"https://api.heidelpay.com/v1/payments/charges\"," + 
				"  \"timestamp\" : \"2019-01-09 15:42:24\"," + 
				"  \"errors\" : [ {\r\n" + 
				"    \"code\" : \"COR.400.100.101\"," + 
				"    \"merchantMessage\" : \"Address untraceable\",\r\n" + 
				"    \"customerMessage\" : \"The provided address is invalid. Please check your input and try agian.\"," + 
				"    \"status\" : {" + 
				"      \"successful\" : false," + 
				"      \"processing\" : false," + 
				"      \"pending\" : false" + 
				"    }" + 
				"  } ]" + 
				"}";
	}
	
}
