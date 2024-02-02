package com.unzer.payment.paymenttypes;

import com.unzer.payment.GeoLocation;
import com.unzer.payment.communication.json.ApiCard;
import com.unzer.payment.communication.json.ApiIdObject;
import com.unzer.payment.communication.json.ApiObject;
import com.unzer.payment.models.googlepay.IntermediateSigningKey;
import com.unzer.payment.models.googlepay.SignedMessage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Google Pay business object. It requires data from payment method token returned by Google in the `PaymentData`.
 * These data are used to create the payment type on the Unzer API.
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class GooglePay extends BasePaymentType {
    private String protocolVersion;
    private String signature;
    private IntermediateSigningKey intermediateSigningKey;
    private SignedMessage signedMessage;
    private String number;
    private String expiryDate;

    @Override
    protected String getResourceUrl() {
        return "/v1/types/googlepay/<resourceId>";
    }

    @Override
    public PaymentType map(PaymentType googlepay, ApiObject jsonId) {
        ((GooglePay) googlepay).setId(jsonId.getId());
        ((GooglePay) googlepay).setRecurring(((ApiIdObject) jsonId).getRecurring());
        ((GooglePay) googlepay).setNumber(((ApiCard) jsonId).getNumber());
        ((GooglePay) googlepay).setExpiryDate(((ApiCard) jsonId).getExpiryDate());
        GeoLocation tempGeoLocation =
                new GeoLocation(((ApiIdObject) jsonId).getGeoLocation().getClientIp(),
                        ((ApiIdObject) jsonId).getGeoLocation().getCountryIsoA2());
        ((GooglePay) googlepay).setGeoLocation(tempGeoLocation);
        return googlepay;
    }
}
