package com.unzer.payment.util;

import java.util.UUID;

public class Uuid {
    public static String generateUuid() {
        return UUID.randomUUID().toString().substring(0, 8);
    }
}
