package com.heidelpay.payment;

/*-
 * #%L
 * Heidelpay Java SDK
 * %%
 * Copyright (C) 2018 Heidelpay GmbH
 * %%
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 * #L%
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
