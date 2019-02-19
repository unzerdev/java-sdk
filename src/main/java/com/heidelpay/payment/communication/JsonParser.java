/**
 * JsonParser.java
 *
 * @license Use of this software requires acceptance of the Evaluation License Agreement. See LICENSE file.
 * @copyright Copyright © 2016-present Heidelberger Payment GmbH. All rights reserved.
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.heidelpay.payment.PaymentError;
import com.heidelpay.payment.PaymentException;
import com.heidelpay.payment.communication.json.JsonErrorObject;

/**
 * Provides functions which is interact with json
 */
public class JsonParser<T> {

	private static final String ERRORS = "errors";
	private static final String ERROR_CODE = "code";

	private Gson gson;

	public JsonParser() {
		gson = new GsonBuilder().setPrettyPrinting().registerTypeAdapter(Date.class, new JsonDateConverter())
				.registerTypeAdapter(String.class, new JsonStringConverter())
				.registerTypeAdapter(BigDecimal.class, new JsonBigDecimalConverter())
				.registerTypeAdapter(URL.class, new JsonURLConverter()).create();
	}

	/**
	 * Provides a function which simple parse object to json
	 * 
	 * @param model refers to object to be parsed
	 * @return json method
	 * @throws NullPointerException if the model is null
	 */
	public String toJson(Object model) {
		if (Objects.isNull(model)) {
			throw new NullPointerException();
		}
		return gson.toJson(model);
	}

	/**
	 * Provide a simple parser method to get object from json
	 * 
	 * @param json
	 * @param clazz
	 * @return an object of type T
	 */
	@SuppressWarnings("hiding")
	public <T> T fromJson(String json, Class<T> clazz) {
		if (Objects.isNull(json) || Objects.isNull(clazz)) {
			throw new NullPointerException();
		}
		if (isError(json) && !clazz.isAssignableFrom(JsonErrorObject.class)) {
			throw toPaymentException(json);
		}
		return gson.fromJson(json, clazz);
	}

	private PaymentException toPaymentException(String json) {
		JsonErrorObject error = gson.fromJson(json, JsonErrorObject.class);

		return new PaymentException(error.getId(), error.getUrl(), error.getTimestamp(), error.getErrors());
	}

	private boolean isError(String json) {
		return json.contains(ERRORS) && json.contains(ERROR_CODE);
	}

}
