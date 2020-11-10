package com.unzer.payment;

/*-
 * #%L
 * Unzer Java SDK
 * %%
 * Copyright (C) 2020 Unzer E-Com GmbH
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

public class GeoLocation {
	private String clientIp;
	private String countryIsoA2;

	public GeoLocation(String clientIp, String countryIsoA2) {
		this.clientIp = clientIp;
		this.countryIsoA2 = countryIsoA2;
	}

	public String getClientIp() {
		return clientIp;
	}

	public GeoLocation setClientIp(String clientIp) {
		this.clientIp = clientIp;
		return this;
	}

	public String getCountryIsoA2() {
		return countryIsoA2;
	}

	public GeoLocation setCountryIsoA2(String countryIsoA2) {
		this.countryIsoA2 = countryIsoA2;
		return this;
	}
}
