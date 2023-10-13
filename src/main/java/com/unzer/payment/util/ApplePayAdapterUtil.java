package com.unzer.payment.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.unzer.payment.ApplePaySession;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder;
import org.apache.hc.client5.http.io.HttpClientConnectionManager;
import org.apache.hc.client5.http.ssl.HttpsSupport;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactory;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;

public class ApplePayAdapterUtil {
  public static final List<String> DEFAULT_VALIDATION_URLS = Collections.unmodifiableList(
      Arrays.asList(
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
      ));

  private static Set<String> urls = new HashSet<>(DEFAULT_VALIDATION_URLS);

  private ApplePayAdapterUtil() {
  }

  public static void setCustomAppleValidationUrls(List<String> appleUrls) {
    urls = new HashSet<>(appleUrls);
  }

  public static String validateApplePayMerchant(
      String merchantValidationUrl,
      ApplePaySession applePaySession,
      KeyManagerFactory keyManagerFactory,
      TrustManagerFactory trustManagerFactory
  ) throws IOException, NoSuchAlgorithmException, KeyManagementException, URISyntaxException,
      ParseException {
    ObjectMapper mapper = new ObjectMapper();
    SSLConnectionSocketFactory sslsf =
        setupSslSocketFactory(keyManagerFactory, trustManagerFactory);
    String response = "";


    HttpClientConnectionManager cm =
        PoolingHttpClientConnectionManagerBuilder.create().setSSLSocketFactory(sslsf).build();

    if (doesUrlContainValidDomainName(merchantValidationUrl)) {
      try (CloseableHttpClient client = HttpClients.custom().setConnectionManager(cm).build()) {
        HttpPost post = new HttpPost(merchantValidationUrl);
        StringEntity reqEntity = new StringEntity(mapper.writeValueAsString(applePaySession),
            ContentType.APPLICATION_JSON);
        post.setEntity(reqEntity);

        CloseableHttpResponse res = client.execute(post);
        HttpEntity entity = res.getEntity();
        response = EntityUtils.toString(entity, "UTF-8");
        EntityUtils.consume(entity);
      }
    }

    return response;
  }

  private static SSLConnectionSocketFactory setupSslSocketFactory(
      KeyManagerFactory keyManagerFactory,
      TrustManagerFactory trustManagerFactory
  ) throws KeyManagementException, NoSuchAlgorithmException {
    SSLContext sslcontext = SSLContext.getInstance("TLS");
    sslcontext.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(),
        null);

    return new SSLConnectionSocketFactory(sslcontext, new String[] {"TLSv1.2"}, null,
        HttpsSupport.getDefaultHostnameVerifier());
  }

  public static boolean doesUrlContainValidDomainName(String merchantValidationUrl)
      throws URISyntaxException {
    String merchantValidationUrlDomain = getPlainDomainName(merchantValidationUrl);

    return urls.contains(merchantValidationUrlDomain);
  }

  private static String getPlainDomainName(String url) throws URISyntaxException {
    URI uri = new URI(url);
    String domain = uri.getHost();
    return domain.startsWith("www.") ? domain.substring(4) : domain;
  }
}
