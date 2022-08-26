package com.unzer.payment.models;

/*-
 * #%L
 * Unzer Java SDK
 * %%
 * Copyright (C) 2020 - today Unzer E-Com GmbH
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

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
     */    public ShippingTransactionData setDeliveryService(String deliveryService) {
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
}