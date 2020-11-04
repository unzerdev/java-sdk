package com.unzer.payment.business.paymenttypes;

/*-
 * #%L
 * Unzer Java SDK
 * %%
 * Copyright (C) 2018 - 2019 Unzer GmbH
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

import java.math.BigDecimal;
import java.net.URL;
import java.util.ArrayList;
import java.util.Currency;
import java.util.Date;
import java.util.List;

import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import com.unzer.payment.Authorization;
import com.unzer.payment.communication.HttpCommunicationException;
import com.unzer.payment.communication.JsonDateConverter;
import com.unzer.payment.communication.json.JsonHirePurchaseRatePlan;
import com.unzer.payment.communication.json.JsonObject;
import com.unzer.payment.paymenttypes.AbstractPaymentType;
import com.unzer.payment.paymenttypes.PaymentType;

/**
 * @deprecated use {@code InstallmentSecuredRatePlan} as a default implementation.
 */
@Deprecated
public class HirePurchaseRatePlan extends AbstractPaymentType implements PaymentType, JsonObject {

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
	private List<HirePurchaseRate> rateList = new ArrayList<HirePurchaseRate>();
	@Deprecated
	public int getNumberOfRates() {
		return numberOfRates;
	}
	@Deprecated
	public void setNumberOfRates(int numberOfRates) {
		this.numberOfRates = numberOfRates;
	}
	@Deprecated
	public Date getOrderDate() {
		return orderDate;
	}
	@Deprecated
	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
	}
	@Deprecated
	public BigDecimal getTotalPurchaseAmount() {
		return totalPurchaseAmount;
	}
	@Deprecated
	public void setTotalPurchaseAmount(BigDecimal totalPurchaseAmount) {
		this.totalPurchaseAmount = totalPurchaseAmount;
	}
	@Deprecated
	public BigDecimal getTotalInterestAmount() {
		return totalInterestAmount;
	}
	@Deprecated
	public void setTotalInterestAmount(BigDecimal totalInterestAmount) {
		this.totalInterestAmount = totalInterestAmount;
	}
	@Deprecated
	public BigDecimal getTotalAmount() {
		return totalAmount;
	}
	@Deprecated
	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}
	@Deprecated
	public BigDecimal getEffectiveInterestRate() {
		return effectiveInterestRate;
	}
	@Deprecated
	public void setEffectiveInterestRate(BigDecimal effectiveInterestRate) {
		this.effectiveInterestRate = effectiveInterestRate;
	}
	@Deprecated
	public BigDecimal getNominalInterestRate() {
		return nominalInterestRate;
	}
	@Deprecated
	public void setNominalInterestRate(BigDecimal nominalInterestRate) {
		this.nominalInterestRate = nominalInterestRate;
	}
	@Deprecated
	public BigDecimal getFeeFirstRate() {
		return feeFirstRate;
	}
	@Deprecated
	public void setFeeFirstRate(BigDecimal feeFirstRate) {
		this.feeFirstRate = feeFirstRate;
	}
	@Deprecated
	public BigDecimal getFeePerRate() {
		return feePerRate;
	}
	@Deprecated
	public void setFeePerRate(BigDecimal feePerRate) {
		this.feePerRate = feePerRate;
	}
	@Deprecated
	public BigDecimal getMonthlyRate() {
		return monthlyRate;
	}
	@Deprecated
	public void setMonthlyRate(BigDecimal monthlyRate) {
		this.monthlyRate = monthlyRate;
	}
	@Deprecated
	public BigDecimal getLastRate() {
		return lastRate;
	}
	@Deprecated
	public void setLastRate(BigDecimal lastRate) {
		this.lastRate = lastRate;
	}
	@Deprecated
	public List<HirePurchaseRate> getRateList() {
		return rateList;
	}
	@Deprecated
	public void setRateList(List<HirePurchaseRate> rateList) {
		this.rateList = rateList;
	}
	@Deprecated
	@Override
	public String getTypeUrl() {
		return "types/hire-purchase-direct-debit";
	}
	@Deprecated
	@Override
	public PaymentType map(PaymentType paymentType, JsonObject jsonPaymentType) {
		((HirePurchaseRatePlan) paymentType).setAccountHolder(((JsonHirePurchaseRatePlan) jsonPaymentType).getAccountHolder());
		((HirePurchaseRatePlan) paymentType).setBic(((JsonHirePurchaseRatePlan) jsonPaymentType).getBic());
		((HirePurchaseRatePlan) paymentType).setEffectiveInterestRate(((JsonHirePurchaseRatePlan) jsonPaymentType).getEffectiveInterestRate());
		((HirePurchaseRatePlan) paymentType).setFeeFirstRate(((JsonHirePurchaseRatePlan) jsonPaymentType).getFeeFirstRate());
		((HirePurchaseRatePlan) paymentType).setFeePerRate(((JsonHirePurchaseRatePlan) jsonPaymentType).getFeePerRate());
		((HirePurchaseRatePlan) paymentType).setIban(((JsonHirePurchaseRatePlan) jsonPaymentType).getIban());
		((HirePurchaseRatePlan) paymentType).setId(jsonPaymentType.getId());
		((HirePurchaseRatePlan) paymentType).setInvoiceDate(((JsonHirePurchaseRatePlan) jsonPaymentType).getInvoiceDate());
		((HirePurchaseRatePlan) paymentType).setInvoiceDueDate(((JsonHirePurchaseRatePlan) jsonPaymentType).getInvoiceDueDate());
		((HirePurchaseRatePlan) paymentType).setLastRate(((JsonHirePurchaseRatePlan) jsonPaymentType).getLastRate());
		((HirePurchaseRatePlan) paymentType).setMonthlyRate(((JsonHirePurchaseRatePlan) jsonPaymentType).getMonthlyRate());
		((HirePurchaseRatePlan) paymentType).setNominalInterestRate(((JsonHirePurchaseRatePlan) jsonPaymentType).getNominalInterestRate());
		((HirePurchaseRatePlan) paymentType).setNumberOfRates(((JsonHirePurchaseRatePlan) jsonPaymentType).getNumberOfRates());
		((HirePurchaseRatePlan) paymentType).setOrderDate(((JsonHirePurchaseRatePlan) jsonPaymentType).getOrderDate());
		((HirePurchaseRatePlan) paymentType).setRateList(((JsonHirePurchaseRatePlan) jsonPaymentType).getRateList());
		((HirePurchaseRatePlan) paymentType).setRecurring(((JsonHirePurchaseRatePlan) jsonPaymentType).getRecurring());
		((HirePurchaseRatePlan) paymentType).setTotalAmount(((JsonHirePurchaseRatePlan) jsonPaymentType).getTotalAmount());
		((HirePurchaseRatePlan) paymentType).setTotalInterestAmount(((JsonHirePurchaseRatePlan) jsonPaymentType).getTotalInterestAmount());
		((HirePurchaseRatePlan) paymentType).setTotalPurchaseAmount(((JsonHirePurchaseRatePlan) jsonPaymentType).getTotalPurchaseAmount());
		return paymentType;
	}
	@Deprecated
	public String getIban() {
		return iban;
	}
	@Deprecated
	public void setIban(String iban) {
		this.iban = iban;
	}
	@Deprecated
	public String getBic() {
		return bic;
	}
	@Deprecated
	public void setBic(String bic) {
		this.bic = bic;
	}
	@Deprecated
	public String getAccountHolder() {
		return accountHolder;
	}
	@Deprecated
	public void setAccountHolder(String accountHolder) {
		this.accountHolder = accountHolder;
	}
	@Deprecated
	public Date getInvoiceDate() {
		return invoiceDate;
	}
	@Deprecated
	public void setInvoiceDate(Date invoiceDate) {
		this.invoiceDate = invoiceDate;
	}
	@Deprecated
	public Date getInvoiceDueDate() {
		return invoiceDueDate;
	}
	@Deprecated
	public void setInvoiceDueDate(Date invoiceDueDate) {
		this.invoiceDueDate = invoiceDueDate;
	}
	@Deprecated
	public Authorization authorize(BigDecimal amount, Currency currency, URL returnUrl, String customerId, String basketId, BigDecimal effectiveInterestRate) throws HttpCommunicationException {
		return getUnzer().authorize(getAuthorization(amount, currency, returnUrl, customerId, basketId, effectiveInterestRate));
	}
	@Deprecated
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
