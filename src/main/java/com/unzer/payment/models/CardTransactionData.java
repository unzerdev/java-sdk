package com.unzer.payment.models;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.gson.annotations.SerializedName;
import com.unzer.payment.enums.RecurrenceType;

@JsonTypeName("card")
@JsonTypeInfo(include = JsonTypeInfo.As.WRAPPER_OBJECT, use = JsonTypeInfo.Id.NAME)
public class CardTransactionData {
    private RecurrenceType recurrenceType;
    private Liability liability;
    private ExemptionType exemptionType;

    public CardTransactionData() {
    }

    public CardTransactionData(RecurrenceType recurrenceType) {
        this.recurrenceType = recurrenceType;
    }

    public RecurrenceType getRecurrenceType() {
        return recurrenceType;
    }

    public CardTransactionData setRecurrenceType(RecurrenceType recurrenceType) {
        this.recurrenceType = recurrenceType;
        return this;
    }

    public Liability getLiability() {
        return liability;
    }

    public CardTransactionData setLiability(Liability liability) {
        this.liability = liability;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        CardTransactionData that = (CardTransactionData) o;

        if (recurrenceType != that.recurrenceType) {
            return false;
        }
        return liability == that.liability;
    }

    @Override
    public int hashCode() {
        int result = recurrenceType != null ? recurrenceType.hashCode() : 0;
        result = 31 * result + (liability != null ? liability.hashCode() : 0);
        return result;
    }

    public ExemptionType getExemptionType() {
        return exemptionType;
    }

    public CardTransactionData setExemptionType(ExemptionType exemptionType) {
        this.exemptionType = exemptionType;
        return this;
    }

    public enum Liability {
        ISSUER,
        MERCHANT,
    }

    public enum ExemptionType {
        /**
         * Low Value Payment
         */
        @SerializedName("lvp")
        LVP,
        /**
         * Transaction Risk Analysis
         */
        @SerializedName("tra")
        TRA
    }
}
