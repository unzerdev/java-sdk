package com.unzer.payment;

/*-
 * #%L
 * Unzer Java SDK
 * %%
 * Copyright (C) 2020 - today Unzer E-Com GmbH
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import com.google.gson.annotations.SerializedName;
import com.unzer.payment.communication.json.JSonCompanyInfo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CustomerCompanyData {
    public CompanyType getCompanyType() {
        return companyType;
    }

    public void setCompanyType(CompanyType companyType) {
        this.companyType = companyType;
    }

    public Owner getOwner() {
        return owner;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }

    public enum RegistrationType {REGISTERED, NOT_REGISTERED}

    private RegistrationType registrationType;
    private String commercialRegisterNumber;
    private CommercialSector commercialSector;
    private CompanyType companyType;
    private Owner owner;

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

    public enum CompanyType {
        @SerializedName("authority")
        AUTHORITY,
        @SerializedName("association")
        ASSOCIATION,
        @SerializedName("sole")
        SOLE,
        @SerializedName("company")
        COMPANY,
        @SerializedName("other")
        OTHER
    }

    public static class Owner {
        private String firstname;
        private String lastname;
        private String birthdate;

        private final static SimpleDateFormat yyyy_MM_dd_DateFormat = new SimpleDateFormat("yyyy-MM-dd");
        private final static SimpleDateFormat dd_MM_yyyyDateFormat = new SimpleDateFormat("dd.MM.yyyy");


        public String getFirstname() {
            return firstname;
        }

        public void setFirstname(String firstname) {
            this.firstname = firstname;
        }

        public String getLastname() {
            return lastname;
        }

        public void setLastname(String lastname) {
            this.lastname = lastname;
        }

        public Date getBirthdate() {
            if (this.birthdate == null) {
                return null;
            }

            Date result;
            try {
                if (this.birthdate.contains("-")) {
                    result = yyyy_MM_dd_DateFormat.parse(this.birthdate);
                } else {
                    result = dd_MM_yyyyDateFormat.parse(this.birthdate);
                }
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }

            return result;
        }

        public void setBirthdate(Date birthdate) {
            this.birthdate = yyyy_MM_dd_DateFormat.format(birthdate);
        }
    }

}
