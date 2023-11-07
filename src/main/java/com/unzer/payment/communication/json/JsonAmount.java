package com.unzer.payment.communication.json;

import java.math.BigDecimal;

public class JsonAmount {
    private BigDecimal total;
    private BigDecimal charged;
    private BigDecimal canceled;
    private BigDecimal remaining;

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public BigDecimal getCharged() {
        return charged;
    }

    public void setCharged(BigDecimal charged) {
        this.charged = charged;
    }

    public BigDecimal getCanceled() {
        return canceled;
    }

    public void setCanceled(BigDecimal canceled) {
        this.canceled = canceled;
    }

    public BigDecimal getRemaining() {
        return remaining;
    }

    public void setRemaining(BigDecimal remaining) {
        this.remaining = remaining;
    }

}
