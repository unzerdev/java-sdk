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

import java.util.Objects;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Provides functions which is interact with json
 */
public class JsonParser {

  private Gson gson;

  private JsonParser() {
    gson = new GsonBuilder()
    		.setPrettyPrinting()
//    		.registerTypeAdapterFactory(new JsonTypeAdapterFactory())
//    		.registerTypeAdapter(BigDecimal.class, new JsonBigDecimalConverter())
//    		.registerTypeAdapter(Currency.class, new JsonCurrencyConverter())
    		.create();
  }
  
  /**
   * Provides a function which simple parse object to json
   * @param model refers to object to be parsed
   * @return json method
   * @throws NullPointerException if the model is null
   */
  public String toJson(Object model) {
    if(Objects.isNull(model)) {
      throw new NullPointerException();
    }
    return gson.toJson(model);
  }
  
  /**
   * Provide a simple parser method to get object from json
   * @param json
   * @param clazz
   * @return
   */
  public <T> T fromJson(String json, Class<T> clazz) {
    if(Objects.isNull(json) || Objects.isNull(clazz)) {
      throw new NullPointerException();
    }
    return gson.fromJson(json, clazz);
  }
  
  /**
   * Create new instance of {@link JsonParser}
   * @return {@link JsonParser}
   */
  public static final JsonParser create() {
    return new JsonParser();
  }
}
