package com.heidelpay.payment.service;

import com.heidelpay.payment.Heidelpay;
import com.heidelpay.payment.Linkpay;
import com.heidelpay.payment.PaymentException;
import com.heidelpay.payment.communication.HeidelpayRestCommunication;
import com.heidelpay.payment.communication.HttpCommunicationException;
import com.heidelpay.payment.communication.JsonParser;
import com.heidelpay.payment.communication.impl.HttpClientBasedRestCommunication;
import com.heidelpay.payment.communication.json.JsonLinkpay;
import com.heidelpay.payment.communication.mapper.JsonToBusinessClassMapper;

public class LinkpayService {
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
	public LinkpayService(Heidelpay heidelpay) {
		this(heidelpay, new HttpClientBasedRestCommunication());
	}

	/**
	 * Creates the {@code PaymentService} with the given {@code Heidelpay} facade,
	 * bound to the given {@code HeidelpayRestCommunication} implementation used for
	 * http-communication.
	 *
	 * @param heidelpay - the {@code Heidelpay} Facade
	 * @param restCommunication - the implementation of {@code HeidelpayRestCommunication} to be used for network communication.
	 */
	public LinkpayService(Heidelpay heidelpay, HeidelpayRestCommunication restCommunication) {
		super();
		this.heidelpay = heidelpay;
		this.urlUtil = new UrlUtil(heidelpay.getEndPoint());
		this.restCommunication = restCommunication;
	}

	public Linkpay initialize(Linkpay linkpay) throws PaymentException, HttpCommunicationException {
		return initialize(linkpay, urlUtil.getRestUrl(linkpay));
	}

	public Linkpay initialize(Linkpay linkpay, String url) throws HttpCommunicationException {
		String response = restCommunication.httpPost(url, heidelpay.getPrivateKey(), jsonToBusinessClassMapper.map(linkpay));
		JsonLinkpay jsonLinkpay = new JsonParser<JsonLinkpay>().fromJson(response, JsonLinkpay.class);
		linkpay = jsonToBusinessClassMapper.mapToBusinessObject(linkpay, jsonLinkpay);
		return linkpay;
	}

}
