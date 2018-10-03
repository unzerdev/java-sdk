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

import java.math.BigDecimal;
import java.net.URL;
import java.util.Currency;

import com.heidelpay.payment.communication.HttpCommunicationException;
import com.heidelpay.payment.paymenttypes.PaymentType;
import com.heidelpay.payment.service.PaymentService;

/**
 * Main class to communicate with Heidelpay systems. All operations can be found here.
 * The class must be initialized with either the private or public key
 */
public class Heidelpay {
	private String privateKey;
	private PaymentService paymentService;

	/**
	 * The Heidelpay class must be initialized with either the private or public key.
	 * The public key can be used for createPaymentType, all other operations need the private key.
	 */
	public Heidelpay(String privateKey) {
		super();
		this.privateKey = privateKey;
		this.paymentService = new PaymentService(this);
	}
	
	/**
	 * Create a customer if it is not null. In case the customer is null it will return null
	 * @param customer
	 * @return Customer with id 
	 * @throws HttpCommunicationException
	 */
	public Customer createCustomerIfPresent(Customer customer) throws HttpCommunicationException {
		if (customer == null) return null;
		return createCustomer(customer);
	}
	
	/**
	 * Create a customer. This is only possible for new customers. If the customer is already on the system (has an id) this method will throw an Exception. 
	 * @param customer
	 * @return Customer with Id
	 * @throws HttpCommunicationException
	 */
	public Customer createCustomer(Customer customer) throws HttpCommunicationException {
		if (customer == null) throw new NullPointerException("Customer must not be null");
		if (customer.getId() != null) throw new PaymentException("Customer has an id set. createCustomer can only be called without Customer.id. Please use updateCustomer or remove the id from Customer.");
		return paymentService.createCustomer(customer);
	}
	
	/**
	 * Update a customers data. All parameters that are null are ignored. Only the non null values are updated.
	 * @param id
	 * @param customer
	 * @return Customer
	 * @throws HttpCommunicationException
	 */
	public Customer updateCustomer(String id, Customer customer) throws HttpCommunicationException {
		return paymentService.updateCustomer(id, customer);
	}

	/**
	 * Delete a customer at Heidelpay
	 * @param customerId
	 * @throws HttpCommunicationException
	 */
	public void deleteCustomer(String customerId) throws HttpCommunicationException {
		paymentService.deleteCustomer(customerId);
	}

	/**
	 * Create a new PaymentType at Heidelpay. This can be any Object which implements the Interface PaymentType
	 * @param paymentType
	 * @return PaymentType Object with an id
	 * @throws HttpCommunicationException
	 */
	public <T extends PaymentType> T createPaymentType(T paymentType) throws HttpCommunicationException {
		if (paymentType != null && paymentType.getId() == null) {
			return  paymentService.createPaymentType(paymentType);
		} else if (paymentType != null && paymentType.getId() != null){
			return paymentType;
		} else {
			return null;
		}
	}

	/**
	 * Authorize call with customerId and typeId. This is used if the type is created using the Javascript SDK or the Mobile SDK
	 * @param amount
	 * @param currency
	 * @param typeId
	 * @param customerId
	 * @return Authorization with paymentId and authorize id 
	 * @throws HttpCommunicationException
	 */
	public Authorization authorize(BigDecimal amount, Currency currency, String typeId, String customerId) throws HttpCommunicationException {
		return authorize(amount, currency, typeId, (URL)null, customerId);
	}
	/**
	 * Authorize call. This is used if the type is created using the Javascript SDK or the Mobile SDK
	 * @param amount
	 * @param currency
	 * @param typeId
	 * @return Authorization with paymentId and authorize id 
	 * @throws HttpCommunicationException
	 */
	public Authorization authorize(BigDecimal amount, Currency currency, String typeId) throws HttpCommunicationException {
		return authorize(amount, currency, typeId, (URL)null, (String)null);
	}
	
