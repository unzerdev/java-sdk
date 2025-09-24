package com.unzer.payment.business;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.unzer.payment.communication.JsonParser;
import com.unzer.payment.models.AdditionalTransactionData;
import com.unzer.payment.models.WeroEventDependentPayment;
import com.unzer.payment.models.WeroTransactionData;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AdditionalTransactionDataWeroTest {

    @Test
    void serializationIncludesWeroDataWithExpectedFieldsAndValues() {
        // Arrange
        WeroEventDependentPayment edp = new WeroEventDependentPayment()
                .setCaptureTrigger(WeroEventDependentPayment.CaptureTrigger.SERVICEFULFILMENT)
                .setAmountPaymentType(WeroEventDependentPayment.AmountPaymentType.PAY)
                .setMaxAuthToCaptureTime(300)
                .setMultiCapturesAllowed(false);

        WeroTransactionData wero = new WeroTransactionData()
                .setEventDependentPayment(edp);

        AdditionalTransactionData atd = new AdditionalTransactionData()
                .setWero(wero);

        // Act
        String json = new JsonParser().toJson(atd);

        // Assert
        JsonObject root = new Gson().fromJson(json, JsonObject.class);
        assertNotNull(root, "Root JSON should not be null");
        assertTrue(root.has("wero"), "JSON should contain 'wero' wrapper inside additional transaction data");

        JsonObject weroObj = root.getAsJsonObject("wero");
        assertNotNull(weroObj, "'wero' object should not be null");
        assertTrue(weroObj.has("eventDependentPayment"), "'wero' should contain 'eventDependentPayment'");

        JsonObject edpObj = weroObj.getAsJsonObject("eventDependentPayment");
        assertNotNull(edpObj, "'eventDependentPayment' object should not be null");

        assertEquals("servicefulfilment", edpObj.get("captureTrigger").getAsString());
        assertEquals("pay", edpObj.get("amountPaymentType").getAsString());
        assertEquals(300, edpObj.get("maxAuthToCaptureTime").getAsInt());
        assertFalse(edpObj.get("multiCapturesAllowed").getAsBoolean());
    }

    @Test
    void deserializationParsesWeroDataCorrectly() {
        // Arrange
        String json = "{" +
                "\"wero\": {" +
                "  \"eventDependentPayment\": {" +
                "    \"captureTrigger\": \"servicefulfilment\"," +
                "    \"amountPaymentType\": \"pay\"," +
                "    \"maxAuthToCaptureTime\": 300," +
                "    \"multiCapturesAllowed\": false" +
                "  }" +
                "}" +
                "}";

        // Act
        AdditionalTransactionData atd = new JsonParser().fromJson(json, AdditionalTransactionData.class);

        // Assert
        assertNotNull(atd);
        assertNotNull(atd.getWero());
        assertNotNull(atd.getWero().getEventDependentPayment());
        WeroEventDependentPayment edp = atd.getWero().getEventDependentPayment();
        assertEquals(WeroEventDependentPayment.CaptureTrigger.SERVICEFULFILMENT, edp.getCaptureTrigger());
        assertEquals(WeroEventDependentPayment.AmountPaymentType.PAY, edp.getAmountPaymentType());
        assertEquals(300, edp.getMaxAuthToCaptureTime());
        assertFalse(edp.getMultiCapturesAllowed());
    }
}
