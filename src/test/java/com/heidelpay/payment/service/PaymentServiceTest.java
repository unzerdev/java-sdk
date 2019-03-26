package com.heidelpay.payment.service;

/*-
 * #%L
 * Heidelpay Java SDK
 * %%
 * Copyright (C) 2018 - 2019 Heidelpay GmbH
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.heidelpay.payment.Charge;
import com.heidelpay.payment.Heidelpay;
import com.heidelpay.payment.PaymentError;
import com.heidelpay.payment.PaymentException;
import com.heidelpay.payment.TestData;
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
		PaymentService paymentService = setUpPaymentService(TestData.errorJson(), 500);
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
	
	@Test
	public void test200WithErrorJsonIsConvertedToPaymentException() throws HttpCommunicationException {
		PaymentService paymentService = setUpPaymentService(TestData.errorJson(), 200);
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
	
	
}
