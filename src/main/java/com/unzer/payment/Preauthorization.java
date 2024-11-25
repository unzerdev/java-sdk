package com.unzer.payment;

/**
 * Business object for Authorization. Amount, currency and typeId are mandatory parameter to
 * execute an Authorization.
 * <p>
 * The returnUrl is mandatory in case of redirectPayments like Sofort, Paypal, Giropay, Card 3DS
 *
 * @author Unzer E-Com GmbH
 */
public class Preauthorization extends Authorization {

    public Preauthorization() {
        super();
    }

    @Override
    protected String getTransactionUrl() {
        return "/v1/payments/<paymentId>/preauthorize/<transactionId>";
    }
}
