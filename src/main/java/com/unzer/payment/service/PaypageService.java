package com.unzer.payment.service;

import com.unzer.payment.Paypage;
import com.unzer.payment.Unzer;
import com.unzer.payment.communication.HttpCommunicationException;
import com.unzer.payment.communication.JsonParser;
import com.unzer.payment.communication.UnzerRestCommunication;
import com.unzer.payment.communication.json.JsonPaypage;
import com.unzer.payment.communication.mapper.JsonToBusinessClassMapper;

public class PaypageService {
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
    public PaypageService(Unzer unzer, UnzerRestCommunication restCommunication) {
        super();
        this.unzer = unzer;
        this.urlUtil = new UrlUtil(unzer.getEndPoint());
        this.restCommunication = restCommunication;
    }

    public Paypage initialize(Paypage paypage) throws HttpCommunicationException {
        return initialize(paypage, urlUtil.getRestUrl(paypage));
    }

    public Paypage initialize(Paypage paypage, String url) throws HttpCommunicationException {
        String response = restCommunication.httpPost(url, unzer.getPrivateKey(), jsonToBusinessClassMapper.map(paypage));
        JsonPaypage jsonPaypage = new JsonParser().fromJson(response, JsonPaypage.class);
        paypage = jsonToBusinessClassMapper.mapToBusinessObject(paypage, jsonPaypage);
        return paypage;
    }


}