	/**
	 * Authorize call for redirect payments with a returnUrl. This is used if the type is created using the Javascript SDK or the Mobile SDK
	 * @param amount
	 * @param currency
	 * @param typeId
	 * @param returnUrl
	 * @return Authorization with paymentId and authorize id 
	 * @throws HttpCommunicationException
	 */
	public Authorization authorize(BigDecimal amount, Currency currency, String typeId, URL returnUrl) throws HttpCommunicationException {
		return authorize(amount, currency, typeId, returnUrl, (String)null);
	}
	/**
	 * Authorize call for redirect payments with returnUrl and a customer. This is used if the type is created using the Javascript SDK or the Mobile SDK. 
	 * The customer can already exist. If the customer does not exist it will be created
	 * @param amount
	 * @param currency
	 * @param typeId
	 * @param returnUrl
	 * @param customer
	 * @return Authorization with paymentId and authorize id 
	 * @throws HttpCommunicationException
	 */
	public Authorization authorize(BigDecimal amount, Currency currency, String typeId, URL returnUrl, Customer customer) throws HttpCommunicationException {
		return authorize(amount, currency, typeId, returnUrl, getCustomerId(createCustomerIfPresent(customer)));
	}

	/**
	 * Authorize call. Creates a new paymentType
	 * @param amount
	 * @param currency
	 * @param paymentType
	 * @return Authorization with paymentId and authorize id 
	 * @throws HttpCommunicationException
	 */
	public Authorization authorize(BigDecimal amount, Currency currency, PaymentType paymentType) throws HttpCommunicationException {
		return authorize(amount, currency, paymentType, (URL)null);
	}
	
	/**
	 * Authorize call for redirect payments with returnUrl. Creates a new paymentType
	 * @param amount
	 * @param currency
	 * @param paymentType
	 * @param returnUrl
	 * @return Authorization with paymentId and authorize id 
	 * @throws HttpCommunicationException
	 */
	public Authorization authorize(BigDecimal amount, Currency currency, PaymentType paymentType, URL returnUrl) throws HttpCommunicationException {
		return authorize(amount, currency, paymentType, returnUrl, (Customer)null);
	}
	
	/**
	 * Authorize call for redirect payments with returnUrl and a customer. Creates a new paymentType
	 * The customer can already exist. If the customer does not exist it will be created
	 * @param amount
	 * @param currency
	 * @param paymentType
	 * @param returnUrl
	 * @param customer
	 * @return Authorization with paymentId and authorize id 
	 * @throws HttpCommunicationException
	 */
	public Authorization authorize(BigDecimal amount, Currency currency, PaymentType paymentType, URL returnUrl, Customer customer) throws HttpCommunicationException {
		return authorize(amount, currency, createPaymentType(paymentType).getId(), returnUrl, getCustomerId(createCustomerIfPresent(customer)));
	}
	
	/**
	 * Authorize call for redirect payments with returnUrl and a customerId. 
	 * The customer must exist.
	 * @param amount
	 * @param currency
	 * @param typeId
	 * @param returnUrl
	 * @param customerId
	 * @return Authorization with paymentId and authorize id 
	 * @throws HttpCommunicationException
	 */
	public Authorization authorize(BigDecimal amount, Currency currency, String typeId, URL returnUrl, String customerId) throws HttpCommunicationException {
		return authorize(getAuthorization(amount, currency, typeId, returnUrl, customerId));
	}
	
	/**
	 * Authorize call with an Authorization object. The Authorization object must have at least an amount, a currency, a typeId
	 * @param authorization
	 * @return Authorization with paymentId and authorize id 
	 * @throws HttpCommunicationException
	 */
	public Authorization authorize(Authorization authorization) throws HttpCommunicationException {
		return paymentService.authorize(authorization);
	}
	
	/**
	 * Charge call with a typeId that was created using the Javascript or Mobile SDK
	 * @param amount
	 * @param currency
	 * @param typeId
	 * @return Charge with paymentId and authorize id 
	 * @throws HttpCommunicationException
	 */
	public Charge charge(BigDecimal amount, Currency currency, String typeId) throws HttpCommunicationException {
		return charge(amount, currency, typeId, (URL)null);
	}

