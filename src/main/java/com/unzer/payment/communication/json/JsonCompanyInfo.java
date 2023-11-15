package com.unzer.payment.communication.json;

import com.unzer.payment.CompanyInfo;

public class JsonCompanyInfo {

    private String registrationType;
    private String commercialRegisterNumber;
    private String function;
    private String commercialSector;
    private CompanyInfo.CompanyType companyType;
    private CompanyInfo.Owner owner;

    public String getRegistrationType() {
        return registrationType;
    }


    public void setRegistrationType(String registrationType) {
        this.registrationType = registrationType;
    }

    public String getCommercialRegisterNumber() {
        return commercialRegisterNumber;
    }

    public void setCommercialRegisterNumber(String commercialRegisterNumber) {
        this.commercialRegisterNumber = commercialRegisterNumber;
    }


    public String getFunction() {
        return function;
    }


    public void setFunction(String function) {
        this.function = function;
    }

    public String getCommercialSector() {
        return commercialSector;
    }

    public void setCommercialSector(String commercialSector) {
        this.commercialSector = commercialSector;
    }

    public CompanyInfo.CompanyType getCompanyType() {
        return companyType;
    }

    public void setCompanyType(CompanyInfo.CompanyType companyType) {
        this.companyType = companyType;
    }
}
