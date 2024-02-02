package com.unzer.payment.models.googlepay;

import lombok.Data;

import java.util.List;

@Data
public class IntermediateSigningKey {
    private SignedKey signedKey;
    private List<String> signatures;
}
