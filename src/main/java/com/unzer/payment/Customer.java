package com.unzer.payment;

import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.Locale;

/**
 * Business object for Customer together with billingAddress.
 * <p>
 * firstname and lastname are mandatory to create a new Customer.
 *
 * @author Unzer E-Com GmbH
 */
public class Customer extends BaseResource {
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
    private ShippingAddress shippingAddress;
    private String company;

    /**
     * Customer language.
     * <p>
     * Mandatory depending on payment type
     * Format: ISO 639 alpha-2 code
     */
    private Locale language;

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

    public ShippingAddress getShippingAddress() {
        return shippingAddress;
    }

    public Customer setShippingAddress(ShippingAddress shippingAddress) {
        this.shippingAddress = shippingAddress;
        return this;
    }

    /**
     * @deprecated use {@link #setShippingAddress(ShippingAddress)} instead
     */
    @Deprecated
    public Customer setShippingAddress(Address shippingAddress) {
        this.shippingAddress = ShippingAddress.of(shippingAddress, null);
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

    public Customer setCompany(String company) {
        this.company = company;
        return this;
    }

    @Override
    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Locale getLanguage() {
        return language;
    }

    public Customer setLanguage(Locale language) {
        this.language = language;
        return this;
    }

    @Override
    public String getResourceUrl() {
        return "/v1/customers/<resourceId>";
    }

    public enum Salutation {
        @SerializedName("mr")
        MR,
        @SerializedName("mrs")
        MRS,
        @SerializedName("unknown")
        UNKNOWN
    }
}
