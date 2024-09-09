package com.unzer.payment.models.paypage;

import lombok.Data;

import java.util.Date;

@Data
public class Risk {
    private Integer registrationLevel;
    private Date registrationDate;
    private String customerGroup;
    private Integer confirmedOrders;
    private double confirmedAmount;
}
