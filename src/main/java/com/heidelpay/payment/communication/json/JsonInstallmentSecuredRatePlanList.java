package com.heidelpay.payment.communication.json;

import java.util.ArrayList;
import java.util.List;

import com.heidelpay.payment.business.paymenttypes.InstallmentSecuredRatePlan;

public class JsonInstallmentSecuredRatePlanList {
	private String code;
	private List<InstallmentSecuredRatePlan> entity = new ArrayList<InstallmentSecuredRatePlan>();
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public List<InstallmentSecuredRatePlan> getEntity() {
		return entity;
	}
	public void setEntity(List<InstallmentSecuredRatePlan> entity) {
		this.entity = entity;
	}
}
