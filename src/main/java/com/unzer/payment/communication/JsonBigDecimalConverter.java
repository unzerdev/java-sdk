package com.unzer.payment.communication;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class JsonBigDecimalConverter implements JsonDeserializer<BigDecimal>, JsonSerializer<BigDecimal> {

    @Override
    public BigDecimal deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
        String jsonValue = json.getAsJsonPrimitive().getAsString();
        if (jsonValue == null || "".equalsIgnoreCase(jsonValue)) {
            return null;
        }
        BigDecimal number = new BigDecimal(jsonValue);
        return number.setScale(4, BigDecimal.ROUND_HALF_UP);
    }

    @Override
    public JsonElement serialize(BigDecimal src, Type typeOfSrc, JsonSerializationContext context) {
        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(4);
        df.setMinimumFractionDigits(4);
        DecimalFormatSymbols decimalFormatSymbols = new DecimalFormatSymbols(Locale.ENGLISH);
        decimalFormatSymbols.setDecimalSeparator('.');
        df.setDecimalFormatSymbols(decimalFormatSymbols);
        df.setGroupingUsed(false);
        return new JsonPrimitive(df.format(src.setScale(4, BigDecimal.ROUND_HALF_UP)));
    }

}
