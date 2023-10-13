package com.unzer.payment.communication;

/**
 * Generic exception for HttpCommunication module.
 */
public class HttpCommunicationException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  /**
   * Constructor create instance without message.
   */
  public HttpCommunicationException() {
    super();
  }

  /**
   * Constructor create instance with message.
   *
   * @param message refers to the declared message
   */
  public HttpCommunicationException(String message) {
    super(message);
  }
}
