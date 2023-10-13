package com.unzer.payment.communication;

import static org.apache.hc.core5.http.HttpHeaders.ACCEPT_LANGUAGE;
import static org.apache.hc.core5.http.HttpHeaders.AUTHORIZATION;
import static org.apache.hc.core5.http.HttpHeaders.CONTENT_TYPE;
import static org.apache.hc.core5.http.HttpHeaders.USER_AGENT;

import com.unzer.payment.PaymentError;
import com.unzer.payment.PaymentException;
import com.unzer.payment.communication.UnzerHttpRequest.UnzerHttpMethod;
import com.unzer.payment.communication.impl.HttpClientBasedRestCommunication;
import com.unzer.payment.communication.json.JsonErrorObject;
import com.unzer.payment.util.SDKInfo;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import javax.xml.bind.DatatypeConverter;

/**
 * Template implementation of the {@code UnzerRestCommunication}. You should
 * use this class as a starting point for custom implementations of the
 * {@code UnzerRestCommunication}. While the basic business-flow is already
 * implemented in the {@code AbstractUnzerRestCommunication}, there are
 * extensions-points defined, allowing to inject a custom implementation for the
 * network-communication as well as for logging aspects.
 * <p>
 * The {@code AbstractUnzerRestCommunication#execute(UnzerHttpRequest)} will already to any
 * required non-funcional concerns, like
 * <ul>
 * <li>call logging, as implemented by the inheriting class in the logXxx Methods</li>
 * <li>set the authentication header</li>
 * <li>fix the content-type to application/json</li>
 * <li>sets the user-agent, so we could identify the sdk</li>
 * <li>business errors, such as validation exceptions, at API level,
 * are converted to PaymentException.</li>
 * </ul>
 *
 * @see HttpClientBasedRestCommunication for a reference implementation
 */
public abstract class AbstractUnzerRestCommunication implements UnzerRestCommunication {

  public static final String BASIC = "Basic ";
  static final String USER_AGENT_PREFIX = "UnzerJava";
  private static final String CONTENT_TYPE_JSON = "application/json; charset=UTF-8";
  private static final String CLIENTIP_HEADER = "CLIENTIP";
  private static final String SDK_TYPE_HEADER = "SDK-TYPE";
  private static final String SDK_VERSION_HEADER = "SDK-VERSION";
  private static final String JAVA_VERSION_HEADER = "JAVA-VERSION";
  private static final String EMPTY_JSON = "{}";
  private final Locale locale;
  private final String clientIp;

  public AbstractUnzerRestCommunication(Locale locale) {
    this(locale, null);
  }

  public AbstractUnzerRestCommunication(Locale locale, String clientIp) {
    this.locale = locale;
    this.clientIp = clientIp;
  }

  @Override
  public String httpGet(String url, String privateKey) throws HttpCommunicationException {
    Objects.requireNonNull(url);
    return this.execute(createRequest(url, UnzerHttpMethod.GET), privateKey, EMPTY_JSON);
  }

  @Override
  public String httpPost(String url, String privateKey, Object data)
      throws HttpCommunicationException {
    Objects.requireNonNull(url);
    Objects.requireNonNull(data);
    return sendRequestWithBody(createRequest(url, UnzerHttpMethod.POST), privateKey, data);
  }

  @Override
  public String httpPut(String url, String privateKey, Object data)
      throws HttpCommunicationException {
    Objects.requireNonNull(url);
    Objects.requireNonNull(data);
    return sendRequestWithBody(createRequest(url, UnzerHttpMethod.PUT), privateKey, data);
  }

  public String httpDelete(String url, String privateKey) throws HttpCommunicationException {
    Objects.requireNonNull(url);
    return this.execute(createRequest(url, UnzerHttpMethod.DELETE), privateKey, EMPTY_JSON);
  }

  @Override
  public String httpPatch(String url, String privateKey, Object data)
      throws HttpCommunicationException {
    Objects.requireNonNull(url);
    Objects.requireNonNull(data);
    return sendRequestWithBody(createRequest(url, UnzerHttpMethod.PATCH), privateKey, data);
  }

  private String sendRequestWithBody(UnzerHttpRequest request, String privateKey, Object data)
      throws HttpCommunicationException {
    String json = new JsonParser().toJson(data);
    return this.execute(request, privateKey, json);
  }

  String execute(UnzerHttpRequest request, String privateKey, String jsonBody)
      throws HttpCommunicationException {
    addUserAgent(request);
    addUnzerAuthentication(privateKey, request);
    addAcceptLanguageHeader(request);
    addSdkInfo(request);
    addClientIpHeader(request);
    setContentType(request);

    logRequest(request);

    if (!jsonBody.equals(EMPTY_JSON)) {
      request.setContent(jsonBody, "UTF-8");
      logRequestBody(jsonBody);
    }

    UnzerHttpResponse response = doExecute(request);

    logResponse(response);

    if (isError(response)) {
      throwPaymentException(response);
    }

    return response.getContent();

  }

