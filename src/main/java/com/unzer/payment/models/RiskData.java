package com.unzer.payment.models;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class RiskData {
  // must be double. See UPL specification https://unz.atlassian.net/wiki/spaces/UCC/pages/892436661/Tech+Spec+-+UPL+invoice+type+integration+to+SDKs
  private static final DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
  private String threatMetrixId;
  private Integer registrationLevel;
  private String registrationDate;
  private String customerId;
  private CustomerGroup customerGroup;
  private String confirmedOrders;
  // must be int. See UPL specification https://unz.atlassian.net/wiki/spaces/UCC/pages/892436661/Tech+Spec+-+UPL+invoice+type+integration+to+SDKs
  private String confirmedAmount;

  public String getThreatMetrixId() {
    return threatMetrixId;
  }

  public RiskData setThreatMetrixId(String threatMetrixId) {
    this.threatMetrixId = threatMetrixId;
    return this;
  }

  public Integer getRegistrationLevel() {
    return registrationLevel;
  }

  public RiskData setRegistrationLevel(Integer registrationLevel) {
    this.registrationLevel = registrationLevel;
    return this;
  }

  public Date getRegistrationDate() {
    try {
      return dateFormat.parse(this.registrationDate);
    } catch (ParseException e) {
      throw new RuntimeException(
          "Failed to parse internal string representation: " + this.registrationDate,
          e
      );
    }
  }

  public RiskData setRegistrationDate(Date registrationDate) {
    this.registrationDate = dateFormat.format(registrationDate);
    return this;
  }

  public String getCustomerId() {
    return customerId;
  }

  public RiskData setCustomerId(String customerId) {
    this.customerId = customerId;
    return this;
  }

  public CustomerGroup getCustomerGroup() {
    return customerGroup;
  }

  public RiskData setCustomerGroup(CustomerGroup customerGroup) {
    this.customerGroup = customerGroup;
    return this;
  }

  public Integer getConfirmedOrders() {
    if (this.confirmedOrders == null) {
      return null;
    }
    return Integer.valueOf(confirmedOrders);
  }

  public RiskData setConfirmedOrders(Integer confirmedOrders) {
    if (confirmedOrders == null) {
      this.confirmedOrders = null;
    } else {
      this.confirmedOrders = confirmedOrders.toString();
    }
    return this;
  }

  public Double getConfirmedAmount() {
    if (this.confirmedAmount == null) {
      return null;
    }
    return Double.valueOf(confirmedAmount);
  }

  public RiskData setConfirmedAmount(Double confirmedAmount) {
    if (confirmedAmount == null) {
      this.confirmedAmount = null;
    } else {
      this.confirmedAmount = confirmedAmount.toString();
    }
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

    RiskData riskData = (RiskData) o;

    if (!Objects.equals(threatMetrixId, riskData.threatMetrixId)) {
      return false;
    }
    if (!Objects.equals(registrationLevel, riskData.registrationLevel)) {
      return false;
    }
    if (!Objects.equals(registrationDate, riskData.registrationDate)) {
      return false;
    }
    if (!Objects.equals(customerId, riskData.customerId)) {
      return false;
    }
    if (customerGroup != riskData.customerGroup) {
      return false;
    }
    if (!Objects.equals(confirmedOrders, riskData.confirmedOrders)) {
      return false;
    }
    return Objects.equals(confirmedAmount, riskData.confirmedAmount);
  }

  @Override
  public int hashCode() {
    int result = threatMetrixId != null ? threatMetrixId.hashCode() : 0;
    result = 31 * result + (registrationLevel != null ? registrationLevel.hashCode() : 0);
    result = 31 * result + (registrationDate != null ? registrationDate.hashCode() : 0);
    result = 31 * result + (customerId != null ? customerId.hashCode() : 0);
    result = 31 * result + (customerGroup != null ? customerGroup.hashCode() : 0);
    result = 31 * result + (confirmedOrders != null ? confirmedOrders.hashCode() : 0);
    result = 31 * result + (confirmedAmount != null ? confirmedAmount.hashCode() : 0);
    return result;
  }

  /**
   * Customer classification for the customer
   */
  public enum CustomerGroup {
    TOP, GOOD, NEUTRAL, BAD
  }

  /**
   * Customer registration level 0=guest, 1=registered
   */
  public interface RegistrationLevel {
    Integer GUEST = 0;
    Integer REGISTERED = 1;
  }
}
