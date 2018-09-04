package com.heidelpay.payment;

import java.math.BigDecimal;
import java.util.List;

public class Charge extends AbstractPayment {
	private List<Cancel> cancelList;
	
	public Charge() {
		super();
	}
	public Charge(Heidelpay heidelpay) {
		super(heidelpay);
	}
	
	public Cancel cancel() {
		return getHeidelpay().cancelCharge(getPayment().getId(), getId());
	}
	public Cancel cancel(BigDecimal amount) {
		return getHeidelpay().cancelCharge(getPayment().getId(), getId(), amount);
	}
	@Override
	public String getTypeUrl() {
		return "";
	}
	public List<Cancel> getCancelList() {
		return cancelList;
	}
	public void setCancelList(List<Cancel> cancelList) {
		this.cancelList = cancelList;
	}

}
