package com.heidelpay.payment.paymenttypes;

public class SepaDirectDebit extends AbstractPaymentType implements PaymentType {
	private String iban;
	private String bic;
	private String holder;
	
	public SepaDirectDebit(String iban) {
		super();
		this.iban = iban;
	}

	public String getIban() {
		return iban;
	}

	public SepaDirectDebit setIban(String iban) {
		this.iban = iban;
		return this;
	}

	public String getBic() {
		return bic;
	}

	public SepaDirectDebit setBic(String bic) {
		this.bic = bic;
		return this;
	}

	public String getHolder() {
		return holder;
	}

	public SepaDirectDebit setHolder(String holder) {
		this.holder = holder;
		return this;
	}

	@Override
	public String getTypeUrl() {
		return "types/sepa-direct-debit";
	}
	
}
