package com.unzer.payment.communication;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Date;

public class JsonDateTimeIso8601Converter extends JsonDateConverter
        implements JsonDeserializer<Date>, JsonSerializer<Date> {

    @Override
    public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
        return getDate(json);
    }

    @Override
    public JsonElement serialize(Date src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").format(src));
    }
}
