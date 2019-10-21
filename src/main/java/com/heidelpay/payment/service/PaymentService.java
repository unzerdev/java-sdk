package com.heidelpay.payment.service;

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
import java.net.URL;
import java.util.ArrayList;
import java.util.Currency;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.heidelpay.payment.Authorization;
import com.heidelpay.payment.Basket;
import com.heidelpay.payment.Cancel;
import com.heidelpay.payment.Charge;
import com.heidelpay.payment.Customer;
import com.heidelpay.payment.Heidelpay;
import com.heidelpay.payment.Metadata;
import com.heidelpay.payment.Payment;
import com.heidelpay.payment.PaymentException;
import com.heidelpay.payment.Payout;
import com.heidelpay.payment.Recurring;
import com.heidelpay.payment.Shipment;
import com.heidelpay.payment.business.paymenttypes.HirePurchaseRatePlan;
import com.heidelpay.payment.communication.HeidelpayRestCommunication;
import com.heidelpay.payment.communication.HttpCommunicationException;
import com.heidelpay.payment.communication.JsonParser;
import com.heidelpay.payment.communication.impl.RestCommunication;
import com.heidelpay.payment.communication.json.JsonApplepayResponse;
import com.heidelpay.payment.communication.json.JsonAuthorization;
import com.heidelpay.payment.communication.json.JsonCancel;
import com.heidelpay.payment.communication.json.JsonCard;
import com.heidelpay.payment.communication.json.JsonCharge;
import com.heidelpay.payment.communication.json.JsonCustomer;
import com.heidelpay.payment.communication.json.JsonHirePurchaseRatePlan;
import com.heidelpay.payment.communication.json.JsonHirePurchaseRatePlanList;
import com.heidelpay.payment.communication.json.JsonIdObject;
import com.heidelpay.payment.communication.json.JsonIdeal;
import com.heidelpay.payment.communication.json.JsonPayment;
import com.heidelpay.payment.communication.json.JsonPayout;
import com.heidelpay.payment.communication.json.JsonPis;
import com.heidelpay.payment.communication.json.JsonRecurring;
import com.heidelpay.payment.communication.json.JsonSepaDirectDebit;
import com.heidelpay.payment.communication.json.JsonShipment;
import com.heidelpay.payment.communication.json.JsonTransaction;
import com.heidelpay.payment.communication.mapper.JsonToBusinessClassMapper;
import com.heidelpay.payment.paymenttypes.AbstractPaymentType;
import com.heidelpay.payment.paymenttypes.Alipay;
import com.heidelpay.payment.paymenttypes.Applepay;
import com.heidelpay.payment.paymenttypes.Card;
import com.heidelpay.payment.paymenttypes.Eps;
import com.heidelpay.payment.paymenttypes.Giropay;
import com.heidelpay.payment.paymenttypes.Ideal;
import com.heidelpay.payment.paymenttypes.Invoice;
import com.heidelpay.payment.paymenttypes.InvoiceFactoring;
import com.heidelpay.payment.paymenttypes.InvoiceGuaranteed;
import com.heidelpay.payment.paymenttypes.PaymentType;
import com.heidelpay.payment.paymenttypes.Paypal;
import com.heidelpay.payment.paymenttypes.Pis;
import com.heidelpay.payment.paymenttypes.Prepayment;
import com.heidelpay.payment.paymenttypes.Przelewy24;
import com.heidelpay.payment.paymenttypes.SepaDirectDebit;
import com.heidelpay.payment.paymenttypes.SepaDirectDebitGuaranteed;
import com.heidelpay.payment.paymenttypes.Sofort;
import com.heidelpay.payment.paymenttypes.Wechatpay;

public class PaymentService {
	private static final String TRANSACTION_TYPE_AUTHORIZATION = "authorize";
	private static final String TRANSACTION_TYPE_CHARGE = "charge";
	private static final String TRANSACTION_TYPE_PAYOUT = "payout";
	private static final String TRANSACTION_TYPE_CANCEL_AUTHORIZE = "cancel-authorize";
	private static final String TRANSACTION_TYPE_CANCEL_CHARGE = "cancel-charge";

