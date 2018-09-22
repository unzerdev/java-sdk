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

import java.util.Date;

public class Customer extends AbstractPayment {
	public enum Salutation {mr, ms, unknown};

	private String firstname;
	private String lastname;
	private Salutation salutation;
	private String customerId;
	private Date birthDate;
	private String email;
	private String phone;
	private String mobile;
	private Address billingAddress;
	
	

	public Customer(String firstname, String lastname) {
		super();
		this.firstname = firstname;
		this.lastname = lastname;
	}

	public String getFirstname() {
		return firstname;
	}

	public Customer setFirstname(String firstname) {
		this.firstname = firstname;
		return this;
	}

	public String getLastname() {
		return lastname;
	}

	public Customer setLastname(String lastname) {
		this.lastname = lastname;
		return this;
	}

	public Salutation getSalutation() {
		return salutation;
	}

	public Customer setSalutation(Salutation salutation) {
		this.salutation = salutation;
		return this;
	}

	public String getCustomerId() {
		return customerId;
	}

	public Customer setCustomerId(String customerId) {
		this.customerId = customerId;
		return this;
	}

	public Date getBirthDate() {
		return birthDate;
	}

	public Customer setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
		return this;
	}

	public String getEmail() {
		return email;
	}

	public Customer setEmail(String email) {
		this.email = email;
		return this;
	}

	public String getPhone() {
		return phone;
	}

	public Customer setPhone(String phone) {
		this.phone = phone;
		return this;
	}

	public String getMobile() {
		return mobile;
	}

	public Customer setMobile(String mobile) {
		this.mobile = mobile;
		return this;
	}

	public Address getBillingAddress() {
		return billingAddress;
	}

	public Customer setBillingAddress(Address billingAddress) {
		this.billingAddress = billingAddress;
		return this;
	}

	@Override
	public String getTypeUrl() {
		return "customers";
	}
	
}
