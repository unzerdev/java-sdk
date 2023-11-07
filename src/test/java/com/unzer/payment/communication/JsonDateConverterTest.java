package com.unzer.payment.communication;

import com.google.gson.JsonObject;
import org.junit.jupiter.api.Test;

import java.time.format.DateTimeParseException;
import java.util.Calendar;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


public class JsonDateConverterTest {

    @Test
    public void date_parsed_successfully() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("date", "1980-12-01");

        JsonDateConverter jsonDateConverter = new JsonDateConverter();
        Date jsonDate = jsonDateConverter.deserialize(jsonObject.get("date"), null, null);
        assertEquals("Mon Dec 01 01:00:00 CET 1980", jsonDate.toString());
    }

    @Test
    public void dateTime_parsed_successfully() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("date", "1980-12-01 11:59:00");

        JsonDateConverter jsonDateConverter = new JsonDateConverter();

        Date jsonDate = jsonDateConverter.deserialize(jsonObject.get("date"), null, null);
        assertEquals("Mon Dec 01 12:59:00 CET 1980", jsonDate.toString());
    }

    @Test
    public void getDateTestWrongFormat() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("date", "1980-12-01 12:00");


        JsonDateConverter jsonDateConverter = new JsonDateConverter();
        assertThrows(DateTimeParseException.class, () -> {
            jsonDateConverter.deserialize(jsonObject.get("date"), null, null);
        });
    }

    @Test
    public void getDateTestInvalidDate() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("date", "2000-12-32");


        JsonDateConverter jsonDateConverter = new JsonDateConverter();
        assertThrows(DateTimeParseException.class, () -> {
            jsonDateConverter.deserialize(jsonObject.get("date"), null, null);
        });
    }

    @Test
    public void getDateTestInvalidDateTime() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("date", "2000-12-31 25:00:00");


        JsonDateConverter jsonDateConverter = new JsonDateConverter();
        assertThrows(DateTimeParseException.class, () -> {
            jsonDateConverter.deserialize(jsonObject.get("date"), null, null);
        });
    }

    @Test
    public void date_time_converted_without_time() {
        Date initialDate = new Date(94, Calendar.DECEMBER, 1, 11, 59, 31);

        String convertedDate = new JsonDateConverter().serialize(initialDate, null, null).getAsString();
        assertEquals("1994-12-01", convertedDate);
    }
}
