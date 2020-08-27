package com.heidelpay.payment.service;

import java.math.BigDecimal;
import java.net.URL;
import java.util.ArrayList;
import java.util.Currency;
import java.util.Date;
import java.util.List;
import java.util.Map;

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

import com.heidelpay.payment.Authorization;
import com.heidelpay.payment.Basket;
import com.heidelpay.payment.Cancel;
import com.heidelpay.payment.Charge;
import com.heidelpay.payment.Customer;
import com.heidelpay.payment.Heidelpay;
import com.heidelpay.payment.Metadata;
import com.heidelpay.payment.Payment;
import com.heidelpay.payment.PaymentException;
import com.heidelpay.payment.paymenttypes.PaymentTypeEnum;
import com.heidelpay.payment.Payout;
import com.heidelpay.payment.Recurring;
import com.heidelpay.payment.Shipment;
import com.heidelpay.payment.business.paymenttypes.HirePurchaseRatePlan;
import com.heidelpay.payment.business.paymenttypes.InstallmentSecuredRatePlan;
import com.heidelpay.payment.communication.HeidelpayRestCommunication;
import com.heidelpay.payment.communication.HttpCommunicationException;
import com.heidelpay.payment.communication.JsonParser;
import com.heidelpay.payment.communication.json.*;
import com.heidelpay.payment.communication.mapper.JsonToBusinessClassMapper;
import com.heidelpay.payment.paymenttypes.*;

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
	 * Creates the {@code PaymentService} with the given {@code Heidelpay} facade,
	 * bound to the given {@code HeidelpayRestCommunication} implementation used for
	 * http-communication.
	 *
	 * @param heidelpay         - the {@code Heidelpay} Facade
	 * @param restCommunication - the implementation of {@code HeidelpayRestCommunication} to be used for network communication.
	 */
	public PaymentService(Heidelpay heidelpay, HeidelpayRestCommunication restCommunication) {
		super();
		this.heidelpay = heidelpay;
		this.urlUtil = new UrlUtil(heidelpay.getEndPoint());
		this.restCommunication = restCommunication;
	}


	/**
	 * @deprecated use {@code installmentSecuredPlan} as a default implementation.
	 */
	@Deprecated
	public List<HirePurchaseRatePlan> hirePurchasePlan(BigDecimal amount, Currency currency, BigDecimal effectiveInterestRate, Date orderDate) throws HttpCommunicationException {
		String response = restCommunication.httpGet(urlUtil.getHirePurchaseRateUrl(amount, currency, effectiveInterestRate, orderDate), heidelpay.getPrivateKey());
		JsonHirePurchaseRatePlanList json = new JsonParser<Customer>().fromJson(response, JsonHirePurchaseRatePlanList.class);
		return json.getEntity();
	}

	public List<InstallmentSecuredRatePlan> installmentSecuredPlan(BigDecimal amount, Currency currency, BigDecimal effectiveInterestRate, Date orderDate) throws HttpCommunicationException {
		String response = restCommunication.httpGet(urlUtil.getHirePurchaseRateUrl(amount, currency, effectiveInterestRate, orderDate), heidelpay.getPrivateKey());
		JsonInstallmentSecuredRatePlanList json = new JsonParser<Customer>().fromJson(response, JsonInstallmentSecuredRatePlanList.class);
		return json.getEntity();
	}

	public <T extends PaymentType> T createPaymentType(T paymentType) throws HttpCommunicationException {
		String response = restCommunication.httpPost(urlUtil.getRestUrl(paymentType), heidelpay.getPrivateKey(),
				paymentType);
		JsonIdObject jsonResponse = new JsonParser<JsonIdObject>().fromJson(response, JsonIdObject.class);
		return fetchPaymentType(jsonResponse.getId());
	}

	public <T extends PaymentType> T updatePaymentType(T paymentType) throws HttpCommunicationException {
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

	public Customer fetchCustomer(String customerId) throws HttpCommunicationException, PaymentException {
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

	public Basket updateBasket(String id, Basket basket) throws HttpCommunicationException {
		restCommunication.httpPut(urlUtil.getHttpGetUrl(basket, id), heidelpay.getPrivateKey(), basket);
		return fetchBasket(id);
	}


	public Metadata createMetadata(Metadata metadata) throws HttpCommunicationException {
		String response = restCommunication.httpPost(urlUtil.getRestUrl(metadata), heidelpay.getPrivateKey(), metadata.getMetadataMap());
		Metadata metadataJson = new JsonParser<Metadata>().fromJson(response, Metadata.class);
		metadata.setHeidelpay(heidelpay);
		metadata.setId(metadataJson.getId());
		return metadata;
	}

	public Metadata fetchMetadata(String id) throws HttpCommunicationException {
		Metadata metadata = new Metadata();
		metadata.setId(id);
		String response = restCommunication.httpGet(urlUtil.getHttpGetUrl(metadata, metadata.getId()),
				heidelpay.getPrivateKey());
		Map<String, String> metadataMap = new JsonParser<Map>().fromJson(response, Map.class);
		metadata.setMetadataMap(metadataMap);
		return metadata;
	}

	public Basket createBasket(Basket basket) throws HttpCommunicationException {
		String response = restCommunication.httpPost(urlUtil.getRestUrl(basket), heidelpay.getPrivateKey(), basket);
		Basket jsonBasket = new JsonParser<Basket>().fromJson(response, Basket.class);
		basket.setId(jsonBasket.getId());
		return basket;
	}

	public Basket fetchBasket(String id) throws HttpCommunicationException {
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

	public Shipment shipment(String paymentId, String invoiceId, String orderId) throws HttpCommunicationException {
		return shipment(new Shipment(invoiceId, orderId), urlUtil.getPaymentUrl(new Shipment(), paymentId));
	}

	public Shipment doShipment(Shipment shipment, String paymentId) throws HttpCommunicationException {
		return shipment(shipment, urlUtil.getPaymentUrl(new Shipment(), paymentId));
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
		charge.setInvoiceId(jsonCharge.getInvoiceId());
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

	public Recurring recurring(Recurring recurring) throws HttpCommunicationException {
		String url = urlUtil.getRecurringUrl(recurring);
		String response = restCommunication.httpPost(url, heidelpay.getPrivateKey(), jsonToBusinessClassMapper.map(recurring));
		JsonRecurring json = new JsonParser<JsonRecurring>().fromJson(response, JsonRecurring.class);
		recurring = jsonToBusinessClassMapper.mapToBusinessObject(recurring, json);
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
		if (jsonChargesTransactionList == null || jsonChargesTransactionList.isEmpty())
			return new ArrayList<Charge>();
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
		if (jsonTransactionList == null || jsonTransactionList.isEmpty())
			return new ArrayList<Payout>();
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
			return new ArrayList<Cancel>();
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
			return new ArrayList<Cancel>();
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
		if (jsonChargesTransactionList == null || jsonChargesTransactionList.isEmpty())
			return new ArrayList<Cancel>();
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
		String paymentType = getTypeIdentifier(typeId);
		PaymentTypeEnum paymentTypeEnum = PaymentTypeEnum.getPaymentTypeEnumByShortName(paymentType);
		switch (paymentTypeEnum) {
			case EPS:
			case GIROPAY:
			case INVOICE:
			case INVOICE_GUARANTEED:
			case INVOICE_FACTORING:
			case INVOICE_SECURED:
			case PREPAYMENT:
			case PRZELEWY24:
			case SOFORT:
			case ALIPAY:
			case WECHATPAY:
				return new JsonIdObject();
			case PAYPAL:
				return new JsonPaypal();
			case CARD:
				return new JsonCard();
			case IDEAL:
				return new JsonIdeal();
			case SEPA_DIRECT_DEBIT:
			case SEPA_DIRECT_DEBIT_GUARANTEED:
			case SEPA_DIRECT_DEBIT_SECURED:
				return new JsonSepaDirectDebit();
			case PIS:
				return new JsonPis();
			case APPLEPAY:
				return new JsonApplepayResponse();
			case HIRE_PURCHASE_RATE_PLAN:
				return new JsonHirePurchaseRatePlan();
			case INSTALLMENT_SECURED_RATE_PLAN:
				return new JsonInstallmentSecuredRatePlan();
			case BANCONTACT:
				return new JsonBancontact();
			default:
				throw new PaymentException("Type '" + typeId + "' is currently not supported by the SDK");
		}
	}

	private AbstractPaymentType getPaymentTypeFromTypeId(String typeId) {
		if (typeId.length() < 5) {
			throw new PaymentException("TypeId '" + typeId + "' is invalid");
		}
		String paymentType = getTypeIdentifier(typeId);

		PaymentTypeEnum paymentTypeEnum = PaymentTypeEnum.getPaymentTypeEnumByShortName(paymentType);
		switch (paymentTypeEnum) {
			case CARD:
				return new Card("", "");
			case EPS:
				return new Eps();
			case GIROPAY:
				return new Giropay();
			case IDEAL:
				return new Ideal();
			case INVOICE:
				return new Invoice();
			case INVOICE_GUARANTEED:
				return new InvoiceGuaranteed();
			case INVOICE_FACTORING:
				return new InvoiceFactoring();
			case INVOICE_SECURED:
				return new InvoiceSecured();
			case PAYPAL:
				return new Paypal();
			case PREPAYMENT:
				return new Prepayment();
			case PRZELEWY24:
				return new Przelewy24();
			case SEPA_DIRECT_DEBIT:
				return new SepaDirectDebit("");
			case SEPA_DIRECT_DEBIT_GUARANTEED:
				return new SepaDirectDebitGuaranteed("");
			case SEPA_DIRECT_DEBIT_SECURED:
				return new SepaDirectDebitSecured("");
			case SOFORT:
				return new Sofort();
			case PIS:
				return new Pis();
			case ALIPAY:
				return new Alipay();
			case WECHATPAY:
				return new Wechatpay();
			case APPLEPAY:
				return new Applepay();
			case HIRE_PURCHASE_RATE_PLAN:
				return new HirePurchaseRatePlan();
			case INSTALLMENT_SECURED_RATE_PLAN:
				return new InstallmentSecuredRatePlan();
			case BANCONTACT:
				return new Bancontact("");
			default:
				throw new PaymentException("Type '" + typeId + "' is currently not supported by the SDK");
		}
	}
	private String getTypeIdentifier (String typeId){
		return typeId.substring(2, 5);
	}
}