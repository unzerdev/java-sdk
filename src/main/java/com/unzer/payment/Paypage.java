package com.unzer.payment;

public class Paypage extends BasePaypage {
    @Override
    protected String getResourceUrl() {
        return "/v1/paypage/<action>/<resourceId>";
    }
}
