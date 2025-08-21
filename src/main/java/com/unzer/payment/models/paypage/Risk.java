package com.unzer.payment.models.paypage;

import com.google.gson.annotations.JsonAdapter;
import com.unzer.payment.communication.RiskRegistrationDateConverter;
import lombok.Data;

import java.util.Date;

@Data
public class Risk {
    private Integer registrationLevel;
    @JsonAdapter(value = RiskRegistrationDateConverter.class)
    private Date registrationDate;
    private String customerGroup;
    private Integer confirmedOrders;
    private double confirmedAmount;
}
