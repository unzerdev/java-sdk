package com.unzer.payment.communication;


import com.unzer.payment.Payment;
import com.unzer.payment.PaymentError;
import com.unzer.payment.PaymentException;
import com.unzer.payment.TestData;
import com.unzer.payment.business.AbstractPaymentTest;
import com.unzer.payment.communication.json.ApiCharge;
import com.unzer.payment.communication.json.JsonErrorObject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class JsonParserTest extends AbstractPaymentTest {

    @Test
    public void given_an_error_message_then_payment_exception_is_thrown() {
        try {
            JsonParser parser = new JsonParser();
            parser.fromJson(TestData.errorJson(), ApiCharge.class);

            fail("Expected PaymentException");
        } catch (PaymentException exception) {
            assertBasicErrorAttributes(exception.getId(), exception.getUrl(), exception.getTimestamp());
            assertPaymentError(exception.getPaymentErrorList().get(0));
        }
    }

    @Test
    public void given_an_error_json_then_fromJson_returnes_jsonerrorobject() {
        assertJsonError(new JsonParser().fromJson(TestData.errorJson(), JsonErrorObject.class));
    }

    @Test
    public void testValidJson() {
        assertTrue(new JsonParser().isJsonValid("{\"name\": \"value\"}"));
    }

    @Test
    public void testInvalidJson() {
        assertFalse(new JsonParser().isJsonValid("This is an error message!"));
    }

    @Test
    public void testNullJson() {
        assertThrows(IllegalArgumentException.class, () -> {
            new JsonParser().fromJson(null, Payment.class);
        });
    }

    @Test
    public void testNullClass() {
        assertThrows(IllegalArgumentException.class, () -> {
            new JsonParser().fromJson("{\"name\": \"value\"}", null);
        });
    }

    @Test
    public void testNullValidJson() {
        assertFalse(new JsonParser().isJsonValid(null));
    }

    @Test
    public void testFromInvalidJson() {
        assertThrows(IllegalArgumentException.class, () -> {
            new JsonParser().fromJson("This is an error message!", Payment.class);
        });
    }

    private void assertJsonError(JsonErrorObject expectedError) {
        assertBasicErrorAttributes(expectedError.getId(), expectedError.getUrl(), expectedError.getTimestamp());
        assertEquals(1, expectedError.getErrors().size());
        assertPaymentError(expectedError.getErrors().get(0));
    }

    private void assertBasicErrorAttributes(String id, String url, String timestamp) {
        assertEquals("s-err-f2ea241e5e8e4eb3b1513fab12c", id);
        assertEquals("https://api.unzer.com/v1/payments/charges", url);
        assertEquals("2019-01-09 15:42:24", timestamp);

    }

    private void assertPaymentError(PaymentError error) {
        assertEquals("COR.400.100.101", error.getCode());
        assertEquals("Address untraceable", error.getMerchantMessage());
        assertEquals("The provided address is invalid. Please check your input and try agian.",
                error.getCustomerMessage());
    }
}
