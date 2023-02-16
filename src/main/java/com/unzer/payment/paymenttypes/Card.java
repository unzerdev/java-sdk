/*
 * Copyright 2020-today Unzer E-Com GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.unzer.payment.paymenttypes;

import com.google.gson.annotations.SerializedName;
import com.unzer.payment.Authorization;
import com.unzer.payment.Charge;
import com.unzer.payment.Customer;
import com.unzer.payment.GeoLocation;
import com.unzer.payment.communication.HttpCommunicationException;
import com.unzer.payment.communication.json.JsonCard;
import com.unzer.payment.communication.json.JsonCardDetails;
import com.unzer.payment.communication.json.JsonObject;

import java.math.BigDecimal;
import java.net.URL;
import java.util.Currency;

/**
 * Credit / Debit Card business object.
 * <p>
 * When fetching a Card the number and cvc will be masked.
 *
 * @author Unzer E-Com GmbH
 */
public class Card extends AbstractPaymentType implements PaymentType {
    private String number;
    private String cvc;
    private String expiryDate;
    private String brand;
    @SerializedName("3ds")
    private Boolean threeDs;
    private String method;
    private String cardHolder;
    private String email;
    private CardDetails cardDetails;

    public Card(String number, String expiryDate) {
        super();
        this.number = number;
        this.expiryDate = expiryDate;
        this.email = null;
    }

    public Card(String number, String expiryDate, String cvc) {
        super();
        this.number = number;
        this.expiryDate = expiryDate;
        this.cvc = cvc;
        this.email = null;
    }

    public Card(String number, String expiryDate, String cvc, String email) {
        super();
        this.number = number;
        this.expiryDate = expiryDate;
        this.cvc = cvc;
        this.email = email;
    }

    public String getNumber() {
        return number;
    }

    public Card setNumber(String number) {
        this.number = number;
        return this;
    }

    public String getCvc() {
        return cvc;
    }

    public Card setCvc(String cvc) {
        this.cvc = cvc;
        return this;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public Card setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
        return this;
    }

    @Override
    public String getTypeUrl() {
        return "types/card";
    }

    @Override
    public PaymentType map(PaymentType card, JsonObject jsonCard) {
        ((Card) card).setCvc(((JsonCard) jsonCard).getCvc());
        ((Card) card).setExpiryDate(((JsonCard) jsonCard).getExpiryDate());
        ((Card) card).setNumber(((JsonCard) jsonCard).getNumber());
        ((Card) card).setId(jsonCard.getId());
        ((Card) card).set3ds(((JsonCard) jsonCard).get3ds());
        ((Card) card).setRecurring(((JsonCard) jsonCard).getRecurring());
        ((Card) card).setBrand(((JsonCard) jsonCard).getBrand());
        ((Card) card).setMethod(((JsonCard) jsonCard).getMethod());
        ((Card) card).setCardHolder(((JsonCard) jsonCard).getCardHolder());
        ((Card) card).setEmail(((JsonCard) jsonCard).getEmail());
        CardDetails tempCardDetails = mapCardDetails(((JsonCard) jsonCard).getCardDetails());
        ((Card) card).setCardDetails(tempCardDetails);
        GeoLocation tempGeoLocation = new GeoLocation(((JsonCard) jsonCard).getGeoLocation().getClientIp(), ((JsonCard) jsonCard).getGeoLocation().getCountryIsoA2());
        ((Card) card).setGeoLocation(tempGeoLocation);
        return card;
    }

    private CardDetails mapCardDetails(JsonCardDetails jsonCardDetails) {
        CardDetails tempCardDetails = new CardDetails();
        tempCardDetails.setAccount(jsonCardDetails.getAccount());
        tempCardDetails.setCardType(jsonCardDetails.getCardType());
        tempCardDetails.setCountryIsoA2(jsonCardDetails.getCountryIsoA2());
        tempCardDetails.setCountryName(jsonCardDetails.getCountryName());
        tempCardDetails.setIssuerName(jsonCardDetails.getIssuerName());
        tempCardDetails.setIssuerPhoneNumber(jsonCardDetails.getIssuerPhoneNumber());
        tempCardDetails.setIssuerUrl(jsonCardDetails.getIssuerUrl());
        return tempCardDetails;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
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

    public Boolean get3ds() {
        return threeDs;
    }

    public Card set3ds(Boolean threeDs) {
        this.threeDs = threeDs;
        return this;
    }


    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getCardHolder() {
        return cardHolder;
    }

    public void setCardHolder(String cardHolder) {
        this.cardHolder = cardHolder;
    }

    public CardDetails getCardDetails() {
        return cardDetails;
    }

    public void setCardDetails(CardDetails cardDetails) {
        this.cardDetails = cardDetails;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
