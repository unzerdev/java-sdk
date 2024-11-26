package com.unzer.payment;

import java.math.BigDecimal;

/**
 * Business object for Cancellations
 *
 * @author Unzer E-Com GmbH
 */
public class Cancel extends BaseTransaction<Payment> {
    private final static String TRANSACTION_TYPE_TOKEN = "<transactionType>";

    private BigDecimal amountGross;
    private BigDecimal amountNet;
    private BigDecimal amountVat;
    private ReasonCode reasonCode;
    private String transactionType = TransactionType.AUTHORIZE;

    public Cancel() {
        super();
    }

    @Deprecated
    public Cancel(Unzer unzer) {
        super(unzer);
    }

    @Override
    public String getTransactionUrl() {
        return "/v1/payments/<paymentId>/<transactionType>/<transactionId>/cancels";
    }

    @Override
    public String getUrl() {
        String partialResult = getPaymentId() == null
                ? getTransactionUrl().replaceAll(PAYMENT_ID_TOKEN + "/", "")
                : getTransactionUrl().replaceAll(PAYMENT_ID_TOKEN, getPaymentId());

        return partialResult
                .replaceAll(TRANSACTION_TYPE_TOKEN, getTransactionType() == null ? TransactionType.AUTHORIZE : getTransactionType())
                .replaceAll(TRANSACTION_ID_TOKEN + "/", getId() == null ? "" : getId());
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

    public ReasonCode getReasonCode() {
        return reasonCode;
    }

    public Cancel setReasonCode(ReasonCode reasonCode) {
        this.reasonCode = reasonCode;
        return this;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public enum ReasonCode {
        CANCEL, RETURN, CREDIT
    }

    public interface TransactionType {
        String CHARGES = "charges";
        String AUTHORIZE = "authorize";
        String PREAUTHORIZE = "preauthorize";
    }
}
