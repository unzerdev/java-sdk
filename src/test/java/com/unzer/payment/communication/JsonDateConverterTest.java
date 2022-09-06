/*
 * Copyright 2020-today Unzer E-Com GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.unzer.payment.communication;

import com.google.gson.JsonObject;
import org.junit.jupiter.api.Test;

import java.time.format.DateTimeParseException;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class JsonDateConverterTest {

    @Test
    public void getDateTestFormatDateTime() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("date", "1980-12-01 12:00:00");


        JsonDateConverter jsonDateConverter = new JsonDateConverter();
        Date jsonDate = jsonDateConverter.getDate(jsonObject.get("date"));
        assertEquals("Mon Dec 01 13:00:00 CET 1980", jsonDate.toString());
    }

    @Test
    public void getDateTestFormatDate() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("date", "1980-12-01");


        JsonDateConverter jsonDateConverter = new JsonDateConverter();
        Date jsonDate = jsonDateConverter.getDate(jsonObject.get("date"));
        assertEquals("Mon Dec 01 01:00:00 CET 1980", jsonDate.toString());
    }

    @Test
    public void getDateTestWrongFormat() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("date", "1980-12-01 12:00");


        JsonDateConverter jsonDateConverter = new JsonDateConverter();
        assertThrows(DateTimeParseException.class, () -> {
            jsonDateConverter.getDate(jsonObject.get("date"));
        });
    }

    @Test
    public void getDateTestInvalidDate() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("date", "2000-12-32");


        JsonDateConverter jsonDateConverter = new JsonDateConverter();
        assertThrows(DateTimeParseException.class, () -> {
            jsonDateConverter.getDate(jsonObject.get("date"));
        });
    }

    @Test
    public void getDateTestInvalidDateTime() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("date", "2000-12-31 25:00:00");


        JsonDateConverter jsonDateConverter = new JsonDateConverter();
        assertThrows(DateTimeParseException.class, () -> {
            jsonDateConverter.getDate(jsonObject.get("date"));
        });
    }
}
