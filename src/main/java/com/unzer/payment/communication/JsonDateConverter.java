package com.unzer.payment.communication;

/*-
 * #%L
 * Unzer Java SDK
 * %%
 * Copyright (C) 2020 - today Unzer E-Com GmbH
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import com.google.gson.*;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.time.*;
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
