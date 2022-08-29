package com.unzer.payment.paymenttypes;

import com.unzer.payment.Basket;
import com.unzer.payment.Charge;
import com.unzer.payment.Customer;
import com.unzer.payment.GeoLocation;
import com.unzer.payment.communication.HttpCommunicationException;
import com.unzer.payment.communication.json.JsonObject;
import com.unzer.payment.communication.json.JsonSepaDirectDebit;

import java.math.BigDecimal;
import java.net.URL;
import java.util.Currency;

/**
 * Sepa direct debit business object. Iban is mandatory, bic and holder are optional fields
 *
 * @author Unzer E-Com GmbH
 */
public class SepaDirectDebit extends AbstractPaymentType implements PaymentType {
    private String iban;
    private String bic;
    private String holder;

    public SepaDirectDebit(String iban) {
        super();
        this.iban = iban;
    }

    public String getIban() {
        return iban;
    }

    public SepaDirectDebit setIban(String iban) {
        this.iban = iban;
        return this;
    }

    public String getBic() {
        return bic;
    }

    public SepaDirectDebit setBic(String bic) {
        this.bic = bic;
        return this;
    }

    public String getHolder() {
        return holder;
    }

    public SepaDirectDebit setHolder(String holder) {
        this.holder = holder;
        return this;
    }

    @Override
    public String getTypeUrl() {
        return "types/sepa-direct-debit";
    }

    @Override
    public PaymentType map(PaymentType sdd, JsonObject jsonSdd) {
        ((SepaDirectDebit) sdd).setId(jsonSdd.getId());
        ((SepaDirectDebit) sdd).setBic(((JsonSepaDirectDebit) jsonSdd).getBic());
        ((SepaDirectDebit) sdd).setIban(((JsonSepaDirectDebit) jsonSdd).getIban());
        ((SepaDirectDebit) sdd).setHolder(((JsonSepaDirectDebit) jsonSdd).getHolder());
        ((SepaDirectDebit) sdd).setRecurring(((JsonSepaDirectDebit) jsonSdd).getRecurring());
        GeoLocation tempGeoLocation = new GeoLocation(((JsonSepaDirectDebit) jsonSdd).getGeoLocation().getClientIp(), ((JsonSepaDirectDebit) jsonSdd).getGeoLocation().getCountryIsoA2());
        ((SepaDirectDebit) sdd).setGeoLocation(tempGeoLocation);
        return sdd;
    }

    public Charge charge(BigDecimal amount, Currency currency, URL returnUrl) throws HttpCommunicationException {
        return getUnzer().charge(amount, currency, this, returnUrl);
    }

    public Charge charge(BigDecimal amount, Currency currency, URL returnUrl, Customer customer) throws HttpCommunicationException {
        return getUnzer().charge(amount, currency, this, returnUrl, customer);
    }

    public Charge charge(BigDecimal amount, Currency currency, URL returnUrl, Customer customer, Basket basket) throws HttpCommunicationException {
        return getUnzer().charge(amount, currency, this, returnUrl, customer, basket);

    }

}
