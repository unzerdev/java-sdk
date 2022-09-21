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

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Date;

public class JsonShipment extends JsonIdObject implements JsonObject {
    private String isSuccess;
    private String isPending;
    private String isError;
    private JsonMessage message;
    private Date date;

    private BigDecimal amount;
    private Currency currency;

    private JsonResources resources;
    private JsonProcessing processing = new JsonProcessing();

    public JsonShipment() {
        super();
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public JsonShipment setAmount(BigDecimal amount) {
        this.amount = amount;
        return this;
    }

    public Currency getCurrency() {
        return currency;
    }

    public JsonShipment setCurrency(Currency currency) {
        this.currency = currency;
        return this;
    }

    public JsonProcessing getProcessing() {
        return processing;
    }

    public JsonShipment setProcessing(JsonProcessing processing) {
        this.processing = processing;
        return this;
    }


    public JsonResources getResources() {
        return resources;
    }

    public void setResources(JsonResources resources) {
        this.resources = resources;
    }

    public String getIsSuccess() {
        return isSuccess;
    }

    public void setIsSuccess(String isSuccess) {
        this.isSuccess = isSuccess;
    }

    public String getIsPending() {
        return isPending;
    }

    public void setIsPending(String isPending) {
        this.isPending = isPending;
    }

    public String getIsError() {
        return isError;
    }

    public void setIsError(String isError) {
        this.isError = isError;
    }

    public JsonMessage getMessage() {
        return message;
    }

    public void setMessage(JsonMessage message) {
        this.message = message;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

}
