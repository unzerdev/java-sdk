package com.unzer.payment.models;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("additionalTransactionDataModel")
@JsonTypeInfo(include = JsonTypeInfo.As.WRAPPER_OBJECT, use = JsonTypeInfo.Id.NAME)
public class AdditionalTransactionData {
    private CardTransactionData card;

    public CardTransactionData getCard() {
        return card;
    }

    public void setCard(CardTransactionData card) {
        this.card = card;
    }
}
