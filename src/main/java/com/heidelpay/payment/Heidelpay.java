package com.heidelpay.payment;

import java.math.BigDecimal;
import java.net.URL;
import java.util.Currency;

import com.heidelpay.payment.communication.HttpCommunicationException;
import com.heidelpay.payment.paymenttypes.AbstractPaymentType;
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
	
	public Customer createCustomer(Customer customer) throws HttpCommunicationException {
		if (customer != null && customer.getId() == null) {
			customer = paymentService.createCustomer(customer);
		} 
		return customer;
	}
	
	public PaymentType createPaymentType(PaymentType paymentType) throws HttpCommunicationException {
		if (paymentType != null && paymentType.getId() == null) {
			return paymentService.createPaymentType((AbstractPaymentType)paymentType);
		} else if (paymentType != null && paymentType.getId() != null){
			return paymentType;
		} else {
			return null;
		}
	}

	// Authorization calls
// currently returnUrl is mandatory
//	public Authorization authorize(BigDecimal amount, Currency currency, String typeId, String customerId) throws HttpCommunicationException {
//		return authorize(amount, currency, typeId, customerId, (URL)null);
//	}
	public Authorization authorize(BigDecimal amount, Currency currency, String typeId) throws HttpCommunicationException {
		return authorize(amount, currency, typeId, (URL)null, (String)null);
	}
	public Authorization authorize(BigDecimal amount, Currency currency, String typeId, URL returnUrl) throws HttpCommunicationException {
		return authorize(amount, currency, typeId, returnUrl, (String)null);
	}
	public Authorization authorize(BigDecimal amount, Currency currency, String typeId, URL returnUrl, Customer customer) throws HttpCommunicationException {
		return authorize(amount, currency, typeId, returnUrl, getCustomerId(customer));
	}

	public Authorization authorize(BigDecimal amount, Currency currency, PaymentType paymentType) throws HttpCommunicationException {
		return authorize(amount, currency, paymentType.getId(), (URL)null, (String)null);
	}
	public Authorization authorize(BigDecimal amount, Currency currency, PaymentType paymentType, URL returnUrl) throws HttpCommunicationException {
		return authorize(amount, currency, paymentType, returnUrl, (Customer)null);
	}
	public Authorization authorize(BigDecimal amount, Currency currency, PaymentType paymentType, URL returnUrl, Customer customer) throws HttpCommunicationException {
		return authorize(amount, currency, createPaymentType(paymentType).getId(), returnUrl, getCustomerId(createCustomer(customer)));
	}
	public Authorization authorize(BigDecimal amount, Currency currency, String typeId, URL returnUrl, String customerId) throws HttpCommunicationException {
		return authorize(getAuthorization(amount, currency, typeId, returnUrl, customerId));
	}
	public Authorization authorize(Authorization authorization) throws HttpCommunicationException {
		return paymentService.authorize(authorization);
	}
	private String getCustomerId(Customer customer) {
		if (customer == null) return null;
		return customer.getId();
	}

	private Authorization getAuthorization(BigDecimal amount, Currency currency, String paymentTypeId, URL returnUrl, String customerId) {
		Authorization authorization = new Authorization(this);
		authorization
		.setAmount(amount)
		.setCurrency(currency)
		.setReturnUrl(returnUrl)
		.setTypeId(paymentTypeId)
		.setCustomerId(customerId);
		return authorization;
	}


	// Charge without Authorization calls