	private HeidelpayRestCommunication restCommunication;

	private UrlUtil urlUtil;
	private JsonToBusinessClassMapper jsonToBusinessClassMapper = new JsonToBusinessClassMapper();
	private Heidelpay heidelpay;

	/**
	 * Creates a PaymentService with the @deprecated {@code RestCommunication}
	 * implementation.
	 * 
	 * @param heidelpay
	 */
	public PaymentService(Heidelpay heidelpay) {
		this(heidelpay, new RestCommunication());
	}

	/**
	 * Creates the {@code PaymentService} with the given {@code Heidelpay} facade,
	 * bound to the given {@code HeidelpayRestCommunication} implementation used for
	 * http-communication.
	 * 
	 * @param heidelpay - the {@code Heidelpay} Facade
	 * @param restCommunication - the implementation of {@code HeidelpayRestCommunication} to be used for network communication.
	 */
	public PaymentService(Heidelpay heidelpay, HeidelpayRestCommunication restCommunication) {
		super();
		this.heidelpay = heidelpay;
		this.urlUtil = new UrlUtil(heidelpay.getEndPoint());
		this.restCommunication = restCommunication;
	}

	public List<HirePurchaseRatePlan> hirePurchasePlan(BigDecimal amount, Currency currency, BigDecimal effectiveInterestRate, Date orderDate) throws PaymentException, HttpCommunicationException {
		String response = restCommunication.httpGet(urlUtil.getHirePurchaseRateUrl(amount, currency, effectiveInterestRate, orderDate), heidelpay.getPrivateKey());
		JsonHirePurchaseRatePlanList json = new JsonParser<Customer>().fromJson(response, JsonHirePurchaseRatePlanList.class);
		return json.getEntity();
	}

	public <T extends PaymentType> T createPaymentType(T paymentType) throws HttpCommunicationException {
		String response = restCommunication.httpPost(urlUtil.getRestUrl(paymentType), heidelpay.getPrivateKey(),
				paymentType);
		JsonIdObject jsonResponse = new JsonParser<JsonIdObject>().fromJson(response, JsonIdObject.class);
		return fetchPaymentType(jsonResponse.getId());
	}

	public <T extends PaymentType> T updatePaymentType(T paymentType) throws PaymentException, HttpCommunicationException {
		String url = urlUtil.getRestUrl(paymentType);
		url = addId(url, paymentType.getId());
		String response = restCommunication.httpPut(url, heidelpay.getPrivateKey(),
				paymentType);
		JsonIdObject jsonResponse = new JsonParser<JsonIdObject>().fromJson(response, JsonIdObject.class);
		return fetchPaymentType(jsonResponse.getId());
	}

	private String addId(String url, String id) {
		if (!url.endsWith("/")) url += "/";
		return url + id;
	}

	public Customer createCustomer(Customer customer) throws HttpCommunicationException {
		String response = restCommunication.httpPost(urlUtil.getRestUrl(customer), heidelpay.getPrivateKey(), jsonToBusinessClassMapper.map(customer));
		JsonIdObject jsonId = new JsonParser<Customer>().fromJson(response, JsonIdObject.class);
		return fetchCustomer(jsonId.getId());
	}

	public Customer fetchCustomer(String customerId) throws HttpCommunicationException {
		Customer customer = new Customer("", "");
		customer.setId(customerId);
		String response = restCommunication.httpGet(urlUtil.getHttpGetUrl(customer, customer.getId()),
				heidelpay.getPrivateKey());
		JsonCustomer json = new JsonParser<Customer>().fromJson(response, JsonCustomer.class);
		return jsonToBusinessClassMapper.mapToBusinessObject(new Customer("", ""), json);
	}
	
	public Customer updateCustomer(String id, Customer customer) throws HttpCommunicationException {
		restCommunication.httpPut(urlUtil.getHttpGetUrl(customer, id), heidelpay.getPrivateKey(), jsonToBusinessClassMapper.map(customer));
		return fetchCustomer(id);
	}

