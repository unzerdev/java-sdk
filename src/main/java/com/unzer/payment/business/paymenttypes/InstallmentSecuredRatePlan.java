package com.unzer.payment.business.paymenttypes;

import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import com.unzer.payment.Authorization;
import com.unzer.payment.communication.HttpCommunicationException;
import com.unzer.payment.communication.JsonDateConverter;
import com.unzer.payment.communication.json.ApiInstallmentSecuredRatePlan;
import com.unzer.payment.communication.json.ApiObject;
import com.unzer.payment.paymenttypes.BasePaymentType;
import com.unzer.payment.paymenttypes.PaylaterInstallment;
import com.unzer.payment.paymenttypes.PaymentType;

import java.math.BigDecimal;
import java.net.URL;
import java.util.ArrayList;
import java.util.Currency;
import java.util.Date;
import java.util.List;

/**
 * @deprecated Will be replaced by {@link PaylaterInstallment} in the future.
 */
@Deprecated
public class InstallmentSecuredRatePlan extends BasePaymentType implements ApiObject {

    private String iban;
    private String bic;
    private String accountHolder;
    @JsonAdapter(JsonDateConverter.class)
    private Date invoiceDate;
    @JsonAdapter(JsonDateConverter.class)
    private Date invoiceDueDate;

    private int numberOfRates;
    @SerializedName("dayOfPurchase")
    @JsonAdapter(JsonDateConverter.class)
    private Date orderDate;

    private BigDecimal totalPurchaseAmount;
    private BigDecimal totalInterestAmount;
    private BigDecimal totalAmount;
    private BigDecimal effectiveInterestRate;
    private BigDecimal nominalInterestRate;
    private BigDecimal feeFirstRate;
    private BigDecimal feePerRate;
    private BigDecimal monthlyRate;
    private BigDecimal lastRate;
    private List<InstallmentSecuredRate> rateList = new ArrayList<InstallmentSecuredRate>();

    public int getNumberOfRates() {
        return numberOfRates;
    }

