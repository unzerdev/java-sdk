package com.unzer.payment.paymenttypes;

import com.unzer.payment.GeoLocation;
import com.unzer.payment.communication.json.ApiCard;
import com.unzer.payment.communication.json.ApiIdObject;
import com.unzer.payment.communication.json.ApiObject;
import com.unzer.payment.models.googlepay.IntermediateSigningKey;
import com.unzer.payment.models.googlepay.SignedMessage;

/**
 * Google Pay business object. It requires data from payment method token returned by Google in the `PaymentData`.
 * These data are used to create the payment type on the Unzer API.
 */
public class GooglePay extends BasePaymentType {
  private String protocolVersion;
  private String signature;
  private IntermediateSigningKey intermediateSigningKey;
  private SignedMessage signedMessage;
  private String number;
  private String expiryDate;

  public GooglePay() {
  }

  public GooglePay(String protocolVersion, String signature,
                   IntermediateSigningKey intermediateSigningKey, SignedMessage signedMessage) {
    this.protocolVersion = protocolVersion;
    this.signature = signature;
    this.intermediateSigningKey = intermediateSigningKey;
    this.signedMessage = signedMessage;
  }

  public String getProtocolVersion() {
    return protocolVersion;
  }

  public GooglePay setProtocolVersion(String protocolVersion) {
    this.protocolVersion = protocolVersion;
    return this;
  }

  public String getSignature() {
    return signature;
  }

  public GooglePay setSignature(String signature) {
    this.signature = signature;
    return this;
  }

  public IntermediateSigningKey getIntermediateSigningKey() {
    return intermediateSigningKey;
  }

  public GooglePay setIntermediateSigningKey(
      IntermediateSigningKey intermediateSigningKey) {
    this.intermediateSigningKey = intermediateSigningKey;
    return this;
  }

  public SignedMessage getSignedMessage() {
    return signedMessage;
  }

  public GooglePay setSignedMessage(SignedMessage signedMessage) {
    this.signedMessage = signedMessage;
    return this;
  }

  public String getNumber() {
    return number;
  }

  public GooglePay setNumber(String number) {
    this.number = number;
    return this;
  }

  public String getExpiryDate() {
    return expiryDate;
  }

  public GooglePay setExpiryDate(String expiryDate) {
    this.expiryDate = expiryDate;
    return this;
  }

  @Override
  protected String getResourceUrl() {
    return "/v1/types/googlepay/<resourceId>";
  }

  @Override
  public PaymentType map(PaymentType googlepay, ApiObject jsonId) {
    ((GooglePay) googlepay).setId(jsonId.getId());
    ((GooglePay) googlepay).setRecurring(((ApiIdObject) jsonId).getRecurring());
    ((GooglePay) googlepay).setNumber(((ApiCard) jsonId).getNumber());
    ((GooglePay) googlepay).setExpiryDate(((ApiCard) jsonId).getExpiryDate());
    GeoLocation tempGeoLocation =
        new GeoLocation(((ApiIdObject) jsonId).getGeoLocation().getClientIp(),
            ((ApiIdObject) jsonId).getGeoLocation().getCountryIsoA2());
    ((GooglePay) googlepay).setGeoLocation(tempGeoLocation);
    return googlepay;
  }
}
