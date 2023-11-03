package com.unzer.payment;

import com.unzer.payment.communication.JsonFieldIgnore;
import com.unzer.payment.models.AdditionalTransactionData;
import java.math.BigDecimal;
import java.net.URL;
import java.util.Currency;
import java.util.Date;

public abstract class BaseTransaction<T extends BasePayment> implements Resource {
  protected final static String TRANSACTION_ID_TOKEN = "<transactionId>";
  protected final static String PAYMENT_ID_TOKEN = "<paymentId>";
  private String id;
  private BigDecimal amount;
  private Currency currency;
  private URL returnUrl;
  private Boolean card3ds;
  private String orderId;
  private String invoiceId;
  private String typeId;
  private String customerId;
  private String metadataId;
  private String paymentId;
  private String riskId;
  private String basketId;
  private String paypageId;
  private String paymentReference;
  private Status status;
  private URL redirectUrl;
  private Processing processing = new Processing();
  private String traceId;
  private Message message;
  private Date date;
  private String type;
  private AdditionalTransactionData additionalTransactionData;
  @JsonFieldIgnore
  private transient T payment;
  @JsonFieldIgnore
  private transient Unzer unzer;
  @JsonFieldIgnore
  private URL resourceUrl;

  public BaseTransaction() {
    super();
  }

  @Deprecated
  public BaseTransaction(Unzer unzer) {
    this.unzer = unzer;
  }

