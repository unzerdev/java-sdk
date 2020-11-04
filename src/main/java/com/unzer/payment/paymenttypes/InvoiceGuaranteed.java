package com.unzer.payment.paymenttypes;

/*-
 * #%L
 * Unzer Java SDK
 * %%
 * Copyright (C) 2018 Unzer GmbH
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

import com.unzer.payment.Basket;
import java.math.BigDecimal;
import java.net.URL;
import java.util.Currency;

import com.unzer.payment.Charge;
import com.unzer.payment.Customer;
import com.unzer.payment.GeoLocation;
import com.unzer.payment.communication.HttpCommunicationException;
import com.unzer.payment.communication.json.JsonIdObject;
import com.unzer.payment.communication.json.JsonObject;

/**
 * @deprecated use {@code InvoiceSecured} as a default implementation.
 * Invoice guaranteed is an Invoice payment with guarantee for the Merchant
 * @author rene.felder
 *
 */
@Deprecated
public class InvoiceGuaranteed extends AbstractPaymentType implements PaymentType {

	@Deprecated
	@Override
	public String getTypeUrl() {
		return "types/invoice-guaranteed";
	}

	@Deprecated
	@Override
	public PaymentType map(PaymentType invoiceGuaranteed, JsonObject jsonId) {
		((InvoiceGuaranteed) invoiceGuaranteed).setId(jsonId.getId());
		((InvoiceGuaranteed) invoiceGuaranteed).setRecurring(((JsonIdObject) jsonId).getRecurring());
		GeoLocation tempGeoLocation = new GeoLocation(((JsonIdObject) jsonId).getGeoLocation().getClientIp(), ((JsonIdObject) jsonId).getGeoLocation().getCountryIsoA2());
		((InvoiceGuaranteed) invoiceGuaranteed).setGeoLocation(tempGeoLocation);
		return invoiceGuaranteed;
	}

	@Deprecated
	public Charge charge(BigDecimal amount, Currency currency, URL returnUrl) throws HttpCommunicationException {
		return charge(amount, currency, returnUrl, null);
	}

	@Deprecated
	public Charge charge(BigDecimal amount, Currency currency, URL returnUrl, Customer customer) throws HttpCommunicationException {
		return getUnzer().charge(amount, currency, this, returnUrl, customer);
	}

	@Deprecated
	public Charge charge(BigDecimal amount, Currency currency, URL returnUrl, Customer customer, Basket basket, String invoiceId) throws HttpCommunicationException {
		return getUnzer().charge(getCharge(amount, currency, this, returnUrl, customer, basket, invoiceId));
	}

	@Deprecated
	private Charge getCharge(BigDecimal amount, Currency currency, InvoiceGuaranteed invoiceGuaranteed, URL returnUrl,
			Customer customer, Basket basket, String invoiceId) throws HttpCommunicationException {
		return ((Charge) new Charge()
				.setAmount(amount)
				.setCurrency(currency)
				.setTypeId(getUnzer().createPaymentType(invoiceGuaranteed).getId())
				.setReturnUrl(returnUrl)
				.setCustomerId(getUnzer().createCustomerIfPresent(customer).getId())
				.setBasketId(getUnzer().createBasket(basket).getId()))
				.setInvoiceId(invoiceId);
	}

}
