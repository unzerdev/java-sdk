package com.unzer.payment.util;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.regex.Pattern;
import org.junit.jupiter.api.Test;

public class VersionTest {
    @Test
    public void testVersionIsCorrect() {
        // Version must be a semver consisting of 3 digit groups, rest is not important
        // e.g. 1.22.33-SNAPSHOT
        assertTrue(Pattern.matches("^\\d+[.]\\d+[.]\\d+.*", SDKInfo.VERSION));
    }
}
