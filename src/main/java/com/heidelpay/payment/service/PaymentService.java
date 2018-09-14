package com.heidelpay.payment.service;

import java.math.BigDecimal;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.heidelpay.payment.Authorization;
import com.heidelpay.payment.Cancel;
import com.heidelpay.payment.Charge;
import com.heidelpay.payment.Customer;
import com.heidelpay.payment.Heidelpay;
import com.heidelpay.payment.Payment;
import com.heidelpay.payment.PaymentException;
import com.heidelpay.payment.Shipment;
import com.heidelpay.payment.communication.HttpCommunicationException;
import com.heidelpay.payment.communication.JsonParser;
import com.heidelpay.payment.communication.RestCommunication;
import com.heidelpay.payment.communication.json.JsonAuthorization;
import com.heidelpay.payment.communication.json.JsonCancel;
import com.heidelpay.payment.communication.json.JsonCard;
import com.heidelpay.payment.communication.json.JsonCardFetch;
import com.heidelpay.payment.communication.json.JsonCharge;
import com.heidelpay.payment.communication.json.JsonIdObject;
import com.heidelpay.payment.communication.json.JsonIdeal;
import com.heidelpay.payment.communication.json.JsonPayment;
import com.heidelpay.payment.communication.json.JsonSepaDirectDebit;
import com.heidelpay.payment.communication.json.JsonTransaction;
import com.heidelpay.payment.communication.mapper.JsonToBusinessClassMapper;
import com.heidelpay.payment.paymenttypes.AbstractPaymentType;
import com.heidelpay.payment.paymenttypes.Card;
import com.heidelpay.payment.paymenttypes.Eps;
import com.heidelpay.payment.paymenttypes.Giropay;
import com.heidelpay.payment.paymenttypes.Ideal;
import com.heidelpay.payment.paymenttypes.Invoice;
import com.heidelpay.payment.paymenttypes.InvoiceGuaranteed;
import com.heidelpay.payment.paymenttypes.PaymentType;
import com.heidelpay.payment.paymenttypes.Paypal;
import com.heidelpay.payment.paymenttypes.Prepayment;
import com.heidelpay.payment.paymenttypes.Przelewy24;
import com.heidelpay.payment.paymenttypes.SepaDirectDebit;
import com.heidelpay.payment.paymenttypes.SepaDirectDebitGuaranteed;
import com.heidelpay.payment.paymenttypes.Sofort;

public class PaymentService {
	private static final String TRANSACTION_TYPE_AUTHORIZATION = "authorize";
	private static final String TRANSACTION_TYPE_CHARGE = "charge";
	private static final String TRANSACTION_TYPE_CANCEL_AUTHORIZE = "cancel-authorize";
	private static final String TRANSACTION_TYPE_CANCEL_CHARGE = "cancel-charge";

	public final static Logger logger = Logger.getLogger(PaymentService.class);

	private RestCommunication restCommunication = new RestCommunication(); 
	private UrlUtil urlUtil = new UrlUtil();
	private JsonToBusinessClassMapper jsonToBusinessClassMapper = new JsonToBusinessClassMapper(); 
	private Heidelpay heidelpay;

	public PaymentService(Heidelpay heidelpay) {
		super();
		this.heidelpay = heidelpay;
	}

	public <T extends PaymentType> T createPaymentType(T paymentType) throws HttpCommunicationException {
		String response = restCommunication.httpPost(urlUtil.getRestUrl(paymentType), heidelpay.getPrivateKey(), paymentType);
		JsonIdObject jsonResponse = new JsonParser<JsonIdObject>().fromJson(response, JsonIdObject.class);
		return fetchPaymentType(jsonResponse.getId());
	}