	public Basket updateBasket(String id, Basket basket) throws PaymentException, HttpCommunicationException {
		restCommunication.httpPut(urlUtil.getHttpGetUrl(basket, id), heidelpay.getPrivateKey(), basket);
		return fetchBasket(id);
	}


	public Metadata createMetadata(Metadata metadata) throws PaymentException, HttpCommunicationException {
		String response = restCommunication.httpPost(urlUtil.getRestUrl(metadata), heidelpay.getPrivateKey(), metadata.getMetadataMap());
		Metadata metadataJson = new JsonParser<Metadata>().fromJson(response, Metadata.class);
		metadata.setHeidelpay(heidelpay);
		metadata.setId(metadataJson.getId());
		return metadata;
	}
	
	public Metadata fetchMetadata(String id) throws PaymentException, HttpCommunicationException {
		Metadata metadata = new Metadata();
		metadata.setId(id);
		String response = restCommunication.httpGet(urlUtil.getHttpGetUrl(metadata, metadata.getId()),
				heidelpay.getPrivateKey());
		Map<String, String> metadataMap = new JsonParser<Map>().fromJson(response, Map.class);
		metadata.setMetadataMap(metadataMap);
		return metadata;
	}

	public Basket createBasket(Basket basket) throws PaymentException, HttpCommunicationException {
		String response = restCommunication.httpPost(urlUtil.getRestUrl(basket), heidelpay.getPrivateKey(), basket);
		Basket jsonBasket = new JsonParser<Basket>().fromJson(response, Basket.class);
		basket.setId(jsonBasket.getId());
		return basket;
	}

	public Basket fetchBasket(String id) throws PaymentException, HttpCommunicationException {
		Basket basket = new Basket();
		basket.setId(id);
		String response = restCommunication.httpGet(urlUtil.getHttpGetUrl(basket, basket.getId()), heidelpay.getPrivateKey());
		basket = new JsonParser<Basket>().fromJson(response, Basket.class);
		basket.setId(id);
		return basket;
	}


	public Authorization authorize(Authorization authorization) throws HttpCommunicationException {
		String response = restCommunication.httpPost(urlUtil.getRestUrl(authorization), heidelpay.getPrivateKey(),
				jsonToBusinessClassMapper.map(authorization));
		JsonAuthorization jsonAuthorization = new JsonParser<JsonAuthorization>().fromJson(response,
				JsonAuthorization.class);
		authorization = (Authorization) jsonToBusinessClassMapper.mapToBusinessObject(authorization, jsonAuthorization);
		authorization.setPayment(fetchPayment(jsonAuthorization.getResources().getPaymentId()));
		authorization.setHeidelpay(heidelpay);
		return authorization;
	}

	public Charge charge(Charge charge) throws HttpCommunicationException {
		return charge(charge, urlUtil.getRestUrl(charge));
	}

	public Payout payout(Payout payout) throws HttpCommunicationException {
		return payout(payout, urlUtil.getRestUrl(payout));
	}

	public Charge chargeAuthorization(String paymentId) throws HttpCommunicationException {
		Charge charge = new Charge();
		return chargeAuthorization(paymentId, charge);
	}

	public Charge chargeAuthorization(String paymentId, BigDecimal amount) throws HttpCommunicationException {
		Charge charge = new Charge();
		charge.setAmount(amount);
		return chargeAuthorization(paymentId, charge);
	}

	public Charge chargeAuthorization(String paymentId, BigDecimal amount, String paymentReference) throws HttpCommunicationException {
		Charge charge = new Charge();
		charge.setAmount(amount);
		charge.setPaymentReference(paymentReference);
		return chargeAuthorization(paymentId, charge);
	}

	private Charge chargeAuthorization(String paymentId, Charge charge) throws HttpCommunicationException {
		return charge(charge, urlUtil.getPaymentUrl(charge, paymentId));
	}

