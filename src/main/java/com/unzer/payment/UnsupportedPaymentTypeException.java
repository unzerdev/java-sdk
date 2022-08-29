package com.unzer.payment;

/**
 * Thrown if the api returns a payment-type not known/supported by the sdk.
 */
public class UnsupportedPaymentTypeException extends RuntimeException {

    private static final long serialVersionUID = -1285106124514039751L;

    public UnsupportedPaymentTypeException(String message) {
        super(message);
    }

}
