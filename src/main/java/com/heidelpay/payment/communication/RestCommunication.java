package com.heidelpay.payment.communication;

import java.io.IOException;
import java.util.Base64;
import java.util.Objects;

import org.apache.http.HttpEntity;
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
		httpGet = (HttpGet)addAuthentication(privateKey, httpGet);
		String response =  this.execute(httpGet);
		return response;
	}

	public String httpPost(String url, String privateKey, Object data) throws HttpCommunicationException {
		if (Objects.isNull(url) || Objects.isNull(data)) {
			throw new NullPointerException("Cannot create a http post request with null params");
		}
		HttpPost httpPost = getHttpPost(url, "application/json; charset=UTF-8");
		httpPost = (HttpPost)addAuthentication(privateKey, httpPost);
		String json = new JsonParser<String>().toJson(data);
		logger.debug("Request: '" + json + "'");
		HttpEntity entity = new StringEntity(json, "UTF-8");
		httpPost.setEntity(entity);
		String response =  this.execute(httpPost);
		logger.debug("Response: '" + json + "'");
		return response;
	}

	public String httpDelete(String url, String privateKey) throws HttpCommunicationException {
		HttpDelete httpDelete = getHttpDelete(url);
		httpDelete = (HttpDelete)addAuthentication(privateKey, httpDelete);
		return this.execute(httpDelete);
	}

	public String httpPut(String url, String privateKey, Object data) throws HttpCommunicationException {
		HttpPut httpPut = getHttpPut(url, "application/json; charset=UTF-8");
		httpPut = (HttpPut)addAuthentication(privateKey, httpPut);
		String json = new JsonParser<String>().toJson(data);
		logger.debug("Request: '" + json + "'");
		HttpEntity entity = new StringEntity(json, "UTF-8");
		httpPut.setEntity(entity);
		String response =  this.execute(httpPut);
		logger.debug("Response: '" + json + "'");
		return response;
	}


	private HttpUriRequest addAuthentication(String privateKey, HttpUriRequest http) {
		if (!privateKey.endsWith(":")) {
			privateKey = privateKey + ":";
		}
		String privateKeyBase64 = new String(Base64.getEncoder().encode(privateKey.getBytes()));
		http.addHeader("Authorization", "Basic " + privateKeyBase64);
		return http;
	}

	private String execute(HttpUriRequest httpPost) throws HttpCommunicationException {
		CloseableHttpResponse response = null;
		try {
			logger.debug(httpPost);
			response = getHttpClient().execute(httpPost);

			StatusLine status = response.getStatusLine();
			String content = EntityUtils.toString(response.getEntity());
			logger.debug(status);
			logger.debug(content);

			if (status.getStatusCode() > 201 || status.getStatusCode() < 200) {
				JsonErrorObject error = new JsonParser<JsonErrorObject>().fromJson(content, JsonErrorObject.class);
				throw new PaymentException("Heidelpay responded with " + status.toString() + " when calling URL '" + httpPost.getURI() + "'. Details: " + error.getErrors(), error.getErrors());
			}
			return content;
		} catch (Exception e) {
			throw new HttpCommunicationException("Error communicating to Heidelpay API: " + e.getMessage());
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
		httpPost.setHeader("User-Agent", "HeidelpayJava");
		httpPost.addHeader("Content-Type", contentType);
		return httpPost;
	}
	
	private HttpGet getHttpGet(String url) {
		HttpGet httpGet = new HttpGet(url);
		httpGet.setHeader("User-Agent", "HeidelpayJava");
		return httpGet;
	}

	private HttpDelete getHttpDelete(String url) {
		HttpDelete httpDelete = new HttpDelete(url);
		httpDelete.setHeader("User-Agent", "HeidelpayJava");
		return httpDelete;
	}

	private HttpPut getHttpPut(String url, String contentType) {
		HttpPut httpPut = new HttpPut(url);
		httpPut.setHeader("User-Agent", "HeidelpayJava");
		httpPut.addHeader("Content-Type", contentType);
		return httpPut;
	}

}