  @Override
  public int hashCode() {
    int result = getId() != null ? getId().hashCode() : 0;
    result = 31 * result + (getAmount() != null ? getAmount().hashCode() : 0);
    result = 31 * result + (getCurrency() != null ? getCurrency().hashCode() : 0);
    result = 31 * result + (getReturnUrl() != null ? getReturnUrl().hashCode() : 0);
    result = 31 * result + (getCard3ds() != null ? getCard3ds().hashCode() : 0);
    result = 31 * result + (getOrderId() != null ? getOrderId().hashCode() : 0);
    result = 31 * result + (getInvoiceId() != null ? getInvoiceId().hashCode() : 0);
    result = 31 * result + (getTypeId() != null ? getTypeId().hashCode() : 0);
    result = 31 * result + (getCustomerId() != null ? getCustomerId().hashCode() : 0);
    result = 31 * result + (getMetadataId() != null ? getMetadataId().hashCode() : 0);
    result = 31 * result + (getPaymentId() != null ? getPaymentId().hashCode() : 0);
    result = 31 * result + (getRiskId() != null ? getRiskId().hashCode() : 0);
    result = 31 * result + (getBasketId() != null ? getBasketId().hashCode() : 0);
    result = 31 * result + (getPaypageId() != null ? getPaypageId().hashCode() : 0);
    result = 31 * result + (getPaymentReference() != null ? getPaymentReference().hashCode() : 0);
    result = 31 * result + (getStatus() != null ? getStatus().hashCode() : 0);
    result = 31 * result + (getRedirectUrl() != null ? getRedirectUrl().hashCode() : 0);
    result = 31 * result + (getTraceId() != null ? getTraceId().hashCode() : 0);
    result = 31 * result + (getMessage() != null ? getMessage().hashCode() : 0);
    result = 31 * result + (getDate() != null ? getDate().hashCode() : 0);
    result = 31 * result + (getType() != null ? getType().hashCode() : 0);
    result = 31 * result
        + (getAdditionalTransactionData() != null ? getAdditionalTransactionData().hashCode() : 0);
    result = 31 * result + (getTransactionUrl() != null ? getTransactionUrl().hashCode() : 0);
    return result;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    BaseTransaction<?> that = (BaseTransaction<?>) o;

    if (getId() != null ? !getId().equals(that.getId()) : that.getId() != null) {
      return false;
    }

    if (getAmount() != null ? getAmount().compareTo(that.getAmount()) != 0 :
        that.getAmount() != null) {
      return false;
    }
    if (getCurrency() != null ? !getCurrency().equals(that.getCurrency()) :
        that.getCurrency() != null) {
      return false;
    }
    if (getReturnUrl() != null ? !getReturnUrl().equals(that.getReturnUrl()) :
        that.getReturnUrl() != null) {
      return false;
    }
    if (getCard3ds() != null ? !getCard3ds().equals(that.getCard3ds()) :
        that.getCard3ds() != null) {
      return false;
    }
    if (getOrderId() != null ? !getOrderId().equals(that.getOrderId()) :
        that.getOrderId() != null) {
      return false;
    }
    if (getInvoiceId() != null ? !getInvoiceId().equals(that.getInvoiceId()) :
        that.getInvoiceId() != null) {
      return false;
    }
    if (getTypeId() != null ? !getTypeId().equals(that.getTypeId()) : that.getTypeId() != null) {
      return false;
    }
    if (getCustomerId() != null ? !getCustomerId().equals(that.getCustomerId()) :
        that.getCustomerId() != null) {
      return false;
    }
    if (getMetadataId() != null ? !getMetadataId().equals(that.getMetadataId()) :
        that.getMetadataId() != null) {
      return false;
    }
    if (getPaymentId() != null ? !getPaymentId().equals(that.getPaymentId()) :
        that.getPaymentId() != null) {
      return false;
    }
    if (getRiskId() != null ? !getRiskId().equals(that.getRiskId()) : that.getRiskId() != null) {
      return false;
    }
    if (getBasketId() != null ? !getBasketId().equals(that.getBasketId()) :
        that.getBasketId() != null) {
      return false;
    }
    if (getPaypageId() != null ? !getPaypageId().equals(that.getPaypageId()) :
        that.getPaypageId() != null) {
      return false;
    }
    if (getPaymentReference() != null ? !getPaymentReference().equals(that.getPaymentReference()) :
        that.getPaymentReference() != null) {
      return false;
    }
    if (getStatus() != that.getStatus()) {
      return false;
    }
    if (getRedirectUrl() != null ? !getRedirectUrl().equals(that.getRedirectUrl()) :
        that.getRedirectUrl() != null) {
      return false;
    }
    if (getTraceId() != null ? !getTraceId().equals(that.getTraceId()) :
        that.getTraceId() != null) {
      return false;
    }
    if (getMessage() != null ? !getMessage().equals(that.getMessage()) :
        that.getMessage() != null) {
      return false;
    }
    if (getDate() != null ? !getDate().equals(that.getDate()) : that.getDate() != null) {
      return false;
    }
    if (getType() != null ? !getType().equals(that.getType()) : that.getType() != null) {
      return false;
    }
    if (getAdditionalTransactionData() != null
        ? !getAdditionalTransactionData().equals(that.getAdditionalTransactionData()) :
        that.getAdditionalTransactionData() != null) {
      return false;
    }

    if (getProcessing() != null
        ? !getProcessing().equals(that.getProcessing()) :
        that.getProcessing() != null) {
      return false;
    }

    return getTransactionUrl() != null ? getTransactionUrl().equals(that.getTransactionUrl()) :
        that.getTransactionUrl() == null;
  }

  public Processing getProcessing() {
    return processing;
  }

  public BaseTransaction<T> setProcessing(Processing processing) {
    this.processing = processing;
    return this;
  }

  public String getId() {
    return id;
  }

  public BigDecimal getAmount() {
    return amount;
  }

  public BaseTransaction<T> setAmount(BigDecimal amount) {
    this.amount = amount;
    return this;
  }

  public Currency getCurrency() {
    return currency;
  }

  public BaseTransaction<T> setCurrency(Currency currency) {
    this.currency = currency;
    return this;
  }

  public URL getReturnUrl() {
    return returnUrl;
  }

  public BaseTransaction<T> setReturnUrl(URL returnUrl) {
    this.returnUrl = returnUrl;
    return this;
  }

  public Boolean getCard3ds() {
    return card3ds;
  }

  public BaseTransaction<T> setCard3ds(Boolean card3ds) {
    this.card3ds = card3ds;
    return this;
  }

  public String getOrderId() {
    return orderId;
  }

  public BaseTransaction<T> setOrderId(String orderId) {
    this.orderId = orderId;
    return this;
  }

