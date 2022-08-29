package com.unzer.payment.communication;

import java.net.URI;

/**
 * Abstraction for any http-request executed by the
 * {@code UnzerRestCommunication} and its basic implementation in
 * {@code AbstractUnzerRestCommunication}.
 */
public interface UnzerHttpRequest {

    void addHeader(String header, String value);

    URI getURI();

    void setContent(String content, String encoding);

    UnzerHttpMethod getMethod();

    enum UnzerHttpMethod {
        GET, POST, DELETE, PUT
    }
}
