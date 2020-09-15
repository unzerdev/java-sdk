package com.heidelpay.payment.service.marketplace;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.heidelpay.payment.Heidelpay;
import com.heidelpay.payment.communication.HeidelpayRestCommunication;
import com.heidelpay.payment.communication.HttpCommunicationException;
import com.heidelpay.payment.communication.json.JsonAuthorization;
import com.heidelpay.payment.communication.json.JsonCancel;
import com.heidelpay.payment.communication.json.JsonCharge;
import com.heidelpay.payment.communication.json.JsonPayment;
import com.heidelpay.payment.communication.json.JsonTransaction;
import com.heidelpay.payment.marketplace.MarketplaceAuthorization;
import com.heidelpay.payment.marketplace.MarketplaceCancel;
import com.heidelpay.payment.marketplace.MarketplaceCharge;
import com.heidelpay.payment.marketplace.MarketplacePayment;
import com.heidelpay.payment.service.PaymentService;

public class MarketplacePaymentService extends PaymentService {

	public MarketplacePaymentService(Heidelpay heidelpay, HeidelpayRestCommunication restCommunication) {
		super(heidelpay, restCommunication);
	}

	/**
	 * Execute a marketplace authorization.
	 * 
	 * @param authorization refers to normal authorization request.
	 * @return MarketplaceAuthorization refers to an authorization response with id, paymentId, etc.
	 * @throws HttpCommunicationException
	 */
	public MarketplaceAuthorization marketplaceAuthorize(MarketplaceAuthorization authorization) throws HttpCommunicationException {
		String response = restCommunication.httpPost(urlUtil.getRestUrl(authorization), heidelpay.getPrivateKey(), jsonToBusinessClassMapper.map(authorization));
		JsonAuthorization jsonAuthorization = jsonParser.fromJson(response, JsonAuthorization.class);
		authorization = (MarketplaceAuthorization) jsonToBusinessClassMapper.mapToBusinessObject(authorization, jsonAuthorization);
		authorization.setPayment(fetchMarketplacePayment(jsonAuthorization.getResources().getPaymentId()));
		authorization.setHeidelpay(heidelpay);
		return authorization;
	}
	
	/**
	 * Execute a marketplace charge.
	 * 
	 * @param charge refers to normal charge request.
	 * @return MarketplaceCharge
	 * @throws HttpCommunicationException
	 */
	public MarketplaceCharge marketplaceCharge(MarketplaceCharge charge) throws HttpCommunicationException {
		String response = restCommunication.httpPost(urlUtil.getRestUrl(charge), heidelpay.getPrivateKey(), jsonToBusinessClassMapper.map(charge));
		JsonCharge jsonCharge = jsonParser.fromJson(response, JsonCharge.class);
		charge = (MarketplaceCharge) jsonToBusinessClassMapper.mapToBusinessObject(charge, jsonCharge);
		charge.setInvoiceId(jsonCharge.getInvoiceId());
		charge.setPayment(fetchMarketplacePayment(jsonCharge.getResources().getPaymentId()));
		charge.setPaymentId(jsonCharge.getResources().getPaymentId());
		charge.setHeidelpay(heidelpay);
		return charge;
	}
	
	/**
	 * Execute a marketplace charge for one authorization.
	 * @param paymentId refers to marketplace payment.
	 * @param authorizeId refers to marketplace authorization to be charged.
	 * @param charge refers to charge request with amount, payment reference, etc.
	 * @return
	 * @throws HttpCommunicationException
	 */
	public MarketplaceCharge marketplaceChargeAuthorization(String paymentId, String authorizeId, MarketplaceCharge charge) throws HttpCommunicationException {
		String url = urlUtil.getRestUrl().concat("/").concat(charge.getChargeAuthorizationUrl(paymentId, authorizeId));
		String response = restCommunication.httpPost(url, heidelpay.getPrivateKey(), jsonToBusinessClassMapper.map(charge));
		JsonCharge jsonCharge = jsonParser.fromJson(response, JsonCharge.class);
		charge = (MarketplaceCharge) jsonToBusinessClassMapper.mapToBusinessObject(charge, jsonCharge);
		charge.setInvoiceId(jsonCharge.getInvoiceId());
		charge.setPayment(fetchMarketplacePayment(jsonCharge.getResources().getPaymentId()));
		charge.setPaymentId(jsonCharge.getResources().getPaymentId());
		charge.setHeidelpay(heidelpay);
		return charge;
	}
	
