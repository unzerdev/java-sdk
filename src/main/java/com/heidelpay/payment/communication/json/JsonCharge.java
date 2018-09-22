package com.heidelpay.payment.communication.json;

/*-
 * #%L
 * Heidelpay Java SDK
 * %%
 * Copyright (C) 2018 Heidelpay GmbH
 * %%
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 * #L%
 */

import java.math.BigDecimal;
import java.net.URL;
import java.util.Currency;
import java.util.Date;

public class JsonCharge extends JsonIdObject implements JsonObject {
	private String isSuccess;
	private String isPending;
	private String isError;
	private JsonMessage message;
	private Date date;	
	
	private BigDecimal amount;
	private Currency currency;
	private URL returnUrl;
	private URL redirectUrl;
	
	private JsonResources resources; 
	private JsonProcessing processing = new JsonProcessing();
	
	public JsonCharge() {
		super();
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public JsonCharge setAmount(BigDecimal amount) {
		this.amount = amount;
		return this;
	}

	public Currency getCurrency() {
		return currency;
	}

	public JsonCharge setCurrency(Currency currency) {
		this.currency = currency;
		return this;
	}

	public URL getReturnUrl() {
		return returnUrl;
	}

	public JsonCharge setReturnUrl(URL returnUrl) {
		this.returnUrl = returnUrl;
		return this;
	}

	public JsonProcessing getProcessing() {
		return processing;
	}

	public JsonCharge setProcessing(JsonProcessing processing) {
		this.processing = processing;
		return this;
	}


	public JsonResources getResources() {
		return resources;
	}

	public void setResources(JsonResources resources) {
		this.resources = resources;
	}

	public String getIsSuccess() {
		return isSuccess;
	}

	public void setIsSuccess(String isSuccess) {
		this.isSuccess = isSuccess;
	}

	public String getIsPending() {
		return isPending;
	}

	public void setIsPending(String isPending) {
		this.isPending = isPending;
	}

	public String getIsError() {
		return isError;
	}

	public void setIsError(String isError) {
		this.isError = isError;
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

}
