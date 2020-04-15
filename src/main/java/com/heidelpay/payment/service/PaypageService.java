package com.heidelpay.payment.service;

/*-
 * #%L
 * Heidelpay Java SDK
 * %%
 * Copyright (C) 2018 - 2019 Heidelpay GmbH
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

	private UrlUtil urlUtil;
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
		this.urlUtil = new UrlUtil(heidelpay.getEndPoint());
		this.restCommunication = restCommunication;
	}

	public Paypage initialize(Paypage paypage) throws PaymentException, HttpCommunicationException {
		return initialize(paypage, urlUtil.getRestUrl(paypage));
	}
	
	public Paypage initialize(Paypage paypage, String url) throws HttpCommunicationException {
		String response = restCommunication.httpPost(url, heidelpay.getPrivateKey(), jsonToBusinessClassMapper.map(paypage));
		JsonPaypage jsonPaypage = new JsonParser<JsonPaypage>().fromJson(response, JsonPaypage.class);
		paypage = jsonToBusinessClassMapper.mapToBusinessObject(paypage, jsonPaypage);
		return paypage;
	}


}
