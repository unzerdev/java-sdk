package com.unzer.payment.communication.json;

import com.unzer.payment.Message;

public class JsonMessage implements Message {
    private String code;
    private String customer;
    private String merchant;

    @Override
    public int hashCode() {
        int result = getCode() != null ? getCode().hashCode() : 0;
        result = 31 * result + (getCustomer() != null ? getCustomer().hashCode() : 0);
        result = 31 * result + (getMerchant() != null ? getMerchant().hashCode() : 0);
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

        JsonMessage that = (JsonMessage) o;

        if (getCode() != null ? !getCode().equals(that.getCode()) : that.getCode() != null) {
            return false;
        }
        if (getCustomer() != null ? !getCustomer().equals(that.getCustomer()) :
                that.getCustomer() != null) {
            return false;
        }
        return getMerchant() != null ? getMerchant().equals(that.getMerchant()) :
                that.getMerchant() == null;
    }

    public String getCode() {
        return code;
    }

    public JsonMessage setCode(String code) {
        this.code = code;
        return this;
    }

    public String getCustomer() {
        return customer;
    }

    public JsonMessage setCustomer(String customer) {
        this.customer = customer;
        return this;
    }

    public String getMerchant() {
        return merchant;
    }

    public JsonMessage setMerchant(String merchant) {
        this.merchant = merchant;
        return this;
    }

}
