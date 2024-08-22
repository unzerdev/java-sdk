package com.unzer.payment.service;

import com.unzer.payment.AuthToken;
import com.unzer.payment.Unzer;
import com.unzer.payment.business.AbstractPaymentTest;
import com.unzer.payment.communication.HttpCommunicationMockUtils;
import com.unzer.payment.communication.JsonParser;
import com.unzer.payment.communication.UnzerRestCommunication;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class TokenServiceTest extends AbstractPaymentTest {

    public TokenService setupTokenService(String json, int status) {
        UnzerRestCommunication rest = HttpCommunicationMockUtils.withFixedResponse(json, status);
        Unzer unzer = new Unzer(rest, "anykey");
        return new TokenService(unzer, rest, new JsonParser());
    }

    @Test
    void createToken() {
        TokenService tokenService = setupTokenService("{\"accessToken\": \"64header.64payload.signature \"}", 201);
        AuthToken authToken = tokenService.create();
        assertNotNull(authToken);
    }
    @Test
    void prepareTokenShouldReuseValidToken() {
        Unzer unzer = getUnzer();
        unzer.prepareJwtToken();
        assertNotNull(unzer.getJwtToken());

        String initialToken = unzer.getJwtToken();
        unzer.prepareJwtToken();

        assertEquals(initialToken, unzer.getJwtToken());
    }
}
