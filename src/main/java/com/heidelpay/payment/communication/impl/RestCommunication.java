package com.heidelpay.payment.communication.impl;

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

import java.io.IOException;
import java.util.Base64;
import java.util.Objects;

import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.heidelpay.payment.PaymentException;
import com.heidelpay.payment.communication.HeidelpayRestCommunication;
import com.heidelpay.payment.communication.HttpCommunicationException;
import com.heidelpay.payment.communication.JsonParser;
import com.heidelpay.payment.communication.json.JsonErrorObject;

/**
 * @deprecated use {@code HttpClientBasedRestCommunication} as a default
 *             implementation.
 *
 */
public class RestCommunication implements HeidelpayRestCommunication {

	private final static Logger logger = LogManager.getLogger(RestCommunication.class);

	public String httpGet(String url, String privateKey) throws HttpCommunicationException {
		HttpGet httpGet = getHttpGet(url);
		httpGet = (HttpGet) addAuthentication(privateKey, httpGet);
		return this.execute(httpGet);
	}

	public String httpPost(String url, String privateKey, Object data) throws HttpCommunicationException {
		if (Objects.isNull(url) || Objects.isNull(data)) {
			throw new IllegalArgumentException("Cannot create a http post request to an empty URL or with null params");
		}
		HttpPost httpPost = getHttpPost(url);
		httpPost = (HttpPost) addAuthentication(privateKey, httpPost);
		return makeRequest(httpPost, data);
	}

	public String httpDelete(String url, String privateKey) throws HttpCommunicationException {
		HttpDelete httpDelete = getHttpDelete(url);
		httpDelete = (HttpDelete) addAuthentication(privateKey, httpDelete);
		return this.execute(httpDelete);
	}

	public String httpPut(String url, String privateKey, Object data) throws HttpCommunicationException {
		HttpPut httpPut = getHttpPut(url);
		httpPut = (HttpPut) addAuthentication(privateKey, httpPut);
		return makeRequest(httpPut, data);
	}

	private String makeRequest(HttpEntityEnclosingRequestBase request, Object data) throws HttpCommunicationException {
		String json = new JsonParser<String>().toJson(data);
		logger.debug("Request: '" + json + "'");
		HttpEntity entity = new StringEntity(json, "UTF-8");
		request.setEntity(entity);
		String response = this.execute(request);
		logger.debug("Response: '" + json + "'");
		return response;
	}

	private HttpUriRequest addAuthentication(String privateKey, HttpUriRequest http) {
		if (privateKey == null) {
			String uri;
			if (http.getURI() == null) {
				uri = null;
			} else {
				uri = http.getURI().toString();
			}
			throw new PaymentException("PrivateKey/PublicKey is missing",
					"There was a problem authenticating your request.Please contact us for more information.",
					"API.000.000.001", uri);
		}
		if (!privateKey.endsWith(":")) {
			privateKey = privateKey + ":";
		}
		String privateKeyBase64 = new String(Base64.getEncoder().encode(privateKey.getBytes()));
		http.addHeader("Authorization", "Basic " + privateKeyBase64);
		return http;
	}

	private String execute(HttpUriRequest httpPost) throws HttpCommunicationException {
		CloseableHttpResponse response = null;
		logger.debug(httpPost);
		try {
			response = getHttpClient().execute(httpPost);

			StatusLine status = response.getStatusLine();
			String content;
			content = EntityUtils.toString(response.getEntity());
			logger.debug(status);
			logger.debug(content);

			if (status.getStatusCode() > 201 || status.getStatusCode() < 200) {
				JsonErrorObject error = new JsonParser<JsonErrorObject>().fromJson(content, JsonErrorObject.class);
				throw new PaymentException(error.getUrl(), status.getStatusCode(), error.getTimestamp(), error.getId(),
						error.getErrors());
			}
			return content;
		} catch (IOException e) {
			throw new HttpCommunicationException(
					"Error communicating to " + httpPost.getURI() + ": Detail: " + e.getMessage());
		} catch (ParseException e) {
			throw new HttpCommunicationException(
							"Error communicating to " + httpPost.getURI() + ": Detail: " + e.getMessage());
		} finally {
			if (response != null) {
				try {
					response.close();
				} catch (IOException e) {
					logger.debug("Closing the http stream threw an error: " + e.getMessage(), e);
				}
			}
		}
	}

	private CloseableHttpClient getHttpClient() {
		HttpClientBuilder builder = HttpClients.custom().useSystemProperties();
		return builder.build();
	}

	private void setUserAgent(HttpUriRequest httpRequest) {
		httpRequest.setHeader("User-Agent", "heidelpay-Java-1.0.0.2");
	}

	private HttpGet getHttpGet(String url) {
		HttpGet httpGet = new HttpGet(url);
		setUserAgent(httpGet);
		return httpGet;
	}

	private HttpDelete getHttpDelete(String url) {
		HttpDelete httpDelete = new HttpDelete(url);
		setUserAgent(httpDelete);
		return httpDelete;
	}

	private HttpPut getHttpPut(String url) {
		HttpPut httpPut = new HttpPut(url);
		setUserAgent(httpPut);
		httpPut.addHeader("Content-Type", "application/json; charset=UTF-8");
		return httpPut;
	}

	private HttpPost getHttpPost(String url) {
		HttpPost httpPost = new HttpPost(url);
		setUserAgent(httpPost);
		httpPost.addHeader("Content-Type", "application/json; charset=UTF-8");
		return httpPost;
	}

}
