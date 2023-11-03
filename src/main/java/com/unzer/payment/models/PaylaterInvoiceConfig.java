package com.unzer.payment.models;

public class PaylaterInvoiceConfig {
    private String dataPrivacyConsent;
    private String dataPrivacyDeclaration;
    private String termsAndConditions;

    public PaylaterInvoiceConfig() {
    }

    public PaylaterInvoiceConfig(String dataPrivacyConsent, String dataPrivacyDeclaration,
                                 String termsAndConditions) {
        this.dataPrivacyConsent = dataPrivacyConsent;
        this.dataPrivacyDeclaration = dataPrivacyDeclaration;
        this.termsAndConditions = termsAndConditions;
    }

    public String getDataPrivacyConsent() {
        return dataPrivacyConsent;
    }

    public String getDataPrivacyDeclaration() {
        return dataPrivacyDeclaration;
    }

    public String getTermsAndConditions() {
        return termsAndConditions;
    }
}
