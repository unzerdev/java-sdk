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
	public Heidelpay getHeidelpay(String key) {
		return new Heidelpay(key);
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
	protected Card createPaymentTypeCard() throws HttpCommunicationException {
		Card card = getPaymentTypeCard();
		card = (Card)getHeidelpay().createPaymentType(card);
		return card;
	}
	protected Card getPaymentTypeCard() {
		Card card = new Card("4444333322221111", "03/20");
		card.setCvc("123");
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
		.setBillingAddress(getAddress());
		return customer;
	}

	private Address getAddress() {
		Address address = new Address();
		address
		.setName("Mozart")
		.setStreet("Grüngasse 16")
		.setCity("Vienna")
		.setState("AT-1")
		.setZip("1010")
		.setCountry("AT");
		return address;
	}

	protected Date getDate(String date) throws ParseException {
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
		assertAddressEquals(customerExpected.getBillingAddress(), customer.getBillingAddress());		
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

	protected String maskIban(String text) {
		return maskString(text, 6, text.length()-4, '*');
	}

	protected static String maskString(String strText, int start, int end, char maskChar) {

		if (strText == null) return null;
		if (strText.equals("")) return "";
		if (start < 0) start = 0;
		if (end > strText.length()) end = strText.length();

		int maskLength = end - start;

		if (maskLength == 0) return strText;

		StringBuilder sbMaskString = new StringBuilder(maskLength);

		for (int i = 0; i < maskLength; i++) {
			sbMaskString.append(maskChar);
		}

		return strText.substring(0, start) + sbMaskString.toString() + strText.substring(start + maskLength);
	}

	protected Charge getCharge() throws MalformedURLException, HttpCommunicationException {
		Charge charge = new Charge();
		charge.setAmount(BigDecimal.ONE)
		.setCurrency(Currency.getInstance("EUR"))
		.setTypeId(createPaymentTypeCard().getId())
		.setReturnUrl(new URL("https://www.google.at"));
		return charge;
	}

}
