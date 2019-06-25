package com.heidelpay.payment.communication.json;

import java.util.ArrayList;
import java.util.List;

import com.heidelpay.payment.business.paymenttypes.HirePurchaseRatePlan;

public class JsonHirePurchaseRatePlanList {
	private String code;
	private List<HirePurchaseRatePlan> entity = new ArrayList<HirePurchaseRatePlan>();
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public List<HirePurchaseRatePlan> getEntity() {
		return entity;
	}
	public void setEntity(List<HirePurchaseRatePlan> entity) {
		this.entity = entity;
	}
}
