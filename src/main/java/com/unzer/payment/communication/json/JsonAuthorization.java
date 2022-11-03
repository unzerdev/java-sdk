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

public class JsonAuthorization extends JsonInitPayment implements JsonObject {

    public JsonAuthorization() {
        super();
    }

    public JsonAuthorization(JsonInitPayment json) {
        super();
        this.setAmount(json.getAmount());
        this.setCard3ds(json.getCard3ds());
        this.setCurrency(json.getCurrency());
        this.setOrderId(json.getOrderId());
        this.setInvoiceId(json.getInvoiceId());
        this.setResources(json.getResources());
        this.setReturnUrl(json.getReturnUrl());
        this.setDate(json.getDate());
        this.setIsError(json.getIsError());
        this.setIsPending(json.getIsPending());
        this.setIsSuccess(json.getIsSuccess());
        this.setMessage(json.getMessage());
        this.setProcessing(json.getProcessing());
        this.setRedirectUrl(json.getRedirectUrl());
        this.setPaymentReference(json.getPaymentReference());
        this.setId(json.getId());
        this.setAdditionalTransactionData(json.getAdditionalTransactionData());
    }


}
