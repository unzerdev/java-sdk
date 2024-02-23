package com.unzer.payment.communication;

import com.google.gson.*;
import lombok.extern.log4j.Log4j2;

import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;

@Log4j2
public class JsonURLConverter implements JsonDeserializer<URL>, JsonSerializer<URL> {

    @Override
    public URL deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
        String urlValue = json.getAsJsonPrimitive().getAsString();

        if (urlValue == null || "".equalsIgnoreCase(urlValue)) {
            return null;
        }

        try {
            return new URL(urlValue);
        } catch (MalformedURLException e) {
            log.warn("Invalid URL '{}': {}", urlValue, e.getMessage());
            return null;
        }
    }

    @Override
    public JsonElement serialize(URL src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(src.toString());
    }
}
