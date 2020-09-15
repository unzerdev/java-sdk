package com.heidelpay.payment.communication.impl;

import static org.apache.http.HttpHeaders.AUTHORIZATION;
import static org.apache.http.HttpHeaders.CONTENT_TYPE;
import static org.apache.http.HttpHeaders.USER_AGENT;

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
import com.heidelpay.payment.communication.AbstractHeidelpayRestCommunication;
import com.heidelpay.payment.communication.HeidelpayRestCommunication;
import com.heidelpay.payment.communication.HttpCommunicationException;
import com.heidelpay.payment.communication.JsonParser;
import com.heidelpay.payment.communication.json.JsonErrorObject;
import com.heidelpay.payment.util.SDKInfo;

/**
 * @deprecated use {@code HttpClientBasedRestCommunication} as a default
 *             implementation.
 *
 */
@Deprecated
public class RestCommunication implements HeidelpayRestCommunication {

	private static final Logger logger = LogManager.getLogger(RestCommunication.class);
	public static final String BASIC = "Basic ";

	public String httpGet(String url, String privateKey) throws HttpCommunicationException {
		HttpGet httpGet = getHttpGet(url);
		httpGet.addHeader(AUTHORIZATION, BASIC + AbstractHeidelpayRestCommunication.addAuthentication(privateKey));
		return this.execute(httpGet);
	}

	public String httpPost(String url, String privateKey, Object data) throws HttpCommunicationException {
		if (url == null || data == null) {
			throw new IllegalArgumentException("Cannot create a http post request to an empty URL or with null params");
		}
		HttpPost httpPost = getHttpPost(url);
		httpPost.addHeader(AUTHORIZATION, BASIC + AbstractHeidelpayRestCommunication.addAuthentication(privateKey));
		return makeRequest(httpPost, data);
	}

	public String httpDelete(String url, String privateKey) throws HttpCommunicationException {
		HttpDelete httpDelete = getHttpDelete(url);
		httpDelete.addHeader(AUTHORIZATION, BASIC + AbstractHeidelpayRestCommunication.addAuthentication(privateKey));
		return this.execute(httpDelete);
	}

	public String httpPut(String url, String privateKey, Object data) throws HttpCommunicationException {
		HttpPut httpPut = getHttpPut(url);
		httpPut.addHeader(AUTHORIZATION, BASIC + AbstractHeidelpayRestCommunication.addAuthentication(privateKey));
		return makeRequest(httpPut, data);
	}

	private String makeRequest(HttpEntityEnclosingRequestBase request, Object data) throws HttpCommunicationException {
		String json = new JsonParser().toJson(data);
		logger.debug("Request: '%s'", json);
		HttpEntity entity = new StringEntity(json, "UTF-8");
		request.setEntity(entity);
		String response = this.execute(request);
		logger.debug("Response: '%s'", json);
		return response;
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
				JsonErrorObject error = new JsonParser().fromJson(content, JsonErrorObject.class);
				throw new PaymentException(error.getUrl(), status.getStatusCode(), error.getTimestamp(), error.getId(), error.getErrors(), "");
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
		httpRequest.setHeader(USER_AGENT, "heidelpay-Java-" + SDKInfo.getVersion());
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
		httpPut.addHeader(CONTENT_TYPE, "application/json; charset=UTF-8");
		return httpPut;
	}

	private HttpPost getHttpPost(String url) {
		HttpPost httpPost = new HttpPost(url);
		setUserAgent(httpPost);
		httpPost.addHeader(CONTENT_TYPE, "application/json; charset=UTF-8");
		return httpPost;
	}

}
