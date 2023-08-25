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

import java.io.IOException;
import org.apache.hc.client5.http.classic.methods.HttpDelete;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPatch;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.classic.methods.HttpPut;
import org.apache.hc.client5.http.classic.methods.HttpUriRequest;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class HttpClientMock implements UnzerRestCommunication {
  private static final Logger logger = LogManager.getLogger(HttpClientMock.class);

  @Override
  public String httpGet(String url, String privateKey) throws HttpCommunicationException {
    return execute(new HttpGet(proxyUrl(url)), null);
  }

  @Override
  public String httpPost(String url, String privateKey, Object data)
      throws HttpCommunicationException {
    return execute(new HttpPost(proxyUrl(url)), data);
  }

  @Override
  public String httpPut(String url, String privateKey, Object data)
      throws HttpCommunicationException {
    return execute(new HttpPut(proxyUrl(url)), data);
  }

  @Override
  public String httpDelete(String url, String privateKey) throws HttpCommunicationException {
    return execute(new HttpDelete(proxyUrl(url)), null);
  }

  @Override
  public String httpPatch(String url, String privateKey, Object data)
      throws HttpCommunicationException {
    return execute(new HttpPatch(proxyUrl(url)), data);
  }

  private String execute(HttpUriRequest request, Object data) {
    logger.debug("Sending request: \n{}\n{}", request, data);

    if (data != null) {
      request.setEntity(new StringEntity(
          new JsonParser().toJson(data),
          ContentType.APPLICATION_JSON,
          "UTF-8",
          false
      ));
    }

    try (CloseableHttpClient client = HttpClients.createDefault()) {
      String response = EntityUtils.toString(client.execute(request).getEntity());
      logger.debug("Received response: \n{}", response);
      return response;
    } catch (IOException | ParseException e) {
      throw new RuntimeException(e);
    }
  }

  private String proxyUrl(String url) {
    return url.replace("https://sbx-api.unzer.com", "http://localhost:8080");
  }
}
