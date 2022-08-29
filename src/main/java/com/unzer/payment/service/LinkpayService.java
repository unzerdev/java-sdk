package com.unzer.payment.service;

import com.unzer.payment.Linkpay;
import com.unzer.payment.Unzer;
import com.unzer.payment.communication.HttpCommunicationException;
import com.unzer.payment.communication.JsonParser;
import com.unzer.payment.communication.UnzerRestCommunication;
import com.unzer.payment.communication.json.JsonLinkpay;
import com.unzer.payment.communication.mapper.JsonToBusinessClassMapper;

public class LinkpayService {
    private UnzerRestCommunication restCommunication;

    private UrlUtil urlUtil;
    private JsonToBusinessClassMapper jsonToBusinessClassMapper = new JsonToBusinessClassMapper();
    private Unzer unzer;

    /**
     * Creates the {@code PaymentService} with the given {@code Unzer} facade,
     * bound to the given {@code UnzerRestCommunication} implementation used for
     * http-communication.
     *
     * @param unzer             - the {@code Unzer} Facade
     * @param restCommunication - the implementation of {@code UnzerRestCommunication} to be used for network communication.
     */
    public LinkpayService(Unzer unzer, UnzerRestCommunication restCommunication) {
        super();
        this.unzer = unzer;
        this.urlUtil = new UrlUtil(unzer.getEndPoint());
        this.restCommunication = restCommunication;
    }

    public Linkpay initialize(Linkpay linkpay) throws HttpCommunicationException {
        return initialize(linkpay, urlUtil.getRestUrl(linkpay));
    }

    public Linkpay initialize(Linkpay linkpay, String url) throws HttpCommunicationException {
        String response = restCommunication.httpPost(url, unzer.getPrivateKey(), jsonToBusinessClassMapper.map(linkpay));
        JsonLinkpay jsonLinkpay = new JsonParser().fromJson(response, JsonLinkpay.class);
        linkpay = jsonToBusinessClassMapper.mapToBusinessObject(linkpay, jsonLinkpay);
        return linkpay;
    }

}
