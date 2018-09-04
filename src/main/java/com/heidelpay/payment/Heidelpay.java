package com.heidelpay.payment;

import java.math.BigDecimal;
import java.net.URL;
import java.util.Currency;

import com.heidelpay.payment.communication.HttpCommunicationException;
import com.heidelpay.payment.paymenttypes.Card;
import com.heidelpay.payment.paymenttypes.PaymentType;
import com.heidelpay.payment.service.PaymentService;

public class Heidelpay {
	private String privateKey;
	private PaymentService paymentService;

	public Heidelpay(String privateKey) {
		super();
		this.privateKey = privateKey;
		this.paymentService = new PaymentService(this);
	}
	
	public Customer createCustomer(Customer customer) {
		return customer;
	}
	
	public PaymentType createPaymentType(PaymentType paymentType) throws HttpCommunicationException {
		return paymentService.createPaymentType((AbstractPayment)paymentType);
	}

	// Authorization calls
	public Authorization authorize(BigDecimal amount, Currency currency, String typeId) throws HttpCommunicationException {
		return authorize(amount, currency, typeId, (String)null, (URL)null);
	}
	public Authorization authorize(BigDecimal amount, Currency currency, String typeId, URL returnUrl) throws HttpCommunicationException {
		return authorize(amount, currency, typeId, (String)null, returnUrl);
	}
	public Authorization authorize(BigDecimal amount, Currency currency, String typeId, String customerId) throws HttpCommunicationException {
		return authorize(amount, currency, typeId, customerId, (URL)null);
	}
	public Authorization authorize(BigDecimal amount, Currency currency, String typeId, Customer customer, URL returnUrl) throws HttpCommunicationException {
		return authorize(amount, currency, typeId, customer.getCustomerId(), returnUrl);
	}
	public Authorization authorize(BigDecimal amount, Currency currency, PaymentType paymentType) throws HttpCommunicationException {
		if (paymentType.getId() == null) {
			paymentType = createPaymentType(paymentType);
		}
		return authorize(amount, currency, paymentType.getId(), (String)null, (URL)null);
	}
	public Authorization authorize(BigDecimal amount, Currency currency, PaymentType paymentType, Customer customer, URL returnUrl) throws HttpCommunicationException {
		return authorize(amount, currency, paymentType.getId(), customer!=null?customer.getCustomerId():null, returnUrl);
	}
	public Authorization authorize(BigDecimal amount, Currency currency, String typeId, String customerId, URL returnUrl) throws HttpCommunicationException {
		return paymentService.authorize(amount, currency, typeId, customerId, returnUrl);
	}
	public Authorization authorize(Authorization authorization) {
		// TODO Auto-generated method stub
		return null;
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

	public String getPrivateKey() {
		return privateKey;
	}



}
