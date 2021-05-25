package com.unzer.payment.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.unzer.payment.ApplePaySession;
import com.unzer.payment.service.PropertiesUtil;
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
import java.net.URI;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;

public class ApplePayAdapterUtil {

    private ApplePayAdapterUtil() {

    }

    public static String validateApplePayMerchant(String merchantValidationURL, ApplePaySession applePaySession, KeyManagerFactory keyManagerFactory, TrustManagerFactory trustManagerFactory) throws IOException, NoSuchAlgorithmException, KeyManagementException, URISyntaxException {
        ObjectMapper mapper = new ObjectMapper();
        SSLConnectionSocketFactory sslsf = setupSSLSocketFactory(keyManagerFactory, trustManagerFactory);
        String response = "";

        if (doesUrlContainValidDomainName(merchantValidationURL)) {
            try (CloseableHttpClient httpclient = HttpClients.custom().setSSLSocketFactory(sslsf).build()) {
                HttpPost post = new HttpPost(merchantValidationURL);
                StringEntity reqEntity = new StringEntity(mapper.writeValueAsString(applePaySession), ContentType.APPLICATION_JSON);
                post.setEntity(reqEntity);

                CloseableHttpResponse res = httpclient.execute(post);
                HttpEntity entity = res.getEntity();
                response = EntityUtils.toString(entity, "UTF-8");
                EntityUtils.consume(entity);
            }
        }

        return response;
    }

    private static SSLConnectionSocketFactory setupSSLSocketFactory(KeyManagerFactory keyManagerFactory, TrustManagerFactory trustManagerFactory) throws KeyManagementException, NoSuchAlgorithmException {
        SSLContext sslcontext = SSLContext.getInstance("TLS");
        sslcontext.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), null);

        return new SSLConnectionSocketFactory(sslcontext, new String[]{"TLSv1.2"}, null, SSLConnectionSocketFactory.getDefaultHostnameVerifier());
    }

    public static String getPlainDomainName(String url) throws URISyntaxException {
        URI uri = new URI(url);
        String domain = uri.getHost();
        return domain.startsWith("www.") ? domain.substring(4) : domain;
    }

    public static boolean doesUrlContainValidDomainName(String merchantValidationURL) throws URISyntaxException {
        PropertiesUtil propertiesUtil = new PropertiesUtil();
        String merchantValidationUrlDomain = getPlainDomainName(merchantValidationURL);
        List<String> validApplePayDomains = Arrays.asList(propertiesUtil.getString("applepay.validValidationUrls").split(","));

        return validApplePayDomains.contains(merchantValidationUrlDomain);
    }
}
