package com.unzer.payment.enums;

import com.google.gson.annotations.SerializedName;

public enum RecurrenceType {
    @SerializedName("oneclick")
    ONECLICK,
    @SerializedName("scheduled")
    SCHEDULED,
    @SerializedName("unscheduled")
    UNSCHEDULED;

    @Override
    public String toString() {
        return this.name().toLowerCase();
    }
}
