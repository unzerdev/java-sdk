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
import com.heidelpay.payment.communication.json.JsonIdObject;
import com.heidelpay.payment.communication.json.JsonObject;

/**
 * @deprecated use {@code InvoiceSecured} as a default implementation.
 * Invoice guaranteed is an Invoice payment with guarantee for the Merchant
 * @author rene.felder
 *
 */
public class InvoiceFactoring extends AbstractPaymentType implements PaymentType {

	@Deprecated
	@Override
	public String getTypeUrl() {
		return "types/invoice-factoring";
	}

	@Deprecated
	@Override
	public PaymentType map(PaymentType invoice, JsonObject jsonId) {
		((InvoiceFactoring) invoice).setId(jsonId.getId());
		((InvoiceFactoring) invoice).setRecurring(((JsonIdObject) jsonId).getRecurring());
		return invoice;
	}

	@Deprecated
	public Charge charge(BigDecimal amount, Currency currency, URL returnUrl, Customer customer, Basket basket) throws HttpCommunicationException {
		return getHeidelpay().charge(amount, currency, this, returnUrl, customer, basket);
	}

	@Deprecated
	public Charge charge(BigDecimal amount, Currency currency, URL returnUrl, Customer customer, Basket basket, String invoiceId) throws HttpCommunicationException {
		return getHeidelpay().charge(getCharge(amount, currency, this, returnUrl, customer, basket, invoiceId));
	}

	@Deprecated
	private Charge getCharge(BigDecimal amount, Currency currency, InvoiceFactoring invoiceFactoring, URL returnUrl,
			Customer customer, Basket basket, String invoiceId) throws HttpCommunicationException {
		return ((Charge) new Charge()
            .setAmount(amount)
            .setCurrency(currency)
            .setTypeId(getHeidelpay().createPaymentType(invoiceFactoring).getId())
            .setReturnUrl(returnUrl)
            .setCustomerId(getHeidelpay().createCustomerIfPresent(customer).getId())
            .setBasketId(getHeidelpay().createBasket(basket).getId()))
            .setInvoiceId(invoiceId);
	}
}
