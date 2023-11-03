package com.unzer.payment.paymenttypes;

import com.unzer.payment.Basket;
import com.unzer.payment.Charge;
import com.unzer.payment.Customer;
import com.unzer.payment.GeoLocation;
import com.unzer.payment.communication.HttpCommunicationException;
import com.unzer.payment.communication.json.ApiObject;
import com.unzer.payment.communication.json.ApiSepaDirectDebit;

import java.math.BigDecimal;
import java.net.URL;
import java.util.Currency;

/**
 * Sepa direct debit business object. Iban is mandatory, bic and holder are optional fields
 *
 * @author Unzer E-Com GmbH
 */
@Deprecated
public class SepaDirectDebit extends BasePaymentType {
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
    public String getResourceUrl() {
        return "/v1/types/sepa-direct-debit/<resourceId>";
    }

    @Override
    public PaymentType map(PaymentType sdd, ApiObject jsonSdd) {
        ((SepaDirectDebit) sdd).setId(jsonSdd.getId());
        ((SepaDirectDebit) sdd).setBic(((ApiSepaDirectDebit) jsonSdd).getBic());
        ((SepaDirectDebit) sdd).setIban(((ApiSepaDirectDebit) jsonSdd).getIban());
        ((SepaDirectDebit) sdd).setHolder(((ApiSepaDirectDebit) jsonSdd).getHolder());
        ((SepaDirectDebit) sdd).setRecurring(((ApiSepaDirectDebit) jsonSdd).getRecurring());
        GeoLocation tempGeoLocation =
                new GeoLocation(((ApiSepaDirectDebit) jsonSdd).getGeoLocation().getClientIp(),
                        ((ApiSepaDirectDebit) jsonSdd).getGeoLocation().getCountryIsoA2());
        ((SepaDirectDebit) sdd).setGeoLocation(tempGeoLocation);
        return sdd;
    }

    @Deprecated
    public Charge charge(BigDecimal amount, Currency currency, URL returnUrl)
            throws HttpCommunicationException {
        return getUnzer().charge(amount, currency, this, returnUrl);
    }

    @Deprecated
    public Charge charge(BigDecimal amount, Currency currency, URL returnUrl, Customer customer)
            throws HttpCommunicationException {
        return getUnzer().charge(amount, currency, this, returnUrl, customer);
    }

    @Deprecated
    public Charge charge(BigDecimal amount, Currency currency, URL returnUrl, Customer customer,
                         Basket basket) throws HttpCommunicationException {
        return getUnzer().charge(amount, currency, this, returnUrl, customer, basket);

    }

}
