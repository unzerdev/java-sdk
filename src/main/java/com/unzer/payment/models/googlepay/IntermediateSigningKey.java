package com.unzer.payment.models.googlepay;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class IntermediateSigningKey {
    @Setter
    private SignedKey signedKey;
    private List<String> signatures;
}