	public Cancel cancelAuthorization(String paymentId) throws HttpCommunicationException {
		Cancel cancel = new Cancel();
		return cancelAuthorization(paymentId, cancel);
	}

	public Cancel cancelAuthorization(String paymentId, BigDecimal amount) throws HttpCommunicationException {
		Cancel cancel = new Cancel();
		cancel.setAmount(amount);
		return cancelAuthorization(paymentId, cancel);
	}

	public Cancel cancelAuthorization(String paymentId, Cancel cancel)
			throws HttpCommunicationException {
		return cancel(cancel, urlUtil.getPaymentUrl(cancel, paymentId));
	}

	public Cancel cancelCharge(String paymentId, String chargeId) throws HttpCommunicationException {
		Cancel cancel = new Cancel();
		return cancelCharge(paymentId, chargeId, cancel);
	}

	public Cancel cancelCharge(String paymentId, String chargeId, BigDecimal amount) throws HttpCommunicationException {
		Cancel cancel = new Cancel();
		cancel.setAmount(amount);
		return cancelCharge(paymentId, chargeId, cancel);
	}

	public Cancel cancelCharge(String paymentId, String chargeId, Cancel cancel)
			throws HttpCommunicationException {
		return cancel(cancel, urlUtil.getRefundUrl(paymentId, chargeId));
	}

	public Shipment shipment(String paymentId, String invoiceId) throws HttpCommunicationException {
		return shipment(new Shipment(invoiceId), urlUtil.getPaymentUrl(new Shipment(), paymentId));
	}

	private Shipment shipment(Shipment shipment, String url) throws HttpCommunicationException {
		String response = restCommunication.httpPost(url, heidelpay.getPrivateKey(), shipment);
		JsonShipment jsonShipment = new JsonParser<JsonShipment>().fromJson(response, JsonShipment.class);
		shipment = jsonToBusinessClassMapper.mapToBusinessObject(shipment, jsonShipment);
		shipment.setPayment(fetchPayment(jsonShipment.getResources().getPaymentId()));
		shipment.setHeidelpay(heidelpay);
		return shipment;
	}

	private Cancel cancel(Cancel cancel, String url) throws HttpCommunicationException {
		String response = restCommunication.httpPost(url, heidelpay.getPrivateKey(),
				jsonToBusinessClassMapper.map(cancel));
		JsonCancel jsonCancel = new JsonParser<JsonCancel>().fromJson(response, JsonCancel.class);
		cancel = jsonToBusinessClassMapper.mapToBusinessObject(cancel, jsonCancel);
		cancel.setPayment(fetchPayment(jsonCancel.getResources().getPaymentId()));
		cancel.setHeidelpay(heidelpay);
		return cancel;
	}

	private Charge charge(Charge charge, String url) throws HttpCommunicationException {
		String response = restCommunication.httpPost(url, heidelpay.getPrivateKey(),
				jsonToBusinessClassMapper.map(charge));
		JsonCharge jsonCharge = new JsonParser<JsonCharge>().fromJson(response, JsonCharge.class);
		charge = (Charge) jsonToBusinessClassMapper.mapToBusinessObject(charge, jsonCharge);
		charge.setPayment(fetchPayment(jsonCharge.getResources().getPaymentId()));
		charge.setPaymentId(jsonCharge.getResources().getPaymentId());
		charge.setHeidelpay(heidelpay);
		return charge;
	}

	private Payout payout(Payout payout, String url) throws HttpCommunicationException {
		com.heidelpay.payment.communication.json.JsonObject json = jsonToBusinessClassMapper.map(payout);
		String response = restCommunication.httpPost(url, heidelpay.getPrivateKey(), json);
		JsonPayout jsonPayout = new JsonParser<JsonPayout>().fromJson(response, JsonPayout.class);
		payout = (Payout) jsonToBusinessClassMapper.mapToBusinessObject(payout, jsonPayout);
		payout.setPayment(fetchPayment(jsonPayout.getResources().getPaymentId()));
		payout.setPaymentId(jsonPayout.getResources().getPaymentId());
		payout.setHeidelpay(heidelpay);
		return payout;
	}

