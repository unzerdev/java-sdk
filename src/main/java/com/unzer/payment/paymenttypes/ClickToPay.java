package com.unzer.payment.paymenttypes;

import com.unzer.payment.GeoLocation;
import com.unzer.payment.communication.json.ApiIdObject;
import com.unzer.payment.communication.json.ApiObject;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ClickToPay extends BasePaymentType {
    private String mcCorrelationId;
    private String mcCxFlowId;
    private String mcMerchantTransactionId;
    private String brand;


    @Override
    protected String getResourceUrl() {
        return "/v1/types/clicktopay/<resourceId>";
    }

    @Override
    public PaymentType map(PaymentType paymentType, ApiObject apiObject) {

        ApiIdObject idObject = (ApiIdObject) apiObject;

        ((ClickToPay) paymentType).setId(apiObject.getId());
        ((ClickToPay) paymentType).setRecurring(idObject.getRecurring());

        GeoLocation tempGeoLocation =
                new GeoLocation(idObject.getGeoLocation().getClientIp(),
                        idObject.getGeoLocation().getCountryIsoA2());

        ((ClickToPay) paymentType).setGeoLocation(tempGeoLocation);

        return paymentType;
    }
}
