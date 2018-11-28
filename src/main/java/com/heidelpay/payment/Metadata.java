package com.heidelpay.payment;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

import com.heidelpay.payment.paymenttypes.PaymentType;

public class Metadata implements PaymentType {
	public Metadata() {
		this(false);
	}
	public Metadata(boolean sorted) {
		super();
		if (sorted) {
			metadataMap = new TreeMap<String, String>();
		} else {
			metadataMap = new LinkedHashMap<String, String>();
		}
	}
	
	private String id;
	private Map<String, String> metadataMap = new LinkedHashMap<>();
	private Heidelpay heidelpay;

	
	public Metadata addMetadata(String key, String value) {
		getMetadataMap().put(key, value);
		return this;
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
