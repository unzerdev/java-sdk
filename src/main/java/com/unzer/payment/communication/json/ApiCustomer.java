package com.unzer.payment.communication.json;

import com.google.gson.annotations.JsonAdapter;
import com.unzer.payment.Address;
import com.unzer.payment.Customer.Salutation;
import com.unzer.payment.ShippingAddress;
import com.unzer.payment.communication.JsonDateConverter;
import java.util.Date;

public class ApiCustomer extends ApiIdObject implements ApiObject {
  private String firstname;
  private String lastname;
  private String company;
  private Salutation salutation;
  private String customerId;

  @JsonAdapter(JsonDateConverter.class)
  private Date birthDate;
  private String email;
  private String phone;
  private String mobile;
  private Address billingAddress;
  private ShippingAddress shippingAddress;
  private String language;
  private JsonCompanyInfo companyInfo;

  public String getFirstname() {
    return firstname;
  }

  public void setFirstname(String firstname) {
    this.firstname = firstname;
  }

  public String getLastname() {
    return lastname;
  }

  public void setLastname(String lastname) {
    this.lastname = lastname;
  }

  public Salutation getSalutation() {
    return salutation;
  }

  public void setSalutation(Salutation salutation) {
    this.salutation = salutation;
  }

  public String getCustomerId() {
    return customerId;
  }

  public void setCustomerId(String customerId) {
    this.customerId = customerId;
  }

  public Date getBirthDate() {
    return birthDate;
  }

  public void setBirthDate(Date birthDate) {
    this.birthDate = birthDate;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public String getMobile() {
    return mobile;
  }

  public void setMobile(String mobile) {
    this.mobile = mobile;
  }

  public Address getBillingAddress() {
    return billingAddress;
  }

  public void setBillingAddress(Address billingAddress) {
    this.billingAddress = billingAddress;
  }

  public ShippingAddress getShippingAddress() {
    return shippingAddress;
  }

  public void setShippingAddress(ShippingAddress shippingAddress) {
    this.shippingAddress = shippingAddress;
  }

  public JsonCompanyInfo getCompanyInfo() {
    return companyInfo;
  }

  public void setCompanyInfo(JsonCompanyInfo companyInfo) {
    this.companyInfo = companyInfo;
  }

  public String getCompany() {
    return company;
  }

  public void setCompany(String company) {
    this.company = company;
  }

  public String getLanguage() {
    return language;
  }

  public void setLanguage(String language) {
    this.language = language;
  }
}
