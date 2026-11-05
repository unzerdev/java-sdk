package com.unzer.payment.paymenttypes;

import com.unzer.payment.BaseResource;
import com.unzer.payment.GeoLocation;
import com.unzer.payment.Unzer;

public abstract class BasePaymentType extends BaseResource implements PaymentType {
    private String id;
    /**
     * Indicates whether the payment type is registered for recurring collection.
     *
     * <p>For most payment types this is a read-only value returned by the API.
     * {@code sepa-direct-debit} is the type where it is also accepted on creation
     * (see {@link SepaDirectDebit#recurring(boolean)}).</p>
     */
    private Boolean recurring;
    private GeoLocation geoLocation;

    private transient Unzer unzer;

    @Deprecated
    public BasePaymentType(Unzer unzer) {
        this.setUnzer(unzer);
    }

    public BasePaymentType() {
    }

    @Override
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Deprecated
    public Unzer getUnzer() {
        return unzer;
    }

    @Deprecated
    public void setUnzer(Unzer unzer) {
        this.unzer = unzer;
    }

    public Boolean getRecurring() {
        return recurring;
    }

    public void setRecurring(Boolean recurring) {
        this.recurring = recurring;
    }

    public GeoLocation getGeoLocation() {
        return geoLocation;
    }

    public void setGeoLocation(GeoLocation geoLocation) {
        this.geoLocation = geoLocation;
    }
}
