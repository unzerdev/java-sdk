package com.heidelpay.payment;

public class CustomerCompanyData {
	public enum RegistrationType {REGISTERED, NOT_REGISTERED};

	private RegistrationType registrationType;
	private String commercialRegisterNumber;
	private CommercialSector commercialSector;
	
	public CustomerCompanyData() {
	}

	public RegistrationType getRegistrationType() {
		return registrationType;
	}

	public void setRegistrationType(RegistrationType registrationType) {
		this.registrationType = registrationType;
	}

	public String getCommercialRegisterNumber() {
		return commercialRegisterNumber;
	}

	public void setCommercialRegisterNumber(String commercialRegisterNumber) {
		this.commercialRegisterNumber = commercialRegisterNumber;
	}

	public CommercialSector getCommercialSector() {
		return commercialSector;
	}

	public void setCommercialSector(CommercialSector commercialSector) {
		this.commercialSector = commercialSector;
	}

}
