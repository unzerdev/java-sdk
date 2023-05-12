/*
 * Copyright 2020-today Unzer E-Com GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.unzer.payment.models;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.util.Objects;

@JsonTypeName("additionalTransactionDataModel")
@JsonTypeInfo(include = JsonTypeInfo.As.WRAPPER_OBJECT, use = JsonTypeInfo.Id.NAME)
public class AdditionalTransactionData {
  private CardTransactionData card;
  private ShippingTransactionData shipping;
  private RiskData riskData;
  private PaypalData paypal;

  /**
   * URL to the merchant's Terms and Conditions Page
   */
  private String termsAndConditionUrl;

  /**
   * URL to the merchant's Privacy Policy Page
   */
  private String privacyPolicyUrl;

  public CardTransactionData getCard() {
    return card;
  }

  public AdditionalTransactionData setCard(CardTransactionData card) {
    this.card = card;
    return this;
  }

  public ShippingTransactionData getShipping() {
    return shipping;
  }

  public AdditionalTransactionData setShipping(ShippingTransactionData shipping) {
    this.shipping = shipping;
    return this;
  }

  public RiskData getRiskData() {
    return riskData;
  }

  public AdditionalTransactionData setRiskData(RiskData riskData) {
    this.riskData = riskData;
    return this;
  }

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

  public PaypalData getPaypal() {
    return paypal;
  }

  public AdditionalTransactionData setPaypal(PaypalData paypal) {
    this.paypal = paypal;
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
    result = 31 * result + (termsAndConditionUrl != null ? termsAndConditionUrl.hashCode() : 0);
    result = 31 * result + (privacyPolicyUrl != null ? privacyPolicyUrl.hashCode() : 0);
    return result;
  }
}
