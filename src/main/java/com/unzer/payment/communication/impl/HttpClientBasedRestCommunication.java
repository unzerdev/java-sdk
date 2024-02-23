package com.unzer.payment.communication.impl;

import com.unzer.payment.communication.AbstractUnzerRestCommunication;
import com.unzer.payment.communication.HttpCommunicationException;
import com.unzer.payment.communication.UnzerHttpRequest;
import com.unzer.payment.communication.UnzerHttpResponse;
import lombok.extern.log4j.Log4j2;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Locale;

/**
 * Reference implementation of the UnzerRestCommunication, based on Apache HttpClient.
 */
@Log4j2
public class HttpClientBasedRestCommunication extends AbstractUnzerRestCommunication {
    public HttpClientBasedRestCommunication() {
        this(null, null);
    }

    public HttpClientBasedRestCommunication(Locale locale, String clientIp) {
        super(locale, clientIp);
    }

    public HttpClientBasedRestCommunication(Locale locale) {
        this(locale, null);
    }

    @Override
    protected UnzerHttpRequest createRequest(String url, UnzerHttpRequest.UnzerHttpMethod method) {
        return new HttpClientBasedHttpRequest(url, method);
    }

    @Override
    protected void logRequest(UnzerHttpRequest request) {
        log.debug("Sending request {}", request.toString());
    }

    @Override
    protected void logRequestBody(String body) {
        log.debug(body);
    }

    @Override
    protected UnzerHttpResponse doExecute(UnzerHttpRequest request) throws HttpCommunicationException {

        if (!(request instanceof HttpClientBasedHttpRequest)) {
            throw new IllegalArgumentException(
                    "Request is not an instance of HttpClientBasedHttpRequest");
        }

        try {
            request.getURI();
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException("Request URI is not correct", e);
        }

        CloseableHttpResponse response = null;
        try (CloseableHttpClient client = HttpClients.custom().useSystemProperties().build()) {
            response = client.execute(((HttpClientBasedHttpRequest) request).getRequest());
            return new UnzerHttpResponse(
                    request,
                    EntityUtils.toString(response.getEntity()),
                    response.getCode()
            );
        } catch (IOException | ParseException e) {
            try {
                throw new HttpCommunicationException(
                        String.format(
                                "Error communicating to %s: Detail: %s",
                                request.getURI().toString(),
                                e.getMessage()
                        )
                );
            } catch (URISyntaxException ex) {
                // never raises, because uri validation happens in the beginning of this method
                throw new RuntimeException(ex);
            }
        } finally {
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    log.debug("Closing the http stream threw an error: " + e.getMessage(), e);
                }
            }
        }
    }

    @Override
    protected void logResponse(UnzerHttpResponse response) {
        log.debug(
                "Received response {} {}\nHTTP status {}\n{}",
                response.getRequest().getMethod(),
                response.getRequestURI(),
                response.getStatusCode(),
                response.getContent());
    }
}
