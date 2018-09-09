package com.heidelpay.payment.communication;

import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.log4j.Logger;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class JsonURLConverter
		implements JsonDeserializer<URL>, JsonSerializer<URL> {
	public final static Logger logger = Logger.getLogger(JsonURLConverter.class);

	@Override
	public URL deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
			throws JsonParseException {
		String urlValue = json.getAsJsonPrimitive().getAsString();
		if (urlValue == null || "".equalsIgnoreCase(urlValue)) return null;
		try {
			return new URL(urlValue);
		} catch (MalformedURLException e) {
			logger.warn("Invalid URL '" + urlValue + "': " + e.getMessage());
			return null;
		}
	}

	@Override
	public JsonElement serialize(URL src, Type typeOfSrc, JsonSerializationContext context) {
		return new JsonPrimitive(src.toString());
	}

}
