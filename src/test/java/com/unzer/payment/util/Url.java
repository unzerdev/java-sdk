package com.unzer.payment.util;

import java.net.MalformedURLException;
import java.net.URL;

public class Url {
    public static URL unsafeUrl(String value) {
        try {
            return new URL(value);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

}