	public Recurring recurring(Recurring recurring) throws PaymentException, HttpCommunicationException {
		String url = urlUtil.getRecurringUrl(recurring);
		String response = restCommunication.httpPost(url, heidelpay.getPrivateKey(), jsonToBusinessClassMapper.map(recurring));
		JsonRecurring json = new JsonParser<JsonRecurring>().fromJson(response, JsonRecurring.class);
		recurring = (Recurring) jsonToBusinessClassMapper.mapToBusinessObject(recurring, json);
		recurring.setHeidelpay(heidelpay);
		return recurring;
		
	}


	public Payment fetchPayment(String paymentId) throws HttpCommunicationException {
		Payment payment = new Payment(heidelpay);
		payment.setId(paymentId);
		String response = restCommunication.httpGet(urlUtil.getHttpGetUrl(payment, payment.getId()),
				heidelpay.getPrivateKey());
		JsonPayment jsonPayment = new JsonParser<JsonPayment>().fromJson(response, JsonPayment.class);
		payment = jsonToBusinessClassMapper.mapToBusinessObject(payment, jsonPayment);
		payment.setCancelList(fetchCancelList(payment, getCancelsFromTransactions(jsonPayment.getTransactions())));
		payment.setAuthorization(
				fetchAuthorization(payment, getAuthorizationFromTransactions(jsonPayment.getTransactions())));
		payment.setChargesList(fetchChargeList(payment, getChargesFromTransactions(jsonPayment.getTransactions())));
		payment.setPayoutList(fetchPayoutList(payment, getPayoutFromTransactions(jsonPayment.getTransactions())));
		return payment;
	}

	public void deleteCustomer(String customerId) throws HttpCommunicationException {
		String response = restCommunication.httpDelete(urlUtil.getHttpGetUrl(new Customer("a", "b"), customerId),
				heidelpay.getPrivateKey());
		if (!"true".equalsIgnoreCase(response)) {
			throw new PaymentException("Customer '" + customerId + "' cannot be deleted");
		}
	}

	@SuppressWarnings("unchecked")
	public <T extends PaymentType> T fetchPaymentType(String typeId) throws HttpCommunicationException {
		AbstractPaymentType paymentType = getPaymentTypeFromTypeId(typeId);
		paymentType.setHeidelpay(heidelpay);
		String response = restCommunication.httpGet(urlUtil.getHttpGetUrl(paymentType, typeId), heidelpay.getPrivateKey());
		// workaround for Bug AHC-265
		JsonIdObject jsonPaymentType = new JsonParser<JsonIdObject>().fromJson(response, getJsonObjectFromTypeId(typeId).getClass());
		return (T) jsonToBusinessClassMapper.mapToBusinessObject(paymentType, jsonPaymentType);
	}

	public Authorization fetchAuthorization(String paymentId) throws HttpCommunicationException {
		return fetchPayment(paymentId).getAuthorization();
	}

	public Charge fetchCharge(String paymentId, String chargeId) throws HttpCommunicationException {
		return fetchPayment(paymentId).getCharge(chargeId);
	}

	public Payout fetchPayout(String paymentId, String payoutId) throws HttpCommunicationException {
		return fetchPayment(paymentId).getPayout(payoutId);
	}


	public Cancel fetchCancel(String paymentId, String cancelId) throws HttpCommunicationException {
		return fetchPayment(paymentId).getCancel(cancelId);
	}

	public Cancel fetchCancel(String paymentId, String chargeId, String cancelId) throws HttpCommunicationException {
		return fetchPayment(paymentId).getCharge(chargeId).getCancel(cancelId);
	}
	

	private Authorization fetchAuthorization(Payment payment, Authorization authorization, URL url)
			throws HttpCommunicationException {
		String response = restCommunication.httpGet(url.toString(), heidelpay.getPrivateKey());
		JsonAuthorization jsonAuthorization = new JsonParser<JsonAuthorization>().fromJson(response,
				JsonAuthorization.class);
		authorization = (Authorization) jsonToBusinessClassMapper.mapToBusinessObject(authorization, jsonAuthorization);
		authorization.setCancelList(getCancelListForAuthorization(payment.getCancelList()));
		authorization.setHeidelpay(heidelpay);
		return authorization;
	}

