package com.unzer.payment.util;

import org.junit.jupiter.api.Test;

import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class VersionTest {
    @Test
    public void testVersionIsCorrect() {
//        1.2.3
        assertTrue(Pattern.matches("", SDKInfo.VERSION));
    }
}
