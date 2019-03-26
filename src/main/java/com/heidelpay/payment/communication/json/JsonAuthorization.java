package com.heidelpay.payment.communication.json;

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
import java.util.Currency;
import java.util.Date;

public class JsonAuthorization extends JsonIdObject implements JsonObject {
	private Boolean isSuccess;
	private Boolean isPending;
	private Boolean isError;
	private JsonMessage message;
	private Date date;	
	
	private String orderId;
	private BigDecimal amount;
	private Currency currency;
	private URL returnUrl;
	private URL redirectUrl;
	private Boolean card3ds;

	private JsonResources resources; 
	private JsonProcessing processing = new JsonProcessing();
	
	public JsonAuthorization() {
		super();
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public JsonAuthorization setAmount(BigDecimal amount) {
		this.amount = amount;
		return this;
	}

	public Currency getCurrency() {
		return currency;
	}

	public JsonAuthorization setCurrency(Currency currency) {
		this.currency = currency;
		return this;
	}

	public URL getReturnUrl() {
		return returnUrl;
	}

	public JsonAuthorization setReturnUrl(URL returnUrl) {
		this.returnUrl = returnUrl;
		return this;
	}

	public JsonProcessing getProcessing() {
		return processing;
	}

	public JsonAuthorization setProcessing(JsonProcessing processing) {
		this.processing = processing;
		return this;
	}


	public JsonResources getResources() {
		return resources;
	}

	public void setResources(JsonResources resources) {
		this.resources = resources;
	}

	public JsonMessage getMessage() {
		return message;
	}

	public void setMessage(JsonMessage message) {
		this.message = message;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public URL getRedirectUrl() {
		return redirectUrl;
	}

	public void setRedirectUrl(URL redirectUrl) {
		this.redirectUrl = redirectUrl;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public Boolean getIsSuccess() {
		return isSuccess;
	}

	public void setIsSuccess(Boolean isSuccess) {
		this.isSuccess = isSuccess;
	}

	public Boolean getIsPending() {
		return isPending;
	}

	public void setIsPending(Boolean isPending) {
		this.isPending = isPending;
	}

	public Boolean getIsError() {
		return isError;
	}

	public void setIsError(Boolean isError) {
		this.isError = isError;
	}

	public Boolean getCard3ds() {
		return card3ds;
	}

	public void setCard3ds(Boolean card3ds) {
		this.card3ds = card3ds;
	}

}
