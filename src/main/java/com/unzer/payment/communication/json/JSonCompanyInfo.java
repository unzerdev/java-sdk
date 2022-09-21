/*
 * Copyright 2020-today Unzer E-Com GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.unzer.payment.communication.json;

import com.google.gson.annotations.SerializedName;
import com.unzer.payment.CustomerCompanyData;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class JSonCompanyInfo {

    private String registrationType;
    private String commercialRegisterNumber;
    private String function;
    private String commercialSector;private CustomerCompanyData.CompanyType companyType;
    private CustomerCompanyData.Owner owner;

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

public CustomerCompanyData.CompanyType getCompanyType() {
        return companyType;
    }

    public void setCompanyType(CustomerCompanyData.CompanyType companyType) {
        this.companyType = companyType;
    }
}
