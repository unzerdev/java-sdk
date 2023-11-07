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

@SuppressWarnings("checkstyle:AbbreviationAsWordInName")
public class PostFinanceEFinance extends BasePaymentType {

    @Override
    public String getResourceUrl() {
        return "/v1/types/post-finance-efinance/<resourceId>";
    }

    @Override
    public PaymentType map(PaymentType type, ApiObject jsonId) {
        ((PostFinanceEFinance) type).setId(jsonId.getId());
        ((PostFinanceEFinance) type).setRecurring(
                ((ApiIdObject) jsonId).getRecurring());
        GeoLocation tempGeoLocation =
                new GeoLocation(((ApiIdObject) jsonId).getGeoLocation().getClientIp(),
                        ((ApiIdObject) jsonId).getGeoLocation().getCountryIsoA2());
        ((PostFinanceEFinance) type).setGeoLocation(tempGeoLocation);
        return type;
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
