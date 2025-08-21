package com.unzer.payment.models;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Data;

import java.util.Objects;

@Data
@JsonTypeName("additionalTransactionDataModel")
@JsonTypeInfo(include = JsonTypeInfo.As.WRAPPER_OBJECT, use = JsonTypeInfo.Id.NAME)
public class AdditionalTransactionData {
    private CardTransactionData card;
    private ShippingTransactionData shipping;
    private RiskData riskData;
    private PaypalData paypal;
    private WeroTransactionData wero;

    /**
     * URL to the merchant's Terms and Conditions Page
     */
    private String termsAndConditionUrl;

    /**
     * URL to the merchant's Privacy Policy Page
     */
    private String privacyPolicyUrl;



    /**
     * URL to the merchant's Terms and Conditions Page
     */
    public String getTermsAndConditionsUrl() {
        return termsAndConditionUrl;
    }

    /**
     * URL to the merchant's Terms and Conditions Page
     */
    public AdditionalTransactionData setTermsAndConditionsUrl(String termsAndConditionsUrl) {
        this.termsAndConditionUrl = termsAndConditionsUrl;
        return this;
    }

    /**
     * URL to the merchant's Privacy Policy Page
     */
    public String getPrivacyPolicyUrl() {
        return privacyPolicyUrl;
    }

    /**
     * URL to the merchant's Privacy Policy Page
     */
    public AdditionalTransactionData setPrivacyPolicyUrl(String privacyPolicyUrl) {
        this.privacyPolicyUrl = privacyPolicyUrl;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        AdditionalTransactionData that = (AdditionalTransactionData) o;

        if (!Objects.equals(card, that.card)) {
            return false;
        }
        if (!Objects.equals(shipping, that.shipping)) {
            return false;
        }
        if (!Objects.equals(riskData, that.riskData)) {
            return false;
        }
        if (!Objects.equals(paypal, that.paypal)) {
            return false;
        }
        if (!Objects.equals(wero, that.wero)) {
            return false;
        }
        if (!Objects.equals(termsAndConditionUrl, that.termsAndConditionUrl)) {
            return false;
        }
        return Objects.equals(privacyPolicyUrl, that.privacyPolicyUrl);
    }

    @Override
    public int hashCode() {
        int result = card != null ? card.hashCode() : 0;
        result = 31 * result + (shipping != null ? shipping.hashCode() : 0);
        result = 31 * result + (riskData != null ? riskData.hashCode() : 0);
        result = 31 * result + (paypal != null ? paypal.hashCode() : 0);
        result = 31 * result + (wero != null ? wero.hashCode() : 0);
        result = 31 * result + (termsAndConditionUrl != null ? termsAndConditionUrl.hashCode() : 0);
        result = 31 * result + (privacyPolicyUrl != null ? privacyPolicyUrl.hashCode() : 0);
        return result;
    }
}
