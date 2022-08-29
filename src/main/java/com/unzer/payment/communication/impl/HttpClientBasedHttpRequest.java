package com.unzer.payment.communication.impl;

import com.unzer.payment.communication.UnzerHttpRequest;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.client.methods.*;
import org.apache.http.entity.StringEntity;

import java.net.URI;

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

    protected HttpUriRequest request;
    protected UnzerHttpMethod method;

    /**
     * Creates a {@code HttpClientBasedHttpRequest} wrapping a
     * {@code HttpUriRequest} defined by the given {@code UnzerHttpMethod}.
     *
	 * @param uri
	 *            - the RUI of the request
	 * @param method
	 *            - the {@code UnzerHttpMethod} representing one of
	 *            {@code HttpGet}, {@code HttpPost}, {@code HttpPut},
	 *            {@code HttpDelete}
     */
    public HttpClientBasedHttpRequest(String uri, UnzerHttpMethod method) {
        this.method = method;
        request = createRequestForMethod(uri, method);
    }

    private HttpUriRequest createRequestForMethod(String url, UnzerHttpMethod method) {
        if (UnzerHttpMethod.GET.equals(method)) {
            return new HttpGet(url);
        } else if (UnzerHttpMethod.POST.equals(method)) {
            return new HttpPost(url);
        } else if (UnzerHttpMethod.PUT.equals(method)) {
            return new HttpPut(url);
        } else if (UnzerHttpMethod.DELETE.equals(method)) {
            return new HttpDelete(url);
        } else {
            throw new IllegalArgumentException("Unsupported HttpMethod given " + method);
        }
    }

    @Override
    public void addHeader(String header, String value) {
        this.request.addHeader(header, value);
    }

    /**
	 * Returns the wrapped {@code HttpUriRequest} to be passed to the {@code HttpClient} within the {@code HttpClientBasedRestCommunication} implementation. 
	 * @return - the the wrapped {@code HttpUriRequest}  
     */
    public HttpUriRequest getRequest() {
        return request;
    }

    @Override
    public URI getURI() {
        return request.getURI();
    }

    @Override
    public void setContent(String content, String encoding) {
        if (request instanceof HttpEntityEnclosingRequest) {
            HttpEntity entity = new StringEntity(content, encoding);
            ((HttpEntityEnclosingRequest) request).setEntity(entity);
        }
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
