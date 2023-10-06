package com.unzer.payment.communication.json;

public interface TransactionStatus {
  Boolean getSuccess();

  void setSuccess(Boolean value);

  Boolean getError();

  void setError(Boolean value);

  Boolean getPending();

  void setPending(Boolean value);

  Boolean getResumed();

  void setResumed(Boolean value);
}
