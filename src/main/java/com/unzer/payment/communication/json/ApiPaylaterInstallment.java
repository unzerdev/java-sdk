package com.unzer.payment.communication.json;

public class ApiPaylaterInstallment extends ApiIdObject implements ApiObject {
  private String inquiryId;
  private Integer numberOfRates;
  private String iban;
  private String country;
  private String holder;

  public String getInquiryId() {
    return inquiryId;
  }

  public ApiPaylaterInstallment setInquiryId(String inquiryId) {
    this.inquiryId = inquiryId;
    return this;
  }

  public Integer getNumberOfRates() {
    return numberOfRates;
  }

  public ApiPaylaterInstallment setNumberOfRates(Integer numberOfRates) {
    this.numberOfRates = numberOfRates;
    return this;
  }

  public String getIban() {
    return iban;
  }

  public ApiPaylaterInstallment setIban(String iban) {
    this.iban = iban;
    return this;
  }

  public String getCountry() {
    return country;
  }

  public ApiPaylaterInstallment setCountry(String country) {
    this.country = country;
    return this;
  }

  public String getHolder() {
    return holder;
  }

  public ApiPaylaterInstallment setHolder(String holder) {
    this.holder = holder;
    return this;
  }
}
