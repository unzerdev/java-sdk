package com.unzer.payment.paymenttypes;

import com.unzer.payment.Charge;
import com.unzer.payment.Customer;
import com.unzer.payment.GeoLocation;
import com.unzer.payment.Unzer;
import com.unzer.payment.communication.HttpCommunicationException;
import com.unzer.payment.communication.json.ApiIdObject;
import com.unzer.payment.communication.json.ApiObject;

import java.math.BigDecimal;
import java.net.URL;
import java.util.Currency;

public class Eps extends BasePaymentType {

    private String bic;

    public Eps(String bic) {
        this.bic = bic;
    }

    public Eps() {
        super();
    }

    @Deprecated
    public Eps(Unzer unzer) {
        super(unzer);
    }

    @Override
    public String getResourceUrl() {
        return "/v1/types/eps/<resourceId>";
    }

    @Override
    public PaymentType map(PaymentType eps, ApiObject jsonId) {
        ((Eps) eps).setId(jsonId.getId());
        ((Eps) eps).setRecurring(((ApiIdObject) jsonId).getRecurring());
        GeoLocation tempGeoLocation =
                new GeoLocation(((ApiIdObject) jsonId).getGeoLocation().getClientIp(),
                        ((ApiIdObject) jsonId).getGeoLocation().getCountryIsoA2());
        ((Eps) eps).setGeoLocation(tempGeoLocation);
        return eps;
    }

    public String getBic() {
        return bic;
    }

    public void setBic(String bic) {
        this.bic = bic;
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
