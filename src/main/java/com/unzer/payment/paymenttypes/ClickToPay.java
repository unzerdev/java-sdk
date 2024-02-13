package com.unzer.payment.paymenttypes;

import com.unzer.payment.GeoLocation;
import com.unzer.payment.Unzer;
import com.unzer.payment.communication.json.ApiObject;
import com.unzer.payment.communication.json.ApiSepaDirectDebit;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
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
        ((ClickToPay) paymentType).setMcCorrelationId(((ClickToPay) apiObject).getMcCorrelationId());
        ((ClickToPay) paymentType).setMcCxFlowId(((ClickToPay) apiObject).getMcCxFlowId());
        ((ClickToPay) paymentType).setMcMerchantTransactionId(((ClickToPay) apiObject).getMcMerchantTransactionId());
        ((ClickToPay) paymentType).setBrand(((ClickToPay) apiObject).getBrand());
        ((ClickToPay) paymentType).setRecurring(((ClickToPay) apiObject).getRecurring());
        GeoLocation tempGeoLocation =
                new GeoLocation(((ClickToPay) apiObject).getGeoLocation().getClientIp(),
                        ((ClickToPay) apiObject).getGeoLocation().getCountryIsoA2());

        ((ClickToPay) paymentType).setGeoLocation(tempGeoLocation);

        return paymentType;
    }
}
