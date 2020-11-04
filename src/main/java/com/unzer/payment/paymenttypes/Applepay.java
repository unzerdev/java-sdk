package com.unzer.payment.paymenttypes;

/*-
 * #%L
 * Unzer Java SDK
 * %%
 * Copyright (C) 2020 Unzer E-Com GmbH
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
 * Alipay business object 
 * 
 * @author Unzer E-Com GmbH
 *
 */
public class Applepay extends AbstractPaymentType implements PaymentType {
	private String version;
	private String data;
	private String signature;
	private ApplepayHeader header;

	private String number;
	private String expiryDate;
	private String currencyCode;
	private BigDecimal transactionAmount;

	@Override
	public String getTypeUrl() {
		return "types/applepay";
	}

	@Override
	public PaymentType map(PaymentType applepay, JsonObject jsonApplePay) {
		((Applepay) applepay).setId(jsonApplePay.getId());
		((Applepay) applepay).setExpiryDate(((JsonApplepayResponse) jsonApplePay).getApplicationExpirationDate());
		((Applepay) applepay).setNumber(((JsonApplepayResponse) jsonApplePay).getApplicationPrimaryAccountNumber());
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

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(String expiryDate) {
		this.expiryDate = expiryDate;
	}

}
