package com.unzer.payment.service;

import com.unzer.payment.Linkpay;
import com.unzer.payment.Unzer;
import com.unzer.payment.communication.HttpCommunicationException;
import com.unzer.payment.communication.JsonParser;
import com.unzer.payment.communication.UnzerRestCommunication;
import com.unzer.payment.communication.json.ApiLinkpay;
import com.unzer.payment.communication.mapper.ApiToSdkConverter;

public class LinkpayService {
  private final UnzerRestCommunication client;

  private final UrlUtil urlUtil;
  private final ApiToSdkConverter apiToSdkConverter = new ApiToSdkConverter();
  private final Unzer unzer;

  /**
   * Creates the {@code PaymentService} with the given {@code Unzer} facade,
   * bound to the given {@code UnzerRestCommunication} implementation used for
   * http-communication.
   *
   * @param unzer             - the {@code Unzer} Facade
   * @param restCommunication - the implementation of {@code UnzerRestCommunication} to be used
   *                          for network communication.
   */
  public LinkpayService(Unzer unzer, UnzerRestCommunication restCommunication) {
    this.unzer = unzer;
    this.urlUtil = new UrlUtil(unzer.getPrivateKey());
    this.client = restCommunication;
  }

  public Linkpay initialize(Linkpay page) throws HttpCommunicationException {
    String response = client.httpPost(
        urlUtil.getUrl(page), unzer.getPrivateKey(),
        apiToSdkConverter.map(page)
    );
    ApiLinkpay resource = new JsonParser().fromJson(response, ApiLinkpay.class);
    page = apiToSdkConverter.mapToBusinessObject(page, resource);
    return page;
  }

  public Linkpay fetch(String id) {
    Linkpay linkpay = new Linkpay();
    linkpay.setId(id);
    String response = client.httpGet(
        urlUtil.getUrl(linkpay),
        unzer.getPrivateKey()
    );
    ApiLinkpay resource = new JsonParser().fromJson(response, ApiLinkpay.class);
    return apiToSdkConverter.mapToBusinessObject(new Linkpay(), resource);
  }
}
