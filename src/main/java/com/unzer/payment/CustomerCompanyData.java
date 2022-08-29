package com.unzer.payment;

public class CustomerCompanyData {
    private RegistrationType registrationType;
    private String commercialRegisterNumber;
    private CommercialSector commercialSector;

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

    public enum RegistrationType {REGISTERED, NOT_REGISTERED}

}
