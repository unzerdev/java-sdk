package com.heidelpay.payment.service;

import java.math.BigDecimal;
import java.net.URL;
import java.util.Currency;
import java.util.Map;

import com.heidelpay.payment.AbstractPayment;
import com.heidelpay.payment.Authorization;
import com.heidelpay.payment.Customer;
import com.heidelpay.payment.Heidelpay;
import com.heidelpay.payment.communication.HttpCommunicationException;
import com.heidelpay.payment.communication.JsonParser;
import com.heidelpay.payment.communication.RestCommunication;
import com.heidelpay.payment.paymenttypes.PaymentType;

public class PaymentService {
	private RestCommunication restCommunication = new RestCommunication();
	private JsonParser jsonParser = JsonParser.create(); 
	private UrlUtil urlUtil = new UrlUtil();
	private Heidelpay heidelpay;

	public PaymentService(Heidelpay heidelpay) {
		super();
		this.heidelpay = heidelpay;
	}

	public PaymentType createPaymentType(AbstractPayment paymentType) throws HttpCommunicationException {
		String response = restCommunication.httpPost(urlUtil.getRestUrl(paymentType), heidelpay.getPrivateKey(), paymentType);
		Map<String, String> responseMap = jsonParser.fromJson(response, Map.class);
		paymentType.setId(responseMap.get("id"));
		paymentType.setHeidelpay(heidelpay);
		return paymentType;
	}

	public Authorization authorize(BigDecimal amount, Currency currency, String typeId, String customerId, URL returnUrl) throws HttpCommunicationException {
		Authorization authorization = getAuthorization(amount, currency, typeId, customerId, returnUrl);
		String response = restCommunication.httpPost(urlUtil.getRestUrl(authorization), heidelpay.getPrivateKey(), authorization);
		Map<String, String> responseMap = jsonParser.fromJson(response, Map.class);
		authorization.setId(responseMap.get("id"));
		authorization.setHeidelpay(heidelpay);
		return authorization;

	}

	private Authorization getAuthorization(BigDecimal amount, Currency currency, String paymentTypeId, String customerId, URL returnUrl) {
		Authorization authorization = new Authorization(heidelpay);
		authorization
		.setAmount(amount)
		.setCurrency(currency)
		.setReturnUrl(returnUrl)
		.getResources()
		.setTypeId(paymentTypeId);
		return authorization;
	}


}
