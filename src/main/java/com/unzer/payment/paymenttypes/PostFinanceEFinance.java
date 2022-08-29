package com.unzer.payment.paymenttypes;

import com.unzer.payment.Charge;
import com.unzer.payment.Customer;
import com.unzer.payment.GeoLocation;
import com.unzer.payment.communication.HttpCommunicationException;
import com.unzer.payment.communication.json.JsonIdObject;
import com.unzer.payment.communication.json.JsonObject;

import java.math.BigDecimal;
import java.net.URL;
import java.util.Currency;

public class PostFinanceEFinance extends AbstractPaymentType implements PaymentType {

    @Override
    public String getTypeUrl() {
        return "types/post-finance-efinance";
    }

    @Override
    public PaymentType map(PaymentType postFinanceEFinance, JsonObject jsonId) {
        ((PostFinanceEFinance) postFinanceEFinance).setId(jsonId.getId());
        ((PostFinanceEFinance) postFinanceEFinance).setRecurring(((JsonIdObject) jsonId).getRecurring());
        GeoLocation tempGeoLocation = new GeoLocation(((JsonIdObject) jsonId).getGeoLocation().getClientIp(), ((JsonIdObject) jsonId).getGeoLocation().getCountryIsoA2());
        ((PostFinanceEFinance) postFinanceEFinance).setGeoLocation(tempGeoLocation);
        return postFinanceEFinance;
    }

    public Charge charge(BigDecimal amount, Currency currency, URL returnUrl) throws HttpCommunicationException {
        return getUnzer().charge(amount, currency, this, returnUrl);
    }

    public Charge charge(BigDecimal amount, Currency currency, URL returnUrl, Customer customer) throws HttpCommunicationException {
        return getUnzer().charge(amount, currency, this, returnUrl, customer);
    }

}
