package com.heidelpay.payment.service;

import com.heidelpay.payment.Heidelpay;
import com.heidelpay.payment.PaymentException;
import com.heidelpay.payment.Paypage;
import com.heidelpay.payment.communication.HeidelpayRestCommunication;
import com.heidelpay.payment.communication.HttpCommunicationException;
import com.heidelpay.payment.communication.JsonParser;
import com.heidelpay.payment.communication.impl.RestCommunication;
import com.heidelpay.payment.communication.json.JsonPaypage;
import com.heidelpay.payment.communication.mapper.JsonToBusinessClassMapper;

public class PaypageService {
	private HeidelpayRestCommunication restCommunication;

	private UrlUtil urlUtil = new UrlUtil();
	private JsonToBusinessClassMapper jsonToBusinessClassMapper = new JsonToBusinessClassMapper();
	private Heidelpay heidelpay;

	/**
	 * Creates a PaymentService with the @deprecated {@code RestCommunication}
	 * implementation.
	 * 
	 * @param heidelpay
	 */
	public PaypageService(Heidelpay heidelpay) {
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
	public PaypageService(Heidelpay heidelpay, HeidelpayRestCommunication restCommunication) {
		super();
		this.heidelpay = heidelpay;
		this.restCommunication = restCommunication;
	}

	public Paypage initialize(Paypage paypage) throws PaymentException, HttpCommunicationException {
		return initialize(paypage, urlUtil.getRestUrl(paypage));
	}
	
	public Paypage initialize(Paypage paypage, String url) throws PaymentException, HttpCommunicationException {
		String response = restCommunication.httpPost(url, heidelpay.getPrivateKey(), jsonToBusinessClassMapper.map(paypage));
		JsonPaypage jsonPaypage = new JsonParser<JsonPaypage>().fromJson(response, JsonPaypage.class);
		paypage = (Paypage) jsonToBusinessClassMapper.mapToBusinessObject(paypage, jsonPaypage);
		return paypage;
	}


}
