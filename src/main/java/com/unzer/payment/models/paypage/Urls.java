package com.unzer.payment.models.paypage;

import lombok.Data;

@Data
public class Urls {
    private String termsAndCondition;
    private String privacyPolicy;
    private String imprint;
    private String help;
    private String contact;
    private String returnSuccess;
    private String returnPending;
    private String returnFailure;
    private String returnCancel;
    private String subscriptionAgreement;
}