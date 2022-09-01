/*
 * Copyright 2021 Unzer E-Com GmbH
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
package com.unzer.payment.models;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("additionalTransactionDataModel")
@JsonTypeInfo(include = JsonTypeInfo.As.WRAPPER_OBJECT, use = JsonTypeInfo.Id.NAME)
public class AdditionalTransactionData {
    private CardTransactionData card;
    private ShippingTransactionData shipping;
    private RiskData riskData;

    public CardTransactionData getCard() {
        return card;
    }

    public AdditionalTransactionData setCard(CardTransactionData card) {
        this.card = card;
        return this;
    }

    public ShippingTransactionData getShipping() {
        return shipping;
    }

    public AdditionalTransactionData setShipping(ShippingTransactionData shipping) {
        this.shipping = shipping;
        return this;
    }

    public RiskData getRiskData() {
        return riskData;
    }

    public AdditionalTransactionData setRiskData(RiskData riskData) {
        this.riskData = riskData;
        return this;
    }
}
