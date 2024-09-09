package com.unzer.payment.models.paypage;

import com.unzer.payment.communication.json.JsonMessage;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

@Data
@Setter(AccessLevel.PRIVATE)
public class PaypagePayment {
    private String paymentId;
    private String transactionStatus;
    private String creationDate;
    private JsonMessage[] messages;
}
