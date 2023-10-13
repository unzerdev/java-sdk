package com.unzer.payment.communication;


import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

public class MockUnzerHttpRequest implements UnzerHttpRequest {

    private final String uri;
    private final UnzerHttpMethod method;
    Map<String, String> headerMap = new HashMap<String, String>();
    String content, contentEncoding;

    MockUnzerHttpRequest(String uri, UnzerHttpMethod method) {
        this.uri = uri;
        this.method = method;
    }

    @Override
    public void addHeader(String header, String value) {
        headerMap.put(header, value);
    }

    @Override
    public URI getURI() {
        try {
            return new URI(uri);
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void setContent(String content, String encoding) {
        this.content = content;
        this.contentEncoding = encoding;
    }

    @Override
    public UnzerHttpMethod getMethod() {
        return method;
    }

}
