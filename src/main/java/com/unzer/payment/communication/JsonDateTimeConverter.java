package com.unzer.payment.communication;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Date;

public class JsonDateTimeConverter extends JsonDateConverter
        implements JsonDeserializer<Date>, JsonSerializer<Date> {

    @Override
    public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
        return getDate(json);
    }

    @Override
    public JsonElement serialize(Date src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(src));
    }

}
