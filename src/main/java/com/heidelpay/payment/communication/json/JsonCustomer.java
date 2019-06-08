package com.heidelpay.payment.communication.json;

import java.util.Date;

import com.heidelpay.payment.Address;
import com.heidelpay.payment.Customer.Salutation;

public class JsonCustomer extends JsonIdObject implements JsonObject {
	private String firstname;
	private String lastname;
	private String company;
	private Salutation salutation;
	private String customerId;
	private Date birthDate;
	private String email;
	private String phone;
	private String mobile;
	private Address billingAddress;
	private Address shippingAddress;

	private JSonCompanyInfo companyInfo;
	
	public JsonCustomer() {
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public Salutation getSalutation() {
		return salutation;
	}

	public void setSalutation(Salutation salutation) {
		this.salutation = salutation;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public Date getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public Address getBillingAddress() {
		return billingAddress;
	}

	public void setBillingAddress(Address billingAddress) {
		this.billingAddress = billingAddress;
	}

	public Address getShippingAddress() {
		return shippingAddress;
	}

	public void setShippingAddress(Address shippingAddress) {
		this.shippingAddress = shippingAddress;
	}

	public JSonCompanyInfo getCompanyInfo() {
		return companyInfo;
	}

	public void setCompanyInfo(JSonCompanyInfo companyInfo) {
		this.companyInfo = companyInfo;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

}
