package com.unzer.payment.util;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;
import java.util.Date;

public class Types {
    public static URL unsafeUrl(String value) {
        try {
            return new URL(value);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    public static Date date(String date) {
        return Date.from(LocalDate.parse(date).atStartOfDay().toInstant(java.time.ZoneOffset.UTC));
    }
}
