package com.unzer.payment.models.googlepay;

import lombok.Data;

@Data
public class SignedKey {
    private String keyExpiration;
    private String keyValue;
}
