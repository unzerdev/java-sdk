package com.unzer.payment;

public class Linkpay extends BasePaypage {
    private String version;

    private String alias;

    private String expires;

    private String intention;

    private String paymentReference;

    private String orderIdRequired;

    private String invoiceIdRequired;

    private String oneTimeUse;

    private String successfullyProcessed;

    /**
     * @return the alias
     */
    public String getAlias() {
        return alias;
    }

    /**
     * @param alias the alias to set
     */
    public void setAlias(String alias) {
        this.alias = alias;
    }

    /**
     * @return the version
     */
    public String getVersion() {
        return version;
    }

    /**
     * @param version the version to set
     */
    public void setVersion(String version) {
        this.version = version;
    }

    /**
     * @return the expires
     */
    public String getExpires() {
        return expires;
    }

    /**
     * @param expires the expires to set
     */
    public void setExpires(String expires) {
        this.expires = expires;
    }


    /**
     * @return the intention
     */
    public String getIntention() {
        return intention;
    }

    /**
     * @param intention the intention to set
     */
    public void setIntention(String intention) {
        this.intention = intention;
    }

    public String getOrderIdRequired() {
        return orderIdRequired;
    }

    public void setOrderIdRequired(String orderIdRequired) {
        this.orderIdRequired = orderIdRequired;
    }

    public String getInvoiceIdRequired() {
        return invoiceIdRequired;
    }

    public void setInvoiceIdRequired(String invoiceIdRequired) {
        this.invoiceIdRequired = invoiceIdRequired;
    }

    public String getOneTimeUse() {
        return oneTimeUse;
    }

    public void setOneTimeUse(String oneTimeUse) {
        this.oneTimeUse = oneTimeUse;
    }

    public String getSuccessfullyProcessed() {
        return successfullyProcessed;
    }

    public void setSuccessfullyProcessed(String successfullyProcessed) {
        this.successfullyProcessed = successfullyProcessed;
    }

    public String getPaymentReference() {
        return paymentReference;
    }

    public void setPaymentReference(String paymentReference) {
        this.paymentReference = paymentReference;
    }

    @Override
    protected String getResourceUrl() {
        return "/v1/linkpay/<action>/<resourceId>";
    }
}
