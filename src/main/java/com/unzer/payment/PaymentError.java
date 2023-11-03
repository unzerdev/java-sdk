package com.unzer.payment;

import java.util.Objects;

public class PaymentError {

    private String merchantMessage;
    private String customerMessage;
    private String code;

    public PaymentError() {
        super();
    }

    public PaymentError(String merchantMessage, String customerMessage, String code) {
        super();
        this.merchantMessage = merchantMessage;
        this.customerMessage = customerMessage;
        this.code = code;
    }

    public String getMerchantMessage() {
        return merchantMessage;
    }

    public void setMerchantMessage(String merchantMessage) {
        this.merchantMessage = merchantMessage;
    }

    public String getCustomerMessage() {
        return customerMessage;
    }

    public void setCustomerMessage(String customerMessage) {
        this.customerMessage = customerMessage;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public int hashCode() {
        int result = merchantMessage != null ? merchantMessage.hashCode() : 0;
        result = 31 * result + (customerMessage != null ? customerMessage.hashCode() : 0);
        result = 31 * result + (code != null ? code.hashCode() : 0);
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        PaymentError that = (PaymentError) o;

        return Objects.equals(merchantMessage, that.merchantMessage)
                && Objects.equals(customerMessage, that.customerMessage)
                && Objects.equals(code, that.code);
    }

    @Override
    public String toString() {
        return "{merchantMessage:" + merchantMessage + ", customerMessage:" + customerMessage
                + ", code:" + code + "}";
    }
}
