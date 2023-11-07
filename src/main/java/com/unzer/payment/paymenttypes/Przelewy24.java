package com.unzer.payment.paymenttypes;

import com.unzer.payment.Charge;
import com.unzer.payment.Customer;
import com.unzer.payment.GeoLocation;
import com.unzer.payment.communication.HttpCommunicationException;
import com.unzer.payment.communication.json.ApiIdObject;
import com.unzer.payment.communication.json.ApiObject;

import java.math.BigDecimal;
import java.net.URL;
import java.util.Currency;

/**
 * P24 business object
 *
 * @author Unzer E-Com GmbH
 */
public class Przelewy24 extends BasePaymentType {

    @Override
    public String getResourceUrl() {
        return "/v1/types/przelewy24/<resourceId>";
    }

    @Override
    public PaymentType map(PaymentType przelewy24, ApiObject jsonId) {
        ((Przelewy24) przelewy24).setId(jsonId.getId());
        ((Przelewy24) przelewy24).setRecurring(((ApiIdObject) jsonId).getRecurring());
        GeoLocation tempGeoLocation =
                new GeoLocation(((ApiIdObject) jsonId).getGeoLocation().getClientIp(),
                        ((ApiIdObject) jsonId).getGeoLocation().getCountryIsoA2());
        ((Przelewy24) przelewy24).setGeoLocation(tempGeoLocation);
        return przelewy24;
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
