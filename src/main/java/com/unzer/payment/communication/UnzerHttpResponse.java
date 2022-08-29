package com.unzer.payment.communication;

/**
 * Minimal represenation of a http-response.
 */
public class UnzerHttpResponse {

    private String content;
    private int code;

    /**
     * Creates the {@code UnzerHttpResponse} with the given content ond http-status code.
     *
     * @param content - the content of the response. will be application/son, UTF-8 in any cases
     * @param code    - the http-status code
     */
    public UnzerHttpResponse(String content, int code) {
        this.content = content;
        this.code = code;
    }

    /**
     * @return the http-status code
     */
    public int getStatusCode() {
        return code;
    }

    /**
     * @return the content of the response. will be application/son, UTF-8 in any cases
     */
    public String getContent() {
        return this.content;
    }

}