	/**
	 * Execute a marketplace full charge for single or multiple authorization(s).
	 * 
	 * @param paymentId refers to marketplace payment.
	 * @param paymentReference refers to marketplace payment note.
	 * @return MarketplacePayment
	 * @throws HttpCommunicationException
	 */
	public MarketplacePayment marketplaceFullChargeAuthorizations(String paymentId, String paymentReference) throws HttpCommunicationException {
		MarketplaceCharge charge = new MarketplaceCharge();
		charge.setPaymentReference(paymentReference);
		
		String url = urlUtil.getRestUrl().concat("/").concat(charge.getFullChargeAuthorizationsUrl(paymentId));
		String response = restCommunication.httpPost(url, heidelpay.getPrivateKey(), jsonToBusinessClassMapper.map(charge));
		JsonPayment jsonPayment = jsonParser.fromJson(response, JsonPayment.class);
		MarketplacePayment paymentResponse = new MarketplacePayment(this.heidelpay);
		paymentResponse.setId(paymentId);
		paymentResponse = jsonToBusinessClassMapper.mapToBusinessObject(paymentResponse, jsonPayment);
		paymentResponse.setCancelList(fetchCancelList(paymentResponse, getCancelsFromTransactions(jsonPayment.getTransactions())));
		paymentResponse.setAuthorizationsList(fetchAuthorizationList(paymentResponse, getAuthorizationsFromTransactions(jsonPayment.getTransactions())));
		paymentResponse.setChargesList(fetchChargeList(paymentResponse, getChargesFromTransactions(jsonPayment.getTransactions())));
		return paymentResponse;
	}
	
	/**
	 * Execute a marketplace payment fetch action.
	 * 
	 * @param paymentId refers to payment to be fetched.
	 * @return MarketplacePayment
	 * @throws HttpCommunicationException
	 */
	public MarketplacePayment fetchMarketplacePayment(String paymentId) throws HttpCommunicationException {
		MarketplacePayment payment = new MarketplacePayment(heidelpay);
		payment.setId(paymentId);
		String response = getPayment(payment);		
		JsonPayment jsonPayment = jsonParser.fromJson(response, JsonPayment.class);
		payment = jsonToBusinessClassMapper.mapToBusinessObject(payment, jsonPayment);
		payment.setCancelList(fetchCancelList(payment, getCancelsFromTransactions(jsonPayment.getTransactions())));
		payment.setAuthorizationsList(fetchAuthorizationList(payment, getAuthorizationsFromTransactions(jsonPayment.getTransactions())));
		payment.setChargesList(fetchChargeList(payment, getChargesFromTransactions(jsonPayment.getTransactions())));
		return payment;
	}
	
	private List<MarketplaceCharge> fetchChargeList(MarketplacePayment payment, List<JsonTransaction> jsonChargesTransactionList) throws HttpCommunicationException {
		List<MarketplaceCharge> chargesList = new ArrayList<MarketplaceCharge>();
		
		if (jsonChargesTransactionList != null && !jsonChargesTransactionList.isEmpty()) {
			for (JsonTransaction jsonTransaction : jsonChargesTransactionList) {
				MarketplaceCharge charge = fetchCharge(payment, new MarketplaceCharge(heidelpay), jsonTransaction.getUrl());
				charge.setCancelList(getCancelListByParentId(payment.getCancelList(), TRANSACTION_TYPE_CANCEL_CHARGE, charge.getId()));
				charge.setType(jsonTransaction.getType());
				charge.setBasketId(payment.getBasketId());
				charge.setCustomerId(payment.getCustomerId());
				charge.setMetadataId(payment.getMetadataId());
				chargesList.add(charge);
			}
		}
		return chargesList;
	}
	
	/**
	 * Execute a marketplace charge fetch action.
	 * 
	 * @param paymentId refers to payment to be fetched.
	 * @param chargeId refers to charge to be fetched.
	 * @return MarketplaceCharge
	 * @throws HttpCommunicationException
	 */
	public MarketplaceCharge fetchMarketplaceCharge(String paymentId, String chargeId) throws HttpCommunicationException {
		MarketplaceCharge charge = new MarketplaceCharge(heidelpay);
		charge.setId(chargeId);
		String response = restCommunication.httpGet(urlUtil.getPaymentUrl(charge, paymentId, chargeId), heidelpay.getPrivateKey());
		JsonCharge jsonCharge = jsonParser.fromJson(response, JsonCharge.class);
		charge = (MarketplaceCharge) jsonToBusinessClassMapper.mapToBusinessObject(charge, jsonCharge);
		charge.setPayment(fetchMarketplacePayment(jsonCharge.getResources().getPaymentId()));
		return charge;
	}
	
