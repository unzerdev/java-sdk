package com.heidelpay.payment.business;

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
import com.heidelpay.payment.Customer;
import com.heidelpay.payment.Heidelpay;
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

}
