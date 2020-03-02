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
import java.util.Locale;

import org.apache.http.ParseException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.heidelpay.payment.communication.AbstractHeidelpayRestCommunication;
import com.heidelpay.payment.communication.HeidelpayHttpRequest;
import com.heidelpay.payment.communication.HeidelpayHttpRequest.HeidelpayHttpMethod;
import com.heidelpay.payment.communication.HeidelpayHttpResponse;
import com.heidelpay.payment.communication.HttpCommunicationException;

/**
 * Reference implementation of the {@code HeidelpayRestCommunication}, based on apaches {@code HttpClient}. 
 *
 */
public class HttpClientBasedRestCommunication extends AbstractHeidelpayRestCommunication {

	private final static Logger logger = LogManager.getLogger(AbstractHeidelpayRestCommunication.class);

	public HttpClientBasedRestCommunication() {
		super(null);
	}

	public HttpClientBasedRestCommunication(Locale locale) {
		super(locale);
	}

	@Override
	protected HeidelpayHttpRequest createRequest(String url, HeidelpayHttpMethod method) {
		return new HttpClientBasedHttpRequest(url, method);
	}

	@Override
	protected void logRequestBody(String body) {
		logger.debug(body);
	}

	@Override
	protected void logRequest(HeidelpayHttpRequest request) {
		logger.debug(request.toString());
	}

	@Override
	protected void logResponse(HeidelpayHttpResponse response) {
		logger.debug(response.getStatusCode());
		logger.debug(response.getContent());
	}

	@Override
	protected HeidelpayHttpResponse doExecute(HeidelpayHttpRequest request) throws HttpCommunicationException {
		if (!(request instanceof HttpClientBasedHttpRequest)) {
			throw new IllegalArgumentException("Request is not an instance of HttpClientBasedHttpRequest");
		}
		CloseableHttpResponse response = null;
		try {
			response = getHttpClient().execute(((HttpClientBasedHttpRequest) request).getRequest());
			return new HeidelpayHttpResponse(EntityUtils.toString(response.getEntity()),
					response.getStatusLine().getStatusCode());
		} catch (IOException e) {
			throw new HttpCommunicationException(
					"Error communicating to " + request.getURI() + ": Detail: " + e.getMessage());
		} catch (ParseException e) {
			throw new HttpCommunicationException(
							"Error communicating to " + request.getURI() + ": Detail: " + e.getMessage());
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

}
