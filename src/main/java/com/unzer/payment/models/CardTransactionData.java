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
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CardTransactionData that = (CardTransactionData) o;

        if (recurrenceType != that.recurrenceType) return false;
        return liability == that.liability;
    }

    @Override
    public int hashCode() {
        int result = recurrenceType != null ? recurrenceType.hashCode() : 0;
        result = 31 * result + (liability != null ? liability.hashCode() : 0);
        return result;
    }

    public enum Liability {
        ISSUER,
        MERCHANT,
    }
}
