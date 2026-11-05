package com.unzer.payment.business;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.unzer.payment.Address;
import com.unzer.payment.ShippingAddress;
import com.unzer.payment.communication.JsonParser;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AddressTest {

    private static JsonObject toJsonObject(Address address) {
        return new Gson().fromJson(new JsonParser().toJson(address), JsonObject.class);
    }

    @Test
    void companyIsOmittedWhenUnset() {
        Address address = new Address().setName("Max Mustermann").setCity("Berlin");

        assertNull(address.getCompany());
        assertFalse(toJsonObject(address).has("company"),
                "Unset company must be omitted from the serialized JSON");
    }

    @ParameterizedTest
    @ValueSource(strings = {"ACME GmbH", "", "A"})
    void companyIsSerializedWhenSet(String company) {
        Address address = new Address().setCompany(company);

        JsonObject json = toJsonObject(address);
        assertTrue(json.has("company"), "Set company must be serialized");
        assertEquals(company, json.get("company").getAsString());
    }

    @Test
    void getterSetterRoundTrip() {
        Address address = new Address();

        Address returned = address.setCompany("ACME GmbH");

        assertEquals("ACME GmbH", address.getCompany());
        assertEquals(address, returned, "Setter must return the same instance for fluent chaining");
    }

    @Test
    void shippingAddressOfCopiesCompany() {
        Address billing = new Address().setCompany("ACME GmbH");

        ShippingAddress shipping = ShippingAddress.of(billing, ShippingAddress.Type.EQUALS_BILLING);

        assertEquals("ACME GmbH", shipping.getCompany());
    }
}
