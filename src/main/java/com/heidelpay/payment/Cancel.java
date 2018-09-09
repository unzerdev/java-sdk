package com.heidelpay.payment;

import java.math.BigDecimal;

public class Cancel extends AbstractPayment {
	private BigDecimal amount;
	private Processing processing = new Processing();

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public Processing getProcessing() {
		return processing;
	}

	public void setProcessing(Processing processing) {
		this.processing = processing;
	}

	@Override
	public String getTypeUrl() {
		return "payments/<paymentId>/authorize/cancels";
	}


}
