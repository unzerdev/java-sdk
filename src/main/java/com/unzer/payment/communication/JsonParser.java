package com.unzer.payment.communication;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.unzer.payment.PaymentException;
import com.unzer.payment.communication.json.JsonErrorObject;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.util.Currency;
import java.util.Date;
import java.util.Objects;

/**
 * Provides functions which is interact with json
 */
public class JsonParser {

    private static final String ERRORS = "errors";
    private static final String ERROR_CODE = "code";

    private final Gson gson;

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
     * @param <T>   type of used class to be parsed
     * @param json  json to be parsed
     * @param clazz class to be used for the parsing
     * @return an object of type T
     */
    @SuppressWarnings("hiding")
    public <T> T fromJson(String json, Class<T> clazz)
            throws IllegalArgumentException, PaymentException {
        if (Objects.isNull(json) || Objects.isNull(clazz)) {
            throw new IllegalArgumentException("Null object cannot be parsed!");
        }
        if (!isJsonValid(json)) {
            throw new IllegalArgumentException(
                    String.format("The provided JSON String is not valid! The provided Json was: %s", json));
        }
        if (isError(json) && !clazz.isAssignableFrom(JsonErrorObject.class)) {
            throw toPaymentException(json);
        }
        return gson.fromJson(json, clazz);
    }

    private PaymentException toPaymentException(String json) {
        JsonErrorObject error = gson.fromJson(json, JsonErrorObject.class);

        return new PaymentException(error.getId(), error.getUrl(), error.getTimestamp(),
                error.getErrors(), "");
    }

    private boolean isError(String json) {
        return json.contains(ERRORS) && json.contains(ERROR_CODE);
    }

    public boolean isJsonValid(String jsonInString) {
        try {
            final ObjectMapper mapper = new ObjectMapper();
            mapper.readTree(jsonInString);
            return true;
        } catch (IOException | IllegalArgumentException e) {
            return false;
        }
    }

}
