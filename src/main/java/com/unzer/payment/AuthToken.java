package com.unzer.payment;

import lombok.Getter;

@Getter
public class AuthToken implements Resource {
    @Override
    public String getId() {
        return null;
    }

    @Override
    public String getUrl() {
        return "/v1/auth/token";
    }

    private String accessToken;
}
