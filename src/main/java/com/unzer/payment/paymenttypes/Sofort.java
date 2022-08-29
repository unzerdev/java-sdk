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

public class Sofort extends AbstractPaymentType implements PaymentType {

    @Override
    public String getTypeUrl() {
        return "types/sofort";
    }

    @Override
    public PaymentType map(PaymentType sofort, JsonObject jsonId) {
        ((Sofort) sofort).setId(jsonId.getId());
        ((Sofort) sofort).setRecurring(((JsonIdObject) jsonId).getRecurring());
        GeoLocation tempGeoLocation = new GeoLocation(((JsonIdObject) jsonId).getGeoLocation().getClientIp(), ((JsonIdObject) jsonId).getGeoLocation().getCountryIsoA2());
        ((Sofort) sofort).setGeoLocation(tempGeoLocation);
        return sofort;
    }

    public Charge charge(BigDecimal amount, Currency currency, URL returnUrl) throws HttpCommunicationException {
        return getUnzer().charge(amount, currency, this, returnUrl);
    }

    public Charge charge(BigDecimal amount, Currency currency, URL returnUrl, Customer customer) throws HttpCommunicationException {
        return getUnzer().charge(amount, currency, this, returnUrl, customer);
    }

}
