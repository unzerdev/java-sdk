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
package com.unzer.payment.marketplace;

import java.math.BigDecimal;

/**
 * This DTO is part of marketplace cancel request, to request cancel for basket item(s) of one marketplace participant.
 *
 * @author Unzer E-Com GmbH
 */
public class MarketplaceCancelBasketItem {
    private String participantId;
    private String basketItemReferenceId;
    private int quantity;
    private BigDecimal amountGross;

    public String getParticipantId() {
        return participantId;
    }

    public void setParticipantId(String participantId) {
        this.participantId = participantId;
    }

    public String getBasketItemReferenceId() {
        return basketItemReferenceId;
    }

    public void setBasketItemReferenceId(String basketItemReferenceId) {
        this.basketItemReferenceId = basketItemReferenceId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getAmountGross() {
        return amountGross;
    }

    public void setAmountGross(BigDecimal amountGross) {
        this.amountGross = amountGross;
    }

}
