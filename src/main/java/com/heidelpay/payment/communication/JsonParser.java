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
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 * #L%
 */

import java.math.BigDecimal;
import java.net.URL;
import java.util.Date;
import java.util.Objects;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Provides functions which is interact with json
 */
public class JsonParser<T> {

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
	 * @return
	 */
	@SuppressWarnings("hiding")
	public <T> T fromJson(String json, Class<T> clazz) {
		if (Objects.isNull(json) || Objects.isNull(clazz)) {
			throw new NullPointerException();
		}
		return gson.fromJson(json, clazz);
	}

}
