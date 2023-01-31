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
package com.unzer.payment.business.paymenttypes;

import com.unzer.payment.communication.HttpCommunicationException;
import com.unzer.payment.communication.JsonParser;
import com.unzer.payment.communication.UnzerRestCommunication;
import org.apache.hc.client5.http.classic.methods.*;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;

import java.io.IOException;

public class HttpClientTestImpl implements UnzerRestCommunication {
    @Override
    public String httpGet(String url, String privateKey) throws HttpCommunicationException {
        return execute(new HttpGet(proxyUrl(url)), null);
    }

    @Override
    public String httpPost(String url, String privateKey, Object data) throws HttpCommunicationException {
        return execute(new HttpPost(proxyUrl(url)), data);
    }

    @Override
    public String httpPut(String url, String privateKey, Object data) throws HttpCommunicationException {
        return execute(new HttpPut(proxyUrl(url)), data);
    }

    @Override
    public String httpDelete(String url, String privateKey) throws HttpCommunicationException {
        return execute(new HttpDelete(proxyUrl(url)), null);
    }

    @Override
    public String httpPatch(String url, String privateKey, Object data) throws HttpCommunicationException {
        return execute(new HttpPatch(proxyUrl(url)), data);
    }

    private String proxyUrl(String url) {
        return url.replace("https://sbx-api.unzer.com", "http://localhost:8080");
    }

    private String execute(HttpUriRequest request, Object data) {
        if (data != null) {
            request.setEntity(new StringEntity(
                    new JsonParser().toJson(data),
                    ContentType.APPLICATION_JSON,
                    "UTF-8",
                    false
            ));
        }

        try (CloseableHttpClient client = HttpClients.createDefault()) {
            return EntityUtils.toString(client.execute(request).getEntity());
        } catch (IOException | ParseException e) {
            throw new RuntimeException(e);
        }
    }
}
