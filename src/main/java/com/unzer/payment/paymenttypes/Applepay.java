package com.unzer.payment.paymenttypes;

import com.unzer.payment.Authorization;
import com.unzer.payment.Charge;
import com.unzer.payment.Customer;
import com.unzer.payment.GeoLocation;
import com.unzer.payment.communication.HttpCommunicationException;
import com.unzer.payment.communication.json.JsonApplepayResponse;
import com.unzer.payment.communication.json.JsonObject;

import java.math.BigDecimal;
import java.net.URL;
import java.util.Currency;

/**
 * ApplePay business object
 *
 * @author Unzer E-Com GmbH
 */
public class Applepay extends AbstractPaymentType implements PaymentType {
    private String version;
    private String data;
    private String signature;
    private ApplepayHeader header;

    private String method;
    private String applicationPrimaryAccountNumber;
    private String applicationExpirationDate;
    private String currencyCode;
    private BigDecimal transactionAmount;

    public Applepay() {
    }

    public Applepay(String version, String data, String signature, ApplepayHeader header) {
        this.version = version;
        this.data = data;
        this.signature = signature;
        this.header = header;
    }

    @Override
    public String getTypeUrl() {
        return "types/applepay";
    }

    @Override
    public PaymentType map(PaymentType applepay, JsonObject jsonApplePay) {
        ((Applepay) applepay).setId(jsonApplePay.getId());
        ((Applepay) applepay).setApplicationExpirationDate(((JsonApplepayResponse) jsonApplePay).getApplicationExpirationDate());
        ((Applepay) applepay).setApplicationPrimaryAccountNumber(((JsonApplepayResponse) jsonApplePay).getApplicationPrimaryAccountNumber());
        ((Applepay) applepay).setCurrencyCode(((JsonApplepayResponse) jsonApplePay).getCurrencyCode());
        ((Applepay) applepay).setTransactionAmount(((JsonApplepayResponse) jsonApplePay).getTransactionAmount());
        ((Applepay) applepay).setRecurring(((JsonApplepayResponse) jsonApplePay).getRecurring());
        GeoLocation tempGeoLocation = new GeoLocation(((JsonApplepayResponse) jsonApplePay).getGeoLocation().getClientIp(), ((JsonApplepayResponse) jsonApplePay).getGeoLocation().getCountryIsoA2());
        ((Applepay) applepay).setGeoLocation(tempGeoLocation);
        return applepay;
    }

    public Authorization authorize(BigDecimal amount, Currency currency, URL returnUrl) throws HttpCommunicationException {
        return authorize(amount, currency, returnUrl, null);
    }

    public Authorization authorize(BigDecimal amount, Currency currency, URL returnUrl, Customer customer) throws HttpCommunicationException {
        return getUnzer().authorize(amount, currency, this, returnUrl, customer);
    }

    public Charge charge(BigDecimal amount, Currency currency, URL returnUrl) throws HttpCommunicationException {
        return getUnzer().charge(amount, currency, this, returnUrl);
    }

    public Charge charge(BigDecimal amount, Currency currency, URL returnUrl, Customer customer) throws HttpCommunicationException {
        return getUnzer().charge(amount, currency, this, returnUrl, customer);
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public ApplepayHeader getHeader() {
        return header;
    }

    public void setHeader(ApplepayHeader header) {
        this.header = header;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public BigDecimal getTransactionAmount() {
        return transactionAmount;
    }

    public void setTransactionAmount(BigDecimal transactionAmount) {
        this.transactionAmount = transactionAmount;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getApplicationPrimaryAccountNumber() {
        return applicationPrimaryAccountNumber;
    }

    public void setApplicationPrimaryAccountNumber(String applicationPrimaryAccountNumber) {
        this.applicationPrimaryAccountNumber = applicationPrimaryAccountNumber;
    }

    public String getApplicationExpirationDate() {
        return applicationExpirationDate;
    }

    public void setApplicationExpirationDate(String applicationExpirationDate) {
        this.applicationExpirationDate = applicationExpirationDate;
    }
}
