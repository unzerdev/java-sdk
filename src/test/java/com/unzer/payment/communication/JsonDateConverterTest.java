package com.unzer.payment.communication;

import com.google.gson.JsonObject;
import junit.framework.Assert;
import org.junit.Test;

import java.time.format.DateTimeParseException;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class JsonDateConverterTest {

    @Test
    public void getDateTestFormatDateTime(){
        System.setProperty("user.timezone", "UTC");
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("date", "1980-12-01 12:00:00");


        JsonDateConverter jsonDateConverter = new JsonDateConverter();
        Date jsonDate = jsonDateConverter.getDate(jsonObject.get("date"));
        assertEquals("Mon Dec 01 12:00:00 UTC 1980", jsonDate.toString());
    }

    @Test
    public void getDateTestFormatDate(){
        System.setProperty("user.timezone", "UTC");
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("date", "1980-12-01");


        JsonDateConverter jsonDateConverter = new JsonDateConverter();
        Date jsonDate = jsonDateConverter.getDate(jsonObject.get("date"));
        assertEquals("Mon Dec 01 00:00:00 UTC 1980", jsonDate.toString());
    }

    @Test
    public void getDateTestWrongFormat(){
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("date", "1980-12-01 12:00");


        JsonDateConverter jsonDateConverter = new JsonDateConverter();
        assertThrows(DateTimeParseException.class, () -> {
            jsonDateConverter.getDate(jsonObject.get("date"));
        });
    }

    @Test
    public void getDateTestInvalidDate(){
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("date", "2000-12-32");


        JsonDateConverter jsonDateConverter = new JsonDateConverter();
        assertThrows(DateTimeParseException.class, () -> {
            jsonDateConverter.getDate(jsonObject.get("date"));
        });
    }

    @Test
    public void getDateTestInvalidDateTime(){
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("date", "2000-12-31 25:00:00");


        JsonDateConverter jsonDateConverter = new JsonDateConverter();
        assertThrows(DateTimeParseException.class, () -> {
            jsonDateConverter.getDate(jsonObject.get("date"));
        });
    }
}