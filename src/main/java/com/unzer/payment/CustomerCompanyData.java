package com.unzer.payment;

/*-
 * #%L
 * Unzer Java SDK
 * %%
 * Copyright (C) 2020 - today Unzer E-Com GmbH
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

public class CustomerCompanyData {
	public enum RegistrationType {REGISTERED, NOT_REGISTERED}

	private RegistrationType registrationType;
	private String commercialRegisterNumber;
	private CommercialSector commercialSector;
	
	public RegistrationType getRegistrationType() {
		return registrationType;
	}

	public void setRegistrationType(RegistrationType registrationType) {
		this.registrationType = registrationType;
	}

	public String getCommercialRegisterNumber() {
		return commercialRegisterNumber;
	}

	public void setCommercialRegisterNumber(String commercialRegisterNumber) {
		this.commercialRegisterNumber = commercialRegisterNumber;
	}

	public CommercialSector getCommercialSector() {
		return commercialSector;
	}

	public void setCommercialSector(CommercialSector commercialSector) {
		this.commercialSector = commercialSector;
	}

}