  public String getInvoiceId() {
    return invoiceId;
  }

  public String getTypeId() {
    return typeId;
  }

  public BaseTransaction<T> setTypeId(String typeId) {
    this.typeId = typeId;
    return this;
  }

  public String getCustomerId() {
    return customerId;
  }

  public BaseTransaction<T> setCustomerId(String customerId) {
    this.customerId = customerId;
    return this;
  }

  public String getMetadataId() {
    return metadataId;
  }

  public BaseTransaction<T> setMetadataId(String metadataId) {
    this.metadataId = metadataId;
    return this;
  }

  public String getPaymentId() {
    return paymentId;
  }

  public String getRiskId() {
    return riskId;
  }

  public BaseTransaction<T> setRiskId(String riskId) {
    this.riskId = riskId;
    return this;
  }

  public String getBasketId() {
    return basketId;
  }

  public BaseTransaction<T> setBasketId(String basketId) {
    this.basketId = basketId;
    return this;
  }

  public String getPaypageId() {
    return paypageId;
  }

  public String getPaymentReference() {
    return paymentReference;
  }

  public Status getStatus() {
    return status;
  }

  public BaseTransaction<T> setStatus(Status status) {
    this.status = status;
    return this;
  }

  public URL getRedirectUrl() {
    return redirectUrl;
  }

  public void setRedirectUrl(URL redirectUrl) {
    this.redirectUrl = redirectUrl;
  }

  public String getTraceId() {
    return traceId;
  }

  public BaseTransaction<T> setTraceId(String traceId) {
    this.traceId = traceId;
    return this;
  }

  public Message getMessage() {
    return message;
  }

  public BaseTransaction<T> setMessage(Message message) {
    this.message = message;
    return this;
  }

  public Date getDate() {
    return date;
  }

  public BaseTransaction<T> setDate(Date date) {
    this.date = date;
    return this;
  }

  public String getType() {
    return type;
  }

  public AdditionalTransactionData getAdditionalTransactionData() {
    return additionalTransactionData;
  }

  public BaseTransaction<T> setAdditionalTransactionData(
      AdditionalTransactionData additionalTransactionData) {
    this.additionalTransactionData = additionalTransactionData;
    return this;
  }

  public URL getResourceUrl() {
    return resourceUrl;
  }

  public BaseTransaction<T> setResourceUrl(URL resourceUrl) {
    this.resourceUrl = resourceUrl;
    return this;
  }

  public BaseTransaction<T> setType(String type) {
    this.type = type;
    return this;
  }

  public BaseTransaction<T> setPaymentReference(String paymentReference) {
    this.paymentReference = paymentReference;
    return this;
  }

  public BaseTransaction<T> setPaypageId(String paypageId) {
    this.paypageId = paypageId;
    return this;
  }

  public BaseTransaction<T> setPaymentId(String paymentId) {
    this.paymentId = paymentId;
    return this;
  }

  public BaseTransaction<T> setInvoiceId(String invoiceId) {
    this.invoiceId = invoiceId;
    return this;
  }

  public BaseTransaction<T> setId(String id) {
    this.id = id;
    return this;
  }

  @Override
  public String getUrl() {
    String partialResult = getPaymentId() == null
        ? getTransactionUrl().replaceAll(PAYMENT_ID_TOKEN + "/", "")
        : getTransactionUrl().replaceAll(PAYMENT_ID_TOKEN, getPaymentId());

    return partialResult
        .replaceAll(TRANSACTION_ID_TOKEN, getId() == null ? "" : getId());
  }

  protected abstract String getTransactionUrl();

  public T getPayment() {
    return payment;
  }

  public BaseTransaction<T> setPayment(T payment) {
    this.payment = payment;
    return this;
  }

  @Deprecated
  public Unzer getUnzer() {
    return unzer;
  }

  @Deprecated
  public BaseTransaction<T> setUnzer(Unzer unzer) {
    this.unzer = unzer;
    return this;
  }

  public enum Status {
    SUCCESS, PENDING, ERROR, RESUMED
  }
}
