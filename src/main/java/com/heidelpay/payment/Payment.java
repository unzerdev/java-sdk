package com.heidelpay.payment;

import java.math.BigDecimal;
import java.net.URL;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

import com.heidelpay.payment.communication.HttpCommunicationException;
import com.heidelpay.payment.paymenttypes.Card;
import com.heidelpay.payment.paymenttypes.PaymentType;

public class Payment extends AbstractPayment {
	private String typeId;
	private String customerId;
	
	private Customer customer;
	private PaymentType paymentType;
	
	private Authorization authorization;
	private List<Charge> chargesList;
	private List<Cancel> cancelList;

	private List<String> paymentUrlList;
	
	public Payment(Heidelpay heidelpay) {
		super(heidelpay);
	}
	
	public Charge charge() {
		return getAuthorization().charge();
	}
	public Charge charge(BigDecimal amount) {
		return getAuthorization().charge(amount);
	}
	
	// Delegates to Heidelpay charge 
	public Charge charge(BigDecimal amount, Currency currency, String typeId) {
		return getHeidelpay().charge(amount, currency, typeId);
	}
	public Charge charge(BigDecimal amount, Currency currency, String typeId, URL returnUrl) {
		return getHeidelpay().charge(amount, currency, typeId, returnUrl);
	}
	public Charge charge(BigDecimal amount, Currency currency, String typeId, String customerId) {
		return getHeidelpay().charge(amount, currency, typeId, customerId);
	}
	public Charge charge(BigDecimal amount, Currency currency, String typeId, String customerId, URL returnUrl) {
		return getHeidelpay().charge(amount, currency, typeId, customerId, returnUrl);
	}
	public Charge charge(BigDecimal amount, Currency currency, PaymentType paymentType) {
		return getHeidelpay().charge(amount, currency, paymentType);
	}
	public Charge charge(BigDecimal amount, Currency currency, PaymentType paymentType, Customer customer, URL returnUrl) {
		return getHeidelpay().charge(amount, currency, paymentType, customer, returnUrl);
	}
	
	// Delegates to Heidelpay authorize 
	public Authorization authorize(BigDecimal amount, Currency currency, String typeId) throws HttpCommunicationException {
		return getHeidelpay().authorize(amount, currency, typeId);
	}
	public Authorization authorize(BigDecimal amount, Currency currency, String typeId, URL returnUrl) throws HttpCommunicationException {
		return getHeidelpay().authorize(amount, currency, typeId, returnUrl);
	}
	public Authorization authorize(BigDecimal amount, Currency currency, String typeId, String customerId) throws HttpCommunicationException {
		return getHeidelpay().authorize(amount, currency, typeId, customerId);
	}
	public Authorization authorize(BigDecimal amount, Currency currency, String typeId, String customerId, URL returnUrl) throws HttpCommunicationException {
		return getHeidelpay().authorize(amount, currency, typeId, customerId, returnUrl);
	}
	public Authorization authorize(BigDecimal amount, Currency currency, PaymentType paymentType) throws HttpCommunicationException {
		return getHeidelpay().authorize(amount, currency, paymentType);
	}
	public Authorization authorize(BigDecimal amount, Currency currency, PaymentType paymentType, Customer customer, URL returnUrl) throws HttpCommunicationException {
		return getHeidelpay().authorize(amount, currency, paymentType, customer, returnUrl);
	}

	
	public Cancel cancel() {
		return getAuthorization().cancel();
	}
	public Cancel cancel(BigDecimal amount) {
		return getAuthorization().cancel(amount);
	}
	
	public Authorization getAuthorization() {
		return new Authorization();
	}
	public List<Charge> getCharges() {
		return new ArrayList<Charge>();
	}
	public Charge getCharge(String chargeId) {
		return new Charge();
	}
	public Charge getCharge(int index) {
		return new Charge();
	}
	public List<Cancel> getCancelList() {
		return new ArrayList<Cancel>();
	}
	public Cancel getCancel(String cancelId) {
		return new Cancel();
	}
	public Cancel getCancel(String chargeId, String refundId) {
		return new Cancel();
	}
	public PaymentType getPaymentType() {
		return new Card("4444333322221111", "12/20");
	}
	public Customer getCustomer() {
		if (customer == null) {
			customer = getHeidelpay().fetchCustomer(customerId);
		}
		return customer;
	}
	@Override
	public String getTypeUrl() {
		return "";
	}

}
