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

/**
 * P24 business object
 *
 * @author Unzer E-Com GmbH
 */
public class Przelewy24 extends AbstractPaymentType implements PaymentType {

    @Override
    public String getTypeUrl() {
        return "types/przelewy24";
    }

    @Override
    public PaymentType map(PaymentType przelewy24, JsonObject jsonId) {
        ((Przelewy24) przelewy24).setId(jsonId.getId());
        ((Przelewy24) przelewy24).setRecurring(((JsonIdObject) jsonId).getRecurring());
        GeoLocation tempGeoLocation = new GeoLocation(((JsonIdObject) jsonId).getGeoLocation().getClientIp(), ((JsonIdObject) jsonId).getGeoLocation().getCountryIsoA2());
        ((Przelewy24) przelewy24).setGeoLocation(tempGeoLocation);
        return przelewy24;
    }

    public Charge charge(BigDecimal amount, Currency currency, URL returnUrl) throws HttpCommunicationException {
        return getUnzer().charge(amount, currency, this, returnUrl);
    }

    public Charge charge(BigDecimal amount, Currency currency, URL returnUrl, Customer customer) throws HttpCommunicationException {
        return getUnzer().charge(amount, currency, this, returnUrl, customer);
    }

}
