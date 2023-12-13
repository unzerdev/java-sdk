package com.unzer.payment.models.googlepay;

import java.util.List;

public class IntermediateSigningKey {
  private SignedKey signedKey;
  private List<String> signatures;

  public SignedKey getSignedKey() {
    return signedKey;
  }

  public IntermediateSigningKey setSignedKey(SignedKey signedKey) {
    this.signedKey = signedKey;
    return this;
  }

  public List<String> getSignatures() {
    return signatures;
  }

  public IntermediateSigningKey setSignatures(List<String> signatures) {
    this.signatures = signatures;
    return this;
  }
}
