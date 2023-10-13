package com.unzer.payment.communication.json;

import com.unzer.payment.models.AdditionalTransactionData;
import java.net.URL;
import java.util.Date;

public class ApiRecurring extends ApiIdObject implements ApiObject, TransactionStatus {
  private Boolean isSuccess;
  private Boolean isPending;
  private Boolean isError;
  private JsonMessage message;
  private Date date;

  private URL returnUrl;

  private JsonResources resources;
  private JsonProcessing processing = new JsonProcessing();
  private URL redirectUrl;
  private AdditionalTransactionData additionalTransactionData;

  public ApiRecurring() {
    super();
  }

  public URL getReturnUrl() {
    return returnUrl;
  }

  public ApiRecurring setReturnUrl(URL returnUrl) {
    this.returnUrl = returnUrl;
    return this;
  }

  public JsonProcessing getProcessing() {
    return processing;
  }

  public ApiRecurring setProcessing(JsonProcessing processing) {
    this.processing = processing;
    return this;
  }


  public JsonResources getResources() {
    return resources;
  }

  public void setResources(JsonResources resources) {
    this.resources = resources;
  }

  public JsonMessage getMessage() {
    return message;
  }

  public void setMessage(JsonMessage message) {
    this.message = message;
  }

  public Date getDate() {
    return date;
  }

  public void setDate(Date date) {
    this.date = date;
  }

  public Boolean getSuccess() {
    return isSuccess;
  }

  public void setSuccess(Boolean isSuccess) {
    this.isSuccess = isSuccess;
  }

  public Boolean getPending() {
    return isPending;
  }

  public void setPending(Boolean isPending) {
    this.isPending = isPending;
  }

  public Boolean getError() {
    return isError;
  }

  public void setError(Boolean isError) {
    this.isError = isError;
  }

  public Boolean getResumed() {
    return null;
  }

  public void setResumed(Boolean value) {
    // Do nothing
  }

  public URL getRedirectUrl() {
    return redirectUrl;
  }

  public void setRedirectUrl(URL redirectUrl) {
    this.redirectUrl = redirectUrl;
  }

  public AdditionalTransactionData getAdditionalTransactionData() {
    return additionalTransactionData;
  }

  public void setAdditionalTransactionData(AdditionalTransactionData additionalTransactionData) {
    this.additionalTransactionData = additionalTransactionData;
  }
}
