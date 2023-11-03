package com.unzer.payment.models;

import java.util.Objects;

public class ShippingTransactionData {
    private String deliveryTrackingId;
    private String deliveryService;
    private String returnTrackingId;

    /**
     * Tracking ID from shipping from merchant to customer
     */
    public String getDeliveryTrackingId() {
        return deliveryTrackingId;
    }

    /**
     * Tracking ID from shipping from merchant to customer
     */
    public ShippingTransactionData setDeliveryTrackingId(String deliveryTrackingId) {
        this.deliveryTrackingId = deliveryTrackingId;
        return this;
    }

    /**
     * Delivery service from shipment from merchant to customer.
     */
    public String getDeliveryService() {
        return deliveryService;
    }

    /**
     * Delivery service from shipment from merchant to customer.
     */
    public ShippingTransactionData setDeliveryService(String deliveryService) {
        this.deliveryService = deliveryService;
        return this;
    }

    /**
     * Tracking ID from shipping from merchant to customer
     */
    public String getReturnTrackingId() {
        return returnTrackingId;
    }

    /**
     * Tracking ID from shipping from merchant to customer
     */
    public ShippingTransactionData setReturnTrackingId(String returnTrackingId) {
        this.returnTrackingId = returnTrackingId;
        return this;
    }

    @Override
    public int hashCode() {
        int result = deliveryTrackingId != null ? deliveryTrackingId.hashCode() : 0;
        result = 31 * result + (deliveryService != null ? deliveryService.hashCode() : 0);
        result = 31 * result + (returnTrackingId != null ? returnTrackingId.hashCode() : 0);
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ShippingTransactionData that = (ShippingTransactionData) o;

        if (!Objects.equals(deliveryTrackingId, that.deliveryTrackingId)) {
            return false;
        }
        if (!Objects.equals(deliveryService, that.deliveryService)) {
            return false;
        }
        return Objects.equals(returnTrackingId, that.returnTrackingId);
    }
}