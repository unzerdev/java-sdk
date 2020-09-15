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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import com.heidelpay.payment.Charge;
import com.heidelpay.payment.Customer;
import com.heidelpay.payment.Metadata;
import com.heidelpay.payment.Recurring;
import com.heidelpay.payment.communication.HttpCommunicationException;
import com.heidelpay.payment.paymenttypes.Card;
import com.heidelpay.payment.paymenttypes.Paypal;
import com.heidelpay.payment.paymenttypes.SepaDirectDebit;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.util.Currency;
import org.junit.Ignore;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;

public class RecurringTest extends AbstractSeleniumTest {

	@Test
	@Ignore("Does not work on Bamboo")
	public void testRecurringCardWithSelenium() throws MalformedURLException, HttpCommunicationException, ParseException {
		
		String typeId = createPaymentTypeCard("4711100000000000").getId();
		Recurring recurring = getHeidelpay().recurring(typeId, new URL("https://www.heidelpay.com"));
		assertRecurring(recurring, Recurring.Status.PENDING);
		
		RemoteWebDriver driver = openUrl(recurring.getRedirectUrl().toString());
		WebElement sdd = driver.findElement(By.id("code"));

		sdd.sendKeys("secret3");
		WebElement submit = driver.findElement(By.name("submit"));
		submit.click();
		Card card = (Card)getHeidelpay().fetchPaymentType(typeId);
		assertEquals(true, card.getRecurring());
		close();
	}
	
	@Test
	public void testRecurringCardWithoutCustomer() throws MalformedURLException, HttpCommunicationException, ParseException {
		String typeId = createPaymentTypeCard().getId();
		Recurring recurring = getHeidelpay().recurring(typeId, new URL("https://www.heidelpay.com"));
		assertRecurring(recurring, Recurring.Status.PENDING);
		
		Card type = (Card)getHeidelpay().fetchPaymentType(typeId);
		assertEquals(false, type.getRecurring());

	}

	@Test
	public void testRecurringCardWitCustomerId() throws MalformedURLException, HttpCommunicationException, ParseException {
		Customer customer = getHeidelpay().createCustomer(getMaximumCustomer(getRandomId()));
		String typeId = createPaymentTypeCard().getId();
		Recurring recurring = getHeidelpay().recurring(typeId, customer.getId(), new URL("https://www.heidelpay.com"));
		assertRecurring(recurring, Recurring.Status.PENDING);
		assertNotNull(recurring.getRedirectUrl());

		Card type = (Card)getHeidelpay().fetchPaymentType(typeId);
		assertEquals(false, type.getRecurring());
	}

	@Test
	public void testRecurringCardWitCustomerWithCustomerId() throws MalformedURLException, HttpCommunicationException, ParseException {
		Customer customer = getHeidelpay().createCustomer(getMaximumCustomer(getRandomId()));
		Recurring recurring = getHeidelpay().recurring(createPaymentTypeCard().getId(), customer.getCustomerId(), new URL("https://www.heidelpay.com"));
		assertRecurring(recurring, Recurring.Status.PENDING);
		assertNotNull(recurring.getRedirectUrl());
	}

	@Test
	public void testRecurringCardWitCustomerAndMetadata() throws MalformedURLException, HttpCommunicationException, ParseException {
		Customer customer = getHeidelpay().createCustomer(getMaximumCustomer(getRandomId()));
		Metadata metadata = getHeidelpay().createMetadata(getTestMetadata());
		Recurring recurring = getHeidelpay().recurring(createPaymentTypeCard().getId(), customer.getId(), metadata.getId(), new URL("https://www.heidelpay.com"));
		assertRecurring(recurring, Recurring.Status.PENDING);
		assertNotNull(recurring.getRedirectUrl());
	}
	
