package com.unzer.payment.communication.impl;

import com.unzer.payment.communication.UnzerHttpRequest;
import org.apache.hc.client5.http.classic.methods.*;
import org.apache.hc.core5.http.ClassicHttpRequest;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.io.entity.StringEntity;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * Implementation of the {@code UnzerHttpRequest} wrapping an apache
 * {@code HttpUriRequest}. Currently supported are:
 * <ul>
 * <li>GET: mapped by {@code HttpGet}</li>
 * <li>POST: mapped by {@code HttpPost}</li>
 * <li>PUT: mapped by {@code HttpPut}</li>
 * <li>DELETE: mapped by {@code HttpDelete}</li>
 * </ul>
 */
public class HttpClientBasedHttpRequest implements UnzerHttpRequest {
    protected ClassicHttpRequest request;
    protected UnzerHttpMethod method;

    /**
     * Creates a {@code HttpClientBasedHttpRequest} wrapping a
     * {@code HttpUriRequest} defined by the given {@code UnzerHttpMethod}.
     *
     * @param uri    - the RUI of the request
     * @param method - the {@code UnzerHttpMethod} representing one of
     *               {@code HttpGet}, {@code HttpPost}, {@code HttpPut},
     *               {@code HttpDelete}
     */
    public HttpClientBasedHttpRequest(String uri, UnzerHttpMethod method) {
        this.method = method;
        request = createRequestForMethod(uri, method);
    }

    private HttpUriRequest createRequestForMethod(String url, UnzerHttpMethod method) {
        switch (method) {
            case GET:
                return new HttpGet(url);
            case POST:
                return new HttpPost(url);
            case PUT:
                return new HttpPut(url);
            case DELETE:
                return new HttpDelete(url);
            case PATCH:
                return new HttpPatch(url);
            default:
                throw new IllegalArgumentException("Unsupported HttpMethod given " + method);
        }
    }

    @Override
    public void addHeader(String header, String value) {
        this.request.addHeader(header, value);
    }

    /**
     * Returns the wrapped {@code HttpUriRequest} to be passed to the {@code HttpClient}
     * within the {@code HttpClientBasedRestCommunication} implementation.
     *
     * @return - the wrapped {@code HttpUriRequest}
     */
    public ClassicHttpRequest getRequest() {
        return request;
    }

    @Override
    public URI getURI() throws URISyntaxException {
        return request.getUri();
    }

    @Override
    public void setContent(String content, String encoding) {
        StringEntity entity = new StringEntity(
                content,
                ContentType.APPLICATION_JSON,
                encoding,
                false
        );
        request.setEntity(entity);
    }

    @Override
    public UnzerHttpMethod getMethod() {
        return this.method;
    }

    @Override
    public String toString() {
        return this.request.toString();
    }

}
