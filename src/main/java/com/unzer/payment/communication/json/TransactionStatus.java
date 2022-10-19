package com.unzer.payment.communication.json;

public interface TransactionStatus {
    Boolean getSuccess();
    Boolean getError();
    Boolean getPending();
    Boolean getResumed();

    void setSuccess(Boolean value);
    void setError(Boolean value);
    void setPending(Boolean value);
    void setResumed(Boolean value);
}
