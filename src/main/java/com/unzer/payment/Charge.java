package com.unzer.payment;

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

import com.unzer.payment.communication.HttpCommunicationException;
import com.unzer.payment.communication.json.JsonObject;
import com.unzer.payment.paymenttypes.PaymentType;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Business object for Charge. Amount, currency and typeId are mandatory parameter to 
 * execute an Charge. 
 * 
 * The returnUrl is mandatory in case of redirectPayments like Sofort, Paypal, Giropay, Creditcard 3DS
 * @author Unzer E-Com GmbH
 *
 */
public class Charge extends AbstractTransaction<Payment> {

	private String invoiceId;
	
	private List<Cancel> cancelList;

	public Charge() {
		super();
		setCancelList(new ArrayList<Cancel>());
	}
	public Charge(Unzer unzer) {
		super(unzer);
		setCancelList(new ArrayList<Cancel>());
	}

	public String getInvoiceId() {
		return invoiceId;
	}

	public Charge setInvoiceId(String invoiceId) {
		this.invoiceId = invoiceId;
		return this;
	}

	public Cancel cancel() throws HttpCommunicationException {
		return getUnzer().cancelCharge(getPayment().getId(), getId());
	}

	public Cancel cancel(BigDecimal amount) throws HttpCommunicationException {
		return getUnzer().cancelCharge(getPayment().getId(), getId(), amount);
	}

	public Cancel cancel(Cancel cancel) throws HttpCommunicationException {
		return getUnzer().cancelCharge(getPayment().getId(), getId(), cancel);
	}

	@Override
	public String getTypeUrl() {
		return "payments/<paymentId>/charges";
	}

	@Override
	public PaymentType map(PaymentType paymentType, JsonObject jsonObject) {
		return null;
	}

	public List<Cancel> getCancelList() {
		return cancelList;
	}

	public void setCancelList(List<Cancel> cancelList) {
		this.cancelList = cancelList;
	}

	public Cancel getCancel(String cancelId) {
		if (cancelList == null) return null;
		for (Cancel cancel : cancelList) {
			if (cancelId.equalsIgnoreCase(cancel.getId())) {
				return cancel;
			}
		}
		return null;
	}
}
