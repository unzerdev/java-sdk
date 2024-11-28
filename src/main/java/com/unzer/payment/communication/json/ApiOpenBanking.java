package com.unzer.payment.communication.json;

public class ApiOpenBanking extends ApiIdObject implements ApiObject {
    private String ibanCountry;

    public String getIbanCountry() {
        return ibanCountry;
    }
}