	public Customer createCustomer(Customer customer) throws HttpCommunicationException {
		String response = restCommunication.httpPost(urlUtil.getRestUrl(customer), heidelpay.getPrivateKey(), customer);
		Customer customerJson = new JsonParser<Customer>().fromJson(response, Customer.class);
		customer.setHeidelpay(heidelpay);
		customer.setId(customerJson.getId());
		return customer;
		
	}

	public Customer updateCustomer(String id, Customer customer) throws HttpCommunicationException {
		restCommunication.httpPut(urlUtil.getHttpGetUrl(customer, id), heidelpay.getPrivateKey(), customer);
		return fetchCustomer(id);
	}


	public Authorization authorize(Authorization authorization) throws HttpCommunicationException {
		if (authorization.getCustomerId() != null) {
			Customer customer = fetchCustomer(authorization.getCustomerId());
			authorization.setCustomerId(customer.getId());
		}
		String response = restCommunication.httpPost(urlUtil.getRestUrl(authorization), heidelpay.getPrivateKey(), jsonToBusinessClassMapper.map(authorization));
		JsonAuthorization jsonAuthorization = new JsonParser<JsonAuthorization>().fromJson(response, JsonAuthorization.class);
		authorization = jsonToBusinessClassMapper.mapToBusinessObject(authorization, jsonAuthorization);
		authorization.setPayment(fetchPayment(jsonAuthorization.getResources().getPaymentId()));
		authorization.setHeidelpay(heidelpay);
		return authorization;
	}


	public Charge charge(Charge charge) throws HttpCommunicationException {
		return charge(charge, urlUtil.getRestUrl(charge));
	}

	public Charge chargeAuthorization(String paymentId) throws HttpCommunicationException {
		Charge charge = new Charge();
		return charge(charge, urlUtil.getPaymentUrl(charge, paymentId));
	}
	
	public Charge chargeAuthorization(String paymentId, BigDecimal amount) throws HttpCommunicationException {
		Charge charge = new Charge();
		charge.setAmount(amount);
		return charge(charge, urlUtil.getPaymentUrl(charge, paymentId));
	}

	public Cancel cancelAuthorization(String paymentId) throws HttpCommunicationException {
		Cancel cancel = new Cancel();
		return cancel(cancel, urlUtil.getPaymentUrl(cancel, paymentId));
	}

	public Cancel cancelAuthorization(String paymentId, BigDecimal amount) throws HttpCommunicationException {
		Cancel cancel = new Cancel();
		cancel.setAmount(amount);
		return cancel(cancel, urlUtil.getPaymentUrl(cancel, paymentId));
	}

	public Cancel cancelCharge(String paymentId, String chargeId) throws HttpCommunicationException {
		Cancel cancel = new Cancel();
		return cancel(cancel, urlUtil.getRefundUrl(paymentId, chargeId));
	}

	public Cancel cancelCharge(String paymentId, String chargeId, BigDecimal amount) throws HttpCommunicationException {
		Cancel cancel = new Cancel();
		cancel.setAmount(amount);
		return cancel(cancel, urlUtil.getRefundUrl(paymentId, chargeId));
	}

	public Shipment shipment(String paymentId) throws HttpCommunicationException {
		return shipment(new Shipment(), urlUtil.getPaymentUrl(new Shipment(), paymentId));
	}


	private Shipment shipment(Shipment shipment, String url) throws HttpCommunicationException {
		String response = restCommunication.httpPost(url, heidelpay.getPrivateKey(), shipment);
		return new JsonParser<Shipment>().fromJson(response, Shipment.class);
	}

	private Cancel cancel(Cancel cancel, String url) throws HttpCommunicationException {
		String response = restCommunication.httpPost(url, heidelpay.getPrivateKey(), jsonToBusinessClassMapper.map(cancel));
		JsonCancel jsonCancel = new JsonParser<JsonCancel>().fromJson(response, JsonCancel.class);
		cancel = jsonToBusinessClassMapper.mapToBusinessObject(cancel, jsonCancel);
		cancel.setPayment(fetchPayment(jsonCancel.getResources().getPaymentId()));
		cancel.setHeidelpay(heidelpay);
		return cancel;
	}

