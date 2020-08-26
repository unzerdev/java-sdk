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

import com.heidelpay.payment.Basket;
import com.heidelpay.payment.Charge;
import com.heidelpay.payment.Customer;
import com.heidelpay.payment.communication.HttpCommunicationException;
import com.heidelpay.payment.communication.json.JsonObject;
import com.heidelpay.payment.communication.json.JsonSepaDirectDebit;

/**
 * Sepa direct debit business object. Iban is mandatory, bic and holder are optional fields 
 * @author rene.felder
 *
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
		return sdd;
	}

	public Charge charge(BigDecimal amount, Currency currency, URL returnUrl) throws HttpCommunicationException {
		return getHeidelpay().charge(amount, currency, this, returnUrl);
	}
	public Charge charge(BigDecimal amount, Currency currency, URL returnUrl, Customer customer) throws HttpCommunicationException {
		return getHeidelpay().charge(amount, currency, this, returnUrl, customer);
	}
	public Charge charge(BigDecimal amount, Currency currency, URL returnUrl, Customer customer, Basket basket) throws HttpCommunicationException{
		return getHeidelpay().charge(amount,currency, this, returnUrl, customer, basket);

	}

}
