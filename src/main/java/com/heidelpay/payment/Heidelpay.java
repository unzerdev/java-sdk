package com.heidelpay.payment;

import java.math.BigDecimal;
import java.net.URL;
import java.util.Currency;

import com.heidelpay.payment.business.Authorization;
import com.heidelpay.payment.business.Cancel;
import com.heidelpay.payment.business.Charge;
import com.heidelpay.payment.business.Customer;
import com.heidelpay.payment.business.Payment;
import com.heidelpay.payment.business.paymenttypes.Card;
import com.heidelpay.payment.business.paymenttypes.PaymentType;

public class Heidelpay {

	public Heidelpay(String privateKey) {
		super();
	}
	
	public Customer createCustomer(Customer customer) {
		return customer;
	}
	
	public PaymentType createPaymentType(PaymentType paymentType) {
		return paymentType;
	}

	// Authorization calls
	public Authorization authorize(BigDecimal amount, Currency currency, String typeId) {
		return new Authorization(this);
	}
	public Authorization authorize(BigDecimal amount, Currency currency, String typeId, URL returnUrl) {
		return new Authorization(this);
	}
	public Authorization authorize(BigDecimal amount, Currency currency, String typeId, String customerId) {
		return new Authorization(this);
	}
	public Authorization authorize(BigDecimal amount, Currency currency, String typeId, String customerId, URL returnUrl) {
		return new Authorization(this);
	}
	public Authorization authorize(BigDecimal amount, Currency currency, PaymentType paymentType) {
		return new Authorization(this);
	}
	public Authorization authorize(BigDecimal amount, Currency currency, PaymentType paymentType, Customer customer, URL returnUrl) {
		return new Authorization(this);
	}

	// Charge without Authorization calls
	public Charge charge(BigDecimal amount, Currency currency, String typeId) {
		return new Charge(this);
	}
	public Charge charge(BigDecimal amount, Currency currency, String typeId, URL returnUrl) {
		return new Charge(this);
	}
	public Charge charge(BigDecimal amount, Currency currency, String typeId, String customerId) {
		return new Charge(this);
	}
	public Charge charge(BigDecimal amount, Currency currency, String typeId, String customerId, URL returnUrl) {
		return new Charge(this);
	}
	public Charge charge(BigDecimal amount, Currency currency, PaymentType paymentType) {
		return new Charge(this);
	}
	public Charge charge(BigDecimal amount, Currency currency, PaymentType paymentType, Customer customer, URL returnUrl) {
		return new Charge(this);
	}

	
	// Charge after Authorization 
	public Charge chargeAuthorization(String paymentId) {
		return new Charge();
	}
	public Charge chargeAuthorization(String paymentId, BigDecimal amount) {
		return new Charge();
	}

	// Cancel after Authorization 
	public Cancel cancelAuthorization(String paymentId) {
		return new Cancel();
	}
	public Cancel cancelAuthorization(String paymentId, BigDecimal amount) {
		return new Cancel();
	}
	
	public Cancel cancelCharge(String paymentId, String chargeId) {
		return new Cancel();
	}
	public Cancel cancelCharge(String paymentId, String chargeId, BigDecimal amount) {
		return new Cancel();
	}

	
	// Load data from Interface
	public Payment fetchPayment(String paymentId) {
		return new Payment(this);
	}
	public Authorization fetchAuthorization(String paymentId) {
		return fetchPayment(paymentId).getAuthorization();
	}
	public Charge fetchCharge(String paymentId, String chargeId) {
		return fetchPayment(paymentId).getCharge(chargeId);
	}
	public Cancel fetchCancel(String paymentId, String cancelId) {
		return fetchPayment(paymentId).getCancel(cancelId);
	}
	public Cancel fetchRefund(String paymentId, String refundId) {
		return fetchPayment(paymentId).getCancel(refundId);
	}
	
	public Customer fetchCustomer(String customerId) {
		return new Customer("Rene", "Felder");
	}

	public PaymentType fetchPaymentType(String typeId) {
		return new Card("4444333322221111", "12/18");
	}

}
