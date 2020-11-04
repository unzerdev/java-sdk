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

/**
 * @deprecated use {@code InstallmentSecuredRate} as a default implementation.
 */
@Deprecated
public class HirePurchaseRate {
	private BigDecimal amountOfRepayment;
	private BigDecimal rate;
	private BigDecimal totalRemainingAmount;
	private String type;
	private Integer rateIndex;
	private Boolean ultimo;

	@Deprecated
	public BigDecimal getAmountOfRepayment() {
		return amountOfRepayment;
	}
	@Deprecated
	public void setAmountOfRepayment(BigDecimal amountOfRepayment) {
		this.amountOfRepayment = amountOfRepayment;
	}
	@Deprecated
	public BigDecimal getRate() {
		return rate;
	}
	@Deprecated
	public void setRate(BigDecimal rate) {
		this.rate = rate;
	}
	@Deprecated
	public BigDecimal getTotalRemainingAmount() {
		return totalRemainingAmount;
	}
	@Deprecated
	public void setTotalRemainingAmount(BigDecimal totalRemainingAmount) {
		this.totalRemainingAmount = totalRemainingAmount;
	}
	@Deprecated
	public String getType() {
		return type;
	}
	@Deprecated
	public void setType(String type) {
		this.type = type;
	}
	@Deprecated
	public Integer getRateIndex() {
		return rateIndex;
	}
	@Deprecated
	public void setRateIndex(Integer rateIndex) {
		this.rateIndex = rateIndex;
	}
	@Deprecated
	public Boolean getUltimo() {
		return ultimo;
	}
	@Deprecated
	public void setUltimo(Boolean ultimo) {
		this.ultimo = ultimo;
	}
}
