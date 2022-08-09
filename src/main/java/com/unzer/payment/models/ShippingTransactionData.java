package com.unzer.payment.models;

public class ShippingTransactionData {
    private String deliveryTrackingId;
    private String deliveryService;
    private String returnTrackingId;

    public String getDeliveryTrackingId() {
        return deliveryTrackingId;
    }

    public void setDeliveryTrackingId(String deliveryTrackingId) {
        this.deliveryTrackingId = deliveryTrackingId;
    }

    public String getDeliveryService() {
        return deliveryService;
    }

    public void setDeliveryService(String deliveryService) {
        this.deliveryService = deliveryService;
    }

    public String getReturnTrackingId() {
        return returnTrackingId;
    }

    public void setReturnTrackingId(String returnTrackingId) {
        this.returnTrackingId = returnTrackingId;
    }
}