// returnURL is mandatory for the time being
//	public Charge charge(BigDecimal amount, Currency currency, String typeId) throws HttpCommunicationException {
//		return charge(amount, currency, typeId, (String)null, (URL)null);
//	}
//	public Charge charge(BigDecimal amount, Currency currency, String typeId, String customerId) throws HttpCommunicationException {
//		return charge(amount, currency, typeId, customerId, (URL)null);
//	}
//	public Charge charge(BigDecimal amount, Currency currency, PaymentType paymentType) throws HttpCommunicationException {
//		return charge(amount, currency, createPaymentType(paymentType).getId(), (String)null, (URL)null);
//	}
	public Charge charge(BigDecimal amount, Currency currency, String typeId, URL returnUrl) throws HttpCommunicationException {
		return charge(amount, currency, typeId, returnUrl, (String)null);
	}
	public Charge charge(BigDecimal amount, Currency currency, String typeId, URL returnUrl, String customerId) throws HttpCommunicationException {
		return charge(getCharge(amount, currency, typeId, returnUrl, customerId));
	}
	
	public Charge charge(BigDecimal amount, Currency currency, PaymentType paymentType, URL returnUrl) throws HttpCommunicationException {
		return charge(amount, currency, createPaymentType(paymentType).getId(), returnUrl, (String)null);
	}
	public Charge charge(BigDecimal amount, Currency currency, PaymentType paymentType, URL returnUrl, Customer customer) throws HttpCommunicationException {
		return charge(amount, currency, createPaymentType(paymentType).getId(), returnUrl, getCustomerId(createCustomer(customer)));
	}
	public Charge charge(Charge charge) throws HttpCommunicationException {
		return paymentService.charge(charge);
	}

	private Charge getCharge(BigDecimal amount, Currency currency, String typeId, URL returnUrl, String customerId) {
		Charge charge = new Charge();
		charge.setAmount(amount)
		.setCurrency(currency)
		.setTypeId(typeId)
		.setCustomerId(customerId)
		.setReturnUrl(returnUrl);
		return charge;
	}
	
	// Charge after Authorization 
	public Charge chargeAuthorization(String paymentId) throws HttpCommunicationException {
		return paymentService.chargeAuthorization(paymentId);
	}
	public Charge chargeAuthorization(String paymentId, BigDecimal amount) throws HttpCommunicationException {
		return paymentService.chargeAuthorization(paymentId, amount);
	}

	// Cancel after Authorization 
	public Cancel cancelAuthorization(String paymentId) throws HttpCommunicationException {
		return paymentService.cancelAuthorization(paymentId);
	}
	public Cancel cancelAuthorization(String paymentId, BigDecimal amount) throws HttpCommunicationException {
		return paymentService.cancelAuthorization(paymentId, amount);
	}
	
	public Cancel cancelCharge(String paymentId, String chargeId) throws HttpCommunicationException {
		return paymentService.cancelCharge(paymentId, chargeId);
	}
	public Cancel cancelCharge(String paymentId, String chargeId, BigDecimal amount) throws HttpCommunicationException {
		return paymentService.cancelCharge(paymentId, chargeId, amount);
	}

	
	// Load data from Interface
	public Payment fetchPayment(String paymentId) throws HttpCommunicationException {
		return paymentService.fetchPayment(paymentId);
	}
	public Authorization fetchAuthorization(String paymentId) throws HttpCommunicationException {
		return paymentService.fetchAuthorization(paymentId);
	}
	public Charge fetchCharge(String paymentId, String chargeId) throws HttpCommunicationException {
		return paymentService.fetchCharge(paymentId, chargeId);
	}
	public Cancel fetchCancel(String paymentId, String cancelId) throws HttpCommunicationException {
		return paymentService.fetchCancel(paymentId, cancelId);
	}
	public Cancel fetchCancel(String paymentId, String chargeId, String cancelId) throws HttpCommunicationException {
		return paymentService.fetchCancel(paymentId, chargeId, cancelId);
	}
	
	public Customer fetchCustomer(String customerId) throws HttpCommunicationException {
		return paymentService.fetchCustomer(customerId);
	}

	public PaymentType fetchPaymentType(String typeId) throws HttpCommunicationException {
		return paymentService.fetchPaymentType(typeId);
	}

	public String getPrivateKey() {
		return privateKey;
	}



}
