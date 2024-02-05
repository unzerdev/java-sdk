package com.unzer.payment.models.googlepay;

import lombok.Data;

@Data
public class SignedMessage {
    private String tag;
    private String ephemeralPublicKey;
    private String encryptedMessage;
}
