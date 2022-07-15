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

import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class JsonURLConverter
        implements JsonDeserializer<URL>, JsonSerializer<URL> {
    public static final Logger logger = LogManager.getLogger(JsonURLConverter.class);

    @Override
    public URL deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
        String urlValue = json.getAsJsonPrimitive().getAsString();

        if (urlValue == null || "".equalsIgnoreCase(urlValue)) {
            return null;
        }

        try {
            return new URL(urlValue);
        } catch (MalformedURLException e) {
            logger.warn("Invalid URL '{}': {}", urlValue, e.getMessage());
            // FIXME: throw an exception instead of returning null?
            return null;
        }
    }

    @Override
    public JsonElement serialize(URL src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(src.toString());
    }

}
