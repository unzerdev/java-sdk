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

import java.util.ArrayList;
import java.util.List;

public class PaymentException extends RuntimeException {
	private static final long serialVersionUID = 1490670397634763643L;

	private List<PaymentError> paymentErrorList;
	
	public PaymentException() {
		super();
	}

	public PaymentException(String message, Throwable cause) {
		super(message, cause);
	}

	public PaymentException(String message, Throwable cause, List<PaymentError> paymentErrorList) {
		super(message, cause);
		this.paymentErrorList = paymentErrorList; 
	}

	
	public PaymentException(String message) {
		super(message);
	}

	public PaymentException(String merchantMessage, String customerMessage, String code) {
		super(merchantMessage);
		this.paymentErrorList = new ArrayList<PaymentError>();
		this.paymentErrorList.add(new PaymentError(merchantMessage, customerMessage, code));
	}

	public PaymentException(String message, List<PaymentError> paymentErrorList) {
		super(message);
		this.paymentErrorList = paymentErrorList; 
	}

	public PaymentException(Throwable cause) {
		super(cause);
	}

	public List<PaymentError> getPaymentErrorList() {
		return paymentErrorList;
	}

	public void setPaymentErrorList(List<PaymentError> paymentErrorList) {
		this.paymentErrorList = paymentErrorList;
	}


}
