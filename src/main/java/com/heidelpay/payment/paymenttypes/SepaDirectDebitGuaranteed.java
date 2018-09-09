package com.heidelpay.payment.paymenttypes;

public class SepaDirectDebitGuaranteed extends AbstractPaymentType implements PaymentType {
	private String iban;
	private String bic;
	private String holder;
	
	public SepaDirectDebitGuaranteed(String iban) {
		super();
		this.iban = iban;
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

	@Override
	public String getTypeUrl() {
		return "types/sepa-direct-debit-guaranteed";
	}
	
}
