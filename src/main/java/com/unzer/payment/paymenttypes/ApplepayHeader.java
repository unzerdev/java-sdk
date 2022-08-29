package com.unzer.payment.paymenttypes;

public class ApplepayHeader {
    private String ephemeralPublicKey;
    private String publicKeyHash;
    private String transactionId;

    public ApplepayHeader() {
    }

    public ApplepayHeader(String ephemeralPublicKey, String publicKeyHash, String transactionId) {
        this.ephemeralPublicKey = ephemeralPublicKey;
        this.publicKeyHash = publicKeyHash;
        this.transactionId = transactionId;
    }

    public String getEphemeralPublicKey() {
        return ephemeralPublicKey;
    }

    public void setEphemeralPublicKey(String ephemeralPublicKey) {
        this.ephemeralPublicKey = ephemeralPublicKey;
    }

    public String getPublicKeyHash() {
        return publicKeyHash;
    }

    public void setPublicKeyHash(String publicKeyHash) {
        this.publicKeyHash = publicKeyHash;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

}
