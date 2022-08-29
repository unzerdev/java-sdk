package com.unzer.payment;

import com.unzer.payment.communication.json.JsonObject;
import com.unzer.payment.paymenttypes.PaymentType;

import java.math.BigDecimal;

/**
 * Business object for Cancellations
 *
 * @author Unzer E-Com GmbH
 *
 */
public class Cancel extends AbstractTransaction<Payment> {

    private BigDecimal amountGross;
    private BigDecimal amountNet;
    private BigDecimal amountVat;

    public Cancel() {
        super();
    }

    public Cancel(Unzer unzer) {
        super(unzer);
    }

    @Override
    public String getTypeUrl() {
        return "payments/<paymentId>/authorize/cancels";
    }

    @Override
    public PaymentType map(PaymentType paymentType, JsonObject jsonObject) {
        return null;
    }

    public BigDecimal getAmountGross() {
        return amountGross;
    }

    public void setAmountGross(BigDecimal amountGross) {
        this.amountGross = amountGross;
    }

    public BigDecimal getAmountNet() {
        return amountNet;
    }

    public void setAmountNet(BigDecimal amountNet) {
        this.amountNet = amountNet;
    }

    public BigDecimal getAmountVat() {
        return amountVat;
    }

    public void setAmountVat(BigDecimal amountVat) {
        this.amountVat = amountVat;
    }
}
