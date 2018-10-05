package com.heidelpay.payment.communication;

/*-
 * #%L
 * Heidelpay Java SDK
 * %%
 * Copyright (C) 2018 Heidelpay GmbH
 * %%
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
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
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import com.heidelpay.payment.PaymentException;
import com.heidelpay.payment.communication.json.JsonErrorObject;

public class RestCommunication {

	public final static Logger logger = Logger.getLogger(RestCommunication.class);

	public String httpGet(String url, String privateKey) throws HttpCommunicationException {
		HttpGet httpGet = getHttpGet(url);
		httpGet = (HttpGet) addAuthentication(privateKey, httpGet);
		String response = this.execute(httpGet);
		return response;
	}

	public String httpPost(String url, String privateKey, Object data) throws HttpCommunicationException {
		if (Objects.isNull(url) || Objects.isNull(data)) {
			throw new NullPointerException("Cannot create a http post request with null params");
		}
		HttpPost httpPost = getHttpPost(url, "application/json; charset=UTF-8");
		httpPost = (HttpPost) addAuthentication(privateKey, httpPost);
		String json = new JsonParser<String>().toJson(data);
		logger.debug("Request: '" + json + "'");
		HttpEntity entity = new StringEntity(json, "UTF-8");
		httpPost.setEntity(entity);
		String response = this.execute(httpPost);
		logger.debug("Response: '" + json + "'");
		return response;
	}

	public String httpDelete(String url, String privateKey) throws HttpCommunicationException {
		HttpDelete httpDelete = getHttpDelete(url);
		httpDelete = (HttpDelete) addAuthentication(privateKey, httpDelete);
		return this.execute(httpDelete);
	}

	public String httpPut(String url, String privateKey, Object data) throws HttpCommunicationException {
		HttpPut httpPut = getHttpPut(url, "application/json; charset=UTF-8");
		httpPut = (HttpPut) addAuthentication(privateKey, httpPut);
		String json = new JsonParser<String>().toJson(data);
		logger.debug("Request: '" + json + "'");
		HttpEntity entity = new StringEntity(json, "UTF-8");
		httpPut.setEntity(entity);
		String response = this.execute(httpPut);
		logger.debug("Response: '" + json + "'");
		return response;
	}

	private HttpUriRequest addAuthentication(String privateKey, HttpUriRequest http) {
		if (privateKey == null) {
			throw new PaymentException("PrivateKey/PublicKey is missing", "There was a problem authenticating your request.Please contact us for more information.", "API.000.000.001");
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
				throw new PaymentException("Heidelpay responded with " + status.toString() + " when calling URL '"
						+ httpPost.getURI() + "'. Details: " + error.getErrors(), error.getErrors());
			}
			return content;
		} catch (IOException e) {
			throw new HttpCommunicationException("Error communicating to "+ httpPost.getURI() + ": Detail: " + e.getMessage());
		} catch (ParseException e) {
			throw new HttpCommunicationException("Error communicating to "+ httpPost.getURI() + ": Detail: " + e.getMessage());
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

	private HttpPost getHttpPost(String url, String contentType) {
		HttpPost httpPost = new HttpPost(url);
		setUserAgent(httpPost);
		httpPost.addHeader("Content-Type", contentType);
		return httpPost;
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

	private HttpPut getHttpPut(String url, String contentType) {
		HttpPut httpPut = new HttpPut(url);
		setUserAgent(httpPut);
		httpPut.addHeader("Content-Type", contentType);
		return httpPut;
	}

}
