package com.unzer.payment;

public class GeoLocation {
  private String clientIp;
  private String countryIsoA2;

  public GeoLocation(String clientIp, String countryIsoA2) {
    this.clientIp = clientIp;
    this.countryIsoA2 = countryIsoA2;
  }

  public String getClientIp() {
    return clientIp;
  }

  public GeoLocation setClientIp(String clientIp) {
    this.clientIp = clientIp;
    return this;
  }

  public String getCountryIsoA2() {
    return countryIsoA2;
  }

  public GeoLocation setCountryIsoA2(String countryIsoA2) {
    this.countryIsoA2 = countryIsoA2;
    return this;
  }
}
