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

import java.math.BigDecimal;

public class InstallmentSecuredRate {
	private BigDecimal amountOfRepayment;
	private BigDecimal rate;
	private BigDecimal totalRemainingAmount;
	private String type;
	private Integer rateIndex;
	private Boolean ultimo;
	
	public BigDecimal getAmountOfRepayment() {
		return amountOfRepayment;
	}
	public void setAmountOfRepayment(BigDecimal amountOfRepayment) {
		this.amountOfRepayment = amountOfRepayment;
	}
	public BigDecimal getRate() {
		return rate;
	}
	public void setRate(BigDecimal rate) {
		this.rate = rate;
	}
	public BigDecimal getTotalRemainingAmount() {
		return totalRemainingAmount;
	}
	public void setTotalRemainingAmount(BigDecimal totalRemainingAmount) {
		this.totalRemainingAmount = totalRemainingAmount;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Integer getRateIndex() {
		return rateIndex;
	}
	public void setRateIndex(Integer rateIndex) {
		this.rateIndex = rateIndex;
	}
	public Boolean getUltimo() {
		return ultimo;
	}
	public void setUltimo(Boolean ultimo) {
		this.ultimo = ultimo;
	}
}