	private Charge charge(Charge charge, String url) throws HttpCommunicationException {
		String response = restCommunication.httpPost(url, heidelpay.getPrivateKey(), jsonToBusinessClassMapper.map(charge));
		JsonCharge jsonCharge = new JsonParser<JsonCharge>().fromJson(response, JsonCharge.class);
		charge = jsonToBusinessClassMapper.mapToBusinessObject(charge, jsonCharge);
		charge.setPayment(fetchPayment(jsonCharge.getResources().getPaymentId()));
		charge.setPaymentId(jsonCharge.getResources().getPaymentId());
		charge.setHeidelpay(heidelpay);
		return charge;
	}


	public Payment fetchPayment(String paymentId) throws HttpCommunicationException {
		Payment payment = new Payment(heidelpay);
		payment.setId(paymentId);
		String response = restCommunication.httpGet(urlUtil.getHttpGetUrl(payment, payment.getId()), heidelpay.getPrivateKey());
		JsonPayment jsonPayment = new JsonParser<JsonPayment>().fromJson(response, JsonPayment.class);
		payment = jsonToBusinessClassMapper.mapToBusinessObject(payment, jsonPayment);
		payment.setAuthorization(fetchAuthorization(payment, getAuthorizationFromTransactions(jsonPayment.getTransactions())));
		payment.setCancelList(fetchCancelList(payment, getCancelsFromTransactions(jsonPayment.getTransactions())));
		payment.setChargesList(fetchChargeList(payment, getChargesFromTransactions(jsonPayment.getTransactions())));
		return payment;
	}

	public Customer fetchCustomer(String customerId) throws HttpCommunicationException {
		Customer customer = new Customer("", "");
		customer.setId(customerId);
		String response = restCommunication.httpGet(urlUtil.getHttpGetUrl(customer, customer.getId()), heidelpay.getPrivateKey());
		Customer jsonCustomer = new JsonParser<Customer>().fromJson(response, Customer.class);
		return jsonCustomer;
	}

	public void deleteCustomer(String customerId) throws HttpCommunicationException {
		String response = restCommunication.httpDelete(urlUtil.getHttpGetUrl(new Customer("a", "b"), customerId), heidelpay.getPrivateKey());
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
		JsonIdObject jsonPaymentType = null;
		if (getTypeIdentifier(typeId).equalsIgnoreCase("crd")) {
			jsonPaymentType = new JsonParser<JsonIdObject>().fromJson(response, JsonCardFetch.class);
		} else {
			jsonPaymentType = new JsonParser<JsonIdObject>().fromJson(response, getJsonObjectFromTypeId(typeId).getClass());
		}
		return (T) jsonToBusinessClassMapper.mapToBusinessObject(paymentType, jsonPaymentType);
	}
	
	public Authorization fetchAuthorization(String paymentId) throws HttpCommunicationException {
		return fetchPayment(paymentId).getAuthorization();
	}

	public Charge fetchCharge(String paymentId, String chargeId) throws HttpCommunicationException {
		return fetchPayment(paymentId).getCharge(chargeId);
	}

	public Cancel fetchCancel(String paymentId, String cancelId) throws HttpCommunicationException {
		return fetchPayment(paymentId).getCancel(cancelId);
	}

	public Cancel fetchCancel(String paymentId, String chargeId, String cancelId) throws HttpCommunicationException {
		return fetchPayment(paymentId).getCharge(chargeId).getCancel(cancelId);
	}

