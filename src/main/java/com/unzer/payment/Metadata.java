package com.unzer.payment;

import com.unzer.payment.communication.json.ApiObject;
import com.unzer.payment.paymenttypes.PaymentType;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

public class Metadata implements PaymentType {
  private String id;
  private Map<String, String> metadataMap;
  private Unzer unzer;

  public Metadata() {
    this(false);
  }

  public Metadata(boolean sorted) {
    super();
    if (sorted) {
      metadataMap = new TreeMap<String, String>();
    } else {
      metadataMap = new LinkedHashMap<String, String>();
    }
  }

  public Metadata addMetadata(String key, String value) {
    getMetadataMap().put(key, value);
    return this;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  @Override
  public PaymentType map(PaymentType paymentType, ApiObject apiObject) {
    return null;
  }

  @Override
  public String getTypeUrl() {
    return "metadata";
  }

  @Deprecated
  public Unzer getUnzer() {
    return unzer;
  }

  @Deprecated
  public void setUnzer(Unzer unzer) {
    this.unzer = unzer;
  }

  public Map<String, String> getMetadataMap() {
    return metadataMap;
  }

  public void setMetadataMap(Map<String, String> metadataMap) {
    this.metadataMap = metadataMap;
  }
}
