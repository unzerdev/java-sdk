package com.unzer.payment.communication.json;

public class JsonIdObject implements JsonObject {
    private String id;
    private Boolean recurring;
    private JsonGeoLocation geoLocation;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Boolean getRecurring() {
        return recurring;
    }

    public JsonGeoLocation getGeoLocation() {
        return geoLocation;
    }
}
