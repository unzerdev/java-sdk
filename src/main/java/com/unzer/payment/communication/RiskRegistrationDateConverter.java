package com.unzer.payment.communication;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Gson converter for dates in format yyyyMMdd.
 */
public class RiskRegistrationDateConverter implements JsonDeserializer<Date>, JsonSerializer<Date> {

    public static final String DATE_FORMAT = "yyyyMMdd";

    @Override
    public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
        String value = json.getAsString();
        if (value == null || value.isEmpty()) {
            return null;
        }
        SimpleDateFormat df = new SimpleDateFormat(DATE_FORMAT);
        df.setLenient(false);
        // Interpret as UTC start of day when only date is given
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        try {
            return df.parse(value);
        } catch (ParseException e) {
            throw new RuntimeException("Failed to parse date in format '" + DATE_FORMAT + "': " + value, e);
        }
    }

    @Override
    public JsonElement serialize(Date src, Type typeOfSrc, JsonSerializationContext context) {
        SimpleDateFormat df = new SimpleDateFormat(DATE_FORMAT);
        df.setLenient(false);
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        return new JsonPrimitive(df.format(src));

    }
}
