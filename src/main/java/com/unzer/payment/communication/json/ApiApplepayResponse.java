package com.unzer.payment.communication.json;

import java.math.BigDecimal;

/**
 * Apple Pay business object
 *
 * @author Unzer E-Com GmbH
 */
public class ApiApplepayResponse extends ApiIdObject implements ApiObject {
    private String applicationPrimaryAccountNumber;
    private String applicationExpirationDate;
    private String currencyCode;
    private BigDecimal transactionAmount;

    public String getApplicationPrimaryAccountNumber() {
        return applicationPrimaryAccountNumber;
    }

    public void setApplicationPrimaryAccountNumber(String applicationPrimaryAccountNumber) {
        this.applicationPrimaryAccountNumber = applicationPrimaryAccountNumber;
    }

    public String getApplicationExpirationDate() {
        return applicationExpirationDate;
    }

    public void setApplicationExpirationDate(String applicationExpirationDate) {
        this.applicationExpirationDate = applicationExpirationDate;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public BigDecimal getTransactionAmount() {
        return transactionAmount;
    }

    public void setTransactionAmount(BigDecimal transactionAmount) {
        this.transactionAmount = transactionAmount;
    }
}
