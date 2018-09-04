package com.heidelpay.payment.communication;

import java.io.IOException;
import java.util.Objects;

import org.apache.http.HttpEntity;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;


public class RestCommunication {

	public final static Logger logger = Logger.getLogger(RestCommunication.class);

	
	public String httpPost(String url, String privateKey, Object data) throws HttpCommunicationException {
		if (Objects.isNull(url) || Objects.isNull(data)) {
			throw new NullPointerException("Cannot create a http post request with null params");
		}
		HttpPost httpPost = getHttpPost(url, "application/json; charset=UTF-8");
		httpPost.addHeader("Authorization", privateKey);
		String json = JsonParser.create().toJson(data);
		logger.debug("Json: '" + json + "'");
		HttpEntity entity = new StringEntity(json, "UTF-8");
		httpPost.setEntity(entity);
		return this.execute(httpPost);
	}

	private String execute(HttpPost httpPost) throws HttpCommunicationException {
		CloseableHttpResponse response = null;
		try {
			logger.debug(httpPost);
			response = getHttpClient().execute(httpPost);

			StatusLine status = response.getStatusLine();
			String content = EntityUtils.toString(response.getEntity());

			logger.debug(status);
			logger.debug(content);
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
//		CloseableHttpClient httpclient = HttpClients.createDefault();
//		return httpclient;
		HttpClientBuilder builder = HttpClients.custom().useSystemProperties();
		return builder.build();
	}

	private HttpPost getHttpPost(String url, String contentType) {
		HttpPost httpPost = new HttpPost(url);
		httpPost.setHeader("User-Agent", "HeidelpayJava");
		httpPost.addHeader("Content-Type", contentType);
		return httpPost;
	}

}
