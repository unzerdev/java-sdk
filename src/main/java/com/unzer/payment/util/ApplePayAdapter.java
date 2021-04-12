package com.unzer.payment.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.unzer.payment.ApplePaySession;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

public class ApplePayAdapter {

    public static String validateApplePayMerchant(String merchantValidationURL, ApplePaySession applePaySession, KeyManagerFactory keyManagerFactory, TrustManagerFactory trustManagerFactory) throws IOException, NoSuchAlgorithmException, KeyManagementException {
        ObjectMapper mapper = new ObjectMapper();
        SSLConnectionSocketFactory sslsf = setupSSLSocketFactory(keyManagerFactory, trustManagerFactory);
        String response = "";

        try (CloseableHttpClient httpclient = HttpClients.custom().setSSLSocketFactory(sslsf).build()) {
            HttpPost post = new HttpPost(merchantValidationURL);
            StringEntity reqEntity = new StringEntity(mapper.writeValueAsString(applePaySession), ContentType.APPLICATION_JSON);
            post.setEntity(reqEntity);

            try (CloseableHttpResponse res = httpclient.execute(post)) {
                HttpEntity entity = res.getEntity();
                response = EntityUtils.toString(entity, "UTF-8");
                EntityUtils.consume(entity);
            } catch (Exception e) {
                throw e;
            }
        }

        return response;
    }

    private static SSLConnectionSocketFactory setupSSLSocketFactory(KeyManagerFactory keyManagerFactory, TrustManagerFactory trustManagerFactory) throws KeyManagementException, NoSuchAlgorithmException {
        SSLContext sslcontext = SSLContext.getInstance("TLS");
        sslcontext.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), null);

        return new SSLConnectionSocketFactory(sslcontext, new String[]{"TLSv1.2"}, null, SSLConnectionSocketFactory.getDefaultHostnameVerifier());
    }
}
