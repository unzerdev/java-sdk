package com.unzer.payment;

/**
 * Represents any resource that can be created, fetched, updated or deleted using Payment Gateway.
 */
public interface Resource {
    /**
     * Returns resource unique id.
     * <br/>
     * For example: s-pay-21
     *
     * @return resource id
     */
    String getId();

    /**
     * Returns the versioned URL of the resource.
     * <br/>
     * For example: /v1/payments/s-pay-21/charges/s-chg-3
     *
     * @return resource URL
     */
    String getUrl();
}