	private List<Charge> fetchChargeList(Payment payment, List<JsonTransaction> jsonChargesTransactionList)
			throws HttpCommunicationException {
		if (jsonChargesTransactionList == null || jsonChargesTransactionList.size() == 0)
			return null;
		List<Charge> chargesList = new ArrayList<Charge>();
		for (JsonTransaction jsonTransaction : jsonChargesTransactionList) {
			Charge charge = fetchCharge(payment, new Charge(heidelpay), jsonTransaction.getUrl());
			charge.setCancelList(getCancelListForCharge(charge.getId(), payment.getCancelList()));
			charge.setType(jsonTransaction.getType());
			charge.setBasketId(payment.getBasketId());
			charge.setCustomerId(payment.getCustomerId());
			charge.setMetadataId(payment.getMetadataId());
			chargesList.add(charge);
		}
		return chargesList;
	}

	private List<Payout> fetchPayoutList(Payment payment, List<JsonTransaction> jsonTransactionList)
			throws HttpCommunicationException {
		if (jsonTransactionList == null || jsonTransactionList.size() == 0)
			return null;
		List<Payout> payoutList = new ArrayList<Payout>();
		for (JsonTransaction jsonTransaction : jsonTransactionList) {
			Payout payout = fetchPayout(payment, new Payout(heidelpay), jsonTransaction.getUrl());
			payout.setType(jsonTransaction.getType());
			payout.setBasketId(payment.getBasketId());
			payoutList.add(payout);
		}
		return payoutList;
	}

	private List<Cancel> getCancelListForAuthorization(List<Cancel> cancelList) {
		if (cancelList == null)
			return null;
		List<Cancel> authorizationCancelList = new ArrayList<Cancel>();
		for (Cancel cancel : cancelList) {
			if (TRANSACTION_TYPE_CANCEL_AUTHORIZE.equalsIgnoreCase(cancel.getType())) {
				authorizationCancelList.add(cancel);
			}
		}
		return authorizationCancelList;

	}

	private List<Cancel> getCancelListForCharge(String chargeId, List<Cancel> cancelList) {
		if (cancelList == null)
			return null;
		List<Cancel> chargeCancelList = new ArrayList<Cancel>();
		for (Cancel cancel : cancelList) {
			if (TRANSACTION_TYPE_CANCEL_CHARGE.equalsIgnoreCase(cancel.getType())
					&& containsChargeId(cancel.getResourceUrl(), chargeId)) {
				chargeCancelList.add(cancel);
			}
		}
		return chargeCancelList;
	}

	private boolean containsChargeId(URL resourceUrl, String chargeId) {
		return resourceUrl.toString().contains(chargeId);
	}

	private Charge fetchCharge(Payment payment, Charge charge, URL url) throws HttpCommunicationException {
		String response = restCommunication.httpGet(url.toString(), heidelpay.getPrivateKey());
		JsonCharge jsonCharge = new JsonParser<JsonCharge>().fromJson(response, JsonCharge.class);
		charge = (Charge) jsonToBusinessClassMapper.mapToBusinessObject(charge, jsonCharge);
		charge.setInvoiceId(jsonCharge.getInvoiceId());
		charge.setPayment(payment);
		charge.setResourceUrl(url);
		return charge;
	}

	private Payout fetchPayout(Payment payment, Payout payout, URL url) throws HttpCommunicationException {
		String response = restCommunication.httpGet(url.toString(), heidelpay.getPrivateKey());
		JsonPayout jsonPayout = new JsonParser<JsonPayout>().fromJson(response, JsonPayout.class);
		payout = (Payout) jsonToBusinessClassMapper.mapToBusinessObject(payout, jsonPayout);
		payout.setPayment(payment);
		payout.setResourceUrl(url);
		return payout;
	}