	/**
	 * Execute a marketplace authorization fetch action.
	 * 
	 * @param paymentId refers to payment to be fetched.
	 * @param authorizeId refers to authorization to be fetched.
	 * @return MarketplaceAuthorization
	 * @throws HttpCommunicationException
	 */
	public MarketplaceAuthorization fetchMarketplaceAuthorization(String paymentId, String authorizeId) throws HttpCommunicationException {
		MarketplaceAuthorization authorization = new MarketplaceAuthorization(heidelpay);
		authorization.setId(authorizeId);
		String response = restCommunication.httpGet(urlUtil.getPaymentUrl(authorization, paymentId, authorizeId), heidelpay.getPrivateKey());
		JsonAuthorization jsonAuthorization = jsonParser.fromJson(response, JsonAuthorization.class);
		authorization = (MarketplaceAuthorization) jsonToBusinessClassMapper.mapToBusinessObject(authorization, jsonAuthorization);
		authorization.setPayment(fetchMarketplacePayment(jsonAuthorization.getResources().getPaymentId()));
		return authorization;
	}
	
	/**
	 * Execute a marketplace full cancel for single or multiple authorization(s).
	 * @param paymentId refers to payment to be cancelled.
	 * @param cancel refers to cancel request.
	 * @return MarketplacePayment
	 * @throws HttpCommunicationException
	 */
	public MarketplacePayment marketplaceFullAuthorizationsCancel(String paymentId, MarketplaceCancel cancel) throws HttpCommunicationException {
		String url = urlUtil.getRestUrl().concat(cancel.getFullAuthorizeCancelUrl(paymentId));
		return marketplaceFullCancel(paymentId, url, cancel);
	}
	
	/**
	 * Execute a marketplace full cancel for single or multiple charge(s).
	 * @param paymentId refers to payment to be cancelled.
	 * @param cancel refers to cancel request.
	 * @return MarketplacePayment
	 * @throws HttpCommunicationException
	 */
	public MarketplacePayment marketplaceFullChargesCancel(String paymentId, MarketplaceCancel cancel) throws HttpCommunicationException {
		String url = urlUtil.getRestUrl().concat(cancel.getFullChargesCancelUrl(paymentId));
		return marketplaceFullCancel(paymentId, url, cancel);
	}
	
	/**
	 * Execute a marketplace cancel for one authorization.
	 * @param paymentId refers to payment to be cancelled.
	 * @param authorizeId refers to authorization to be cancelled.
	 * @param cancel refers to cancel request.
	 * @return MarketplaceCancel
	 * @throws HttpCommunicationException
	 */
	public MarketplaceCancel marketplaceAuthorizationCancel(String paymentId, String authorizeId, MarketplaceCancel cancel) throws HttpCommunicationException {
		String url = urlUtil.getRestUrl().concat(cancel.getPartialAuthorizeCancelUrl(paymentId, authorizeId));
		return marketplaceCancel(paymentId, url, cancel);
	}
	
	/**
	 * Execute a marketplace cancel for one charge.
	 * @param paymentId refers to payment to be cancelled.
	 * @param authorizeId refers to charge to be cancelled.
	 * @param cancel refers to cancel request.
	 * @return MarketplaceCancel
	 * @throws HttpCommunicationException
	 */
	public MarketplaceCancel marketplaceChargeCancel(String paymentId, String chargeId, MarketplaceCancel cancel) throws HttpCommunicationException {
		String url = urlUtil.getRestUrl().concat(cancel.getPartialChargeCancelUrl(paymentId, chargeId));
		return marketplaceCancel(paymentId, url, cancel);
	}
	
	private MarketplacePayment marketplaceFullCancel(String paymentId, String url, MarketplaceCancel cancel) throws HttpCommunicationException {
		String response = restCommunication.httpPost(url, heidelpay.getPrivateKey(), jsonToBusinessClassMapper.map(cancel));
		JsonPayment jsonPayment = jsonParser.fromJson(response, JsonPayment.class);
		MarketplacePayment paymentResponse = new MarketplacePayment(this.heidelpay);
		paymentResponse.setId(paymentId);
		paymentResponse = jsonToBusinessClassMapper.mapToBusinessObject(paymentResponse, jsonPayment);
		paymentResponse.setCancelList(fetchCancelList(paymentResponse, getCancelsFromTransactions(jsonPayment.getTransactions())));
		paymentResponse.setAuthorizationsList(fetchAuthorizationList(paymentResponse, getAuthorizationsFromTransactions(jsonPayment.getTransactions())));
		paymentResponse.setChargesList(fetchChargeList(paymentResponse, getChargesFromTransactions(jsonPayment.getTransactions())));
		return paymentResponse;
	}
	
