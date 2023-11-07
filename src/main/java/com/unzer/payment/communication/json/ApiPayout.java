package com.unzer.payment.communication.json;

public class ApiPayout extends ApiInitPayment implements ApiObject {

    public ApiPayout() {
        super();
    }

    public ApiPayout(ApiInitPayment json) {
        super();
        this.setAmount(json.getAmount());
        this.setCard3ds(json.getCard3ds());
        this.setCurrency(json.getCurrency());
        this.setOrderId(json.getOrderId());
        this.setResources(json.getResources());
        this.setReturnUrl(json.getReturnUrl());
        this.setDate(json.getDate());
        this.setError(json.getError());
        this.setPending(json.getPending());
        this.setSuccess(json.getSuccess());
        this.setMessage(json.getMessage());
        this.setProcessing(json.getProcessing());
        this.setRedirectUrl(json.getRedirectUrl());
        this.setId(json.getId());
        this.setPaymentReference(json.getPaymentReference());
    }

}
