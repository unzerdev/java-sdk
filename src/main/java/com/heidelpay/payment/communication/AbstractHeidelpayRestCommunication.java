package com.heidelpay.payment.communication;

/*-
 * #%L
 * Heidelpay Java SDK
 * %%
 * Copyright (C) 2018 Heidelpay GmbH
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.heidelpay.payment.PaymentError;
import com.heidelpay.payment.PaymentException;
import com.heidelpay.payment.communication.HeidelpayHttpRequest.HeidelpayHttpMethod;
import com.heidelpay.payment.communication.impl.HttpClientBasedRestCommunication;
import com.heidelpay.payment.communication.json.JsonErrorObject;
import com.heidelpay.payment.util.SDKInfo;

import javax.xml.bind.DatatypeConverter;

import static org.apache.http.HttpHeaders.*;

/**
 * Template implementation of the {@code HeidelpayRestCommunication}. You should
 * use this class as a starting point for custom implementations of the
 * {@code HeidelpayRestCommunication}. While the basic business-flow is already
 * implemented in the {@code AbstractHeidelpayRestCommunication}, there are
 * extensions-points defined, allowing to inject a custom implementation for the
 * network-communication as well as for logging aspects.
 * 
 * The {@code AbstractHeidelpayRestCommunication#execute(HeidelpayHttpRequest)} will already to any requireed non-funcional concerns, like
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
public abstract class AbstractHeidelpayRestCommunication implements HeidelpayRestCommunication {

	public static final String BASIC = "Basic ";
	static final String USER_AGENT_PREFIX = "HeidelpayJava";
	private static final String CONTENT_TYPE_JSON = "application/json; charset=UTF-8";

	private Locale locale;

	public AbstractHeidelpayRestCommunication(Locale locale) {
		this.locale = locale;
	}

	/**
	 * Creates a {@code HeidelpayHttpRequest} for the given
	 * {@code HeidelpayHttpMethod} based on the http-communication you have choosen.
	 * The request will be passed into the {@code #doExecute(HeidelpayHttpRequest)}
	 * method, where you have to implement the http-method specific behavior.
	 * 
	 * 
	 * @param url-
	 *            the url to be called
	 * @param method
	 *            - the http-method as defined by {@code HeidelpayHttpMethod}
	 * @return the {@code HeidelpayHttpRequest} implementation as your
	 *         implementation of the {@code #doExecute(HeidelpayHttpRequest)} might
	 *         expect.
	 */
	protected abstract HeidelpayHttpRequest createRequest(String url, HeidelpayHttpMethod method);

	/**
	 * Implemetation specific excution of the {@code HeidelpayHttpRequest}. It
	 * depends on the implementor to catch the http-method specific behavior her.
	 * You might have a look into the reference implementation at
	 * {@code HttpClientBasedRestCommunication#doExecute(HeidelpayHttpRequest)}
	 * 
	 * @param request
	 *            - the {@code HeidelpayHttpRequest} as created by the
	 *            {@code #createRequest(String, HeidelpayHttpMethod)}
	 *            implementation.
	 * @return - the content and status-code of the response wrapped into a {@code HeidelpayHttpResponse}.
	 * @throws HttpCommunicationException - thrown for any communication errors
	 */
	protected abstract HeidelpayHttpResponse doExecute(HeidelpayHttpRequest request) throws HttpCommunicationException;

	/**
	 * Extension point to adjust the logging of any request sent to your
	 * implementation.
	 * 
	 * @param request
	 *            - the {@code HeidelpayHttpRequest} as created by the
	 *            {@code #createRequest(String, HeidelpayHttpMethod)}
	 *            implementation.
	 */
	protected abstract void logRequest(HeidelpayHttpRequest request);

	/**
	 * Extension point to log the json representation of the data to be sent.
	 * @param body - the json representation of the data to be sent
	 */
	protected abstract void logRequestBody(String body);

	/**
	 * Extension point for logging the response comming from the api.
	 * @param response - the response as {@code HeidelpayHttpResponse}
	 */
	protected abstract void logResponse(HeidelpayHttpResponse response);

	public String httpGet(String url, String privateKey) throws HttpCommunicationException {

		return this.execute(createRequest(url, HeidelpayHttpMethod.GET), privateKey);
	}

	public String httpPost(String url, String privateKey, Object data)
			throws HttpCommunicationException {
		if (url == null) {
			throw new IllegalArgumentException("Cannot post to a null URL");
		}
		return sendPutOrPost(createRequest(url, HeidelpayHttpMethod.POST), privateKey, data);
	}

	public String httpPut(String url, String privateKey, Object data)
			throws HttpCommunicationException {
		if (url == null) {
			throw new IllegalArgumentException("Cannot put to a null URL");
		}
		return sendPutOrPost(createRequest(url, HeidelpayHttpMethod.PUT), privateKey, data);
	}

	public String httpDelete(String url, String privateKey) throws HttpCommunicationException {
		
		return this.execute(createRequest(url, HeidelpayHttpMethod.DELETE), privateKey);
	}

	private String sendPutOrPost(HeidelpayHttpRequest request, String privateKey, Object data)
			throws HttpCommunicationException {
		if (data == null) {
			throw new IllegalArgumentException("Cannot create a http post request with null params");
		}

		String json = new JsonParser().toJson(data);
		logRequestBody(json);
		request.setContent(new JsonParser().toJson(data), "UTF-8");

		return this.execute(request, privateKey);

	}

	/**
	 * sets the content-type header of the given {@code HeidelpayHttpRequest} to application/json,UTF-8.
	 * This method is called from the {@code #execute(HeidelpayHttpRequest)} method, you do not need to call it explicettely.
	 * @param request the request the content type 
	 */
	private void setContentType(HeidelpayHttpRequest request) {
		request.addHeader(CONTENT_TYPE, CONTENT_TYPE_JSON);
	}

	private void addUserAgent(HeidelpayHttpRequest request) {
		request.addHeader(USER_AGENT, USER_AGENT_PREFIX + " - " + SDKInfo.getVersion());
	}

	private void addHeidelpayAuthentication(String privateKey, HeidelpayHttpRequest request) {
		request.addHeader(AUTHORIZATION, BASIC + addAuthentication(privateKey));
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

	private void addAcceptLanguageHeader(HeidelpayHttpRequest request) {
		if(this.locale != null) {
			request.addHeader(ACCEPT_LANGUAGE, this.locale.getLanguage());
		}
	}

	String execute(HeidelpayHttpRequest request, String privateKey) throws HttpCommunicationException {
		addUserAgent(request);
		addHeidelpayAuthentication(privateKey, request);
		addAcceptLanguageHeader(request);
		setContentType(request);

		logRequest(request);
		
		HeidelpayHttpResponse response = doExecute(request);
		
		logResponse(response);

		if (isError(response)) {
			throwPaymentException(response);
		}

		return response.getContent();

	}

	private void throwPaymentException(HeidelpayHttpResponse response) {
		JsonErrorObject error = new JsonParser().fromJson(response.getContent(), JsonErrorObject.class);
		throw new PaymentException(error.getUrl(), response.getStatusCode(), error.getTimestamp(), error.getId(), error.getErrors(), "");
	}

	private boolean isError(HeidelpayHttpResponse response) {
		return response.getStatusCode() > 201 || response.getStatusCode() < 200;
	}

}