	/**
	 * Charge call with a typeId that was created using the Javascript or Mobile SDK and a customerId
	 * @param amount
	 * @param currency
	 * @param typeId
	 * @param customerId
	 * @return Charge with paymentId and authorize id 
	 * @throws HttpCommunicationException
	 */
	public Charge charge(BigDecimal amount, Currency currency, String typeId, String customerId) throws HttpCommunicationException {
		return charge(amount, currency, typeId, (URL)null, customerId);
	}
	
	/**
	 * Charge call. The PaymentType will be created within this method
	 * @param amount
	 * @param currency
	 * @param paymentType
	 * @return Charge with paymentId and authorize id 
	 * @throws HttpCommunicationException
	 */
	public Charge charge(BigDecimal amount, Currency currency, PaymentType paymentType) throws HttpCommunicationException {
		return charge(amount, currency, createPaymentType(paymentType).getId(), (URL)null, (String)null);
	}
	
	/**
	 * Charge call with a typeId that was created using the Javascript or Mobile SDK for redirect payments
	 * @param amount
	 * @param currency
	 * @param typeId
	 * @param returnUrl
	 * @return Charge with paymentId and authorize id 
	 * @throws HttpCommunicationException
	 */
	public Charge charge(BigDecimal amount, Currency currency, String typeId, URL returnUrl) throws HttpCommunicationException {
		return charge(amount, currency, typeId, returnUrl, (String)null);
	}
	
	/**
	 * Charge call with a typeId that was created using the Javascript or Mobile SDK for redirect payments
	 * @param amount
	 * @param currency
	 * @param typeId
	 * @param returnUrl
	 * @param customerId
	 * @return Charge with paymentId and authorize id 
	 * @throws HttpCommunicationException
	 */
	public Charge charge(BigDecimal amount, Currency currency, String typeId, URL returnUrl, String customerId) throws HttpCommunicationException {
		return charge(getCharge(amount, currency, typeId, returnUrl, customerId));
	}
	
	/**
	 * Charge call for redirect payments. The PaymentType will be created within this method
	 * @param amount
	 * @param currency
	 * @param paymentType
	 * @param returnUrl
	 * @return Charge with paymentId and authorize id 
	 * @throws HttpCommunicationException
	 */
	public Charge charge(BigDecimal amount, Currency currency, PaymentType paymentType, URL returnUrl) throws HttpCommunicationException {
		return charge(amount, currency, createPaymentType(paymentType).getId(), returnUrl, (String)null);
	}
	
	/** 
	 * Charge call for redirect payments. The PaymentType will be created within this method
	 * @param amount
	 * @param currency
	 * @param paymentType
	 * @param returnUrl
	 * @param customer
	 * @return Charge with paymentId and authorize id 
	 * @throws HttpCommunicationException
	 */
	public Charge charge(BigDecimal amount, Currency currency, PaymentType paymentType, URL returnUrl, Customer customer) throws HttpCommunicationException {
		return charge(amount, currency, createPaymentType(paymentType).getId(), returnUrl, getCustomerId(createCustomerIfPresent(customer)));
	}
	
	/**
	 * Charge call with a Charge object. At least amount, currency and paymentTypeId are mandatory
	 * 
	 * @param charge
	 * @return Charge with paymentId and authorize id 
	 * @throws HttpCommunicationException
	 */
	public Charge charge(Charge charge) throws HttpCommunicationException {
		return paymentService.charge(charge);
	}
	
	/**
	 * Charge the full amount that was authorized
	 * @param paymentId
	 * @return Charge with id
	 * @throws HttpCommunicationException
	 */
	public Charge chargeAuthorization(String paymentId) throws HttpCommunicationException {
		return paymentService.chargeAuthorization(paymentId);
	}
	
	/**
	 * Charge partial amount of Authorization. As there is only one Authorization for a Payment id you only need to provide a paymentId 
	 * @param paymentId
	 * @param amount
	 * @return Charge with id
	 * @throws HttpCommunicationException
	 */
	public Charge chargeAuthorization(String paymentId, BigDecimal amount) throws HttpCommunicationException {
		return paymentService.chargeAuthorization(paymentId, amount);
	}

	/**
	 * Cancel the full Authorization. As there is only one Authorization for a Payment id you only need to provide a paymentId 
	 * @param paymentId
	 * @return Cancel with id
	 * @throws HttpCommunicationException
	 */
	public Cancel cancelAuthorization(String paymentId) throws HttpCommunicationException {
		return paymentService.cancelAuthorization(paymentId);
	}
	
