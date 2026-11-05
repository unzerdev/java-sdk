package com.unzer.payment.business.paymenttypes;

import com.unzer.payment.communication.JsonParser;
import com.unzer.payment.communication.json.ApiSepaDirectDebit;
import com.unzer.payment.paymenttypes.SepaDirectDebit;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Objects;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SepaDirectDebitTest {

    private static final String IBAN = "DE89370400440532013000";

    private static String compactJson(SepaDirectDebit sdd) {
        return new JsonParser().toJson(sdd).replaceAll("\\s", "");
    }

    private static String getResponse(String response) {
        return new Scanner(Objects.requireNonNull(
                SepaDirectDebitTest.class.getResourceAsStream("/api-response/sepa-direct-debit/" + response)))
                .useDelimiter("\\A").next();
    }

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    void recurring_flag_is_serialized_when_set(boolean recurring) {
        SepaDirectDebit sdd = new SepaDirectDebit(IBAN).recurring(recurring);

        assertTrue(compactJson(sdd).contains("\"recurring\":" + recurring));
    }

    @Test
    void recurring_fluent_method_returns_same_instance_and_sets_lombok_value() {
        SepaDirectDebit sdd = new SepaDirectDebit(IBAN);

        assertEquals(sdd, sdd.recurring(true));
        assertEquals(Boolean.TRUE, sdd.getRecurring());

        sdd.setRecurring(false);
        assertTrue(compactJson(sdd).contains("\"recurring\":false"));
    }

    @Test
    void recurring_key_absent_when_not_set() {
        SepaDirectDebit sdd = new SepaDirectDebit(IBAN);

        assertFalse(compactJson(sdd).contains("recurring"));
    }

    @Test
    void recurring_is_mapped_from_response() {
        ApiSepaDirectDebit apiObject =
                new JsonParser().fromJson(getResponse("type.json"), ApiSepaDirectDebit.class);

        SepaDirectDebit sdd = new SepaDirectDebit(IBAN);
        sdd.map(sdd, apiObject);

        assertEquals(Boolean.TRUE, sdd.getRecurring());
        assertEquals("s-sdd-giqxlgfvwjb7", sdd.getId());
    }
}
