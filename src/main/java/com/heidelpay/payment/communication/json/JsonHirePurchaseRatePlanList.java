package com.heidelpay.payment.communication.json;

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

import java.util.ArrayList;
import java.util.List;

import com.heidelpay.payment.business.paymenttypes.HirePurchaseRatePlan;

public class JsonHirePurchaseRatePlanList {
	private String code;
	private List<HirePurchaseRatePlan> entity = new ArrayList<HirePurchaseRatePlan>();
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public List<HirePurchaseRatePlan> getEntity() {
		return entity;
	}
	public void setEntity(List<HirePurchaseRatePlan> entity) {
		this.entity = entity;
	}
}