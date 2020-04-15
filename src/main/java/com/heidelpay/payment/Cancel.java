package com.heidelpay.payment;

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

import com.heidelpay.payment.AbstractInitPayment.Status;

/**
 * Business object for Cancelations
 * @author rene.felder
 *
 */
public class Cancel extends AbstractPayment {
	public enum Status {SUCCESS, PENDING, ERRROR}
	
	private BigDecimal amount;
	private Processing processing = new Processing();
	private Status status;
	private String paymentReference;

	private BigDecimal amountGross;
	private BigDecimal amountNet;
	private BigDecimal amountVat;

	public Cancel() {
		super();
	}
	public Cancel(Heidelpay heidelpay) {
		super(heidelpay);
	}
	

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public Processing getProcessing() {
		return processing;
	}

	public void setProcessing(Processing processing) {
		this.processing = processing;
	}

	@Override
	public String getTypeUrl() {
		return "payments/<paymentId>/authorize/cancels";
	}
	public Status getStatus() {
		return status;
	}
	public void setStatus(Status status) {
		this.status = status;
	}

	public String getPaymentReference() {
		return paymentReference;
	}

	public void setPaymentReference(String paymentReference) {
		this.paymentReference = paymentReference;
	}

	public BigDecimal getAmountGross() {
		return amountGross;
	}

	public void setAmountGross(BigDecimal amountGross) {
		this.amountGross = amountGross;
	}

	public BigDecimal getAmountNet() {
		return amountNet;
	}

	public void setAmountNet(BigDecimal amountNet) {
		this.amountNet = amountNet;
	}

	public BigDecimal getAmountVat() {
		return amountVat;
	}

	public void setAmountVat(BigDecimal amountVat) {
		this.amountVat = amountVat;
	}
}
