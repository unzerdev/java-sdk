package com.unzer.payment.communication;


import com.unzer.payment.PaymentError;
import com.unzer.payment.PaymentException;
import com.unzer.payment.util.SDKInfo;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class AbstractUnzerHttpCommunicationTest {


    private final String privateKey = "samplekey";

    @Test
    public void testApiErrorsAreTranslatedToPaymentException() throws HttpCommunicationException {

        PaymentException exception = null;
        MockUnzerRestCommunication rest = setupRest(errorJson(), 409);
        MockUnzerHttpRequest request = new MockUnzerHttpRequest("https://unzer.com", UnzerHttpRequest.UnzerHttpMethod.GET);
        try {
            rest.execute(request, privateKey, "{}");
        } catch (PaymentException e) {
            exception = e;
        }

        assertNotNull(exception);
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
        assertEquals(validJsonResponse(), result);
        assertLoggingHooks(rest, 200);
    }

    @Test
    public void testHttpPost() throws PaymentException, HttpCommunicationException {
        MockUnzerRestCommunication rest = setupRest(validJsonResponse(), 201);
        String result = rest.httpPost("https://unzer.com", privateKey, sampleData());
        assertEquals(validJsonResponse(), result);
        assertLoggingHooks(rest, 201);
    }

    @Test
    public void testhttpPut() throws PaymentException, HttpCommunicationException {
        MockUnzerRestCommunication rest = setupRest(validJsonResponse(), 200);
        String result = rest.httpPut("https://unzer.com", privateKey, sampleData());
        assertEquals(validJsonResponse(), result);
        assertLoggingHooks(rest, 200);

    }

    @Test
    public void testhttpDelete() throws PaymentException, HttpCommunicationException {
        MockUnzerRestCommunication rest = setupRest(validJsonResponse(), 200);
        String result = rest.httpDelete("https://unzer.com", privateKey);
        assertEquals(validJsonResponse(), result);
        assertLoggingHooks(rest, 200);
    }

    private void assertLoggingHooks(MockUnzerRestCommunication rest, int expectedStatus) {
        assertEquals(rest.request, rest.loggedRequest);
        assertEquals(rest.responseMockContent.trim(), rest.loggedResponse.getContent());
        assertEquals(expectedStatus, rest.loggedResponse.getStatusCode());
        if (rest.request.content != null) {
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

    @Test
    public void testClientIpHeader() throws HttpCommunicationException {
        MockUnzerRestCommunication rest = new MockUnzerRestCommunication(Locale.ITALY, "192.168.1.1");
        rest.responseMockStatus = 200;
        rest.responseMockContent = validJsonResponse();

        rest.httpGet("https://unzer.com", privateKey);

        Map<String, String> expectedHeaders = new HashMap<>();
        expectedHeaders.put("Authorization", "Basic " + new String(Base64.getEncoder().encode((privateKey + ":").getBytes())));
        expectedHeaders.put("JAVA-VERSION", System.getProperty("java.version"));
        expectedHeaders.put("User-Agent", "UnzerJava - " + SDKInfo.VERSION);
        expectedHeaders.put("CLIENTIP", "192.168.1.1");
        expectedHeaders.put("Accept-Language", "it");
        expectedHeaders.put("SDK-TYPE", "UnzerJava");
        expectedHeaders.put("SDK-VERSION", SDKInfo.VERSION);
        expectedHeaders.put("Content-Type", "application/json; charset=UTF-8");

        assertNotNull(rest.request);
        assertEquals(expectedHeaders, rest.request.headerMap);
    }private void assertUserAgentHeader(MockUnzerHttpRequest request) {
        assertEquals(AbstractUnzerRestCommunication.USER_AGENT_PREFIX + " - " + SDKInfo.VERSION, request.headerMap.get("User-Agent"));
    }

    private void assertAuthorizationHeader(MockUnzerHttpRequest request) {
        assertEquals("Basic " + new String(Base64.getEncoder().encode((privateKey + ":").getBytes())), request.headerMap.get("Authorization"));
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