	private Authorization fetchAuthorization(Authorization authorization, URL url) throws HttpCommunicationException {
		String response = restCommunication.httpGet(url.toString(), heidelpay.getPrivateKey());
		JsonAuthorization jsonAuthorization = new JsonParser<JsonAuthorization>().fromJson(response, JsonAuthorization.class);
		authorization = jsonToBusinessClassMapper.mapToBusinessObject(authorization, jsonAuthorization);
		authorization.setHeidelpay(heidelpay);
		return authorization;
	}
	private List<Charge> fetchChargeList(Payment payment, List<JsonTransaction> jsonChargesTransactionList) throws HttpCommunicationException {
		if (jsonChargesTransactionList == null || jsonChargesTransactionList.size() == 0) return null; 
		List<Charge> chargesList = new ArrayList<Charge>();
		for (JsonTransaction jsonTransaction : jsonChargesTransactionList) {
			Charge charge = fetchCharge(payment, new Charge(heidelpay), jsonTransaction.getUrl());
			charge.setCancelList(getCancelListForCharge(charge.getId(), payment.getCancelList()));
			charge.setType(jsonTransaction.getType());
			chargesList.add(charge);
		}
		return chargesList;
	}
	private List<Cancel> getCancelListForCharge(String chargeId, List<Cancel> cancelList) {
		if (cancelList == null) return null;
		List<Cancel> chargeCancelList = new ArrayList<Cancel>();
		for (Cancel cancel : cancelList) {
			if(TRANSACTION_TYPE_CANCEL_CHARGE.equalsIgnoreCase(cancel.getType()) && containsChargeId(cancel.getResourceUrl(), chargeId)) {
				chargeCancelList.add(cancel);
			}
		}
		return chargeCancelList;
	}

	private boolean containsChargeId(URL resourceUrl, String chargeId) {
		if (resourceUrl.toString().indexOf(chargeId) == -1) return false;
		else return true;
	}

	private Charge fetchCharge(Payment payment, Charge charge, URL url) throws HttpCommunicationException {
		String response = restCommunication.httpGet(url.toString(), heidelpay.getPrivateKey());
		JsonCharge jsonCharge = new JsonParser<JsonCharge>().fromJson(response, JsonCharge.class);
		charge = jsonToBusinessClassMapper.mapToBusinessObject(charge, jsonCharge);
		charge.setPayment(payment);
		charge.setResourceUrl(url);
		return charge;
	}

	private List<Cancel> fetchCancelList(Payment payment, List<JsonTransaction> jsonChargesTransactionList) throws HttpCommunicationException {
		if (jsonChargesTransactionList == null || jsonChargesTransactionList.size() == 0) return null; 
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

	private List<JsonTransaction> getCancelsFromTransactions(List<JsonTransaction> transactions) {
		List<JsonTransaction> cancelsList = new ArrayList<JsonTransaction>();
		for (JsonTransaction jsonTransaction : transactions) {
			if (TRANSACTION_TYPE_CANCEL_AUTHORIZE.equalsIgnoreCase(jsonTransaction.getType()) || TRANSACTION_TYPE_CANCEL_CHARGE.equalsIgnoreCase(jsonTransaction.getType())) {
				cancelsList.add(jsonTransaction);
			}
		}
		return cancelsList;
	}

	private Authorization fetchAuthorization(Payment payment, JsonTransaction jsonTransaction) throws HttpCommunicationException {
		if (jsonTransaction == null) return null;
		Authorization authorization = fetchAuthorization(new Authorization(), jsonTransaction.getUrl());
		authorization.setPayment(payment);
		authorization.setResourceUrl(jsonTransaction.getUrl());
		authorization.setType(jsonTransaction.getType());
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
		} else {
			throw new PaymentException("Type '" + typeId + "' is currently now supported by the SDK");
		}
		
	}
	private AbstractPaymentType getPaymentTypeFromTypeId(String typeId) {
		if (typeId.length()<5) {
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
		} else {
			throw new PaymentException("Type '" + typeId + "' is currently now supported by the SDK");
		}
	}

	private String getTypeIdentifier(String typeId) {
		String paymentType = typeId.substring(2, 5);
		return paymentType;
	}
}
