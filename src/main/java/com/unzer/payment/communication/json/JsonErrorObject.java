package com.unzer.payment.communication.json;

import com.unzer.payment.PaymentError;

import java.util.List;

public class JsonErrorObject {
    private String url;
    private String timestamp;
    private String id;
    private List<PaymentError> errors;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public List<PaymentError> getErrors() {
        return errors;
    }

    public void setErrors(List<PaymentError> errors) {
        this.errors = errors;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
