package com.unzer.payment.service.v2;

import com.unzer.payment.Unzer;
import com.unzer.payment.communication.HttpCommunicationException;
import com.unzer.payment.communication.JsonParser;
import com.unzer.payment.communication.UnzerHttpRequest;
import com.unzer.payment.communication.UnzerRestCommunication;
import com.unzer.payment.communication.api.ApiConfigs;
import com.unzer.payment.resources.PaypageV2;
import com.unzer.payment.service.UrlUtil;

public class PaypageService {
    private final UnzerRestCommunication restCommunication;

    private final UrlUtil urlUtil;
    private final Unzer unzer;

    /**
     * Creates the {@code PaymentService} with the given {@code Unzer} facade,
     * bound to the given {@code UnzerRestCommunication} implementation used for
     * http-communication.
     *
     * @param unzer             - the {@code Unzer} Facade
     * @param restCommunication - the implementation of {@code UnzerRestCommunication} to be
     *                          used for network communication.
     */
    public PaypageService(Unzer unzer, UnzerRestCommunication restCommunication) {
        super();
        this.unzer = unzer;
        this.urlUtil = new UrlUtil(unzer.getPrivateKey(), ApiConfigs.PAYPAGE_API);
        this.restCommunication = restCommunication;
    }

    public PaypageV2 create(PaypageV2 paypage) throws HttpCommunicationException {
        return create(paypage, urlUtil.getUrl(paypage));
    }

    public PaypageV2 create(PaypageV2 paypage, String url) throws HttpCommunicationException {
        unzer.prepareJwtToken();
        String response = restCommunication.httpPost(
                url,
                unzer.getJwtToken(),
                paypage,
                ApiConfigs.PAYPAGE_API
        );

        return new JsonParser().fromJson(response, PaypageV2.class);
    }

    public PaypageV2 update(PaypageV2 paypage) throws HttpCommunicationException {
        return update(paypage, urlUtil.getUrl(paypage, UnzerHttpRequest.UnzerHttpMethod.PATCH));
    }

    public PaypageV2 update(PaypageV2 paypage, String url) throws HttpCommunicationException {
        unzer.prepareJwtToken();
        String response = restCommunication.httpPatch(
                url,
                unzer.getJwtToken(),
                paypage,
                ApiConfigs.PAYPAGE_API
        );

        return new JsonParser().fromJson(response, PaypageV2.class);
    }

    public PaypageV2 delete(PaypageV2 paypage) throws HttpCommunicationException {
        return delete(paypage, urlUtil.getUrl(paypage, UnzerHttpRequest.UnzerHttpMethod.DELETE));
    }

    public PaypageV2 delete(PaypageV2 paypage, String url) throws HttpCommunicationException {
        unzer.prepareJwtToken();
        String response = restCommunication.httpDelete(
                url,
                unzer.getJwtToken(),
                ApiConfigs.PAYPAGE_API
        );

        return new JsonParser().fromJson(response, PaypageV2.class);
    }

    public PaypageV2 fetch(String paypageId) {
        unzer.prepareJwtToken();
        String response = restCommunication.httpGet(
                urlUtil.getUrl(new PaypageV2()) + "/" + paypageId,
                unzer.getJwtToken(),
                ApiConfigs.PAYPAGE_API
        );
        return new JsonParser().fromJson(response, PaypageV2.class);
    }
}
