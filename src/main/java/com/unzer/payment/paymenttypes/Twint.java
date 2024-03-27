package com.unzer.payment.paymenttypes;

import com.unzer.payment.GeoLocation;
import com.unzer.payment.communication.json.ApiIdObject;
import com.unzer.payment.communication.json.ApiObject;

public class Twint extends BasePaymentType {

    @Override
    public String getResourceUrl() {
        return "/v1/types/twint/<resourceId>";
    }

    @Override
    public PaymentType map(PaymentType twint, ApiObject jsonId) {
        ((Twint) twint).setId(jsonId.getId());
        ((Twint) twint).setRecurring(((ApiIdObject) jsonId).getRecurring());
        GeoLocation tempGeoLocation =
                new GeoLocation(((ApiIdObject) jsonId).getGeoLocation().getClientIp(),
                        ((ApiIdObject) jsonId).getGeoLocation().getCountryIsoA2());
        ((Twint) twint).setGeoLocation(tempGeoLocation);
        return twint;
    }
}
