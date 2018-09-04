package com.heidelpay.payment.communication;

import java.io.IOException;
import java.math.BigDecimal;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

public class JsonTypeAdapterFactory implements TypeAdapterFactory {

	@SuppressWarnings("unchecked")
	@Override
	public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
		if (BigDecimal.class.isAssignableFrom(type.getRawType()))
			return (TypeAdapter<T>) new BigDecimalAdapter();
		return null;
	}

	private static class BigDecimalAdapter extends TypeAdapter<BigDecimal> {
		@Override
		public void write(JsonWriter out, BigDecimal value) throws IOException {
			out.beginObject();
			out.name("amount").value(value.toString());
			out.endObject();
		}

		@Override
		public BigDecimal read(JsonReader in) throws IOException {
			in.beginObject();
			if (in.hasNext() && in.nextName() != null) {
				return new BigDecimal(in.nextString());
			} else {
				return null;
			}
		}
	}
}