	/**
	 * Cancel partial amount of Authorization. As there is only one Authorization for a Payment id you only need to provide a paymentId 
	 * @param paymentId
	 * @param amount
	 * @return Cancel with id
	 * @throws HttpCommunicationException
	 */
	public Cancel cancelAuthorization(String paymentId, BigDecimal amount) throws HttpCommunicationException {
		return paymentService.cancelAuthorization(paymentId, amount);
	}
	
	/**
	 * Cancel (Refund) full Charge
	 * @param paymentId
	 * @param chargeId
	 * @return Cancel with id
	 * @throws HttpCommunicationException
	 */
	public Cancel cancelCharge(String paymentId, String chargeId) throws HttpCommunicationException {
		return paymentService.cancelCharge(paymentId, chargeId);
	}
	
	/**
	 * Cancel partial Charge
	 * @param paymentId
	 * @param chargeId
	 * @param amount
	 * @return Cancel with id
	 * @throws HttpCommunicationException
	 */
	public Cancel cancelCharge(String paymentId, String chargeId, BigDecimal amount) throws HttpCommunicationException {
		return paymentService.cancelCharge(paymentId, chargeId, amount);
	}
	
	/**
	 * Inform about a shipment of goods. From this time the insurance start. 
	 * @param paymentId
	 * @return Shipment with id
	 * @throws HttpCommunicationException
	 */
	public Shipment shipment(String paymentId) throws HttpCommunicationException {
		return paymentService.shipment(paymentId);
	}

	
	/**
	 * Load the whole Payment Object for the given paymentId
	 * @param paymentId
	 * @return Payment object
	 * @throws HttpCommunicationException
	 */
	public Payment fetchPayment(String paymentId) throws HttpCommunicationException {
		return paymentService.fetchPayment(paymentId);
	}
	
	/**
	 * Load the Authorization for the given paymentId. As there is only one Authorization for a Payment the Authorization id is not needed. 
	 * @param paymentId
	 * @return Authorization object
	 * @throws HttpCommunicationException
	 */
	public Authorization fetchAuthorization(String paymentId) throws HttpCommunicationException {
		return paymentService.fetchAuthorization(paymentId);
	}
	
	/**
	 * Load the Charge for the given paymentId and charge Id. 
	 * @param paymentId
	 * @param chargeId
	 * @return Charge
	 * @throws HttpCommunicationException
	 */
	public Charge fetchCharge(String paymentId, String chargeId) throws HttpCommunicationException {
		return paymentService.fetchCharge(paymentId, chargeId);
	}
	
	/**
	 * Load the Cancel for the given paymentId and cancelId
	 * @param paymentId
	 * @param cancelId
	 * @return Cancel 
	 * @throws HttpCommunicationException
	 */
	public Cancel fetchCancel(String paymentId, String cancelId) throws HttpCommunicationException {
		return paymentService.fetchCancel(paymentId, cancelId);
	}
	
	/**
	 * Load the Cancel for the given paymentId, chargeId and cancelId
	 * @param paymentId
	 * @param chargeId
	 * @param cancelId
	 * @return Cancel
	 * @throws HttpCommunicationException
	 */
	public Cancel fetchCancel(String paymentId, String chargeId, String cancelId) throws HttpCommunicationException {
		return paymentService.fetchCancel(paymentId, chargeId, cancelId);
	}
	
	/**
	 * Load the Customer data for the given customerId
	 * @param customerId
	 * @return
	 * @throws HttpCommunicationException
	 */
	public Customer fetchCustomer(String customerId) throws HttpCommunicationException {
		return paymentService.fetchCustomer(customerId);
	}

	/**
	 * Load the PaymentType 
	 * @param typeId
	 * @return
	 * @throws HttpCommunicationException
	 */
	public PaymentType fetchPaymentType(String typeId) throws HttpCommunicationException {
		return paymentService.fetchPaymentType(typeId);
	}

	public String getPrivateKey() {
		return privateKey;
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




}
