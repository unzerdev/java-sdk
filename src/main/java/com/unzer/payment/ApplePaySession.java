package com.unzer.payment;

public class ApplePaySession {
    private String merchantIdentifier;
    private String displayName;
    private String domainName;

    public ApplePaySession(String merchantIdentifier, String displayName, String domainName) {
        this.merchantIdentifier = merchantIdentifier;
        this.displayName = displayName;
        this.domainName = domainName;
    }

    public String getMerchantIdentifier() {
        return merchantIdentifier;
    }

    public void setMerchantIdentifier(String merchantIdentifier) {
        this.merchantIdentifier = merchantIdentifier;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getDomainName() {
        return domainName;
    }

    public void setDomainName(String domainName) {
        this.domainName = domainName;
    }
}
