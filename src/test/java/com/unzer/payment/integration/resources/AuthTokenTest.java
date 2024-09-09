package com.unzer.payment.integration.resources;


import com.unzer.payment.AuthToken;
import com.unzer.payment.business.AbstractPaymentTest;
import org.junit.jupiter.api.Test;

import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;


public class AuthTokenTest extends AbstractPaymentTest {

    @Test
    public void createTokenWithPrivateKey() {
        AuthToken autTokenResponse = getUnzer().createAuthToken();
        assertNull(autTokenResponse.getId());
        assertNotNull(autTokenResponse.getAccessToken());

        String jwtRegex = "^[A-Za-z0-9_-]+\\.([A-Za-z0-9_-]+\\.){1}[A-Za-z0-9_-]+$";
        assertTrue(Pattern.matches(jwtRegex, autTokenResponse.getAccessToken()));
    }
}
