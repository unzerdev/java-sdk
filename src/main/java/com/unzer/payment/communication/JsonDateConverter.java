package com.unzer.payment.communication;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class JsonDateConverter
        implements JsonDeserializer<Date>, JsonSerializer<Date> {

    @Override
    public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
        return getDate(json);
    }

    @Override
    public JsonElement serialize(Date src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(new SimpleDateFormat("yyyy-MM-dd").format(src));
    }

    Date getDate(JsonElement json) {
        String jsonValue = json.getAsJsonPrimitive().getAsString();

        if (jsonValue == null || "".equalsIgnoreCase(jsonValue)) {
            return null;
        }
        if (jsonValue.length() == 10) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate parsedLocalDate = LocalDate.parse(jsonValue, formatter);
            return Date.from(parsedLocalDate.atStartOfDay().atZone(ZoneId.of("UTC")).toInstant());
        } else {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            ZonedDateTime parsedZonedDateTime = LocalDateTime.parse(jsonValue, formatter).atZone(ZoneId.of("UTC"));
            return Date.from(parsedZonedDateTime.toInstant());
        }
    }

}
