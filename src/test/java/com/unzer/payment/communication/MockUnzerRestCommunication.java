package com.unzer.payment.communication;


import com.unzer.payment.communication.UnzerHttpRequest.UnzerHttpMethod;

import java.util.Locale;

public class MockUnzerRestCommunication extends AbstractUnzerRestCommunication {

    MockUnzerHttpRequest request;

    String responseMockContent;
    int responseMockStatus;

    UnzerHttpResponse loggedResponse;
    UnzerHttpRequest loggedRequest;
    String loggedBody;

    public MockUnzerRestCommunication() {
        this(null, null);
    }

    public MockUnzerRestCommunication(Locale locale, String clientIp) {
        super(locale, clientIp);
    }

    @Override
    protected UnzerHttpRequest createRequest(String url, UnzerHttpMethod method) {
        return new MockUnzerHttpRequest(url, method);
    }

    @Override
    protected UnzerHttpResponse doExecute(UnzerHttpRequest request) {
        this.request = (MockUnzerHttpRequest) request;
        return new UnzerHttpResponse(this.request, responseMockContent, responseMockStatus);
    }

    @Override
    protected void logRequest(UnzerHttpRequest request) {
        this.loggedRequest = request;
    }

    @Override
    protected void logRequestBody(String body) {
        this.loggedBody = body;
    }

    @Override
    protected void logResponse(UnzerHttpResponse response) {
        this.loggedResponse = response;
    }


}