	private List<Cancel> fetchCancelList(Payment payment, List<JsonTransaction> jsonChargesTransactionList)
			throws HttpCommunicationException {
		if (jsonChargesTransactionList == null || jsonChargesTransactionList.size() == 0)
			return null;
		List<Cancel> cancelList = new ArrayList<Cancel>();
		for (JsonTransaction jsonTransaction : jsonChargesTransactionList) {
			Cancel cancel = fetchCancel(payment, new Cancel(heidelpay), jsonTransaction.getUrl());
			cancel.setType(jsonTransaction.getType());
			cancelList.add(cancel);
		}
		return cancelList;
	}

	private Cancel fetchCancel(Payment payment, Cancel cancel, URL url) throws HttpCommunicationException {
		String response = restCommunication.httpGet(url.toString(), heidelpay.getPrivateKey());
		JsonCancel jsonCancel = new JsonParser<JsonCancel>().fromJson(response, JsonCancel.class);
		cancel = jsonToBusinessClassMapper.mapToBusinessObject(cancel, jsonCancel);
		cancel.setPayment(payment);
		cancel.setResourceUrl(url);
		return cancel;
	}

	private JsonTransaction getAuthorizationFromTransactions(List<JsonTransaction> transactions) {
		for (JsonTransaction jsonTransaction : transactions) {
			if (TRANSACTION_TYPE_AUTHORIZATION.equalsIgnoreCase(jsonTransaction.getType())) {
				return jsonTransaction;
			}
		}
		return null;
	}

	private List<JsonTransaction> getChargesFromTransactions(List<JsonTransaction> transactions) {
		List<JsonTransaction> chargesList = new ArrayList<JsonTransaction>();
		for (JsonTransaction jsonTransaction : transactions) {
			if (TRANSACTION_TYPE_CHARGE.equalsIgnoreCase(jsonTransaction.getType())) {
				chargesList.add(jsonTransaction);
			}
		}
		return chargesList;
	}

	private List<JsonTransaction> getPayoutFromTransactions(List<JsonTransaction> transactions) {
		List<JsonTransaction> payoutList = new ArrayList<JsonTransaction>();
		for (JsonTransaction jsonTransaction : transactions) {
			if (TRANSACTION_TYPE_PAYOUT.equalsIgnoreCase(jsonTransaction.getType())) {
				payoutList.add(jsonTransaction);
			}
		}
		return payoutList;
	}

	private List<JsonTransaction> getCancelsFromTransactions(List<JsonTransaction> transactions) {
		List<JsonTransaction> cancelsList = new ArrayList<JsonTransaction>();
		for (JsonTransaction jsonTransaction : transactions) {
			if (TRANSACTION_TYPE_CANCEL_AUTHORIZE.equalsIgnoreCase(jsonTransaction.getType())
					|| TRANSACTION_TYPE_CANCEL_CHARGE.equalsIgnoreCase(jsonTransaction.getType())) {
				cancelsList.add(jsonTransaction);
			}
		}
		return cancelsList;
	}

	private Authorization fetchAuthorization(Payment payment, JsonTransaction jsonTransaction)
			throws HttpCommunicationException {
		if (jsonTransaction == null)
			return null;
		Authorization authorization = fetchAuthorization(payment, new Authorization(), jsonTransaction.getUrl());
		authorization.setPayment(payment);
		authorization.setResourceUrl(jsonTransaction.getUrl());
		authorization.setType(jsonTransaction.getType());
		authorization.setCancelList(getCancelListForAuthorization(payment.getCancelList()));
		authorization.setBasketId(payment.getBasketId());
		return authorization;
	}

