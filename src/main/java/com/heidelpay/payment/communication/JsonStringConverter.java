package com.heidelpay.payment.communication;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

public class JsonStringConverter
		implements JsonDeserializer<String> {

	@Override
	public String deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
			throws JsonParseException {
		String jsonValue = json.getAsJsonPrimitive().getAsString();
		if ("".equalsIgnoreCase(jsonValue)) {
			return null;
		} else {
			return jsonValue;
		}
	}


}
