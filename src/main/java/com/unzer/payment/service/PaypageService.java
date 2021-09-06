package com.unzer.payment.service;

/*-
 * #%L
 * Unzer Java SDK
 * %%
 * Copyright (C) 2020 - today Unzer E-Com GmbH
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

import com.unzer.payment.Unzer;
import com.unzer.payment.Paypage;
import com.unzer.payment.communication.UnzerRestCommunication;
import com.unzer.payment.communication.HttpCommunicationException;
import com.unzer.payment.communication.JsonParser;
import com.unzer.payment.communication.json.JsonPaypage;
import com.unzer.payment.communication.mapper.JsonToBusinessClassMapper;

public class PaypageService {
	private UnzerRestCommunication restCommunication;

	private UrlUtil urlUtil;
	private JsonToBusinessClassMapper jsonToBusinessClassMapper = new JsonToBusinessClassMapper();
	private Unzer unzer;

	/**
	 * Creates the {@code PaymentService} with the given {@code Unzer} facade,
	 * bound to the given {@code UnzerRestCommunication} implementation used for
	 * http-communication.
	 * 
	 * @param unzer - the {@code Unzer} Facade
	 * @param restCommunication - the implementation of {@code UnzerRestCommunication} to be used for network communication.
	 */
	public PaypageService(Unzer unzer, UnzerRestCommunication restCommunication) {
		super();
		this.unzer = unzer;
		this.urlUtil = new UrlUtil(unzer.getEndPoint());
		this.restCommunication = restCommunication;
	}

	public Paypage initialize(Paypage paypage) throws HttpCommunicationException {
		return initialize(paypage, urlUtil.getRestUrl(paypage));
	}
	
	public Paypage initialize(Paypage paypage, String url) throws HttpCommunicationException {
		String response = restCommunication.httpPost(url, unzer.getPrivateKey(), jsonToBusinessClassMapper.map(paypage));
		JsonPaypage jsonPaypage = new JsonParser().fromJson(response, JsonPaypage.class);
		paypage = jsonToBusinessClassMapper.mapToBusinessObject(paypage, jsonPaypage);
		return paypage;
	}


}
