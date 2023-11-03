package com.unzer.payment.paymenttypes;

import com.unzer.payment.Charge;
import com.unzer.payment.Customer;
import com.unzer.payment.communication.HttpCommunicationException;
import com.unzer.payment.communication.json.ApiIdObject;
import com.unzer.payment.communication.json.ApiObject;

import java.math.BigDecimal;
import java.net.URL;
import java.util.Currency;

/**
 * Wechatpay business object
 *
 * @author Unzer E-Com GmbH
 */
public class Wechatpay extends BasePaymentType {

    @Override
    public String getResourceUrl() {
        return "/v1/types/wechatpay/<resourceId>";
    }

    @Override
    public PaymentType map(PaymentType wechatpay, ApiObject jsonId) {
        ((Wechatpay) wechatpay).setId(jsonId.getId());
        ((Wechatpay) wechatpay).setRecurring(((ApiIdObject) jsonId).getRecurring());
        return wechatpay;
    }

    @Deprecated
    public Charge charge(BigDecimal amount, Currency currency, URL returnUrl)
            throws HttpCommunicationException {
        return getUnzer().charge(amount, currency, this, returnUrl);
    }

    @Deprecated
    public Charge charge(BigDecimal amount, Currency currency, URL returnUrl, Customer customer)
            throws HttpCommunicationException {
        return getUnzer().charge(amount, currency, this, returnUrl, customer);
    }

}
