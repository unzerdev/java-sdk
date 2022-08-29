package com.unzer.payment;

import com.unzer.payment.communication.json.JsonObject;
import com.unzer.payment.paymenttypes.PaymentType;

import java.util.Date;

/**
 * Business object for Customer together with billingAddress.
 * 
 * firstname and lastname are mandatory to create a new Customer.
 *
 * @author Unzer E-Com GmbH
 */
public class Customer implements PaymentType {
    private String id;
    private String firstname;
    private String lastname;
    private Salutation salutation;
    private String customerId;
    private Date birthDate;
    private String email;
    private String phone;
    private String mobile;
    private Address billingAddress;
    private Address shippingAddress;
    private String company;
    private CustomerCompanyData companyData;

    public Customer(String firstname, String lastname) {
        super();
        this.firstname = firstname;
        this.lastname = lastname;
    }

    public Customer(String company) {
        super();
        this.setCompany(company);
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

    @Override
    public PaymentType map(PaymentType paymentType, JsonObject jsonObject) {
        return null;
    }

    public Address getShippingAddress() {
        return shippingAddress;
    }

    public Customer setShippingAddress(Address shippingAddress) {
        this.shippingAddress = shippingAddress;
        return this;
    }

    public CustomerCompanyData getCompanyData() {
        return companyData;
    }

    public void setCompanyData(CustomerCompanyData companyData) {
        this.companyData = companyData;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    @Override
    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public enum Salutation {
        MR, MRS, UNKNOWN
    }
}
