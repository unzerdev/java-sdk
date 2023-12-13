package com.unzer.payment.models.googlepay;

public class SignedMessage {
  private String tag;
  private String ephemeralPublicKey;
  private String encryptedMessage;

  public String getTag() {
    return tag;
  }

  public SignedMessage setTag(String tag) {
    this.tag = tag;
    return this;
  }

  public String getEphemeralPublicKey() {
    return ephemeralPublicKey;
  }

  public SignedMessage setEphemeralPublicKey(String ephemeralPublicKey) {
    this.ephemeralPublicKey = ephemeralPublicKey;
    return this;
  }

  public String getEncryptedMessage() {
    return encryptedMessage;
  }

  public SignedMessage setEncryptedMessage(String encryptedMessage) {
    this.encryptedMessage = encryptedMessage;
    return this;
  }
}
