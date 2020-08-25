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

import com.heidelpay.payment.Charge;
import com.heidelpay.payment.Customer;
import com.heidelpay.payment.communication.HttpCommunicationException;
import com.heidelpay.payment.communication.json.JsonObject;
import com.heidelpay.payment.communication.json.JsonPis;

/**
 * Paypal business object
 * @author rene.felder
 *
 */
public class Pis extends AbstractPaymentType implements PaymentType {

	private String iban;
	private String bic;
	private String holder;
	
	@Override
	public String getTypeUrl() {
		return "types/pis";
	}

	@Override
	public PaymentType map(PaymentType pis, JsonObject jsonPis) {
		((Pis) pis).setId(jsonPis.getId());
		((Pis) pis).setRecurring(((JsonPis) jsonPis).getRecurring());
		((Pis) pis).setBic(((JsonPis) jsonPis).getBic());
		((Pis) pis).setIban(((JsonPis) jsonPis).getIban());
		((Pis) pis).setHolder(((JsonPis) jsonPis).getHolder());
		return pis;
	}

	public Pis() {
		super();
	}

	public Pis(String iban) {
		super();
		this.iban = iban;
	}

	public Pis(String iban, String bic) {
		super();
		this.iban = iban;
		this.bic = bic;
	}

	public Charge charge(BigDecimal amount, Currency currency, URL returnUrl) throws HttpCommunicationException {
		return getHeidelpay().charge(amount, currency, this, returnUrl);
	}
	public Charge charge(BigDecimal amount, Currency currency, URL returnUrl, Customer customer) throws HttpCommunicationException {
		return getHeidelpay().charge(amount, currency, this, returnUrl, customer);
	}

	public String getIban() {
		return iban;
	}

	public void setIban(String iban) {
		this.iban = iban;
	}

	public String getBic() {
		return bic;
	}

	public void setBic(String bic) {
		this.bic = bic;
	}

	public String getHolder() {
		return holder;
	}

	public void setHolder(String holder) {
		this.holder = holder;
	}

}
