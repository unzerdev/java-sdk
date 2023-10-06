package com.unzer.payment;

public class Paypage extends BasePaypage {
  @Override
  public String getTypeUrl() {
    return "paypage";
  }

  /**
   * @deprecated Use com.unzer.payment.BasePaypage#AUTHORIZE or com.unzer.payment.BasePaypage#CHARGE
   */
  @Deprecated
  public interface Action {
    String CHARGE = "CHARGE";
    String AUTHORIZE = "AUTHORIZE";
  }
}
