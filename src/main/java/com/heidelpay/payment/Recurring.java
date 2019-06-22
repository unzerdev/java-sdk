package com.heidelpay.payment;

import java.net.URL;

import com.heidelpay.payment.paymenttypes.PaymentType;

/**
 * Business object for Charge. Amount, currency and typeId are mandatory parameter to 
 * execute an Charge. 
 * 
 * The returnUrl is mandatory in case of redirectPayments like Sofort, Paypal, Giropay, Creditcard 3DS
 * @author rene.felder
 *
 */
public class Recurring extends AbstractPayment implements PaymentType {
	public enum Status {SUCCESS, PENDING, ERRROR}
	private Status status;
	
	private URL returnUrl;
	private String customerId;
	private String metadataId;

	private String redirectUrl;

	private Processing processing = new Processing();

	public Recurring() {
		super();
	}
	public Recurring(Heidelpay heidelpay) {
		super(heidelpay);
	}


	@Override
	public String getTypeUrl() {
		return "types/<typeId>/recurring";
	}
	public Status getStatus() {
		return status;
	}
	public void setStatus(Status status) {
		this.status = status;
	}
	public Processing getProcessing() {
		return processing;
	}
	public void setProcessing(Processing processing) {
		this.processing = processing;
	}
	public String getCustomerId() {
		return customerId;
	}
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	public String getMetadataId() {
		return metadataId;
	}
	public void setMetadataId(String metadataId) {
		this.metadataId = metadataId;
	}
	public URL getReturnUrl() {
		return returnUrl;
	}
	public void setReturnUrl(URL returnUrl) {
		this.returnUrl = returnUrl;
	}
	public String getRedirectUrl() {
		return redirectUrl;
	}
	public void setRedirectUrl(String redirectUrl) {
		this.redirectUrl = redirectUrl;
	}

}
