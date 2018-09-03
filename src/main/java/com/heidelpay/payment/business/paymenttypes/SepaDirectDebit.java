package com.heidelpay.payment.business.paymenttypes;

import com.heidelpay.payment.business.AbstractPayment;

public class SepaDirectDebit extends AbstractPayment implements PaymentType {
	private String id;
	private String iban;
	private String bic;
	private String holder;
	
	public SepaDirectDebit(String iban) {
		super();
		this.iban = iban;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getIban() {
		return iban;
	}

	public void setIban(String iban) {
		this.iban = iban;
	}

	public String getBic() {
		return bic;
	}

	public void setBic(String bic) {
		this.bic = bic;
	}

	public String getHolder() {
		return holder;
	}

	public void setHolder(String holder) {
		this.holder = holder;
	}
	
}
