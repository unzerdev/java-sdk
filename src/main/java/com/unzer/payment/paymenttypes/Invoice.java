package com.unzer.payment.paymenttypes;

import com.unzer.payment.Charge;
import com.unzer.payment.Customer;
import com.unzer.payment.communication.HttpCommunicationException;
import com.unzer.payment.communication.json.JsonIdObject;
import com.unzer.payment.communication.json.JsonObject;

import java.math.BigDecimal;
import java.net.URL;
import java.util.Currency;

/**
 * Invoice business object
 *
 * @author Unzer E-Com GmbH
 */
public class Invoice extends AbstractPaymentType implements PaymentType {

    @Override
    public String getTypeUrl() {
        return "types/invoice";
    }

    @Override
    public PaymentType map(PaymentType invoice, JsonObject jsonId) {
        ((Invoice) invoice).setId(jsonId.getId());
        ((Invoice) invoice).setRecurring(((JsonIdObject) jsonId).getRecurring());
        return invoice;
    }

    public Charge charge(BigDecimal amount, Currency currency, URL returnUrl) throws HttpCommunicationException {
        return charge(amount, currency, returnUrl, null);
    }

    public Charge charge(BigDecimal amount, Currency currency, URL returnUrl, Customer customer) throws HttpCommunicationException {
        return getUnzer().charge(amount, currency, this, returnUrl, customer);
    }

}
