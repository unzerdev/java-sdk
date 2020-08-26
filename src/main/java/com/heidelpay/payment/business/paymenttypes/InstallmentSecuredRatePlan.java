package com.heidelpay.payment.business.paymenttypes;

/*-
 * #%L
 * Heidelpay Java SDK
 * %%
 * Copyright (C) 2018 - 2019 Heidelpay GmbH
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import com.heidelpay.payment.Authorization;
import com.heidelpay.payment.communication.HttpCommunicationException;
import com.heidelpay.payment.communication.JsonDateConverter;
import com.heidelpay.payment.communication.json.JsonInstallmentSecuredRatePlan;
import com.heidelpay.payment.communication.json.JsonObject;
import com.heidelpay.payment.paymenttypes.AbstractPaymentType;
import com.heidelpay.payment.paymenttypes.PaymentType;

import java.math.BigDecimal;
import java.net.URL;
import java.util.ArrayList;
import java.util.Currency;
import java.util.Date;
import java.util.List;

public class InstallmentSecuredRatePlan extends AbstractPaymentType implements PaymentType, JsonObject {

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
	public String getTypeUrl() {
		return "types/installment-secured";
	}

	@Override
	public PaymentType map(PaymentType paymentType, JsonObject jsonPaymentType) {
		if(paymentType instanceof InstallmentSecuredRatePlan && jsonPaymentType instanceof JsonInstallmentSecuredRatePlan) {
			((InstallmentSecuredRatePlan) paymentType).setAccountHolder(((JsonInstallmentSecuredRatePlan) jsonPaymentType).getAccountHolder());
			((InstallmentSecuredRatePlan) paymentType).setBic(((JsonInstallmentSecuredRatePlan) jsonPaymentType).getBic());
			((InstallmentSecuredRatePlan) paymentType).setEffectiveInterestRate(((JsonInstallmentSecuredRatePlan) jsonPaymentType).getEffectiveInterestRate());
			((InstallmentSecuredRatePlan) paymentType).setFeeFirstRate(((JsonInstallmentSecuredRatePlan) jsonPaymentType).getFeeFirstRate());
			((InstallmentSecuredRatePlan) paymentType).setFeePerRate(((JsonInstallmentSecuredRatePlan) jsonPaymentType).getFeePerRate());
			((InstallmentSecuredRatePlan) paymentType).setIban(((JsonInstallmentSecuredRatePlan) jsonPaymentType).getIban());
			((InstallmentSecuredRatePlan) paymentType).setId(jsonPaymentType.getId());
			((InstallmentSecuredRatePlan) paymentType).setInvoiceDate(((JsonInstallmentSecuredRatePlan) jsonPaymentType).getInvoiceDate());
			((InstallmentSecuredRatePlan) paymentType).setInvoiceDueDate(((JsonInstallmentSecuredRatePlan) jsonPaymentType).getInvoiceDueDate());
			((InstallmentSecuredRatePlan) paymentType).setLastRate(((JsonInstallmentSecuredRatePlan) jsonPaymentType).getLastRate());
			((InstallmentSecuredRatePlan) paymentType).setMonthlyRate(((JsonInstallmentSecuredRatePlan) jsonPaymentType).getMonthlyRate());
			((InstallmentSecuredRatePlan) paymentType).setNominalInterestRate(((JsonInstallmentSecuredRatePlan) jsonPaymentType).getNominalInterestRate());
			((InstallmentSecuredRatePlan) paymentType).setNumberOfRates(((JsonInstallmentSecuredRatePlan) jsonPaymentType).getNumberOfRates());
			((InstallmentSecuredRatePlan) paymentType).setOrderDate(((JsonInstallmentSecuredRatePlan) jsonPaymentType).getOrderDate());
			((InstallmentSecuredRatePlan) paymentType).setRateList(((JsonInstallmentSecuredRatePlan) jsonPaymentType).getRateList());
			((InstallmentSecuredRatePlan) paymentType).setRecurring(((JsonInstallmentSecuredRatePlan) jsonPaymentType).getRecurring());
			((InstallmentSecuredRatePlan) paymentType).setTotalAmount(((JsonInstallmentSecuredRatePlan) jsonPaymentType).getTotalAmount());
			((InstallmentSecuredRatePlan) paymentType).setTotalInterestAmount(((JsonInstallmentSecuredRatePlan) jsonPaymentType).getTotalInterestAmount());
			((InstallmentSecuredRatePlan) paymentType).setTotalPurchaseAmount(((JsonInstallmentSecuredRatePlan) jsonPaymentType).getTotalPurchaseAmount());
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
	
	public Authorization authorize(BigDecimal amount, Currency currency, URL returnUrl, String customerId, String basketId, BigDecimal effectiveInterestRate) throws HttpCommunicationException {
		return getHeidelpay().authorize(getAuthorization(amount, currency, returnUrl, customerId, basketId, effectiveInterestRate));
	}
	private Authorization getAuthorization(BigDecimal amount, Currency currency, URL returnUrl, String customerId, String basketId, BigDecimal effectiveInterestRate) {
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
