package com.unzer.payment.models;

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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class RiskData {
    private String threatMetrixId;
    private Integer registrationLevel;
    private String registrationDate;
    private String customerId;
    private CustomerGroup customerGroup;
    private String confirmedOrders; // must be int. See UPL specification https://unz.atlassian.net/wiki/spaces/UCC/pages/892436661/Tech+Spec+-+UPL+invoice+type+integration+to+SDKs
    private String confirmedAmount; // must be double. See UPL specification https://unz.atlassian.net/wiki/spaces/UCC/pages/892436661/Tech+Spec+-+UPL+invoice+type+integration+to+SDKs
    private static final DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");


    public String getThreatMetrixId() {
        return threatMetrixId;
    }

    public RiskData setThreatMetrixId(String threatMetrixId) {
        this.threatMetrixId = threatMetrixId;
        return this;
    }

    public Integer getRegistrationLevel() {
        return registrationLevel;
    }

    public RiskData setRegistrationLevel(Integer registrationLevel) {
        this.registrationLevel = registrationLevel;
        return this;
    }

    public Date getRegistrationDate() {
        try {
            return dateFormat.parse(this.registrationDate);
        } catch (ParseException e) {
            throw new RuntimeException(
                    "Failed to parse internal string representation: " + this.registrationDate,
                    e
            );
        }
    }

    public RiskData setRegistrationDate(Date registrationDate) {
        this.registrationDate = dateFormat.format(registrationDate);
        return this;
    }

    public String getCustomerId() {
        return customerId;
    }

    public RiskData setCustomerId(String customerId) {
        this.customerId = customerId;
        return this;
    }

    public CustomerGroup getCustomerGroup() {
        return customerGroup;
    }

    public RiskData setCustomerGroup(CustomerGroup customerGroup) {
        this.customerGroup = customerGroup;
        return this;
    }

    public Integer getConfirmedOrders() {
        if (this.confirmedOrders == null) {
            return null;
        }
        return Integer.valueOf(confirmedOrders);
    }

    public RiskData setConfirmedOrders(Integer confirmedOrders) {
        if (confirmedOrders == null) {
            this.confirmedOrders = null;
        } else {
            this.confirmedOrders = confirmedOrders.toString();
        }
        return this;
    }

    public Double getConfirmedAmount() {
        if (this.confirmedAmount == null) {
            return null;
        }
        return Double.valueOf(confirmedAmount);
    }

    public RiskData setConfirmedAmount(Double confirmedAmount) {
        if (confirmedAmount == null) {
            this.confirmedAmount = null;
        } else {
            this.confirmedAmount = confirmedAmount.toString();
        }
        return this;
    }

    /**
     * Customer registration level 0=guest, 1=registered
     */
    public static interface RegistrationLevel {
        Integer GUEST = 0;
        Integer REGISTERED = 1;
    }

    /**
     * Customer classification for the customer
     */
    public static enum CustomerGroup {
        TOP, GOOD, NEUTRAL, BAD
    }
}
