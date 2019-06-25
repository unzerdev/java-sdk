package com.heidelpay.payment.business.paymenttypes;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.gson.annotations.SerializedName;

public class HirePurchaseRatePlan {

	private int numberOfRates;
	
	@SerializedName("dayOfPurchase")
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
	public List<HirePurchaseRate> getRateList() {
		return rateList;
	}
	public void setRateList(List<HirePurchaseRate> rateList) {
		this.rateList = rateList;
	}
	
}
