package com.unzer.payment.communication;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.net.URISyntaxException;

/**
 * Minimal represenation of a http-response.
 */
@Getter
@AllArgsConstructor
public class UnzerHttpResponse {
    private final UnzerHttpRequest request;
    private final String content;
    private final int statusCode;

    /**
     * Fail-safe method to reveal initial request URI
     *
     * @return initial request URI
     */
    public String getRequestURI() {
        try {
            return request.getURI().toString();
        } catch (URISyntaxException e) {
            return "UNKNOWN";
        }
    }
}