    public void setNumberOfRates(int numberOfRates) {
        this.numberOfRates = numberOfRates;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public BigDecimal getTotalPurchaseAmount() {
        return totalPurchaseAmount;
    }

    public void setTotalPurchaseAmount(BigDecimal totalPurchaseAmount) {
        this.totalPurchaseAmount = totalPurchaseAmount;
    }

    public BigDecimal getTotalInterestAmount() {
        return totalInterestAmount;
    }

    public void setTotalInterestAmount(BigDecimal totalInterestAmount) {
        this.totalInterestAmount = totalInterestAmount;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public BigDecimal getEffectiveInterestRate() {
        return effectiveInterestRate;
    }

    public void setEffectiveInterestRate(BigDecimal effectiveInterestRate) {
        this.effectiveInterestRate = effectiveInterestRate;
    }

    public BigDecimal getNominalInterestRate() {
        return nominalInterestRate;
    }

    public void setNominalInterestRate(BigDecimal nominalInterestRate) {
        this.nominalInterestRate = nominalInterestRate;
    }

    public BigDecimal getFeeFirstRate() {
        return feeFirstRate;
    }

    public void setFeeFirstRate(BigDecimal feeFirstRate) {
        this.feeFirstRate = feeFirstRate;
    }

    public BigDecimal getFeePerRate() {
        return feePerRate;
    }

    public void setFeePerRate(BigDecimal feePerRate) {
        this.feePerRate = feePerRate;
    }

    public BigDecimal getMonthlyRate() {
        return monthlyRate;
    }

    public void setMonthlyRate(BigDecimal monthlyRate) {
        this.monthlyRate = monthlyRate;
    }

    public BigDecimal getLastRate() {
        return lastRate;
    }

    public void setLastRate(BigDecimal lastRate) {
        this.lastRate = lastRate;
    }

    public List<InstallmentSecuredRate> getRateList() {
        return rateList;
    }

    public void setRateList(List<InstallmentSecuredRate> rateList) {
        this.rateList = rateList;
    }

    @Override
    protected String getResourceUrl() {
        return "/v1/types/installment-secured/<resourceId>";
    }

    @Override
    public PaymentType map(PaymentType paymentType, ApiObject jsonPaymentType) {
        if (paymentType instanceof InstallmentSecuredRatePlan
                && jsonPaymentType instanceof ApiInstallmentSecuredRatePlan) {
            ((InstallmentSecuredRatePlan) paymentType).setAccountHolder(
                    ((ApiInstallmentSecuredRatePlan) jsonPaymentType).getAccountHolder());
            ((InstallmentSecuredRatePlan) paymentType).setBic(
                    ((ApiInstallmentSecuredRatePlan) jsonPaymentType).getBic());
            ((InstallmentSecuredRatePlan) paymentType).setEffectiveInterestRate(
                    ((ApiInstallmentSecuredRatePlan) jsonPaymentType).getEffectiveInterestRate());
            ((InstallmentSecuredRatePlan) paymentType).setFeeFirstRate(
                    ((ApiInstallmentSecuredRatePlan) jsonPaymentType).getFeeFirstRate());
            ((InstallmentSecuredRatePlan) paymentType).setFeePerRate(
                    ((ApiInstallmentSecuredRatePlan) jsonPaymentType).getFeePerRate());
            ((InstallmentSecuredRatePlan) paymentType).setIban(
                    ((ApiInstallmentSecuredRatePlan) jsonPaymentType).getIban());
            ((InstallmentSecuredRatePlan) paymentType).setId(jsonPaymentType.getId());
            ((InstallmentSecuredRatePlan) paymentType).setInvoiceDate(
                    ((ApiInstallmentSecuredRatePlan) jsonPaymentType).getInvoiceDate());
            ((InstallmentSecuredRatePlan) paymentType).setInvoiceDueDate(
                    ((ApiInstallmentSecuredRatePlan) jsonPaymentType).getInvoiceDueDate());
            ((InstallmentSecuredRatePlan) paymentType).setLastRate(
                    ((ApiInstallmentSecuredRatePlan) jsonPaymentType).getLastRate());
            ((InstallmentSecuredRatePlan) paymentType).setMonthlyRate(
                    ((ApiInstallmentSecuredRatePlan) jsonPaymentType).getMonthlyRate());
            ((InstallmentSecuredRatePlan) paymentType).setNominalInterestRate(
                    ((ApiInstallmentSecuredRatePlan) jsonPaymentType).getNominalInterestRate());
            ((InstallmentSecuredRatePlan) paymentType).setNumberOfRates(
                    ((ApiInstallmentSecuredRatePlan) jsonPaymentType).getNumberOfRates());
            ((InstallmentSecuredRatePlan) paymentType).setOrderDate(
                    ((ApiInstallmentSecuredRatePlan) jsonPaymentType).getOrderDate());
            ((InstallmentSecuredRatePlan) paymentType).setRateList(
                    ((ApiInstallmentSecuredRatePlan) jsonPaymentType).getRateList());
            ((InstallmentSecuredRatePlan) paymentType).setRecurring(
                    ((ApiInstallmentSecuredRatePlan) jsonPaymentType).getRecurring());
            ((InstallmentSecuredRatePlan) paymentType).setTotalAmount(
                    ((ApiInstallmentSecuredRatePlan) jsonPaymentType).getTotalAmount());
            ((InstallmentSecuredRatePlan) paymentType).setTotalInterestAmount(
                    ((ApiInstallmentSecuredRatePlan) jsonPaymentType).getTotalInterestAmount());
            ((InstallmentSecuredRatePlan) paymentType).setTotalPurchaseAmount(
                    ((ApiInstallmentSecuredRatePlan) jsonPaymentType).getTotalPurchaseAmount());
        }
        return paymentType;
    }

    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public String getBic() {
        return bic;
    }

    public void setBic(String bic) {
        this.bic = bic;
    }

    public String getAccountHolder() {
        return accountHolder;
    }

    public void setAccountHolder(String accountHolder) {
        this.accountHolder = accountHolder;
    }

    public Date getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(Date invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public Date getInvoiceDueDate() {
        return invoiceDueDate;
    }

    public void setInvoiceDueDate(Date invoiceDueDate) {
        this.invoiceDueDate = invoiceDueDate;
    }

    public Authorization authorize(BigDecimal amount, Currency currency, URL returnUrl,
                                   String customerId, String basketId,
                                   BigDecimal effectiveInterestRate)
            throws HttpCommunicationException {
        return getUnzer().authorize(
                getAuthorization(amount, currency, returnUrl, customerId, basketId, effectiveInterestRate));
    }

    private Authorization getAuthorization(BigDecimal amount, Currency currency, URL returnUrl,
                                           String customerId, String basketId,
                                           BigDecimal effectiveInterestRate) {
        Authorization authorization = new Authorization();
        authorization.setAmount(amount);
        authorization.setCurrency(currency);
        authorization.setReturnUrl(returnUrl);
        authorization.setCustomerId(customerId);
        authorization.setBasketId(basketId);
        authorization.setTypeId(this.getId());
        authorization.setEffectiveInterestRate(effectiveInterestRate);
        return authorization;
    }
}
