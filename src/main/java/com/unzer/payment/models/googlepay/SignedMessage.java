package com.unzer.payment.models.googlepay;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
public class SignedMessage {
  private String tag;
  private String ephemeralPublicKey;
  private String encryptedMessage;
}
