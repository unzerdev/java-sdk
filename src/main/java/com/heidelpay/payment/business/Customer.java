package com.heidelpay.payment.business;

import java.util.Date;

public class Customer {
	public enum Salutation {mr, ms, unknown};

	private String firstname;
	private String lastname;
	private Salutation salutation;
	private String customerId;
	private Date birthDate;
	private String email;
	private String phone;
	private String mobile;
	private Address address;
	
	

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

	public Address getAddress() {
		return address;
	}

	public Customer setAddress(Address address) {
		this.address = address;
		return this;
	}
	
}
