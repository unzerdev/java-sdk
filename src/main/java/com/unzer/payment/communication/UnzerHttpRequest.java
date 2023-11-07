package com.unzer.payment.communication;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * Abstraction for any http-request executed by the
 * {@code UnzerRestCommunication} and its basic implementation in
 * {@code AbstractUnzerRestCommunication}.
 */
public interface UnzerHttpRequest {

    void addHeader(String header, String value);

    @SuppressWarnings("checkstyle:AbbreviationAsWordInName")
    URI getURI() throws URISyntaxException;

    void setContent(String content, String encoding);

    UnzerHttpMethod getMethod();

    enum UnzerHttpMethod {
        GET, POST, DELETE, PUT, PATCH
    }
}
