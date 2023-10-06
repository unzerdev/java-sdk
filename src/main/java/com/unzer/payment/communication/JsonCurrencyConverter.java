package com.unzer.payment.communication;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;
import java.util.Currency;

public class JsonCurrencyConverter
    implements JsonDeserializer<Currency>, JsonSerializer<Currency> {

  @Override
  public Currency deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
    return Currency.getInstance(json.getAsJsonPrimitive().getAsString());
  }

  @Override
  public JsonElement serialize(Currency src, Type typeOfSrc, JsonSerializationContext context) {
    return new JsonPrimitive(src.getCurrencyCode());
  }

}
