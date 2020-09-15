package com.heidelpay.payment.communication;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;

public class JsonFieldIgnoreStragegy implements ExclusionStrategy {

	@Override
	public boolean shouldSkipField(FieldAttributes f) {
		return f.getAnnotation(JsonFieldIgnore.class) != null;
	}

	@Override
	public boolean shouldSkipClass(Class<?> clazz) {
		return false;
	}

}
