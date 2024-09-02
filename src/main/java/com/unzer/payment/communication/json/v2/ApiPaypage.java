package com.unzer.payment.communication.json.v2;

import lombok.Data;

@Data
public class ApiPaypage {
    private String paypageId;
    private String redirectUrl;
}
