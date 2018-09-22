package com.heidelpay.payment;

/*-
 * #%L
 * Heidelpay Java SDK
 * %%
 * Copyright (C) 2018 Heidelpay GmbH
 * %%
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
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
