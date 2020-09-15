/**
 * JsonParser.java
 *
 * @license Use of this software requires acceptance of the Evaluation License Agreement. See LICENSE file.
 * @copyright Copyright (C) 2016-present Heidelberger Payment GmbH. All rights reserved.
 *
 * @link  http://dev.heidelpay.com/heidelpay-php-api/
 *
 * @author vukhang
 *
 */
package com.heidelpay.payment.communication;

/*-
 * #%L
 * Heidelpay Java SDK
 * %%
 * Copyright (C) 2018 Heidelpay GmbH
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

import java.math.BigDecimal;
import java.net.URL;
import java.util.Currency;
import java.util.Date;
import java.util.Objects;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.heidelpay.payment.PaymentException;
import com.heidelpay.payment.communication.json.JsonErrorObject;

/**
 * Provides functions which is interact with json
 */
public class JsonParser {

	private static final String ERRORS = "errors";
	private static final String ERROR_CODE = "code";

	private Gson gson;

	public JsonParser() {
		gson = new GsonBuilder().setPrettyPrinting()
				.addSerializationExclusionStrategy(new JsonFieldIgnoreStragegy())
				.registerTypeAdapter(Date.class, new JsonDateTimeConverter())
				.registerTypeAdapter(String.class, new JsonStringConverter())
				.registerTypeAdapter(BigDecimal.class, new JsonBigDecimalConverter())
				.registerTypeAdapter(URL.class, new JsonURLConverter())
				.registerTypeAdapter(Currency.class, new JsonCurrencyConverter()).create();
	}

	/**
	 * Provides a function which simple parse object to json
	 * 
	 * @param model refers to object to be parsed
	 * @return json method
	 * @throws IllegalArgumentException if the model is null
	 */
	public String toJson(Object model) {
		if (Objects.isNull(model)) {
			throw new IllegalArgumentException("Null object cannot be parsed!");
		}
		return gson.toJson(model);
	}

	/**
	 * Provide a simple parser method to get object from json
	 *
	 * @param <T> type of used class to be parsed
	 * @param json json to be parsed
	 * @param clazz class to be used for the parsing
	 * @return an object of type T
	 */
	@SuppressWarnings("hiding")
	public <T> T fromJson(String json, Class<T> clazz) {
		if (Objects.isNull(json) || Objects.isNull(clazz)) {
			throw new IllegalArgumentException("Null object cannot be parsed!");
		}
		if (isError(json) && !clazz.isAssignableFrom(JsonErrorObject.class)) {
			throw toPaymentException(json);
		}
		return gson.fromJson(json, clazz);
	}

	private PaymentException toPaymentException(String json) {
		JsonErrorObject error = gson.fromJson(json, JsonErrorObject.class);

		return new PaymentException(error.getId(), error.getUrl(), error.getTimestamp(), error.getErrors(), "");
	}

	private boolean isError(String json) {
		return json.contains(ERRORS) && json.contains(ERROR_CODE);
	}

}
