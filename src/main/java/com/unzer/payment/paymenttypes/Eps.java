package com.unzer.payment.paymenttypes;

import com.unzer.payment.Charge;
import com.unzer.payment.Customer;
import com.unzer.payment.GeoLocation;
import com.unzer.payment.Unzer;
import com.unzer.payment.communication.HttpCommunicationException;
import com.unzer.payment.communication.json.JsonIdObject;
import com.unzer.payment.communication.json.JsonObject;

import java.math.BigDecimal;
import java.net.URL;
import java.util.Currency;

public class Eps extends AbstractPaymentType implements PaymentType {

    private String bic;

    public Eps(String bic) {
        this.bic = bic;
    }

    public Eps() {
        super();
    }

    public Eps(Unzer unzer) {
        super(unzer);
    }

    @Override
    public String getTypeUrl() {
        return "types/eps";
    }

    @Override
    public PaymentType map(PaymentType eps, JsonObject jsonId) {
        ((Eps) eps).setId(jsonId.getId());
        ((Eps) eps).setRecurring(((JsonIdObject) jsonId).getRecurring());
        GeoLocation tempGeoLocation = new GeoLocation(((JsonIdObject) jsonId).getGeoLocation().getClientIp(), ((JsonIdObject) jsonId).getGeoLocation().getCountryIsoA2());
        ((Eps) eps).setGeoLocation(tempGeoLocation);
        return eps;
    }

    public String getBic() {
        return bic;
    }

    public void setBic(String bic) {
        this.bic = bic;
    }

    public Charge charge(BigDecimal amount, Currency currency, URL returnUrl) throws HttpCommunicationException {
        return getUnzer().charge(amount, currency, this, returnUrl);
    }

    public Charge charge(BigDecimal amount, Currency currency, URL returnUrl, Customer customer) throws HttpCommunicationException {
        return getUnzer().charge(amount, currency, this, returnUrl, customer);
    }

}
