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
import java.net.URI;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.*;

public class ApplePayAdapterUtil {
    private static final List<String> defaultUrls = Arrays.asList(
            "apple-pay-gateway.apple.com",
            "cn-apple-pay-gateway.apple.com",
            "apple-pay-gateway-nc-pod1.apple.com",
            "apple-pay-gateway-nc-pod2.apple.com",
            "apple-pay-gateway-nc-pod3.apple.com",
            "apple-pay-gateway-nc-pod4.apple.com",
            "apple-pay-gateway-nc-pod5.apple.com",
            "apple-pay-gateway-pr-pod1.apple.com",
            "apple-pay-gateway-pr-pod2.apple.com",
            "apple-pay-gateway-pr-pod3.apple.com",
            "apple-pay-gateway-pr-pod4.apple.com",
            "apple-pay-gateway-pr-pod5.apple.com",
            "cn-apple-pay-gateway-sh-pod1.apple.com",
            "cn-apple-pay-gateway-sh-pod2.apple.com",
            "cn-apple-pay-gateway-sh-pod3.apple.com",
            "cn-apple-pay-gateway-tj-pod1.apple.com",
            "cn-apple-pay-gateway-tj-pod2.apple.com",
            "cn-apple-pay-gateway-tj-pod3.apple.com",
            "apple-pay-gateway-cert.apple.com",
            "cn-apple-pay-gateway-cert.apple.com"
    );

    private static Set<String> urls = new HashSet<>(defaultUrls);

    public static void setCustomAppleValidationUrls(String... appleUrls) {
        urls = new HashSet<>(Arrays.asList(appleUrls));
    }

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

    public static boolean doesUrlContainValidDomainName(String merchantValidationURL) throws URISyntaxException {
        String merchantValidationUrlDomain = getPlainDomainName(merchantValidationURL);

        return urls.contains(merchantValidationUrlDomain);
    }

    private static SSLConnectionSocketFactory setupSSLSocketFactory(KeyManagerFactory keyManagerFactory, TrustManagerFactory trustManagerFactory) throws KeyManagementException, NoSuchAlgorithmException {
        SSLContext sslcontext = SSLContext.getInstance("TLS");
        sslcontext.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), null);

        return new SSLConnectionSocketFactory(sslcontext, new String[]{"TLSv1.2"}, null, SSLConnectionSocketFactory.getDefaultHostnameVerifier());
    }

    private static String getPlainDomainName(String url) throws URISyntaxException {
        URI uri = new URI(url);
        String domain = uri.getHost();
        return domain.startsWith("www.") ? domain.substring(4) : domain;
    }
}
