package com.unzer.payment.communication;

/*-
 * #%L
 * Unzer Java SDK
 * %%
 * Copyright (C) 2020 Unzer E-Com GmbH
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

import com.unzer.payment.PaymentError;
import com.unzer.payment.PaymentException;
import com.unzer.payment.util.SDKInfo;
import org.junit.Test;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class AbstractUnzerHttpCommunicationTest {


	private String privateKey = "samplekey";
	
	@Test
	public void testApiErrorsAreTranslatedToPaymentException() throws HttpCommunicationException {
		
		PaymentException exception = null;
		MockUnzerRestCommunication rest = setupRest(errorJson(), 409);
		MockUnzerHttpRequest request = new MockUnzerHttpRequest("https://unzer.com", UnzerHttpRequest.UnzerHttpMethod.GET);
		try {
			rest.execute(request, privateKey);
		} catch(PaymentException e) {
			exception = e;
		}
		
		assertNotNull( exception );
		assertEquals("2018-09-13 22:47:35", exception.getTimestamp());
		assertEquals("https://unzer.com", exception.getUrl());
		assertEquals(new Integer(409), exception.getStatusCode());
		PaymentError error = exception.getPaymentErrorList().get(0);
		assertEquals("API.410.200.010", error.getCode());
		assertEquals("Message for the customer.", error.getCustomerMessage());
		assertEquals("Message for the merchant.", error.getMerchantMessage());
	}

	@Test
	public void testhttpGet() throws PaymentException, HttpCommunicationException {
		
		MockUnzerRestCommunication rest = setupRest(validJsonResponse(), 200);
		
		String result = rest.httpGet("https://unzer.com", privateKey);
		assertEquals( validJsonResponse(), result);
		assertLoggingHooks(rest, 200);
	}

	@Test
	public void testHttpPost() throws PaymentException, HttpCommunicationException {
		MockUnzerRestCommunication rest = setupRest(validJsonResponse(), 201);
		String result = rest.httpPost("https://unzer.com", privateKey, sampleData());
		assertEquals( validJsonResponse(), result);
		assertLoggingHooks(rest, 201);
	}

	@Test
	public void testhttpPut() throws PaymentException, HttpCommunicationException {
		MockUnzerRestCommunication rest = setupRest(validJsonResponse(), 200);
		String result = rest.httpPut("https://unzer.com", privateKey, sampleData());
		assertEquals( validJsonResponse(), result);
		assertLoggingHooks(rest, 200);
		
	}

	@Test
	public void testhttpDelete() throws PaymentException, HttpCommunicationException {
		MockUnzerRestCommunication rest = setupRest(validJsonResponse(), 200);
		String result = rest.httpDelete("https://unzer.com", privateKey);
		assertEquals( validJsonResponse(), result);
		assertLoggingHooks(rest, 200);
	}

	private void assertLoggingHooks(MockUnzerRestCommunication rest, int expectedStatus) {
		assertEquals(rest.request, rest.loggedRequest);
		assertEquals(rest.responseMockContent.trim(), rest.loggedResponse.getContent());
		assertEquals(expectedStatus, rest.loggedResponse.getStatusCode());
		if(rest.request.content != null) {
			assertEquals(rest.request.content.trim(), rest.loggedBody.trim());
		}
	}
	
	@Test
	public void testAuthAndUserAgentHeaderAreSetOnGetRequest() throws PaymentException, HttpCommunicationException {
		
		MockUnzerRestCommunication rest = setupRest(validJsonResponse(), 200);

		rest.httpGet("http://unzer.com", privateKey);

		assertUserAgentHeader(rest.request);
		assertAuthorizationHeader(rest.request);
	}

	@Test
	public void testAuthContentTypeAndUserAgentHeaderAndBodiesContentEncodingAreSetOnPostRequest() throws PaymentException, HttpCommunicationException {
		
		MockUnzerRestCommunication rest = setupRest(validJsonResponse(), 200);

		rest.httpPost("http://unzer.com", privateKey, sampleData());

		assertUserAgentHeader(rest.request);
		assertAuthorizationHeader(rest.request);
		assertContentTypeHeader(rest.request);
		assertBodiesContentEncoding(rest.request);
	}

	@Test
	public void testAuthContentTypeAndUserAgentHeaderAndBodiesContentEncodingAreSetOnPutRequest() throws PaymentException, HttpCommunicationException {
		
		MockUnzerRestCommunication rest = setupRest(validJsonResponse(), 200);

		rest.httpPut("http://unzer.com", privateKey, sampleData());

		assertUserAgentHeader(rest.request);
		assertAuthorizationHeader(rest.request);
		assertContentTypeHeader(rest.request);
		assertBodiesContentEncoding(rest.request);
	}

	@Test
	public void testAuthAndUserAgentHeaderAreSetOnDeleteRequest() throws PaymentException, HttpCommunicationException {
		
		MockUnzerRestCommunication rest = setupRest(validJsonResponse(), 200);

		rest.httpDelete("http://unzer.com", privateKey);

		assertUserAgentHeader(rest.request);
		assertAuthorizationHeader(rest.request);
		assertContentTypeHeader(rest.request);
	}

	private void assertUserAgentHeader(MockUnzerHttpRequest request) {
		assertEquals(AbstractUnzerRestCommunication.USER_AGENT_PREFIX + " - " + SDKInfo.getVersion(), request.headerMap.get("User-Agent"));
	}

	private void assertAuthorizationHeader(MockUnzerHttpRequest request) {
		assertEquals("Basic " + new String(Base64.getEncoder().encode( (privateKey + ":").getBytes())), request.headerMap.get("Authorization"));
	}
	
	private void assertContentTypeHeader(MockUnzerHttpRequest request) {
		assertEquals("application/json; charset=UTF-8".trim(), request.headerMap.get("Content-Type").trim());
	}
	
	private void assertBodiesContentEncoding(MockUnzerHttpRequest request) {
		assertEquals("UTF-8", request.contentEncoding);
	}
	
	
	private MockUnzerRestCommunication setupRest(String response, int status) {
		MockUnzerRestCommunication rest = new MockUnzerRestCommunication();
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
				"    \"url\": \"https://unzer.com\"," +
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