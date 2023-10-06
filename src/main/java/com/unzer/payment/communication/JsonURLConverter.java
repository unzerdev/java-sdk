package com.unzer.payment.communication;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@SuppressWarnings("checkstyle:AbbreviationAsWordInName")
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
      return null;
    }
  }

  @Override
  public JsonElement serialize(URL src, Type typeOfSrc, JsonSerializationContext context) {
    return new JsonPrimitive(src.toString());
  }

}
