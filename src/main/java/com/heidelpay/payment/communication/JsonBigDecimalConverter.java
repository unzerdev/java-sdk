package com.heidelpay.payment.communication;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.util.Locale;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class JsonBigDecimalConverter implements JsonDeserializer<BigDecimal>, JsonSerializer<BigDecimal> {

	@Override
	public BigDecimal deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
			throws JsonParseException {
		String jsonValue = json.getAsJsonPrimitive().getAsString();
		if (jsonValue == null || "".equalsIgnoreCase(jsonValue)) {
			return null;
		}
		BigDecimal number = new BigDecimal(jsonValue);
		number.setScale(4, BigDecimal.ROUND_HALF_UP);
		return number;
	}

	@Override
	public JsonElement serialize(BigDecimal src, Type typeOfSrc, JsonSerializationContext context) {
		src.setScale(4, BigDecimal.ROUND_HALF_UP);
		DecimalFormat df = new DecimalFormat();
		df.setMaximumFractionDigits(4);
		df.setMinimumFractionDigits(4);
		DecimalFormatSymbols decimalFormatSymbols = new DecimalFormatSymbols(Locale.ENGLISH);
		decimalFormatSymbols.setDecimalSeparator('.');
		df.setDecimalFormatSymbols(decimalFormatSymbols);
		df.setGroupingUsed(false);
		return new JsonPrimitive(df.format(src));
	}

}