	private JsonIdObject getJsonObjectFromTypeId(String typeId) {
		String paymentType = typeId.substring(2, 5);
		if ("crd".equalsIgnoreCase(paymentType)) {
			return new JsonCard();
		} else if ("eps".equalsIgnoreCase(paymentType)) {
			return new JsonIdObject();
		} else if ("gro".equalsIgnoreCase(paymentType)) {
			return new JsonIdObject();
		} else if ("idl".equalsIgnoreCase(paymentType)) {
			return new JsonIdeal();
		} else if ("ivc".equalsIgnoreCase(paymentType)) {
			return new JsonIdObject();
		} else if ("ivg".equalsIgnoreCase(paymentType)) {
			return new JsonIdObject();
		} else if ("ivf".equalsIgnoreCase(paymentType)) {
			return new JsonIdObject();
		} else if ("ppl".equalsIgnoreCase(paymentType)) {
			return new JsonIdObject();
		} else if ("ppy".equalsIgnoreCase(paymentType)) {
			return new JsonIdObject();
		} else if ("p24".equalsIgnoreCase(paymentType)) {
			return new JsonIdObject();
		} else if ("sdd".equalsIgnoreCase(paymentType)) {
			return new JsonSepaDirectDebit();
		} else if ("ddg".equalsIgnoreCase(paymentType)) {
			return new JsonSepaDirectDebit();
		} else if ("sft".equalsIgnoreCase(paymentType)) {
			return new JsonIdObject();
		} else if ("pis".equalsIgnoreCase(paymentType)) {
			return new JsonPis();
		} else if ("ali".equalsIgnoreCase(paymentType)) {
			return new JsonIdObject();
		} else if ("wcp".equalsIgnoreCase(paymentType)) {
			return new JsonIdObject();
		} else if ("apl".equalsIgnoreCase(paymentType)) {
			return new JsonApplepayResponse();
		} else if ("hdd".equalsIgnoreCase(paymentType)) {
			return new JsonHirePurchaseRatePlan();
		} else {
			throw new PaymentException("Type '" + typeId + "' is currently now supported by the SDK");
		}

	}

	private AbstractPaymentType getPaymentTypeFromTypeId(String typeId) {
		if (typeId.length() < 5) {
			throw new PaymentException("TypeId '" + typeId + "' is invalid");
		}
		String paymentType = getTypeIdentifier(typeId);
		if ("crd".equalsIgnoreCase(paymentType)) {
			return new Card("", "");
		} else if ("eps".equalsIgnoreCase(paymentType)) {
			return new Eps();
		} else if ("gro".equalsIgnoreCase(paymentType)) {
			return new Giropay();
		} else if ("idl".equalsIgnoreCase(paymentType)) {
			return new Ideal();
		} else if ("ivc".equalsIgnoreCase(paymentType)) {
			return new Invoice();
		} else if ("ivg".equalsIgnoreCase(paymentType)) {
			return new InvoiceGuaranteed();
		} else if ("ivf".equalsIgnoreCase(paymentType)) {
			return new InvoiceFactoring();
		} else if ("ppl".equalsIgnoreCase(paymentType)) {
			return new Paypal();
		} else if ("ppy".equalsIgnoreCase(paymentType)) {
			return new Prepayment();
		} else if ("p24".equalsIgnoreCase(paymentType)) {
			return new Przelewy24();
		} else if ("sdd".equalsIgnoreCase(paymentType)) {
			return new SepaDirectDebit("");
		} else if ("ddg".equalsIgnoreCase(paymentType)) {
			return new SepaDirectDebitGuaranteed("");
		} else if ("sft".equalsIgnoreCase(paymentType)) {
			return new Sofort();
		} else if ("pis".equalsIgnoreCase(paymentType)) {
			return new Pis();
		} else if ("ali".equalsIgnoreCase(paymentType)) {
			return new Alipay();
		} else if ("wcp".equalsIgnoreCase(paymentType)) {
			return new Wechatpay();
		} else if ("apl".equalsIgnoreCase(paymentType)) {
			return new Applepay();
		} else if ("hdd".equalsIgnoreCase(paymentType)) {
			return new HirePurchaseRatePlan();
		} else {
			throw new PaymentException("Type '" + typeId + "' is currently now supported by the SDK");
		}
	}

	private String getTypeIdentifier(String typeId) {
		return typeId.substring(2, 5);
	}



}
