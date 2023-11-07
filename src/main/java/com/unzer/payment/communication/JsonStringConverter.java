package com.unzer.payment.communication;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;

import java.lang.reflect.Type;

public class JsonStringConverter
        implements JsonDeserializer<String> {

    @Override
    public String deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
        String jsonValue = json.getAsJsonPrimitive().getAsString();
        if ("".equalsIgnoreCase(jsonValue)) {
            return null;
        } else {
            return jsonValue;
        }
    }


}
