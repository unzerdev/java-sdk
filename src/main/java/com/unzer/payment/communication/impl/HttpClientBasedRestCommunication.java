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

package com.unzer.payment.communication.impl;

import com.unzer.payment.communication.AbstractUnzerRestCommunication;
import com.unzer.payment.communication.HttpCommunicationException;
import com.unzer.payment.communication.UnzerHttpRequest;
import com.unzer.payment.communication.UnzerHttpResponse;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Locale;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Reference implementation of the UnzerRestCommunication, based on Apache HttpClient.
 */
public class HttpClientBasedRestCommunication extends AbstractUnzerRestCommunication {

  private static final Logger logger = LogManager.getLogger(HttpClientBasedRestCommunication.class);

  public HttpClientBasedRestCommunication() {
    this(null, null);
  }

  public HttpClientBasedRestCommunication(Locale locale, String clientIp) {
    super(locale, clientIp);
  }

  public HttpClientBasedRestCommunication(Locale locale) {
    this(locale, null);
  }

  @Override
  protected UnzerHttpRequest createRequest(String url, UnzerHttpRequest.UnzerHttpMethod method) {
    return new HttpClientBasedHttpRequest(url, method);
  }

  @Override
  protected void logRequest(UnzerHttpRequest request) {
    logger.debug("Sending request {}", request.toString());
  }

  @Override
  protected void logRequestBody(String body) {
    logger.debug(body);
  }

  @Override
  protected UnzerHttpResponse doExecute(UnzerHttpRequest request)
      throws HttpCommunicationException {
    if (!(request instanceof HttpClientBasedHttpRequest)) {
      throw new IllegalArgumentException(
          "Request is not an instance of HttpClientBasedHttpRequest");
    }

    try {
      request.getURI();
    } catch (URISyntaxException e) {
      throw new IllegalArgumentException("Request URI is not correct", e);
    }

    CloseableHttpResponse response = null;
    try (CloseableHttpClient client = createClient()) {
      response = client.execute(((HttpClientBasedHttpRequest) request).getRequest());
      return new UnzerHttpResponse(request, EntityUtils.toString(response.getEntity()),
          response.getCode());
    } catch (IOException | ParseException e) {
      try {
        throw new HttpCommunicationException(String.format(
            "Error communicating to %s: Detail: %s",
            request.getURI().toString(),
            e.getMessage()
        ));
      } catch (URISyntaxException ex) {
        // raises never, because uri validation happens in the beginning of this method
        throw new RuntimeException(ex);
      }
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

  @Override
  protected void logResponse(UnzerHttpResponse response) {
    logger.debug("Received response {} {}\nHTTP status {}\n{}",
        response.getRequest().getMethod(),
        response.getRequestURI(),
        response.getStatusCode(),
        response.getContent());
  }

  @Deprecated
  private CloseableHttpClient createClient() {
    HttpClientBuilder builder = HttpClients.custom().useSystemProperties();
    return builder.build();
  }

}
