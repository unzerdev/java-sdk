package com.unzer.payment.paymenttypes;

import com.unzer.payment.GeoLocation;
import com.unzer.payment.communication.json.ApiObject;
import com.unzer.payment.communication.json.ApiOpenBanking;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class OpenBanking  extends BasePaymentType {
    private String ibanCountry;


    @Override
    protected String getResourceUrl() {
        return "/v1/types/openbanking-pis/<resourceId>";
    }

    @Override
    public PaymentType map(PaymentType paymentType, ApiObject apiObject) {

        ApiOpenBanking apiOpenBanking = (ApiOpenBanking) apiObject;

        ((OpenBanking) paymentType).setId(apiObject.getId());
        ((OpenBanking) paymentType).setRecurring(apiOpenBanking.getRecurring());

        GeoLocation tempGeoLocation = new GeoLocation(apiOpenBanking.getGeoLocation().getClientIp(), apiOpenBanking.getGeoLocation().getCountryIsoA2());
        ((OpenBanking) paymentType).setGeoLocation(tempGeoLocation);

        ((OpenBanking) paymentType).setIbanCountry(apiOpenBanking.getIbanCountry());

        return paymentType;
    }
}