  /**
   * Creates a {@code UnzerHttpRequest} for the given
   * {@code UnzerHttpMethod} based on the http-communication you have choosen.
   * The request will be passed into the {@code #doExecute(UnzerHttpRequest)}
   * method, where you have to implement the http-method specific behavior.
   *
   * @param url-   the url to be called
   * @param method - the http-method as defined by {@code UnzerHttpMethod}
   * @return the {@code UnzerHttpRequest} implementation as your
   * implementation of the {@code #doExecute(UnzerHttpRequest)} might
   * expect.
   */
  protected abstract UnzerHttpRequest createRequest(String url, UnzerHttpMethod method);

  private void addUserAgent(UnzerHttpRequest request) {
    request.addHeader(USER_AGENT, USER_AGENT_PREFIX + " - " + SDKInfo.VERSION);
  }

  private void addUnzerAuthentication(String privateKey, UnzerHttpRequest request) {
    request.addHeader(AUTHORIZATION, BASIC + addAuthentication(privateKey));
  }

  private void addAcceptLanguageHeader(UnzerHttpRequest request) {
    if (this.locale != null) {
      request.addHeader(ACCEPT_LANGUAGE, this.locale.getLanguage());
    }
  }

  private void addSdkInfo(UnzerHttpRequest request) {
    request.addHeader(SDK_TYPE_HEADER, USER_AGENT_PREFIX);
    request.addHeader(SDK_VERSION_HEADER, SDKInfo.VERSION);
    request.addHeader(JAVA_VERSION_HEADER, System.getProperty("java.version"));
  }

  private void addClientIpHeader(UnzerHttpRequest request) {
    if (this.clientIp != null && !this.clientIp.isEmpty()) {
      request.addHeader(CLIENTIP_HEADER, clientIp);
    }
  }

  /**
   * sets the content-type header of the given {@code UnzerHttpRequest} to application/json,UTF-8.
   * This method is called from the {@code #execute(UnzerHttpRequest)} method, you do not need
   * to call it explicitly.
   *
   * @param request the request the content type
   */
  private void setContentType(UnzerHttpRequest request) {
    request.addHeader(CONTENT_TYPE, CONTENT_TYPE_JSON);
  }

  /**
   * Extension point to adjust the logging of any request sent to your
   * implementation.
   *
   * @param request - the {@code UnzerHttpRequest} as created by the
   *                {@code #createRequest(String, UnzerHttpMethod)}
   *                implementation.
   */
  protected abstract void logRequest(UnzerHttpRequest request);

  /**
   * Extension point to log the json representation of the data to be sent.
   *
   * @param body - the json representation of the data to be sent
   */
  protected abstract void logRequestBody(String body);

  /**
   * Implemetation specific excution of the {@code UnzerHttpRequest}. It
   * depends on the implementor to catch the http-method specific behavior her.
   * You might have a look into the reference implementation at
   * {@code HttpClientBasedRestCommunication#doExecute(UnzerHttpRequest)}
   *
   * @param request - the {@code UnzerHttpRequest} as created by the
   *                {@code #createRequest(String, UnzerHttpMethod)}
   *                implementation.
   * @return - the content and status-code of the response wrapped into a {@code UnzerHttpResponse}.
   * @throws HttpCommunicationException - thrown for any communication errors
   */
  protected abstract UnzerHttpResponse doExecute(UnzerHttpRequest request)
      throws HttpCommunicationException;

  /**
   * Extension point for logging the response comming from the api.
   *
   * @param response - the response as {@code UnzerHttpResponse}
   */
  protected abstract void logResponse(UnzerHttpResponse response);

  private boolean isError(UnzerHttpResponse response) {
    return response.getStatusCode() > 201 || response.getStatusCode() < 200;
  }

  private void throwPaymentException(UnzerHttpResponse response) {
    JsonErrorObject error = new JsonParser().fromJson(response.getContent(), JsonErrorObject.class);
    throw new PaymentException(error.getUrl(), response.getStatusCode(), error.getTimestamp(),
        error.getId(), error.getErrors(), "");
  }

  public static String addAuthentication(String privateKey) {
    if (privateKey == null) {
      List<PaymentError> paymentErrorList = new ArrayList<PaymentError>();
      paymentErrorList.add(new PaymentError(
          "PrivateKey/PublicKey is missing",
          "There was a problem authenticating your request.Please contact us for more information.",
          "API.000.000.001"));

      throw new PaymentException(paymentErrorList, "");
    }
    if (!privateKey.endsWith(":")) {
      privateKey = privateKey + ":";
    }
    String privateKeyBase64;
    privateKeyBase64 = DatatypeConverter.printBase64Binary(privateKey.getBytes(
        StandardCharsets.UTF_8));

    return privateKeyBase64;
  }

}
