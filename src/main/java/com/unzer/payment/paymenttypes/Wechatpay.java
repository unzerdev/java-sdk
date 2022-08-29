package com.unzer.payment.paymenttypes;

import com.unzer.payment.Charge;
import com.unzer.payment.Customer;
import com.unzer.payment.communication.HttpCommunicationException;
import com.unzer.payment.communication.json.JsonIdObject;
import com.unzer.payment.communication.json.JsonObject;

import java.math.BigDecimal;
import java.net.URL;
import java.util.Currency;

/**
 * Wechatpay business object
 *
 * @author Unzer E-Com GmbH
 */
public class Wechatpay extends AbstractPaymentType implements PaymentType {

    @Override
    public String getTypeUrl() {
        return "types/wechatpay";
    }

    @Override
    public PaymentType map(PaymentType wechatpay, JsonObject jsonId) {
        ((Wechatpay) wechatpay).setId(jsonId.getId());
        ((Wechatpay) wechatpay).setRecurring(((JsonIdObject) jsonId).getRecurring());
        return wechatpay;
    }

    public Charge charge(BigDecimal amount, Currency currency, URL returnUrl) throws HttpCommunicationException {
        return getUnzer().charge(amount, currency, this, returnUrl);
    }

    public Charge charge(BigDecimal amount, Currency currency, URL returnUrl, Customer customer) throws HttpCommunicationException {
        return getUnzer().charge(amount, currency, this, returnUrl, customer);
    }

}
