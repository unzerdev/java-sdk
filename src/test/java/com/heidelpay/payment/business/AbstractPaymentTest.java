package com.heidelpay.payment.business;

/*-
 * #%L
 * Heidelpay Java SDK
 * %%
 * Copyright (C) 2018 Heidelpay GmbH
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

import static org.junit.Assert.assertEquals;

import com.heidelpay.payment.Address;
import com.heidelpay.payment.Authorization;
import com.heidelpay.payment.Basket;
import com.heidelpay.payment.BasketItem;
import com.heidelpay.payment.Cancel;
import com.heidelpay.payment.Charge;
import com.heidelpay.payment.Customer;
import com.heidelpay.payment.Customer.Salutation;
import com.heidelpay.payment.CustomerCompanyData;
import com.heidelpay.payment.Heidelpay;
import com.heidelpay.payment.Metadata;
import com.heidelpay.payment.PaymentException;
import com.heidelpay.payment.Processing;
import com.heidelpay.payment.communication.HttpCommunicationException;
import com.heidelpay.payment.communication.impl.HttpClientBasedRestCommunication;
import com.heidelpay.payment.paymenttypes.Card;
import com.heidelpay.payment.paymenttypes.InvoiceGuaranteed;
import com.heidelpay.payment.paymenttypes.InvoiceSecured;
import com.heidelpay.payment.paymenttypes.SepaDirectDebit;
import com.heidelpay.payment.service.PropertiesUtil;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Currency;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

public abstract class AbstractPaymentTest {

	private final PropertiesUtil properties = new PropertiesUtil();

	public final String publicKey1 = properties.getString(PropertiesUtil.PUBLIC_KEY1);
	public final String privateKey1 = properties.getString(PropertiesUtil.PRIVATE_KEY1);
	public final String privateKey2 = properties.getString(PropertiesUtil.PRIVATE_KEY2);
	public final String privateKey3 = properties.getString(PropertiesUtil.PRIVATE_KEY3);

	protected String getRandomInvoiceId() {
		return getRandomId().substring(0, 5);
	}

	public Heidelpay getHeidelpayWithEndPoint(String endPoint) {
		return new Heidelpay(privateKey1, null, endPoint);
	}

	public Heidelpay getHeidelpay() {
		return new Heidelpay(privateKey1);
	}

	public Heidelpay getHeidelpayDE() {
		return new Heidelpay(privateKey1, Locale.GERMANY);
	}

	public Heidelpay getHeidelpay(String key) {
		return new Heidelpay(new HttpClientBasedRestCommunication(), key);
	}

	public Heidelpay getHeidelpayDE(String key) {
		return new Heidelpay(new HttpClientBasedRestCommunication(Locale.GERMANY), key);
	}

	protected Authorization getAuthorization(String typeId) throws MalformedURLException {
		return getAuthorization(typeId, (String)null);
	}

	protected Authorization getAuthorization(String typeId, Boolean card3ds) throws MalformedURLException {
		return getAuthorization(typeId, null, null, null, null, card3ds);
	}

	protected Authorization getAuthorization(String typeId, String customerId) throws MalformedURLException {
		return getAuthorization(typeId, customerId, null, null, null, null);
	}

	protected Authorization getAuthorization(String typeId, String customerId, String metadataId) throws MalformedURLException {
		return getAuthorization(typeId, customerId, null, metadataId, null, null);
	}

	protected Authorization getAuthorization(String typeId, String customerId, String orderId, String metadataId, String basketId) throws MalformedURLException {
		return getAuthorization(typeId, customerId, orderId, metadataId, basketId, null);
	}

	protected Authorization getAuthorization(String typeId, String customerId, String orderId, String metadataId, String basketId, Boolean card3ds) throws MalformedURLException {
		Authorization authorization = new Authorization();
		authorization
		.setAmount(new BigDecimal(10))
		.setCurrency(Currency.getInstance("EUR"))
		.setTypeId(typeId)
		.setReturnUrl(new URL("https://www.heidelpay.com"))
		.setOrderId(orderId)
		.setCustomerId(customerId)
		.setMetadataId(metadataId)
		.setBasketId(basketId)
		.setCard3ds(card3ds);
		return authorization;
	}
	
	protected Charge getCharge() throws MalformedURLException, HttpCommunicationException {
		return getCharge(null);
	}

	protected Charge getCharge(String orderId) throws MalformedURLException, HttpCommunicationException {
		Charge charge = getCharge(createPaymentTypeCard().getId(), null);
		charge.setOrderId(orderId);
		return charge;
	}

	protected Charge getCharge(String orderId, Boolean card3ds) throws MalformedURLException, HttpCommunicationException {
		return getCharge(createPaymentTypeCard().getId(), null, orderId, null, null, card3ds);
	}

	protected Charge getCharge(String typeId, String customerId, String orderId, String metadataId, String basketId) throws MalformedURLException, HttpCommunicationException {
		return getCharge(typeId, customerId, orderId, metadataId, basketId, null);
	}

	protected Charge getCharge(String typeId, String customerId, String orderId, String metadataId, String basketId, Boolean card3ds) throws MalformedURLException, HttpCommunicationException {
		Charge charge = new Charge();
		charge.setAmount(BigDecimal.ONE)
		.setCurrency(Currency.getInstance("EUR"))
		.setTypeId(typeId)
		.setReturnUrl(new URL("https://www.google.at"))
		.setOrderId(orderId)
		.setCustomerId(customerId)
		.setMetadataId(metadataId)
		.setBasketId(basketId)
		.setCard3ds(card3ds);
		return charge;
	}
	
	protected Card createPaymentTypeCard() throws HttpCommunicationException {
		return createPaymentTypeCard("4444333322221111");
	}

	protected Card createPaymentTypeCard(String cardnumber) throws HttpCommunicationException {
		Card card = getPaymentTypeCard(cardnumber);
		card = (Card)getHeidelpay().createPaymentType(card);
		return card;
	}

	@Deprecated
	protected InvoiceGuaranteed createPaymentTypeInvoiceGuaranteed() throws HttpCommunicationException {
		InvoiceGuaranteed invoice = new InvoiceGuaranteed();
		invoice = (InvoiceGuaranteed)getHeidelpay().createPaymentType(invoice);
		return invoice;
	}

	protected InvoiceSecured createPaymentTypeInvoiceSecured() throws HttpCommunicationException {
		InvoiceSecured invoice = new InvoiceSecured();
		invoice = (InvoiceSecured)getHeidelpay().createPaymentType(invoice);
		return invoice;
	}

	protected Card getPaymentTypeCard() {
		return getPaymentTypeCard("4444333322221111");
	}

	protected Card getPaymentTypeCard(String cardnumber) {
		Card card = new Card(cardnumber, "03/99");
		card.setCvc("123");
		return card;
	}

	protected SepaDirectDebit createPaymentTypeSepaDirectDebit() throws HttpCommunicationException {
		SepaDirectDebit sdd = getHeidelpay().createPaymentType(getSepaDirectDebit());
		return sdd;
	}
	
	protected SepaDirectDebit getSepaDirectDebit() {
		SepaDirectDebit sdd = new SepaDirectDebit("DE89370400440532013000");
		sdd.setBic("COBADEFFXXX");
		sdd.setHolder("Rene Felder");
		return sdd;
	}

	protected String getRandomId() {
		return UUID.randomUUID().toString().substring(0, 8);
	}

	protected Customer createMaximumCustomer() throws HttpCommunicationException, ParseException {
		return getHeidelpay().createCustomer(getMaximumCustomer(getRandomId()));
	}

	protected Basket createBasket() throws HttpCommunicationException, ParseException {
		return getHeidelpay().createBasket(getMaxTestBasket());
	}

	protected Customer createFactoringOKCustomer() throws HttpCommunicationException, ParseException {
		return getHeidelpay().createCustomer(getFactoringOKCustomer(getRandomId()));
	}

	protected Customer createMaximumCustomerSameAddress() throws HttpCommunicationException, ParseException {
		return getHeidelpay().createCustomer(getMaximumCustomerSameAddress(getRandomId()));
	}

	protected Customer getMinimumCustomer() {
		return new Customer("Rene", "Felder"); 
	}

	protected Customer getMinimumRegisteredCustomer() {
		return new Customer("Heidelpay GmbH"); 
	}

	protected Customer getMaximumCustomerSameAddress(String customerId) throws ParseException {
		Customer customer = new Customer( "Peter", "Universum");
		customer
		.setCustomerId(customerId)
		.setSalutation(Salutation.MR)
		.setEmail("info@heidelpay.com")
		.setMobile("+43676123456")
				.setPhone("+49 6221 64 71 100")
				.setBirthDate(getDate("03.10.1974"))
		.setBillingAddress(getAddress())
		.setShippingAddress(getAddress());
		customer.setCompany("heidelpay GmbH");
		return customer;
	}

	protected Customer getMaximumCustomer(String customerId) throws ParseException {
		Customer customer = new Customer("Rene", "Felder");
		customer
		.setCustomerId(customerId)
		.setSalutation(Salutation.MR)
		.setEmail("info@heidelpay.com")
		.setMobile("+43676123456")
		.setBirthDate(getDate("03.10.1974"))
		.setBillingAddress(getAddress())
		.setShippingAddress(getAddress("Schubert", "Vangerowstraße 18", "Heidelberg", "BW", "69115", "DE"));
		return customer;
	}

	protected Customer getMaximumMrsCustomer(String customerId) throws ParseException {
		Customer customer = new Customer("Anna", "Sadriu");
		customer
						.setCustomerId(customerId)
						.setSalutation(Salutation.MRS)
						.setEmail("info@heidelpay.com")
						.setMobile("+43676123456")
						.setBirthDate(getDate("08.05.1986"))
						.setBillingAddress(getAddress())
						.setShippingAddress(getAddress("Schubert", "Vangerowstraße 18", "Heidelberg", "BW", "69115", "DE"));
		return customer;
	}

	protected Customer getMaximumUnknownCustomer(String customerId) throws ParseException {
		Customer customer = new Customer("XXX", "YYY");
		customer
						.setCustomerId(customerId)
						.setSalutation(Salutation.UNKNOWN)
						.setEmail("info@heidelpay.com")
						.setMobile("+43676123456")
						.setBirthDate(getDate("01.01.1999"))
						.setBillingAddress(getAddress())
						.setShippingAddress(getAddress("Schubert", "Vangerowstraße 18", "Heidelberg", "BW", "69115", "DE"));
		return customer;
	}
	
	protected Customer getRegisterdMinimumBusinessCustomer() {
		Customer customer = getMinimumRegisteredCustomer();
		customer.setBillingAddress(getAddress());
		customer.setCompanyData(getRegisteredCompanyData());
		return customer;
	}

	protected Customer getRegisterdMaximumBusinessCustomer(String customerId) throws ParseException {
		Customer customer = getMaximumCustomer(customerId);
		customer.setCompany("Heidelpay GmbH");
		getUnregisteredCompanyData();
		customer.setCompanyData(getRegisteredCompanyData());
		return customer;
	}

	protected Customer getUnRegisterdMinimumBusinessCustomer() throws ParseException {
		Customer customer = getMinimumCustomer();
		customer.setBillingAddress(getAddress());
		
		customer.setCompanyData(getUnregisteredCompanyData());
		return customer;
	}
	
	protected Customer getUnRegisterdMaximumBusinessCustomer() throws ParseException {
		Customer customer = getMaximumCustomer(getRandomId());
		customer.setCompanyData(getUnregisteredCompanyData());
		return customer;
	}

	protected CustomerCompanyData getUnregisteredCompanyData() {
		CustomerCompanyData business = new CustomerCompanyData();
		business.setCommercialRegisterNumber("HRB337681 MANNHEIM");
		return business;
	}

	protected CustomerCompanyData getRegisteredCompanyData() {
		CustomerCompanyData customerBusinessData = new CustomerCompanyData();
		customerBusinessData.setCommercialRegisterNumber("HRB337681 MANNHEIM");
		return customerBusinessData;
	}

	protected Customer getFactoringOKCustomer(String customerId) throws ParseException {
		Customer customer = new Customer("Maximilian", "Mustermann");
		customer
						.setCustomerId(customerId)
						.setSalutation(Salutation.MR)
						.setBirthDate(getDate("22.11.1980"))
						.setBillingAddress(getFactoringOKAddress())
						.setShippingAddress(getFactoringOKAddress());
		return customer;
	}

	protected Address getFactoringOKAddress() {
		return getAddress("Maximilian Mustermann", "Hugo-Junkers-Str. 3", "Frankfurt am Main", "Frankfurt am Main", "60386", "DE");
	}

	protected Address getAddress() {
		return getAddress("Peter Universum", "Hugo-Junkers-Str. 6", "Frankfurt am Main", "DE-BO", "60386", "DE");
	}

	protected Address getAddress(String name, String street, String city, String state, String zip, String country) {
		Address address = new Address();
		address
		.setName(name)
		.setStreet(street)
		.setCity(city)
		.setState(state)
		.setZip(zip)
		.setCountry(country);
		return address;
	}

	protected Metadata createTestMetadata() throws PaymentException, HttpCommunicationException {
		Metadata metadata = getTestMetadata();
		metadata = getHeidelpay().createMetadata(metadata);
		return metadata;

	}

	protected Metadata getTestMetadata() {
		return getTestMetadata(false);
	}

	protected Metadata getTestMetadata(boolean sorted) {
		Metadata metadata = new Metadata(sorted)
				.addMetadata("invoice-nr", "Rg-2018-11-1")
				.addMetadata("shop-id", "4711")
				.addMetadata("delivery-date", "24.12.2018")
				.addMetadata("reason", "X-mas present");
		return metadata;
	}

	protected Date getDate(String date) throws ParseException {
		return new SimpleDateFormat("dd.MM.yy").parse(date);
	}

	protected void assertMapEquals(Map<String, String> testMetadataMap, Map<String, String> metadataMap) {
		for (Map.Entry<String, String> entry : metadataMap.entrySet()) {
			assertEquals(entry.getValue(), testMetadataMap.get(entry.getKey()));
		}
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

	protected void assertBusinessCustomerEquals(CustomerCompanyData customerExpected, CustomerCompanyData customer) {
		if (customerExpected == null && customer == null) return;
		assertEquals(customerExpected.getCommercialRegisterNumber(), customer.getCommercialRegisterNumber());
		assertEquals(customerExpected.getCommercialSector(), customer.getCommercialSector());
	}

	protected void assertCustomerEquals(Customer customerExpected, Customer customer) {
		assertEquals(customerExpected.getFirstname(), customer.getFirstname());
		assertEquals(customerExpected.getLastname(), customer.getLastname());
		assertEquals(customerExpected.getCustomerId(), customer.getCustomerId());
		assertEquals(customerExpected.getBirthDate(), customer.getBirthDate());
		assertEquals(customerExpected.getEmail(), customer.getEmail());
		assertEquals(customerExpected.getMobile(), customer.getMobile());
		assertEquals(customerExpected.getPhone(), customer.getPhone());
		
		assertAddressEquals(customerExpected.getBillingAddress(), customer.getBillingAddress());		
		assertAddressEquals(customerExpected.getShippingAddress(), customer.getShippingAddress());	
		assertBusinessCustomerEquals(customerExpected.getCompanyData(), customer.getCompanyData());
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

	protected void assertNumberEquals(BigDecimal expected, BigDecimal actual) {
		if (expected == null && actual == null) return;
		if (expected == null && actual != null) throw new AssertionError("expected is null, but actual is not");
		if (expected != null && actual == null) throw new AssertionError("expected is not null, but actual is null");
		if (expected.compareTo(actual) != 0) throw new AssertionError("expected: " + expected + ", actual: " + actual);
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

	protected Basket getMaxTestBasket() {
		Basket basket = new Basket();
		basket.setAmountTotalGross(new BigDecimal(380.48));
		basket.setAmountTotalVat(new BigDecimal(380.48*0.2).setScale(2, RoundingMode.HALF_UP));
		basket.setAmountTotalDiscount(BigDecimal.TEN);
		basket.setAmountTotalVat(new BigDecimal(5.41));
		basket.setCurrencyCode(Currency.getInstance("EUR"));
		basket.setNote("Mistery shopping");
		basket.setOrderId(getRandomId());
		basket.addBasketItem(getMaxTestBasketItem1());
		basket.addBasketItem(getMaxTestBasketItem2());
		return basket;
	}
	
	protected Basket getTestBasketForInvoice() {
		Basket basket = new Basket();
		basket.setAmountTotalGross(new BigDecimal(14.49));
		basket.setAmountTotalVat(new BigDecimal(14.49*0.2).setScale(2, RoundingMode.HALF_UP));
		basket.setAmountTotalDiscount(BigDecimal.ONE);
		basket.setAmountTotalVat(new BigDecimal(3.41));
		basket.setCurrencyCode(Currency.getInstance("EUR"));
		basket.setNote("Mistery shopping");
		basket.setOrderId(getRandomId());
		basket.addBasketItem(getMaxTestBasketItem1());
		return basket;
	}

	protected Basket getMinTestBasket() {
		Basket basket = new Basket()
				.setAmountTotalGross(new BigDecimal(500.5))
				.setCurrencyCode(Currency.getInstance("EUR"))
				.setOrderId(getRandomId())
				.addBasketItem(getMinTestBasketItem());
		return basket;
	}

	private BasketItem getMaxTestBasketItem1() {
		BasketItem basketItem = new BasketItem();
		basketItem.setBasketItemReferenceId("Artikelnummer4711");
		basketItem.setAmountDiscount(BigDecimal.ONE);
		basketItem.setAmountGross(new BigDecimal(14.49));
		basketItem.setAmountNet(new BigDecimal(13.49));
		basketItem.setAmountPerUnit(new BigDecimal(14.49));
		basketItem.setAmountVat(new BigDecimal(1.4));
		basketItem.setQuantity(1);
		basketItem.setTitle("Apple iPhone");
		basketItem.setUnit("Pc.");
		basketItem.setVat(19);
		basketItem.setSubTitle("XS in Red");
		basketItem.setType("goods");
		try {
			basketItem.setImageUrl(new URL("https://www.apple.com/v/iphone-xs/d/images/overview/hero_top_device_large_2x.jpg"));
		} catch (MalformedURLException e) {
		}
		return basketItem;
	}

	private BasketItem getMaxTestBasketItem2() {
		BasketItem basketItem = new BasketItem();
		basketItem.setBasketItemReferenceId("Artikelnummer4712");
		basketItem.setAmountDiscount(BigDecimal.ONE);
		basketItem.setAmountGross(new BigDecimal(365.99));
		basketItem.setAmountNet(new BigDecimal(307.55));
		basketItem.setAmountPerUnit(new BigDecimal(223.66));
		basketItem.setAmountVat(new BigDecimal(58.44));
		basketItem.setQuantity(3);
		basketItem.setTitle("Apple iPad Air");
		basketItem.setUnit("Pc.");
		basketItem.setVat(20);
		basketItem.setSubTitle("Nicht nur Pros brauchen Power.");
		basketItem.setType("goods");
		try {
			basketItem.setImageUrl(new URL("https://www.apple.com/de/ipad-air/images/overview/hero__gmn7i7gbziqa_large_2x.jpg"));
		} catch (MalformedURLException e) {
		}
		return basketItem;
	}

	private BasketItem getMinTestBasketItem() {
		BasketItem basketItem = new BasketItem()
				.setBasketItemReferenceId("Artikelnummer4711")
				.setQuantity(5)
				.setAmountPerUnit(new BigDecimal(100.1))
				.setAmountNet(new BigDecimal(420.1))
				.setTitle("Apple iPhone");
		
		basketItem.setAmountGross(basketItem.getAmountPerUnit().multiply(new BigDecimal(basketItem.getQuantity())));
		return basketItem;
	}

}
