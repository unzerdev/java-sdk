package com.heidelpay.payment;

import java.util.LinkedHashMap;
import java.util.Map;

import com.heidelpay.payment.paymenttypes.PaymentType;

public class Metadata implements PaymentType {
	public Metadata() {
		this(false);
	}
	public Metadata(boolean sorted) {
		super();
		this.sorted = sorted;
	}
	
	private String id;
	private Map<String, String> metadataMap = new LinkedHashMap<>();
	private boolean sorted = false;
	private Heidelpay heidelpay;

	
	public void addMetadata(String key, String value) {
		getMetadataMap().put(key, value);
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}

	private String sdkVersion;
	private String sdkType;


	@Override
	public String getTypeUrl() {
		return "metadata";
	}
	public Heidelpay getHeidelpay() {
		return heidelpay;
	}
	public void setHeidelpay(Heidelpay heidelpay) {
		this.heidelpay = heidelpay;
	}
	public Map<String, String> getMetadataMap() {
		return metadataMap;
	}
	public void setMetadataMap(Map<String, String> metadataMap) {
		this.metadataMap = metadataMap;
	}
}
