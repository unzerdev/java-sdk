/*
 * Copyright 2020-today Unzer E-Com GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.unzer.payment.communication;

import com.unzer.payment.PaymentError;
import com.unzer.payment.PaymentException;
import com.unzer.payment.communication.UnzerHttpRequest.UnzerHttpMethod;
import com.unzer.payment.communication.impl.HttpClientBasedRestCommunication;
import com.unzer.payment.communication.json.JsonErrorObject;
import com.unzer.payment.util.SDKInfo;

import javax.xml.bind.DatatypeConverter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import static org.apache.http.HttpHeaders.*;

/**
 * Template implementation of the {@code UnzerRestCommunication}. You should
 * use this class as a starting point for custom implementations of the
 * {@code UnzerRestCommunication}. While the basic business-flow is already
 * implemented in the {@code AbstractUnzerRestCommunication}, there are
 * extensions-points defined, allowing to inject a custom implementation for the
 * network-communication as well as for logging aspects.
 * <p>
 * The {@code AbstractUnzerRestCommunication#execute(UnzerHttpRequest)} will already to any requireed non-funcional concerns, like
 * <ul>
 * <li>call logging, as implemented by the inheriting class in the logXxx Methods</li>
 * <li>set the authentication header</li>
 * <li>fix the content-type to application/json</li>
 * <li>sets the user-agent, so we could identify the sdk</li>
 * <li>business errors, such as validation exceptions, at Api level, are translated into {@code PaymentException}s.</li>
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
    private final Locale locale;
    private final String clientIp;

    public AbstractUnzerRestCommunication(Locale locale) {
        this(locale, null);
    }

    public AbstractUnzerRestCommunication(Locale locale, String clientIp) {
        this.locale = locale;
        this.clientIp = clientIp;
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
        try {
            privateKeyBase64 = DatatypeConverter.printBase64Binary(privateKey.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            throw new PaymentException("Unsupported encoding for the private key: Base64!");
        }

        return privateKeyBase64;
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
    protected abstract UnzerHttpResponse doExecute(UnzerHttpRequest request) throws HttpCommunicationException;

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
    @Deprecated
    protected abstract void logRequestBody(String body);

    /**
     * Extension point for logging the response comming from the api.
     *
     * @param response - the response as {@code UnzerHttpResponse}
     */
    protected abstract void logResponse(UnzerHttpResponse response);

    @Override
    public String httpGet(String url, String privateKey) throws HttpCommunicationException {
        Objects.requireNonNull(url);
        return this.execute(createRequest(url, UnzerHttpMethod.GET), privateKey);
    }

    @Override
    public String httpPost(String url, String privateKey, Object data) throws HttpCommunicationException {
        Objects.requireNonNull(url);
        Objects.requireNonNull(data);
        return sendRequestWithBody(createRequest(url, UnzerHttpMethod.POST), privateKey, data);
    }

    @Override
    public String httpPut(String url, String privateKey, Object data) throws HttpCommunicationException {
        Objects.requireNonNull(url);
        Objects.requireNonNull(data);
        return sendRequestWithBody(createRequest(url, UnzerHttpMethod.PUT), privateKey, data);
    }

    @Override
    public String httpPatch(String url, String privateKey, Object data) throws HttpCommunicationException {
        Objects.requireNonNull(url);
        Objects.requireNonNull(data);
        return sendRequestWithBody(createRequest(url, UnzerHttpMethod.PATCH), privateKey, data);
    }

    public String httpDelete(String url, String privateKey) throws HttpCommunicationException {
        Objects.requireNonNull(url);
        return this.execute(createRequest(url, UnzerHttpMethod.DELETE), privateKey);
    }

    private String sendRequestWithBody(UnzerHttpRequest request, String privateKey, Object data) throws HttpCommunicationException {
        String json = new JsonParser().toJson(data);
        logRequestBody(json);
        request.setContent(json, "UTF-8");
        return this.execute(request, privateKey);
    }

    /**
     * sets the content-type header of the given {@code UnzerHttpRequest} to application/json,UTF-8.
     * This method is called from the {@code #execute(UnzerHttpRequest)} method, you do not need to call it explicettely.
     *
     * @param request the request the content type
     */
    private void setContentType(UnzerHttpRequest request) {
        request.addHeader(CONTENT_TYPE, CONTENT_TYPE_JSON);
    }

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

    private void addClientIpHeader(UnzerHttpRequest request) {
        if (this.clientIp != null && !this.clientIp.isEmpty()) {
            request.addHeader(CLIENTIP_HEADER, clientIp);
        }
    }

    private void addSDKInfo(UnzerHttpRequest request) {
        request.addHeader(SDK_TYPE_HEADER, USER_AGENT_PREFIX);
        request.addHeader(SDK_VERSION_HEADER, SDKInfo.VERSION);
        request.addHeader(JAVA_VERSION_HEADER, System.getProperty("java.version"));
    }

    String execute(UnzerHttpRequest request, String privateKey) throws HttpCommunicationException {
        addUserAgent(request);
        addUnzerAuthentication(privateKey, request);
        addAcceptLanguageHeader(request);
        addSDKInfo(request);
        addClientIpHeader(request);
        setContentType(request);

        logRequest(request);

        UnzerHttpResponse response = doExecute(request);

        logResponse(response);

        if (isError(response)) {
            throwPaymentException(response);
        }

        return response.getContent();

    }

    private void throwPaymentException(UnzerHttpResponse response) {
        JsonErrorObject error = new JsonParser().fromJson(response.getContent(), JsonErrorObject.class);
        throw new PaymentException(error.getUrl(), response.getStatusCode(), error.getTimestamp(), error.getId(), error.getErrors(), "");
    }

    private boolean isError(UnzerHttpResponse response) {
        return response.getStatusCode() > 201 || response.getStatusCode() < 200;
    }

}