	private MarketplaceCancel marketplaceCancel(String paymentId, String url, MarketplaceCancel cancel) throws HttpCommunicationException {
		String response = restCommunication.httpPost(url, heidelpay.getPrivateKey(), jsonToBusinessClassMapper.map(cancel));
		JsonCancel jsonCancel = jsonParser.fromJson(response, JsonCancel.class);
		cancel = (MarketplaceCancel)jsonToBusinessClassMapper.mapToBusinessObject(cancel, jsonCancel);
		cancel.setPayment(fetchMarketplacePayment(paymentId));
		cancel.setHeidelpay(heidelpay);
		return cancel;
	}
	
	private List<MarketplaceAuthorization> fetchAuthorizationList(MarketplacePayment payment, List<JsonTransaction> jsonTransaction) throws HttpCommunicationException {
		List<MarketplaceAuthorization> authorizationsList = new ArrayList<MarketplaceAuthorization>(jsonTransaction.size());
		if(!jsonTransaction.isEmpty()) {
			for(JsonTransaction json : jsonTransaction) {
				MarketplaceAuthorization authorization = fetchAuthorization(payment, new MarketplaceAuthorization(heidelpay), json.getUrl());
				authorization.setCancelList(getCancelListByParentId(payment.getCancelList(), TRANSACTION_TYPE_CANCEL_AUTHORIZE, authorization.getId()));
				authorization.setType(json.getType());
				authorization.setBasketId(payment.getBasketId());
				authorization.setCustomerId(payment.getCustomerId());
				authorization.setMetadataId(payment.getMetadataId());
				authorizationsList.add(authorization);
			}
		}
		
		return authorizationsList;
	}
	
	private List<MarketplaceCancel> fetchCancelList(MarketplacePayment payment, List<JsonTransaction> jsonChargesTransactionList) throws HttpCommunicationException {
		List<MarketplaceCancel> cancelList = new ArrayList<MarketplaceCancel>();
		
		if (jsonChargesTransactionList != null && !jsonChargesTransactionList.isEmpty()) {
			for (JsonTransaction jsonTransaction : jsonChargesTransactionList) {
				MarketplaceCancel cancel = fetchCancel(payment, new MarketplaceCancel(heidelpay), jsonTransaction.getUrl());
				cancel.setType(jsonTransaction.getType());
				cancelList.add(cancel);
			}
		}
		return cancelList;
	}
	
	private MarketplaceCancel fetchCancel(MarketplacePayment payment, MarketplaceCancel cancel, URL url) throws HttpCommunicationException {
		String response = restCommunication.httpGet(url.toString(), heidelpay.getPrivateKey());
		JsonCancel jsonCancel = jsonParser.fromJson(response, JsonCancel.class);
		cancel = (MarketplaceCancel)jsonToBusinessClassMapper.mapToBusinessObject(cancel, jsonCancel);
		cancel.setPayment(payment);
		cancel.setResourceUrl(url);
		return cancel;
	}
	
	private MarketplaceAuthorization fetchAuthorization(MarketplacePayment payment, MarketplaceAuthorization authorization, URL url) throws HttpCommunicationException {
		String response = restCommunication.httpGet(url.toString(), heidelpay.getPrivateKey());
		JsonAuthorization jsonAuthorization = jsonParser.fromJson(response,
				JsonAuthorization.class);
		authorization = (MarketplaceAuthorization) jsonToBusinessClassMapper.mapToBusinessObject(authorization, jsonAuthorization);
		authorization.setCancelList(getCancelListByParentId(payment.getCancelList(), TRANSACTION_TYPE_CANCEL_AUTHORIZE, authorization.getId()));
		authorization.setHeidelpay(heidelpay);
		return authorization;
	}
	
	private MarketplaceCharge fetchCharge(MarketplacePayment payment, MarketplaceCharge charge, URL url) throws HttpCommunicationException {
		String response = restCommunication.httpGet(url.toString(), heidelpay.getPrivateKey());
		JsonCharge jsonCharge = jsonParser.fromJson(response, JsonCharge.class);
		charge = (MarketplaceCharge) jsonToBusinessClassMapper.mapToBusinessObject(charge, jsonCharge);
		charge.setInvoiceId(jsonCharge.getInvoiceId());
		charge.setPayment(payment);
		charge.setResourceUrl(url);
		return charge;
	}
	
	private List<MarketplaceCancel> getCancelListByParentId(List<MarketplaceCancel> cancelList, String cancelType, String authorizeId) {
		List<MarketplaceCancel> authorizationCancelList = new ArrayList<MarketplaceCancel>();
		
		if (cancelList != null) {
			for (MarketplaceCancel cancel : cancelList) {
				if (cancelType.equalsIgnoreCase(cancel.getType()) && cancel.getResourceUrl().toString().contains(authorizeId)) {
					authorizationCancelList.add(cancel);
				}
			}
		}
		return authorizationCancelList;

	}
}
