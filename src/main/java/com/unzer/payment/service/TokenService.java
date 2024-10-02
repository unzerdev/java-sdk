package com.unzer.payment.service;

import com.unzer.payment.AuthToken;
import com.unzer.payment.Unzer;
import com.unzer.payment.communication.HttpCommunicationException;
import com.unzer.payment.communication.JsonParser;
import com.unzer.payment.communication.UnzerRestCommunication;
import com.unzer.payment.communication.api.ApiConfig;
import com.unzer.payment.communication.api.ApiConfigs;

public class TokenService {
    private final UnzerRestCommunication restCommunication;

    private final UrlUtil urlUtil;
    private final Unzer unzer;

    protected JsonParser jsonParser;

    private static final ApiConfig API_CONFIG = ApiConfigs.TOKEN_SERVICE_API;

    public TokenService(Unzer unzer, UnzerRestCommunication restCommunication) {
        this(unzer, restCommunication, new JsonParser());
    }

    public TokenService(Unzer unzer, UnzerRestCommunication restCommunication, JsonParser jsonParser) {
        super();
        this.unzer = unzer;
        this.urlUtil = new UrlUtil(unzer.getPrivateKey(), API_CONFIG);
        this.restCommunication = restCommunication;
        this.jsonParser = jsonParser;
    }

    public AuthToken create() throws HttpCommunicationException {
        AuthToken authToken = new AuthToken();
        String response = restCommunication.httpPost(
                urlUtil.getUrl(authToken),
                unzer.getPrivateKey(),
                authToken,
                API_CONFIG
        );

        return jsonParser.fromJson(response, AuthToken.class);
    }
}
