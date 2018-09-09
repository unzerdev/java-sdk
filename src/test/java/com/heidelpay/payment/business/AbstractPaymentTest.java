package com.heidelpay.payment.business;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Currency;
import java.util.Date;
import java.util.UUID;

import com.heidelpay.payment.Address;
import com.heidelpay.payment.Authorization;
import com.heidelpay.payment.Cancel;
import com.heidelpay.payment.Charge;
import com.heidelpay.payment.Customer;
import com.heidelpay.payment.Heidelpay;
import com.heidelpay.payment.Processing;
import com.heidelpay.payment.Customer.Salutation;
import com.heidelpay.payment.communication.HttpCommunicationException;
import com.heidelpay.payment.paymenttypes.Card;

public class AbstractPaymentTest {
	private Heidelpay heidelpay = new Heidelpay("s-priv-6S59Dt6Q9mJYj8X5qpcxSpA3XLXUw4Zf");

	public Heidelpay getHeidelpay() {
		return heidelpay;
	}

	protected Authorization getAuthorization(String typeId) throws MalformedURLException {
		Authorization authorization = new Authorization();
		authorization
		.setAmount(new BigDecimal(10))
		.setCurrency(Currency.getInstance("EUR"))
		.setTypeId(typeId)
		.setReturnUrl(new URL("https://www.heidelpay.com"));
		return authorization;
	}
	protected Card createPaymentType() throws HttpCommunicationException {
		Card card = new Card("4444333322221111", "03/20");
		card.setCvc("123");
		card = (Card)getHeidelpay().createPaymentType(card);
		return card;
	}

	protected String getRandomId() {
		return UUID.randomUUID().toString();
	}

	protected Customer getMinimumCustomer() {
		return new Customer("Rene", "Felder"); 
	}

	protected Customer getMaximumCustomer(String customerId) throws ParseException {
		Customer customer = new Customer("Rene", "Felder");
		customer
		.setCustomerId(customerId)
		.setSalutation(Salutation.mr)
		.setEmail("rene.felder@felderit.at")
		.setMobile("+43676123456")
		.setBirthDate(getDate("03.10.1974"))
		.setAddress(getAddress());
		return customer;
	}

	private Address getAddress() {
		Address address = new Address();
		address
		.setName("Mozart")
		.setStreet("Grüngasse 16")
		.setCity("Vienna")
// need to deactivate until Bug AHC-264 is fixed
//		.setState("Vienna")
		.setZip("1010")
		.setCountry("AT");
		return address;
	}

	private Date getDate(String date) throws ParseException {
		return new SimpleDateFormat("dd.MM.yy").parse(date);
	}

	protected void assertChargeEquals(Charge initCharge, Charge charge) {
		assertEquals(initCharge.getAmount(), charge.getAmount());
		assertEquals(initCharge.getCurrency(), charge.getCurrency());
		assertEquals(initCharge.getCustomerId(), charge.getCustomerId());
		assertEquals(initCharge.getId(), charge.getId());
		assertEquals(initCharge.getPaymentId(), charge.getPaymentId());
		assertEquals(initCharge.getReturnUrl(), charge.getReturnUrl());
		assertEquals(initCharge.getRiskId(), charge.getRiskId());
		assertEquals(initCharge.getTypeId(), charge.getTypeId());
		assertEquals(initCharge.getTypeUrl(), charge.getTypeUrl());
		assertEquals(initCharge.getCancelList(), charge.getCancelList());
		assertProcessingEquals(initCharge.getProcessing(), charge.getProcessing());
	}

	private void assertProcessingEquals(Processing initProcessing, Processing processing) {
		assertEquals(initProcessing.getShortId(), processing.getShortId());
		assertEquals(initProcessing.getUniqueId(), processing.getUniqueId());
	}
	
	protected void assertCustomerEquals(Customer customerExpected, Customer customer) {
		assertEquals(customerExpected.getFirstname(), customer.getFirstname());
		assertEquals(customerExpected.getLastname(), customer.getLastname());
		assertEquals(customerExpected.getCustomerId(), customer.getCustomerId());
		// Deactivated until Bug  AHC-268 is fixed
//		assertEquals(customerExpected.getBirthDate(), customer.getBirthDate());
		assertEquals(customerExpected.getEmail(), customer.getEmail());
		assertEquals(customerExpected.getMobile(), customer.getMobile());
		assertEquals(customerExpected.getPhone(), customer.getPhone());
		assertAddressEquals(customerExpected.getAddress(), customer.getAddress());		
	}
	protected void assertAddressEquals(Address addressExpected, Address address) {
		if (addressExpected == null) return;
		assertEquals(addressExpected.getCity(), address.getCity());
		assertEquals(addressExpected.getCountry(), address.getCountry());
		assertEquals(addressExpected.getName(), address.getName());
		assertEquals(addressExpected.getState(), address.getState());
		assertEquals(addressExpected.getStreet(), address.getStreet());
		assertEquals(addressExpected.getZip(), address.getZip());
	}

	protected void assertCancelEquals(Cancel cancelInit, Cancel cancel) {
		assertEquals(cancelInit.getAmount(), cancel.getAmount());
		assertEquals(cancelInit.getId(), cancel.getId());
		assertEquals(cancelInit.getTypeUrl(), cancel.getTypeUrl());
	}


	protected BigDecimal getBigDecimal(String number) {
		BigDecimal bigDecimal = new BigDecimal(number);
		bigDecimal.setScale(4);
		return bigDecimal;
	}



}
