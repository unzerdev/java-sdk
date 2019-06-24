package com.heidelpay.payment.communication.json;

import java.net.URL;
import java.util.Date;

public class JsonRecurring extends JsonIdObject implements JsonObject {
	private Boolean isSuccess;
	private Boolean isPending;
	private Boolean isError;
	private JsonMessage message;
	private Date date;	

	private URL returnUrl;
	
	private JsonResources resources; 
	private JsonProcessing processing = new JsonProcessing();
	private String redirectUrl;

	public JsonRecurring() {
		super();
	}

	public URL getReturnUrl() {
		return returnUrl;
	}

	public JsonRecurring setReturnUrl(URL returnUrl) {
		this.returnUrl = returnUrl;
		return this;
	}

	public JsonProcessing getProcessing() {
		return processing;
	}

	public JsonRecurring setProcessing(JsonProcessing processing) {
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

	public String getRedirectUrl() {
		return redirectUrl;
	}

	public void setRedirectUrl(String redirectUrl) {
		this.redirectUrl = redirectUrl;
	}

}
