package com.unzer.payment.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Base64;
import java.util.Date;

public class JwtHelper {

    public static boolean validateExpiryDate(String jwtToken) {
        Date now = new Date();

        String payload = extractPayload(jwtToken);
        ObjectMapper objectMapper = new ObjectMapper();

        JsonNode payloadJson = null;
        try {
            payloadJson = objectMapper.readTree(payload);
        } catch (JsonProcessingException e) {
            return false;
        }

        long exp = payloadJson.get("exp").asLong();
        Date expirationDate = new Date(exp * 1000);
        return now.before(expirationDate);
    }

    private static String extractPayload(String jwtToken) {
        String[] tokenSegments = jwtToken.split("\\.");
        if (tokenSegments.length != 3) {
            throw new IllegalArgumentException("Invalid JWT token format");
        }

        Base64.Decoder decoder = Base64.getUrlDecoder();
        return new String(decoder.decode(tokenSegments[1]));
    }
}