	@Test
	@Ignore("Does not work on Bamboo")
	public void testRecurringPaypalWithSelenium() throws MalformedURLException, HttpCommunicationException, ParseException {
		Paypal paypal = new Paypal();
		paypal = (Paypal) getHeidelpay().createPaymentType(paypal);
		Recurring recurring = getHeidelpay().recurring(paypal.getId(), new URL("https://www.heidelpay.com"));
		assertRecurring(recurring, Recurring.Status.PENDING);

		Paypal type = (Paypal)getHeidelpay().fetchPaymentType(paypal.getId());
		assertEquals(false, type.getRecurring());

		RemoteWebDriver driver = openUrl(recurring.getRedirectUrl().toString());
		
		WebElement email = driver.findElement(By.id("email"));
		email.clear();
		email.sendKeys("paypal-customer@heidelpay.de");
		WebElement password = driver.findElement(By.id("password"));
		password.sendKeys("heidelpay");
		WebElement submit = driver.findElement(By.name("btnLogin"));
		submit.click();
		
		type = (Paypal)getHeidelpay().fetchPaymentType(paypal.getId());
		assertEquals(true, type.getRecurring());
		close();
	}

	
	@Test
	@Ignore("Paypal recurring is set to true even if customer does not login paypal account: https://heidelpay.atlassian.net/browse/AHC-1725")
	public void testRecurringPaypalWithoutCustomer() throws MalformedURLException, HttpCommunicationException, ParseException {
		Paypal paypal = new Paypal();
		paypal = (Paypal) getHeidelpay().createPaymentType(paypal);
		Recurring recurring = getHeidelpay().recurring(paypal.getId(), new URL("https://www.heidelpay.com"));
		assertRecurring(recurring, Recurring.Status.PENDING);

		Paypal type = (Paypal)getHeidelpay().fetchPaymentType(paypal.getId());
		assertEquals(false, type.getRecurring());
	}

	@Test
	@Ignore("Paypal recurring is set to true even if customer does not login paypal account: https://heidelpay.atlassian.net/browse/AHC-1725")
	public void testRecurringPaypalWitCustomerId() throws MalformedURLException, HttpCommunicationException, ParseException {
		Customer customer = getHeidelpay().createCustomer(getMaximumCustomer(getRandomId()));
		Paypal paypal = new Paypal();
		paypal = (Paypal) getHeidelpay().createPaymentType(paypal);
		Recurring recurring = getHeidelpay().recurring(paypal.getId(), customer.getId(), new URL("https://www.heidelpay.com"));
		assertRecurring(recurring, Recurring.Status.PENDING);
		assertNotNull(recurring.getRedirectUrl());

		Paypal type = (Paypal)getHeidelpay().fetchPaymentType(paypal.getId());
		assertEquals(false, type.getRecurring());
	}
	
	@Test
	public void testRecurringCardDuringCharge() throws MalformedURLException, HttpCommunicationException, ParseException {
		String typeId = createPaymentTypeCard().getId();
		Card type = (Card)getHeidelpay().fetchPaymentType(typeId);
		assertEquals(false, type.getRecurring());

		Charge charge = getHeidelpay().charge(BigDecimal.ONE, Currency.getInstance("EUR"), typeId, new URL("https://www.heidelpay.com"), false);
		assertNull(charge.getRedirectUrl());
		
		type = (Card)getHeidelpay().fetchPaymentType(typeId);
		assertEquals(true, type.getRecurring());
	}

	@Test
	public void testRecurringSepaDirectDebitDuringCharge() throws MalformedURLException, HttpCommunicationException, ParseException {
		SepaDirectDebit sdd = createPaymentTypeSepaDirectDebit();
		sdd = (SepaDirectDebit)getHeidelpay().fetchPaymentType(sdd.getId());
		assertEquals(false, sdd.getRecurring());

		Charge charge = getHeidelpay().charge(BigDecimal.ONE, Currency.getInstance("EUR"), sdd.getId(), new URL("https://www.heidelpay.com"));
		assertNull(charge.getRedirectUrl());
		
		sdd = (SepaDirectDebit)getHeidelpay().fetchPaymentType(sdd.getId());
		assertEquals(true, sdd.getRecurring());
	}

	
	private void assertRecurring(Recurring recurring, Recurring.Status status) {
		assertNotNull(recurring);
		assertNotNull(recurring.getProcessing());
		assertNotNull(recurring.getProcessing().getUniqueId());
		
		assertNotNull(recurring.getHeidelpay());
		// Bug with Paypal that no date is returned in recurring call: https://heidelpay.atlassian.net/browse/AHC-1724
//		assertNotNull(recurring.getDate());
		assertNotNull(recurring.getRedirectUrl());
		assertEquals(status, recurring.getStatus());
	}

}
