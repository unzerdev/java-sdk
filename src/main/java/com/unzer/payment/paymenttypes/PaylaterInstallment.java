package com.unzer.payment.paymenttypes;

import com.unzer.payment.GeoLocation;
import com.unzer.payment.communication.json.ApiPaylaterInstallment;
import com.unzer.payment.communication.json.ApiIdObject;
import com.unzer.payment.communication.json.ApiObject;

public class PaylaterInstallment extends AbstractPaymentType implements PaymentType {

  private String inquiryId;
  private Integer numberOfRates;
  private String iban;
  private String country;
  private String holder;

  public String getInquiryId() {
    return inquiryId;
  }

  public PaylaterInstallment setInquiryId(String inquiryId) {
    this.inquiryId = inquiryId;
    return this;
  }

  public Integer getNumberOfRates() {
    return numberOfRates;
  }

  public PaylaterInstallment setNumberOfRates(Integer numberOfRates) {
    this.numberOfRates = numberOfRates;
    return this;
  }

  public String getIban() {
    return iban;
  }

  public PaylaterInstallment setIban(String iban) {
    this.iban = iban;
    return this;
  }

  public String getCountry() {
    return country;
  }

  public PaylaterInstallment setCountry(String country) {
    this.country = country;
    return this;
  }

  public String getHolder() {
    return holder;
  }

  public PaylaterInstallment setHolder(String holder) {
    this.holder = holder;
    return this;
  }

  @Override
  public PaymentType map(PaymentType pit, ApiObject jsonPit) {
    ((PaylaterInstallment) pit).setId(jsonPit.getId());
    ((PaylaterInstallment) pit).setInquiryId(((ApiPaylaterInstallment) jsonPit).getInquiryId());
    ((PaylaterInstallment) pit).setNumberOfRates(
        ((ApiPaylaterInstallment) jsonPit).getNumberOfRates());
    ((PaylaterInstallment) pit).setIban(((ApiPaylaterInstallment) jsonPit).getIban());
    ((PaylaterInstallment) pit).setCountry(((ApiPaylaterInstallment) jsonPit).getCountry());
    ((PaylaterInstallment) pit).setHolder(((ApiPaylaterInstallment) jsonPit).getHolder());
    ((PaylaterInstallment) pit).setRecurring(((ApiPaylaterInstallment) jsonPit).getRecurring());
    GeoLocation geoLocation =
        new GeoLocation(((ApiIdObject) jsonPit).getGeoLocation().getClientIp(),
            ((ApiIdObject) jsonPit).getGeoLocation().getCountryIsoA2());
    ((PaylaterInstallment) pit).setGeoLocation(geoLocation);
    return pit;
  }

  @Override
  public String getTypeUrl() {
    return "types/paylater-installment";
  }
}
