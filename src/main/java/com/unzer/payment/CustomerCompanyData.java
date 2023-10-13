package com.unzer.payment;

import com.google.gson.annotations.SerializedName;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CustomerCompanyData {
  private CompanyType companyType;
  private Owner owner;
  private RegistrationType registrationType;
  private String commercialRegisterNumber;
  private CommercialSector commercialSector;

  public CompanyType getCompanyType() {
    return companyType;
  }

  public CustomerCompanyData setCompanyType(CompanyType companyType) {
    this.companyType = companyType;
    return this;
  }

  public Owner getOwner() {
    return owner;
  }

  public CustomerCompanyData setOwner(Owner owner) {
    this.owner = owner;
    return this;
  }

  public RegistrationType getRegistrationType() {
    return registrationType;
  }

  public CustomerCompanyData setRegistrationType(RegistrationType registrationType) {
    this.registrationType = registrationType;
    return this;
  }

  public String getCommercialRegisterNumber() {
    return commercialRegisterNumber;
  }

  public CustomerCompanyData setCommercialRegisterNumber(String commercialRegisterNumber) {
    this.commercialRegisterNumber = commercialRegisterNumber;
    return this;
  }

  public CommercialSector getCommercialSector() {
    return commercialSector;
  }

  public CustomerCompanyData setCommercialSector(CommercialSector commercialSector) {
    this.commercialSector = commercialSector;
    return this;
  }

  public enum CompanyType {
    @SerializedName("authority")
    AUTHORITY,
    @SerializedName("association")
    ASSOCIATION,
    @SerializedName("sole")
    SOLE,
    @SerializedName("company")
    COMPANY,
    @SerializedName("other")
    OTHER
  }

  public enum RegistrationType { REGISTERED, NOT_REGISTERED }

  public static class Owner {
    private static final SimpleDateFormat yyyy_MM_dd_DateFormat =
        new SimpleDateFormat("yyyy-MM-dd");
    private static final SimpleDateFormat dd_MM_yyyyDateFormat = new SimpleDateFormat("dd.MM.yyyy");
    private String firstname;
    private String lastname;
    private String birthdate;

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

    public Date getBirthdate() {
      if (this.birthdate == null) {
        return null;
      }

      Date result;
      try {
        if (this.birthdate.contains("-")) {
          result = yyyy_MM_dd_DateFormat.parse(this.birthdate);
        } else {
          result = dd_MM_yyyyDateFormat.parse(this.birthdate);
        }
      } catch (ParseException e) {
        throw new RuntimeException(e);
      }

      return result;
    }

    public void setBirthdate(Date birthdate) {
      this.birthdate = yyyy_MM_dd_DateFormat.format(birthdate);
    }
  }

}
