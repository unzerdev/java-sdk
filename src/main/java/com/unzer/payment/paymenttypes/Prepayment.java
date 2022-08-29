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
 * Prepayment business object
 *
 * @author Unzer E-Com GmbH
 */
public class Prepayment extends AbstractPaymentType implements PaymentType {

    @Override
    public String getTypeUrl() {
        return "types/prepayment";
    }

    @Override
    public PaymentType map(PaymentType prepayment, JsonObject jsonId) {
        ((Prepayment) prepayment).setId(jsonId.getId());
        ((Prepayment) prepayment).setRecurring(((JsonIdObject) jsonId).getRecurring());
        GeoLocation tempGeoLocation = new GeoLocation(((JsonIdObject) jsonId).getGeoLocation().getClientIp(), ((JsonIdObject) jsonId).getGeoLocation().getCountryIsoA2());
        ((Prepayment) prepayment).setGeoLocation(tempGeoLocation);
        return prepayment;
    }

    public Charge charge(BigDecimal amount, Currency currency, URL returnUrl) throws HttpCommunicationException {
        return charge(amount, currency, returnUrl, null);
    }

    public Charge charge(BigDecimal amount, Currency currency, URL returnUrl, Customer customer) throws HttpCommunicationException {
        return getUnzer().charge(amount, currency, this, returnUrl, customer);
    }

}
