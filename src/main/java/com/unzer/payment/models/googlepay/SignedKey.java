package com.unzer.payment.models.googlepay;

public class SignedKey {
  private String keyExpiration;
  private String keyValue;

  public String getKeyValue() {
    return keyValue;
  }

  public SignedKey setKeyValue(String keyValue) {
    this.keyValue = keyValue;
    return this;
  }

  public String getKeyExpiration() {
    return keyExpiration;
  }

  public SignedKey setKeyExpiration(String keyExpiration) {
    this.keyExpiration = keyExpiration;
    return this;
  }
}
