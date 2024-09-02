package com.unzer.payment.service.v2;

import com.unzer.payment.Unzer;
import com.unzer.payment.communication.HttpCommunicationException;
import com.unzer.payment.communication.JsonParser;
import com.unzer.payment.communication.UnzerRestCommunication;
import com.unzer.payment.communication.api.ApiConfigs;
import com.unzer.payment.communication.mapper.ApiToSdkConverter;
import com.unzer.payment.resources.PaypageV2;
import com.unzer.payment.service.UrlUtil;

public class PaypageService {
    private final UnzerRestCommunication restCommunication;

    private final UrlUtil urlUtil;
    private final ApiToSdkConverter jsonToObjectMapper = new ApiToSdkConverter();
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
}
