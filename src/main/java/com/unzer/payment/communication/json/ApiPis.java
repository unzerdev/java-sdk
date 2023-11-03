package com.unzer.payment.communication.json;

public class ApiPis extends ApiIdObject implements ApiObject {
    private String iban;
    private String bic;
    private String holder;

    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public String getBic() {
        return bic;
    }

    public void setBic(String bic) {
        this.bic = bic;
    }

    public String getHolder() {
        return holder;
    }

    public void setHolder(String holder) {
        this.holder = holder;
    }
}
