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
import com.heidelpay.payment.communication.json.JsonIdeal;
import com.heidelpay.payment.communication.json.JsonObject;

/**
 * Ideal business object
 * @author rene.felder
 *
 */
public class Ideal extends AbstractPaymentType implements PaymentType {

	private String bic;
	
	@Override
	public String getTypeUrl() {
		return "types/ideal";
	}

	@Override
	public PaymentType map(PaymentType ideal, JsonObject jsonIdeal) {
		((Ideal) ideal).setId(jsonIdeal.getId());
		((Ideal) ideal).setBic(((JsonIdeal) jsonIdeal).getBankName());
		((Ideal) ideal).setRecurring(((JsonIdeal) jsonIdeal).getRecurring());
		return ideal;
	}

	public String getBic() {
		return bic;
	}

	public Ideal setBic(String bic) {
		this.bic = bic;
		return this;
	}

	public Charge charge(BigDecimal amount, Currency currency, URL returnUrl) throws HttpCommunicationException {
		return getHeidelpay().charge(amount, currency, this, returnUrl);
	}
	public Charge charge(BigDecimal amount, Currency currency, URL returnUrl, Customer customer) throws HttpCommunicationException {
		return getHeidelpay().charge(amount, currency, this, returnUrl, customer);
	}

}
