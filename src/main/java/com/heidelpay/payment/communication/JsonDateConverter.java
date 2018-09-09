package com.heidelpay.payment.communication;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class JsonDateConverter
		implements JsonDeserializer<Date>, JsonSerializer<Date> {

	@Override
	public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
			throws JsonParseException {
		String jsonValue = json.getAsJsonPrimitive().getAsString();
		if (jsonValue == null || "".equalsIgnoreCase(jsonValue)) {
			return null;
		}
		try {
			if (jsonValue.length() == 10) {
				return new SimpleDateFormat("yyyy-MM-dd").parse(jsonValue);
			} else {
				return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(jsonValue);
			}
		} catch (ParseException e) {
			throw new JsonParseException("Cannot parse date " + json.getAsJsonPrimitive().getAsString() + ". Date must be in format 'yyyy-MM-dd HH:mm:ss");
		}
	}

	@Override
	public JsonElement serialize(Date src, Type typeOfSrc, JsonSerializationContext context) {
		return new JsonPrimitive(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(src));
	}

}
