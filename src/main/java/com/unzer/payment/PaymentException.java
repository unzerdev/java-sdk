package com.unzer.payment;

import java.util.ArrayList;
import java.util.List;

/**
 * The {@code PaymentException} represents an Api Error as described here:
 */
public class PaymentException extends RuntimeException {

  private static final String EMPTY_STRING = "";
  private static final int STATUS_CODE_ZERO = 0;
  private final transient List<PaymentError> paymentErrorList;
  private final String timestamp;
  private final String url;
  private final Integer statusCode;
  private final String id;

  /**
   * Creates an unspecific {@code PaymentException}, however should be avoided,
   * specific Exceptions are preferred.
   *
   * @param message the message
   */
  public PaymentException(String message) {
    this(EMPTY_STRING, STATUS_CODE_ZERO, EMPTY_STRING, EMPTY_STRING, new ArrayList<PaymentError>(),
        message);
  }

  /**
   * Creates a {@code PaymentException} from the given values.
   *
   * @param url        the url called but respond with an error.
   * @param statusCode the http status code
   * @param timestamp  the timestamp the cal was made
   * @param id         the id for referencing the error
   * @param errors     the list of {@code PaymentError}.
   * @param message    to be thrown
   */
  public PaymentException(String url, Integer statusCode, String timestamp, String id,
                          List<PaymentError> errors, String message) {
    super(toMessage(url, statusCode, errors, message));
    this.timestamp = timestamp;
    this.url = url;
    this.paymentErrorList = errors;
    this.statusCode = statusCode;
    this.id = id;
  }

  private static String toMessage(String url, Integer statusCode, List<PaymentError> errors,
                                  String message) {
    if (message != null && !message.isEmpty()) {
      return message;
    }

    StringBuilder sb = new StringBuilder();
    if (url != null && statusCode != null) {
      sb.append("Unzer responded with ");
      sb.append(statusCode);
      sb.append(" when calling ");
      sb.append(url);
      sb.append(". ");
    }

    return withErrors(sb, errors).toString();
  }

  private static StringBuilder withErrors(StringBuilder sb, List<PaymentError> errors) {
    if (errors == null || errors.isEmpty()) {
      return sb;
    }
    sb.append("[");
    for (int i = STATUS_CODE_ZERO; i < errors.size(); i++) {
      sb.append(errors.get(i).toString());
      if (i < errors.size() - 1) {
        sb.append(",");
      } else {
        sb.append("]");
      }
    }
    return sb;
  }

  /**
   * Creates a {@code PaymentException} from the given values.
   *
   * @param id        the error id as returned from the api
   * @param url       called endpoint causing the error
   * @param timestamp timestamp the call was made
   * @param errors    a list of errors returned from the Api.
   * @param message   to be thrown
   */
  public PaymentException(String id, String url, String timestamp, List<PaymentError> errors,
                          String message) {
    this(url, STATUS_CODE_ZERO, timestamp, id, errors, message);
  }

  /**
   * Creates a {@code PaymentException} for the given payment error. Provided Message will be
   * dismissed if {@code List<PaymentError>} has a single Error with a StatusCode of 0
   *
   * @param errors  a list of errors returned from the Api.
   * @param message to be thrown
   */
  public PaymentException(List<PaymentError> errors, String message) {
    this(EMPTY_STRING, STATUS_CODE_ZERO, EMPTY_STRING, EMPTY_STRING, errors,
        (errors != null && errors.size() == 1) ? errors.get(STATUS_CODE_ZERO).getMerchantMessage() :
            message);
  }

  /**
   * Returns the details of the api/payment error as List of {@code PaymentError}s.
   *
   * @return List of {@code PaymentError}s
   */
  public List<PaymentError> getPaymentErrorList() {
    return paymentErrorList;
  }

  /**
   * Returns the timestamp the api-call was made, or the error occurred at the sdk.
   *
   * @return timestamp the api-call was made, or the error occurred at the sdk.
   */
  public String getTimestamp() {
    return timestamp;
  }

  /**
   * @return the url called but returned with an error.
   */
  public String getUrl() {
    return url;
  }

  /**
   * @return the http-status call
   */
  public Integer getStatusCode() {
    return statusCode;
  }

  public String getId() {
    return id;
  }

}
