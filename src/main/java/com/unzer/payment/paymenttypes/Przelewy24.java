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
import com.unzer.payment.GeoLocation;
import com.unzer.payment.communication.HttpCommunicationException;
import com.unzer.payment.communication.json.JsonIdObject;
import com.unzer.payment.communication.json.JsonObject;

import java.math.BigDecimal;
import java.net.URL;
import java.util.Currency;

/**
 * P24 business object 
 * 
 * @author Unzer E-Com GmbH
 *
 */
public class Przelewy24 extends AbstractPaymentType implements PaymentType {

	@Override
	public String getTypeUrl() {
		return "types/przelewy24";
	}

	@Override
	public PaymentType map(PaymentType przelewy24, JsonObject jsonId) {
		((Przelewy24) przelewy24).setId(jsonId.getId());
		((Przelewy24) przelewy24).setRecurring(((JsonIdObject) jsonId).getRecurring());
		GeoLocation tempGeoLocation = new GeoLocation(((JsonIdObject) jsonId).getGeoLocation().getClientIp(), ((JsonIdObject) jsonId).getGeoLocation().getCountryIsoA2());
		((Przelewy24) przelewy24).setGeoLocation(tempGeoLocation);
		return przelewy24;
	}

	public Charge charge(BigDecimal amount, Currency currency, URL returnUrl) throws HttpCommunicationException {
		return getUnzer().charge(amount, currency, this, returnUrl);
	}
	public Charge charge(BigDecimal amount, Currency currency, URL returnUrl, Customer customer) throws HttpCommunicationException {
		return getUnzer().charge(amount, currency, this, returnUrl, customer);
	}

}