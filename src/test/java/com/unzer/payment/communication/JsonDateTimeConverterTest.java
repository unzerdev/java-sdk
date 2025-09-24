package com.unzer.payment.communication;

import com.google.gson.JsonObject;
import org.junit.jupiter.api.Test;

import java.time.format.DateTimeParseException;
import java.util.Calendar;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


class JsonDateTimeConverterTest {
    private static final JsonDateTimeConverter converter = new JsonDateTimeConverter();

    @Test
    void date_parsed_fine() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("date", "1980-12-01");

        Date jsonDate = converter.deserialize(jsonObject.get("date"), null, null);
        assertEquals("Mon Dec 01 01:00:00 CET 1980", jsonDate.toString());
    }

    @Test
    void dateTime_parsed_fine() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("date", "1980-12-01 11:59:00");

        Date parsedDate = converter.deserialize(jsonObject.get("date"), null, null);
        assertEquals("Mon Dec 01 12:59:00 CET 1980", parsedDate.toString());

    }

    @Test
    void getDateTestWrongFormat() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("date", "1980-12-01 12:00");


        assertThrows(DateTimeParseException.class,
                () -> converter.deserialize(jsonObject.get("date"), null, null));
    }

    @Test
    void getDateTestInvalidDate() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("date", "2000-12-32");


        assertThrows(DateTimeParseException.class,
                () -> converter.deserialize(jsonObject.get("date"), null, null));
    }

    @Test
    void getDateTestInvalidDateTime() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("date", "2000-12-31 25:00:00");


        assertThrows(DateTimeParseException.class,
                () -> converter.deserialize(jsonObject.get("date"), null, null));
    }

    @Test
    void date_time_converted_with_time() {
        Date initialDate = new Date(94, Calendar.DECEMBER, 1, 11, 59, 31);

        String convertedDate = converter.serialize(initialDate, null, null).getAsString();
        assertEquals("1994-12-01 11:59:31", convertedDate);
    }
}
