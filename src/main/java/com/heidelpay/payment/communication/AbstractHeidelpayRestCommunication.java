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

import java.util.Base64;

import com.heidelpay.payment.PaymentError;
import com.heidelpay.payment.PaymentException;
import com.heidelpay.payment.communication.HeidelpayHttpRequest.HeidelpayHttpMethod;
import com.heidelpay.payment.communication.impl.HttpClientBasedRestCommunication;
import com.heidelpay.payment.communication.json.JsonErrorObject;
import com.heidelpay.payment.util.SDKInfo;

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

	protected static final String USER_AGENT = "User-Agent";
	protected static final String AUTHORIZATION = "Authorization";
	protected static final String BASIC = "Basic ";
	protected static final String USER_AGENT_PREFIX = "heidelpay-Java-";
	protected static final String CONTENT_TYPE_JSON = "application/json; charset=UTF-8";
	protected static final String CONTENT_TYPE = "Content-Type";

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

	public String httpGet(String url, String privateKey) throws HttpCommunicationException, PaymentException {

		return this.execute(createRequest(url, HeidelpayHttpMethod.GET), privateKey);
	}

	public String httpPost(String url, String privateKey, Object data)
			throws HttpCommunicationException, PaymentException {
		if (url == null) {
			throw new IllegalArgumentException("Cannot post to a null URL");
		}
		return sendPutOrPost(createRequest(url, HeidelpayHttpMethod.POST), privateKey, data);
	}

	public String httpPut(String url, String privateKey, Object data)
			throws HttpCommunicationException, PaymentException {
		if (url == null) {
			throw new IllegalArgumentException("Cannot put to a null URL");
		}
		return sendPutOrPost(createRequest(url, HeidelpayHttpMethod.PUT), privateKey, data);
	}

	public String httpDelete(String url, String privateKey) throws HttpCommunicationException, PaymentException {
		
		return this.execute(createRequest(url, HeidelpayHttpMethod.DELETE), privateKey);
	}

	protected String sendPutOrPost(HeidelpayHttpRequest request, String privateKey, Object data)
			throws HttpCommunicationException, PaymentException {
		if (data == null) {
			throw new IllegalArgumentException("Cannot create a http post request with null params");
		}
		setContentType(request);

		String json = new JsonParser<String>().toJson(data);
		logRequestBody(json);
		request.setContent(new JsonParser<String>().toJson(data), "UTF-8");

		return this.execute(request, privateKey);

	}

	/**
	 * sets the content-type header of the given {@code HeidelpayHttpRequest} to application/json,UTF-8.
	 * This method is called from the {@code #execute(HeidelpayHttpRequest)} method, you do not need to call it explicettely.
	 * @param request the request the content type 
	 */
	protected void setContentType(HeidelpayHttpRequest request) {
		request.addHeader(CONTENT_TYPE, CONTENT_TYPE_JSON);
	}

	protected void addUserAgent(HeidelpayHttpRequest request) {
		request.addHeader(USER_AGENT, USER_AGENT_PREFIX + SDKInfo.getVersion() + " - " + getClass().getCanonicalName());
	}

	protected void addAuthentication(String privateKey, HeidelpayHttpRequest request) throws PaymentException {

		if (privateKey == null) {
			throw new PaymentException("PrivateKey/PublicKey is missing",
					"There was a problem authenticating your request. Please contact us for more information.",
					"API.000.000.001", request.getURI().toString());
		}
		if (!privateKey.endsWith(":")) {
			privateKey = privateKey + ":";
		}
		String privateKeyBase64 = new String(Base64.getEncoder().encode(privateKey.getBytes()));
		request.addHeader(AUTHORIZATION, BASIC + privateKeyBase64);
	}

	
	protected String execute(HeidelpayHttpRequest request, String privateKey) throws PaymentException, HttpCommunicationException {
		addUserAgent(request);
		addAuthentication(privateKey, request);
		
		logRequest(request);
		
		HeidelpayHttpResponse response = doExecute(request);
		
		logResponse(response);

		if (isError(response)) {
			throwPaymentException(request, response);
		}

		return response.getContent();

	}

	protected void throwPaymentException(HeidelpayHttpRequest request, HeidelpayHttpResponse response)
			throws PaymentException {
		JsonErrorObject error = new JsonParser<JsonErrorObject>().fromJson(response.getContent(),
				JsonErrorObject.class);
		throw new PaymentException(error.getUrl(), response.getStatusCode(), error.getTimestamp(), error.getId(), error.getErrors());
	}

	protected boolean isError(HeidelpayHttpResponse response) {
		return response.getStatusCode() > 201 || response.getStatusCode() < 200;
	}

}
