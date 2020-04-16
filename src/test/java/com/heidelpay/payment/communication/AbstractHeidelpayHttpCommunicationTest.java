package com.heidelpay.payment.communication;

/*-
 * #%L
 * Heidelpay Java SDK
 * %%
 * Copyright (C) 2018 Heidelpay GmbH
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

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.heidelpay.payment.PaymentError;
import com.heidelpay.payment.PaymentException;
import com.heidelpay.payment.communication.HeidelpayHttpRequest.HeidelpayHttpMethod;
import com.heidelpay.payment.util.SDKInfo;

public class AbstractHeidelpayHttpCommunicationTest {


	private String privateKey = "samplekey";
	
	@Test
	public void testApiErrorsAreTranslatedToPaymentException() throws HttpCommunicationException {
		
		PaymentException exception = null;
		MockHeidelpayRestCommunication rest = setupRest(errorJson(), 409);
		MockHeidelpayHttpRequest request = new MockHeidelpayHttpRequest("https://heidelpay.com", HeidelpayHttpMethod.GET);
		try {
			rest.execute(request, privateKey);
		} catch(PaymentException e) {
			exception = e;
		}
		
		assertNotNull( exception );
		assertEquals("2018-09-13 22:47:35", exception.getTimestamp());
		assertEquals("https://heidelpay.com", exception.getUrl());
		assertEquals(new Integer(409), exception.getStatusCode());
		PaymentError error = exception.getPaymentErrorList().get(0);
		assertEquals("API.410.200.010", error.getCode());
		assertEquals("Message for the customer.", error.getCustomerMessage());
		assertEquals("Message for the merchant.", error.getMerchantMessage());
	}

	@Test
	public void testhttpGet() throws PaymentException, HttpCommunicationException {
		
		MockHeidelpayRestCommunication rest = setupRest(validJsonResponse(), 200);
		
		String result = rest.httpGet("https://heidelpay.com", privateKey);
		assertEquals( validJsonResponse(), result);
		assertLoggingHooks(rest, 200);
	}

	@Test
	public void testHttpPost() throws PaymentException, HttpCommunicationException {
		MockHeidelpayRestCommunication rest = setupRest(validJsonResponse(), 201);
		String result = rest.httpPost("https://heidelpay.com", privateKey, sampleData());
		assertEquals( validJsonResponse(), result);
		assertLoggingHooks(rest, 201);
	}

	@Test
	public void testhttpPut() throws PaymentException, HttpCommunicationException {
		MockHeidelpayRestCommunication rest = setupRest(validJsonResponse(), 200);
		String result = rest.httpPut("https://heidelpay.com", privateKey, sampleData());
		assertEquals( validJsonResponse(), result);
		assertLoggingHooks(rest, 200);
		
	}

	@Test
	public void testhttpDelete() throws PaymentException, HttpCommunicationException {
		MockHeidelpayRestCommunication rest = setupRest(validJsonResponse(), 200);
		String result = rest.httpDelete("https://heidelpay.com", privateKey);
		assertEquals( validJsonResponse(), result);
		assertLoggingHooks(rest, 200);
	}

	private void assertLoggingHooks(MockHeidelpayRestCommunication rest, int expectedStatus) {
		assertEquals(rest.request, rest.loggedRequest);
		assertEquals(rest.responseMockContent.trim(), rest.loggedResponse.getContent());
		assertEquals(expectedStatus, rest.loggedResponse.getStatusCode());
		if(rest.request.content != null) {
			assertEquals(rest.request.content.trim(), rest.loggedBody.trim());
		}
	}
	
	@Test
	public void testAuthAndUserAgentHeaderAreSetOnGetRequest() throws PaymentException, HttpCommunicationException {
		
		MockHeidelpayRestCommunication rest = setupRest(validJsonResponse(), 200);

		rest.httpGet("http://heidelpay.com", privateKey);

		assertUserAgentHeader(rest.request);
		assertAuthorizationHeader(rest.request);
	}

	@Test
	public void testAuthContentTypeAndUserAgentHeaderAndBodiesContentEncodingAreSetOnPostRequest() throws PaymentException, HttpCommunicationException {
		
		MockHeidelpayRestCommunication rest = setupRest(validJsonResponse(), 200);

		rest.httpPost("http://heidelpay.com", privateKey, sampleData());

		assertUserAgentHeader(rest.request);
		assertAuthorizationHeader(rest.request);
		assertContentTypeHeader(rest.request);
		assertBodiesContentEncoding(rest.request);
	}

	@Test
	public void testAuthContentTypeAndUserAgentHeaderAndBodiesContentEncodingAreSetOnPutRequest() throws PaymentException, HttpCommunicationException {
		
		MockHeidelpayRestCommunication rest = setupRest(validJsonResponse(), 200);

		rest.httpPut("http://heidelpay.com", privateKey, sampleData());

		assertUserAgentHeader(rest.request);
		assertAuthorizationHeader(rest.request);
		assertContentTypeHeader(rest.request);
		assertBodiesContentEncoding(rest.request);
	}

	@Test
	public void testAuthAndUserAgentHeaderAreSetOnDeleteRequest() throws PaymentException, HttpCommunicationException {
		
		MockHeidelpayRestCommunication rest = setupRest(validJsonResponse(), 200);

		rest.httpDelete("http://heidelpay.com", privateKey);

		assertUserAgentHeader(rest.request);
		assertAuthorizationHeader(rest.request);
		assertContentTypeHeader(rest.request);
	}

	private void assertUserAgentHeader(MockHeidelpayHttpRequest request) {
		assertEquals(AbstractHeidelpayRestCommunication.USER_AGENT_PREFIX + " - " + SDKInfo.getVersion(), request.headerMap.get("User-Agent"));
	}

	private void assertAuthorizationHeader(MockHeidelpayHttpRequest request) {
		assertEquals("Basic " + new String(Base64.getEncoder().encode( (privateKey + ":").getBytes())), request.headerMap.get("Authorization"));
	}
	
	private void assertContentTypeHeader(MockHeidelpayHttpRequest request) {
		assertEquals("application/json; charset=UTF-8".trim(), request.headerMap.get("Content-Type").trim());
	}
	
	private void assertBodiesContentEncoding(MockHeidelpayHttpRequest request) {
		assertEquals("UTF-8", request.contentEncoding);
	}
	
	
	private MockHeidelpayRestCommunication setupRest(String response, int status) {
		MockHeidelpayRestCommunication rest = new MockHeidelpayRestCommunication();
		rest.responseMockStatus = status;
		rest.responseMockContent = response;
		
		return rest;
	}
	
	private String validJsonResponse() {
		return "{\"key\": \"value\"}";
	}
	
	private Map<String, String> sampleData() {

		Map<String, String> data = new HashMap<String, String>();
		data.put("firstname", "Mary Foo");
		data.put("lastname", "Bar");

		return data;
	}
	
	private String errorJson() {
		return "{" + 
				"    \"url\": \"https://heidelpay.com\"," + 
				"    \"timestamp\": \"2018-09-13 22:47:35\"," + 
				"    \"errors\": [" + 
				"        {" + 
				"            \"code\": \"API.410.200.010\"," + 
				"            \"customerMessage\": \"Message for the customer.\"," + 
				"			 \"merchantMessage\": \"Message for the merchant.\"" + 
				"        }" + 
				"    ]" + 
				"}";
	}
	
}
