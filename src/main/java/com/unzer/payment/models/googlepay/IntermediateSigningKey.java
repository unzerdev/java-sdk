package com.unzer.payment.models.googlepay;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IntermediateSigningKey {
    private SignedKey signedKey;
    private List<String> signatures;
}
