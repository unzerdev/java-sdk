package com.unzer.payment.communication.json;

import com.google.gson.annotations.SerializedName;

public class ApiCard extends ApiIdObject implements ApiObject {
    private String number;
    private String cvc;
    private String expiryDate;
    @SerializedName("3ds")
    private Boolean threeDs;
    private String brand;
    private String method;
    private String cardHolder;
    private String email;
    private JsonCardDetails cardDetails;

    public String getNumber() {
        return number;
    }

    public ApiCard setNumber(String number) {
        this.number = number;
        return this;
    }

    public String getCvc() {
        return cvc;
    }

    public ApiCard setCvc(String cvc) {
        this.cvc = cvc;
        return this;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public ApiCard setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
        return this;
    }

    public Boolean get3ds() {
        return threeDs;
    }

    public void set3ds(Boolean threeDs) {
        this.threeDs = threeDs;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getCardHolder() {
        return cardHolder;
    }

    public void setCardHolder(String cardHolder) {
        this.cardHolder = cardHolder;
    }

    public JsonCardDetails getCardDetails() {
        return cardDetails;
    }

    public void setCardDetails(JsonCardDetails cardDetails) {
        this.cardDetails = cardDetails;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
