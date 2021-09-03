package com.unzer.payment.service;

/*-
 * #%L
 * Unzer Java SDK
 * %%
 * Copyright (C) 2020 - today Unzer E-Com GmbH
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


import com.unzer.payment.*;
import com.unzer.payment.communication.UnzerRestCommunication;
import com.unzer.payment.communication.HttpCommunicationException;
import com.unzer.payment.communication.HttpCommunicationMockUtils;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class PaymentServiceTest {

	
	public PaymentService setUpPaymentService(String json, int status) {
		UnzerRestCommunication rest = HttpCommunicationMockUtils.withFixedResponse(json, status);
		Unzer unzer = new Unzer(rest, "anykey");
		return new PaymentService(unzer, rest);
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
