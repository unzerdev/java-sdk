package com.unzer.payment.enums;

public enum RecurrenceType {
    ONECLICK,
    SCHEDULED,
    UNSCHEDULED;

    @Override
    public String toString(){
        return this.name().toLowerCase();
    }
}
