package com.unzer.payment.communication.json;

public class JsonGeoLocation {
  private String clientIp;
  private String countryIsoA2;

  public String getClientIp() {
    return clientIp;
  }

  public void setClientIp(String clientIp) {
    this.clientIp = clientIp;
  }

  public String getCountryIsoA2() {
    return countryIsoA2;
  }

  public void setCountryIsoA2(String countryIsoA2) {
    this.countryIsoA2 = countryIsoA2;
  }
}
