package com.unzer.payment.communication.json;

public class JsonPayout extends JsonInitPayment implements JsonObject {

    public JsonPayout() {
        super();
    }

    public JsonPayout(JsonInitPayment json) {
        super();
        this.setAmount(json.getAmount());
        this.setCard3ds(json.getCard3ds());
        this.setCurrency(json.getCurrency());
        this.setOrderId(json.getOrderId());
        this.setResources(json.getResources());
        this.setReturnUrl(json.getReturnUrl());
        this.setDate(json.getDate());
        this.setIsError(json.getIsError());
        this.setIsPending(json.getIsPending());
        this.setIsSuccess(json.getIsSuccess());
        this.setMessage(json.getMessage());
        this.setProcessing(json.getProcessing());
        this.setRedirectUrl(json.getRedirectUrl());
        this.setId(json.getId());
        this.setPaymentReference(json.getPaymentReference());
    }

}
