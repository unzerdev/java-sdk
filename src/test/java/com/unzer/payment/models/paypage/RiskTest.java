package com.unzer.payment.models.paypage;

import com.unzer.payment.communication.JsonParser;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RiskTest {

    private final JsonParser jsonParser = new JsonParser();

    @Test
    void defaultRiskOmitsConfirmedAmount() {
        Risk risk = new Risk();

        assertNull(risk.getConfirmedAmount());
        assertFalse(jsonParser.toJson(risk).contains("confirmedAmount"),
                "confirmedAmount must not be serialized when it was never set");
    }

    @ParameterizedTest
    @ValueSource(doubles = {0.0, 100.0, -5.0})
    void confirmedAmountIsSerializedWhenSet(double amount) {
        Risk risk = new Risk();
        risk.setConfirmedAmount(amount);

        assertTrue(jsonParser.toJson(risk).contains("\"confirmedAmount\": " + amount),
                "confirmedAmount must be serialized once explicitly set, including 0.0");
    }
}
