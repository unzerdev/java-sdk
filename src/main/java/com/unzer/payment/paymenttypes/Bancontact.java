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

import com.unzer.payment.Charge;
import com.unzer.payment.Customer;
import com.unzer.payment.Unzer;
import com.unzer.payment.communication.HttpCommunicationException;
import com.unzer.payment.communication.json.JsonBancontact;
import com.unzer.payment.communication.json.JsonObject;

import java.math.BigDecimal;
import java.net.URL;
import java.util.Currency;

public class Bancontact extends AbstractPaymentType implements PaymentType {

	private String holder;
	
	public Bancontact(String holder) {
		this.holder = holder;
	}

	public Bancontact() {
		super();
	}

	public Bancontact(Unzer unzer) {
		super(unzer);
	}

	@Override
	public String getTypeUrl() {
		return "types/bancontact";
	}

	@Override
	public PaymentType map(PaymentType bancontact, JsonObject jsonId) {
		if(bancontact instanceof Bancontact && jsonId instanceof JsonBancontact) {
			((Bancontact) bancontact).setId(jsonId.getId());
			((Bancontact) bancontact).setRecurring(((JsonBancontact) jsonId).getRecurring());
			((Bancontact) bancontact).setHolder(((JsonBancontact) jsonId).getHolder());
		}
		return bancontact;
	}

	public String getHolder() {
		return holder;
	}

	public void setHolder(String holder) {
		this.holder = holder;
	}

	public Charge charge(BigDecimal amount, Currency currency, URL returnUrl) throws HttpCommunicationException {
		return getUnzer().charge(amount, currency, this, returnUrl);
	}
	public Charge charge(BigDecimal amount, Currency currency, URL returnUrl, Customer customer) throws HttpCommunicationException {
		return getUnzer().charge(amount, currency, this, returnUrl, customer);
	}

}
