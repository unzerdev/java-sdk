package com.unzer.payment;

import com.google.gson.annotations.SerializedName;

public class ShippingAddress extends Address {
    private Type shippingType;

    public Type getShippingType() {
        return shippingType;
    }

    public ShippingAddress setShippingType(Type shippingType) {
        this.shippingType = shippingType;
        return this;
    }

    /**
     * Creates ShippingAddress object from Address object.
     * @param address
     * @param shippingType optional. could be null
     * @return
     */
    public static ShippingAddress of(Address address, Type shippingType) {
        return (ShippingAddress) new ShippingAddress()
                .setShippingType(shippingType)
                .setName(address.getName())
                .setCountry(address.getCountry())
                .setStreet(address.getStreet())
                .setCity(address.getCity())
                .setZip(address.getZip())
                .setState(address.getState());
    }

    public enum Type {
        @SerializedName("equals-billing")
        EQUALS_BILLING,
        @SerializedName("different-addresses")
        DIFFERENT_ADDRESSES,
        @SerializedName("branch-pickup")
        BRANCH_PICKUP,
        @SerializedName("post-office-pickup")
        POST_OFFICE_PICKUP,
        @SerializedName("pack-station")
        PACK_STATION
    }
}
