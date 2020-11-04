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

/**
 * Business object for Address data
 * @author Unzer E-Com GmbH
 *
 */
public class Address {
	private String name;
	private String street;
	private String state;
	private String zip;
	private String city;
	private String country;
	public String getName() {
		return name;
	}
	public Address setName(String name) {
		this.name = name;
		return this;
	}
	public String getStreet() {
		return street;
	}
	public Address setStreet(String street) {
		this.street = street;
		return this;
	}
	public String getState() {
		return state;
	}
	public Address setState(String state) {
		this.state = state;
		return this;
	}
	public String getZip() {
		return zip;
	}
	public Address setZip(String zip) {
		this.zip = zip;
		return this;
	}
	public String getCity() {
		return city;
	}
	public Address setCity(String city) {
		this.city = city;
		return this;
	}
	public String getCountry() {
		return country;
	}
	public Address setCountry(String country) {
		this.country = country;
		return this;
	}


}
