package com.heidelpay.payment;

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
