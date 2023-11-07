package com.unzer.payment.communication.json;

public class ApiAuthorization extends ApiInitPayment implements ApiObject {

    public ApiAuthorization() {
        super();
    }

    public ApiAuthorization(ApiInitPayment json) {
        super();
        this.setAmount(json.getAmount());
        this.setCard3ds(json.getCard3ds());
        this.setCurrency(json.getCurrency());
        this.setOrderId(json.getOrderId());
        this.setInvoiceId(json.getInvoiceId());
        this.setResources(json.getResources());
        this.setReturnUrl(json.getReturnUrl());
        this.setDate(json.getDate());
        this.setError(json.getError());
        this.setPending(json.getPending());
        this.setSuccess(json.getSuccess());
        this.setResumed(json.getResumed());
        this.setMessage(json.getMessage());
        this.setProcessing(json.getProcessing());
        this.setRedirectUrl(json.getRedirectUrl());
        this.setPaymentReference(json.getPaymentReference());
        this.setId(json.getId());
        this.setAdditionalTransactionData(json.getAdditionalTransactionData());
    }


}
