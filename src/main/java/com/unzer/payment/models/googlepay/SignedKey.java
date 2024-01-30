package com.unzer.payment.models.googlepay;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignedKey {
  private String keyExpiration;
  private String keyValue;
}
