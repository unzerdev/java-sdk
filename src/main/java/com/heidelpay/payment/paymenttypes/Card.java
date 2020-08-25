package com.heidelpay.payment.paymenttypes;

/*-
 * #%L
 * Heidelpay Java SDK
 * %%
 * Copyright (C) 2018 Heidelpay GmbH
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

import java.math.BigDecimal;
import java.net.URL;
import java.util.Currency;

import com.google.gson.annotations.SerializedName;
import com.heidelpay.payment.Authorization;
import com.heidelpay.payment.Charge;
import com.heidelpay.payment.Customer;
import com.heidelpay.payment.communication.HttpCommunicationException;
import com.heidelpay.payment.communication.json.JsonCard;
import com.heidelpay.payment.communication.json.JsonCardDetails;
import com.heidelpay.payment.communication.json.JsonObject;

/**
 * Credit / Debit Card business object. 
 * 
 * When fetching a Card the number and cvc will be masked. 
 * @author rene.felder
 *
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
	private CardDetails cardDetails;
	
	public Card(String number, String expiryDate) {
		super();
		this.number = number;
		this.expiryDate = expiryDate;
	}
	public Card(String number, String expiryDate, String cvc) {
		super();
		this.number = number;
		this.expiryDate = expiryDate;
		this.cvc = cvc;
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
		CardDetails tempCardDetails = mapCardDetails(((JsonCard) jsonCard).getCardDetails());
		((Card) card).setCardDetails(tempCardDetails);
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
		return getHeidelpay().authorize(amount, currency, this, returnUrl, customer);
	}
	public Charge charge(BigDecimal amount, Currency currency, URL returnUrl) throws HttpCommunicationException {
		return getHeidelpay().charge(amount, currency, this, returnUrl);
	}
	public Charge charge(BigDecimal amount, Currency currency, URL returnUrl, Customer customer) throws HttpCommunicationException {
		return getHeidelpay().charge(amount, currency, this, returnUrl, customer);
	}
	public Boolean get3ds() {
		return threeDs;
	}
	public void set3ds(Boolean threeDs) {
		this.threeDs = threeDs;
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
}
