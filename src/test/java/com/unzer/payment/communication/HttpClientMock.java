package com.unzer.payment.communication;

import java.io.IOException;
import java.util.Optional;

import org.apache.hc.client5.http.classic.methods.*;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.text.html.Option;

public class HttpClientMock implements UnzerRestCommunication {
    private static final Logger logger = LogManager.getLogger(HttpClientMock.class);

    @Override
    public String httpGet(String url, String privateKey) {
        return execute(new HttpGet(proxyUrl(url)), null);
    }

    @Override
    public String httpPost(String url, String privateKey, Object data) {
        return execute(new HttpPost(proxyUrl(url)), data);
    }

    @Override
    public String httpPut(String url, String privateKey, Object data) {
        return execute(new HttpPut(proxyUrl(url)), data);
    }

    @Override
    public String httpDelete(String url, String privateKey) {
        return execute(new HttpDelete(proxyUrl(url)), null);
    }

    @Override
    public String httpPatch(String url, String privateKey, Object data) {
        return execute(new HttpPatch(proxyUrl(url)), data);
    }

    private String execute(HttpUriRequest request, Object data) {
        String jsonBody = null;

        if (data != null) {
            jsonBody =  new JsonParser().toJson(data);
            request.setEntity(new StringEntity(
                    jsonBody,
                    ContentType.APPLICATION_JSON,
                    "UTF-8",
                    false
            ));
        }

        logger.debug("Sending request: \n{}\n{}", request, jsonBody);

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
