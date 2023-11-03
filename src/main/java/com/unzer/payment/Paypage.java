package com.unzer.payment;

public class Paypage extends BasePaypage {
  @Override
  public String getTypeUrl() {
    return "paypage";
  }

  @Override
  protected String getResourceUrl() {
    return "/v1/paypage/<resourceId>";
  }
}
