package com.unzer.payment.paymenttypes;

import com.unzer.payment.GeoLocation;
import com.unzer.payment.communication.json.ApiIdObject;
import com.unzer.payment.communication.json.ApiObject;

public class Wero extends BasePaymentType {

    @Override
    public String getResourceUrl() {
        return "/v1/types/wero/<resourceId>";
    }

    @Override
    public PaymentType map(PaymentType wero, ApiObject jsonId) {
        ((Wero) wero).setId(jsonId.getId());
        ((Wero) wero).setRecurring(((ApiIdObject) jsonId).getRecurring());
        GeoLocation tempGeoLocation =
                new GeoLocation(((ApiIdObject) jsonId).getGeoLocation().getClientIp(),
                        ((ApiIdObject) jsonId).getGeoLocation().getCountryIsoA2());
        ((Wero) wero).setGeoLocation(tempGeoLocation);
        return wero;
    }
}
